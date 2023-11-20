package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.ActivityGroupBoardRepository;
import page.clab.api.type.dto.ActivityGroupBoardDto;
import page.clab.api.type.entity.ActivityGroupBoard;
import page.clab.api.type.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityGroupBoardService {

    private final ActivityGroupBoardRepository activityGroupBoardRepository;

    private final MemberService memberService;

    private final ActivityGroupAdminService activityGroupAdminService;

    public void createActivityGroupBoard(Long parentId, Long activityGroupId, ActivityGroupBoardDto activityGroupBoardDto) {
        Member member = memberService.getCurrentMember();
        activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        ActivityGroupBoard board = ActivityGroupBoard.of(activityGroupBoardDto);
        board.setMember(member);
        board.setActivityGroupId(activityGroupId);
        if (parentId != null) {
            ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);
            board.setParent(parentBoard);
            activityGroupBoardRepository.save(board);
            parentBoard.getChildren().add(board);
            activityGroupBoardRepository.save(parentBoard);
        }else{
            activityGroupBoardRepository.save(board);
        }
    }

    public List<ActivityGroupBoardDto> getAllActivityGroupBoard(){
        List<ActivityGroupBoard> boards = activityGroupBoardRepository.findAll();
        return boards.stream()
                .map(ActivityGroupBoardDto::of)
                .collect(Collectors.toList());
    }

    public ActivityGroupBoardDto getActivityGroupBoardById(Long activityGroupBoardId) {
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        return ActivityGroupBoardDto.of(board);
    }

    public List<ActivityGroupBoardDto> getActivityGroupBoardByParent(Long id) {
        List<ActivityGroupBoard> boardList = findChildBoards(id);
        return boardList.stream()
                .map(ActivityGroupBoardDto::of)
                .collect(Collectors.toList());
    }

    public void updateActivityGroupBoard (Long activityGroupBoardId, ActivityGroupBoardDto activityGroupBoardDto) {
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
//        board.setParent(activityGroupBoardDto.getParentId() != null ? getActivityGroupBoardByIdOrThrow(activityGroupBoardDto.getParentId()) : null);
        board.setCategory(activityGroupBoardDto.getCategory());
        board.setTitle(activityGroupBoardDto.getTitle());
        board.setContent(activityGroupBoardDto.getContent());
        board.setFilePath(activityGroupBoardDto.getFilePath());
        board.setFileName(activityGroupBoardDto.getFileName());
        activityGroupBoardRepository.save(board);
    }

    public void deleteActivityGroupBoard (Long activityGroupBoardId) {
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        activityGroupBoardRepository.delete(board);
    }

    private ActivityGroupBoard getActivityGroupBoardByIdOrThrow(Long activityGroupBoardId) {
        return activityGroupBoardRepository.findById(activityGroupBoardId)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));
    }

    private List<ActivityGroupBoard> findChildBoards (Long id){
        List<ActivityGroupBoard> boardList = new ArrayList<>();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(id);
        if (board.getParent() == null || board.getChildren()!= null) {
            boardList.add(board);
            for (ActivityGroupBoard child : board.getChildren()) {
                boardList.addAll(findChildBoards(child.getId()));
            }
        }else{
            boardList.add(board);
        }
        return boardList;
    }
}
