package page.clab.api.global.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import org.springframework.stereotype.Component;

@Component
public class ColumnValidator {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean isValidColumn(Class<?> entityClass, String columnName) {
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<?> entityType = metamodel.entity(entityClass);
        return entityType.getAttributes().stream()
                .anyMatch(attribute -> attribute.getName().equals(columnName));
    }
}