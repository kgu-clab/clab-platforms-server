package page.clab.api.domain.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.Role;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.ModelMapperUtil;
import page.clab.api.global.util.RandomNicknameUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Size(min = 1, max = 50, message = "{size.board.category}")
    private String category;

    @Column(nullable = false)
    @Size(min = 1, max = 100, message = "{size.board.title}")
    private String title;

    @Column(nullable = false)
    @Size(min = 1, max = 10000, message = "{size.board.content}")
    private String content;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_files")
    private List<UploadedFile> uploadedFiles = new ArrayList<>();

    private LocalDateTime updateTime;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean wantAnonymous;

    private Long likes;

    public static Board create(BoardRequestDto dto, Member member, List<UploadedFile> uploadedFiles) {
        Board board = ModelMapperUtil.getModelMapper().map(dto, Board.class);
        board.setMember(member);
        board.setUploadedFiles(uploadedFiles);
        board.setNickname(RandomNicknameUtil.makeRandomNickname());
        board.setWantAnonymous(dto.isWantAnonymous());
        board.setLikes(0L);
        return board;
    }

    public void update(BoardUpdateRequestDto boardUpdateRequestDto) {
        Optional.ofNullable(boardUpdateRequestDto.getCategory()).ifPresent(this::setCategory);
        Optional.ofNullable(boardUpdateRequestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(boardUpdateRequestDto.getContent()).ifPresent(this::setContent);
        Optional.of(boardUpdateRequestDto.isWantAnonymous()).ifPresent(this::setWantAnonymous);
        updateTime = LocalDateTime.now();
    }

    public boolean shouldNotifyForNewBoard() {
        return !this.member.getRole().equals(Role.USER) && this.category.equals("공지사항");
    }

    public void incrementLikes() {
        this.likes++;
    }

    public void decrementLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }

    public boolean isOwner(Member member) {
        return this.member.isSameMember(member);
    }

    public void checkPermission(Member member) throws PermissionDeniedException {
        if (!isOwner(member) && !member.isAdminRole()) {
            throw new PermissionDeniedException("해당 게시글을 수정할 권한이 없습니다.");
        }
    }

}