package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.BoardRepository;
import page.clab.api.repository.CommentRepository;
import page.clab.api.type.dto.CommentDto;
import page.clab.api.type.entity.Comment;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;

    private final MemberService memberService;

    private final EntityManager entityManager;

    public void createComment(CommentDto commentDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        boardRepository.findById(commentDto.getBoard().getId())
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        Comment comment = Comment.of(commentDto);
        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, CommentDto commentDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
        if (comment.getWriter() != memberService.getCurrentMember()){
            throw new PermissionDeniedException("댓글 작성자만 수정할 수 있습니다.");
        }
        Comment updateComment = Comment.of(commentDto);
        updateComment.setId(comment.getId());
        updateComment.setBoard(comment.getBoard());
        updateComment.setWriter(comment.getWriter());
        updateComment.setUpdateTime(commentDto.getUpdateTime());
        updateComment.setContent(commentDto.getContent());
        commentRepository.save(updateComment);
    }

    public void deleteComment(Long commentId) throws PermissionDeniedException{
        memberService.checkMemberAdminRole();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
        if (comment.getWriter() != memberService.getCurrentMember()) {
            throw new PermissionDeniedException("댓글 작성자만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }

    public List<CommentDto> getComments(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        return comments.stream()
                .map(CommentDto::of)
                .collect(Collectors.toList());
    }

    public List<CommentDto> searchComment(Long memberId, String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> commentRoot = criteriaQuery.from(Comment.class);

        List<Predicate> predicates = new ArrayList<>();
        if (memberId != null) {
            predicates.add(criteriaBuilder.equal(commentRoot.get("memberId"), memberId));
        }
        if (name != null) {
            predicates.add(criteriaBuilder.like(commentRoot.get("name"), "%" + name + "%"));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Comment> query = entityManager.createQuery(criteriaQuery);
        List<Comment> comments = query.getResultList();
        return comments.stream()
                .map(CommentDto::of)
                .collect(Collectors.toList());
    }

}
