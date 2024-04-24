package page.clab.api.global.common.softDelete;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.global.common.domain.BaseEntity;

@Service
@RequiredArgsConstructor
public class SoftDeleteService<T extends BaseEntity> {
/*
    private final SoftDeleteRepository softDeleteRepository;

    public void softDelete(T entity) {
        SoftDeletedEntity softDeletedEntity = new SoftDeletedEntity();
        softDeletedEntity.setEntityName(entity.getClass().getSimpleName());
        softDeletedEntity.setEntityId(entity.getId().toString());

        softDeleteRepository.save(softDeletedEntity);
        entity.updateIsDeleted();
    }

    public List<Object> findDeletedEntityByClassName(Object name){
        softDeletedEntityRepository.findByEntityNameAndEntityId("Movie", "1");
    } */
}
