package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.repository.AccuseRepository;
import page.clab.api.type.dto.AccuseRequestDto;
import page.clab.api.type.dto.AccuseResponseDto;
import page.clab.api.type.entity.Accuse;
import page.clab.api.type.entity.Blog;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Comment;
import page.clab.api.type.entity.News;
import page.clab.api.type.entity.Review;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<AccuseResponseDto> getAccuses() {
        List<Accuse> accuses = accuseRepository.findAll();
        return accuses.stream()
                .map(AccuseResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<AccuseResponseDto> getAccuseByTargetId(Long targetId, String category) {
        List<Accuse> accuses = findAllByTargetIdAndCategory(targetId, category);
        return accuses.stream()
                .map(AccuseResponseDto::of)
                .collect(Collectors.toList());
    }

    public void deleteAccuse(Long targetId, String category) {
        List<Accuse> accuse = findAllByTargetIdAndCategory(targetId, category);
        accuseRepository.deleteAll(accuse);
    }

    public void memberAccuse(String memberId, AccuseRequestDto accuseRequestDto) {
        memberService.getMemberByIdOrThrow(memberId);
        Accuse accuse = Accuse.of(Long.parseLong(memberId));
        accuse.setCategory("member");
        accuse.setDescription(accuseRequestDto.getDescription());
        accuseRepository.save(accuse);
    }

    public void boardAccuse(Long boardId, AccuseRequestDto accuseRequestDto) {
        Board board = boardService.getBoardByIdOrThrow(boardId);
        Accuse accuse = Accuse.of(boardId);
        accuse.setCategory("board");
        accuse.setDescription(accuseRequestDto.getDescription());
        accuse.setContent(board.getContent());
        accuseRepository.save(accuse);
    }

    public void commentAccuse(Long commentId, AccuseRequestDto accuseRequestDto){
        Comment comment = commentService.getCommentByIdOrThrow(commentId);
        Accuse accuse = Accuse.of(commentId);
        accuse.setCategory("comment");
        accuse.setDescription(accuseRequestDto.getDescription());
        accuse.setContent(comment.getContent());
        accuseRepository.save(accuse);

    }

    public void blogAccuse(Long blogId, AccuseRequestDto accuseRequestDto){
        Blog blog = blogService.getBlogByIdOrThrow(blogId);
        Accuse accuse = Accuse.of(blogId);
        accuse.setCategory("blog");
        accuse.setDescription(accuseRequestDto.getDescription());
        accuse.setContent(blog.getContent());
        accuseRepository.save(accuse);
    }

    public void newsAccuse(Long newsId, AccuseRequestDto accuseRequestDto){
        News news = newsService.getNewsByIdOrThrow(newsId);
        Accuse accuse = Accuse.of(newsId);
        accuse.setCategory("news");
        accuse.setDescription(accuseRequestDto.getDescription());
        accuse.setContent(news.getContent());
        accuseRepository.save(accuse);
    }

    public void reviewAccuse(Long reviewId, AccuseRequestDto accuseRequestDto){
        Review review = reviewService.getReviewByIdOrThrow(reviewId);
        Accuse accuse = Accuse.of(reviewId);
        accuse.setCategory("review");
        accuse.setDescription(accuseRequestDto.getDescription());
        accuse.setContent(review.getContent());
        accuseRepository.save(accuse);
    }

    private List<Accuse> findAllByTargetIdAndCategory(Long targetId, String category) {
        return accuseRepository.findAllByTargetIdAndCategory(targetId, category);
    }

}
