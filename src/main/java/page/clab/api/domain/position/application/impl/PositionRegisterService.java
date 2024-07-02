package page.clab.api.domain.position.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.position.application.PositionRegisterUseCase;
import page.clab.api.domain.position.dao.PositionRepository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.dto.request.PositionRequestDto;

@Service
@RequiredArgsConstructor
public class PositionRegisterService implements PositionRegisterUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final PositionRepository positionRepository;

    @Transactional
    public Long register(PositionRequestDto requestDto) {
        memberLookupUseCase.ensureMemberExists(requestDto.getMemberId());
        return positionRepository.findByMemberIdAndYearAndPositionType(requestDto.getMemberId(), requestDto.getYear(), requestDto.getPositionType())
                .map(Position::getId)
                .orElseGet(() -> {
                    Position position = PositionRequestDto.toEntity(requestDto);
                    return positionRepository.save(position).getId();
                });
    }
}
