package page.clab.api.domain.position.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.position.dao.PositionRepository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.dto.request.PositionRequestDto;

@Service
@RequiredArgsConstructor
public class CreatePositionServiceImpl implements CreatePositionService {

    private final MemberLookupService memberLookupService;
    private final PositionRepository positionRepository;

    @Transactional
    public Long execute(PositionRequestDto requestDto) {
        memberLookupService.ensureMemberExists(requestDto.getMemberId());
        return positionRepository.findByMemberIdAndYearAndPositionType(requestDto.getMemberId(), requestDto.getYear(), requestDto.getPositionType())
                .map(Position::getId)
                .orElseGet(() -> {
                    Position position = PositionRequestDto.toEntity(requestDto);
                    return positionRepository.save(position).getId();
                });
    }
}
