package page.clab.api.domain.position.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;

public interface PositionRepositoryCustom {

    Page<Position> findByConditions(String year, PositionType positionType, Pageable pageable);

}