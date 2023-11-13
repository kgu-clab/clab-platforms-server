package page.clab.api.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.AccuseRepository;
import page.clab.api.type.dto.AccuseDto;
import page.clab.api.type.entity.Accuse;

@Service
@RequiredArgsConstructor
public class AccuseService {

    private final BoardService boardService;

    private final CommentService commentService;

    private final BlogService blogService;

    private final NewsService newsService;

    private final ReviewService reviewService;

    private final MemberService memberService;

    private final AccuseRepository accuseRepository;

    public List<AccuseDto> getAccuses() {
        List<Accuse> accuses = accuseRepository.findAll();
        return accuses.stream()
                .map(AccuseDto::of)
                .collect(Collectors.toList());
    }

    public void deleteAccuse(Long targetId) {
        Accuse accuse = getAccuseByTargetIdOrThrow(targetId);
        accuseRepository.delete(accuse);
    }

    public void memberAccuse(String memberId) {
        memberService.getMemberByIdOrThrow(memberId);
        Accuse accuse = checkIsAccused(Long.parseLong(memberId));
        accuse.setCount(accuse.getCount() + 1);
        accuseRepository.save(accuse);
    }

    public void boardAccuse(Long boardId) {
        boardService.getBoardByIdOrThrow(boardId);
        Accuse accuse = checkIsAccused(boardId);
        accuse.setCount(accuse.getCount() + 1);
        accuseRepository.save(accuse);
    }

    public void commentAccuse(Long commentId){
        commentService.getCommentByIdOrThrow(commentId);
        Accuse accuse = checkIsAccused(commentId);
        accuse.setCount(accuse.getCount() + 1);
        accuseRepository.save(accuse);

    }

    public void blogAccuse(Long blogId){
        blogService.getBlogByIdOrThrow(blogId);
        Accuse accuse = checkIsAccused(blogId);
        accuse.setCount(accuse.getCount() + 1);
        accuseRepository.save(accuse);
    }

    public void newsAccuse(Long newsId){
        newsService.getNewsByIdOrThrow(newsId);
        Accuse accuse = checkIsAccused(newsId);
        accuse.setCount(accuse.getCount() + 1);
        accuseRepository.save(accuse);
    }

    public void reviewAccuse(Long reviewId){
        reviewService.getReviewByIdOrThrow(reviewId);
        Accuse accuse = checkIsAccused(reviewId);
        accuse.setCount(accuse.getCount() + 1);
        accuseRepository.save(accuse);
    }

    private Accuse getAccuseByTargetIdOrThrow(Long id) {
        return accuseRepository.findByTargetId(id)
                .orElseThrow(() -> new NotFoundException("해당 신고 내역이 존재하지 않습니다."));
    }

    private Accuse checkIsAccused(Long targetId){
        Accuse accuse = accuseRepository.findByTargetId(targetId).orElse(null);
        if (accuse == null) {
            return Accuse.of(targetId);
        }
        return accuse;
    }

}
