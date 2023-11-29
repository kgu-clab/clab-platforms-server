package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.ActivityPhotoRepository;
import page.clab.api.type.dto.ActivityPhotoRequestDto;
import page.clab.api.type.dto.ActivityPhotoResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.ActivityPhoto;
import page.clab.api.type.entity.Member;

@Service
@RequiredArgsConstructor
public class ActivityPhotoService {

    private final MemberService memberService;

    private final ActivityPhotoRepository activityPhotoRepository;

    public void createActivityPhoto(ActivityPhotoRequestDto activityPhotoRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("활동 사진을 등록할 권한이 없습니다.");
        }
        ActivityPhoto activityPhoto = ActivityPhoto.of(activityPhotoRequestDto);
        activityPhotoRepository.save(activityPhoto);
    }

    public PagedResponseDto<ActivityPhotoResponseDto> getActivityPhotos(Pageable pageable) {
        Page<ActivityPhoto> activityPhotos = activityPhotoRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(activityPhotos.map(ActivityPhotoResponseDto::of));
    }

    public PagedResponseDto<ActivityPhotoResponseDto> getPublicActivityPhotos(Pageable pageable) {
        Page<ActivityPhoto> activityPhotos = getActivityPhotoByIsPublicTrue(pageable);
        return new PagedResponseDto<>(activityPhotos.map(ActivityPhotoResponseDto::of));
    }

    public void updateActivityPhoto(Long activityPhotoId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("활동 사진을 수정할 권한이 없습니다.");
        }
        ActivityPhoto activityPhoto = getActivityPhotoByIdOrThrow(activityPhotoId);
        activityPhoto.setIsPublic(!activityPhoto.getIsPublic());
        activityPhotoRepository.save(activityPhoto);
    }

    public void deleteActivityPhoto(Long activityPhotoId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("활동 사진을 삭제할 권한이 없습니다.");
        }
        ActivityPhoto activityPhoto = getActivityPhotoByIdOrThrow(activityPhotoId);
        activityPhotoRepository.delete(activityPhoto);
    }

    private ActivityPhoto getActivityPhotoByIdOrThrow(Long activityPhotoId) {
        return activityPhotoRepository.findById(activityPhotoId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 활동 사진입니다."));
    }

    private Page<ActivityPhoto> getActivityPhotoByIsPublicTrue(Pageable pageable) {
        return activityPhotoRepository.findAllByIsPublicTrueOrderByCreatedAtDesc(pageable);
    }

}
