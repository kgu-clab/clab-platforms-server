package page.clab.api.domain.hiring.application.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.hiring.application.domain.Application;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    ApplicationJpaEntity toJpaEntity(Application application);

    Application toDomain(ApplicationJpaEntity entity);
}
