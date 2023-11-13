package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.AccuseRepository;
import page.clab.api.type.dto.AccuseDto;
import page.clab.api.type.entity.Accuse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccuseService {

    private final BoardService boardService;

    private final CommentService commentService;

    private final BlogService blogService;

    private final NewsService newsService;

    private final ReviewService reviewService;

    private final AccuseRepository accuseRepository;

    public List<AccuseDto> getAccuseList() {
        List<Accuse> accuses = accuseRepository.findAll();
        return accuses.stream()
                .map(AccuseDto::of)
                .toList();
    }

    public void boardAccuse(Long boardId) {
        if(boardService.getBoardByIdOrThrow(boardId) == null) {
            throw new NotFoundException("해당 게시글이 존재하지 않습니다.");
        }
        Accuse accuse = Accuse.of(boardId);
        accuse.setCount(accuse.getCount() + 1);
        accuseRepository.save(accuse);
    }

    public void commentAccuse(Long commentId){
        if (commentService.getCommentByIdOrThrow(commentId) == null) {
            throw new NotFoundException("해당 댓글이 존재하지 않습니다.");
        }
        Accuse accuse = Accuse.of(commentId);
        accuse.setCount(accuse.getCount() + 1);
        accuseRepository.save(accuse);

    }

    public void blogAccuse(Long blogId){
        if (blogService.getBlogByIdOrThrow(blogId) == null) {
            throw new NotFoundException("해당 블로그가 존재하지 않습니다.");
        }
        Accuse accuse = Accuse.of(blogId);
        accuse.setCount(accuse.getCount() + 1);
        accuseRepository.save(accuse);
    }

    public void newsAccuse(Long newsId){
        if (newsService.getNewsByIdOrThrow(newsId) == null) {
            throw new NotFoundException("해당 뉴스가 존재하지 않습니다.");
        }
        Accuse accuse = Accuse.of(newsId);
        accuse.setCount(accuse.getCount() + 1);
        accuseRepository.save(accuse);
    }

    public void reviewAccuse(Long reviewId){
        if (reviewService.getReviewByIdOrThrow(reviewId) == null) {
            throw new NotFoundException("해당 리뷰가 존재하지 않습니다.");
        }
        Accuse accuse = Accuse.of(reviewId);
        accuse.setCount(accuse.getCount() + 1);
        accuseRepository.save(accuse);
    }

}
