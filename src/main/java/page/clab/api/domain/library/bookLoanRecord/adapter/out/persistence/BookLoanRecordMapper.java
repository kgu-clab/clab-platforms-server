package page.clab.api.domain.library.bookLoanRecord.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;

@Mapper(componentModel = "spring")
public interface BookLoanRecordMapper {

    BookLoanRecordJpaEntity toJpaEntity(BookLoanRecord bookLoanRecord);

    BookLoanRecord toDomain(BookLoanRecordJpaEntity entity);
}
