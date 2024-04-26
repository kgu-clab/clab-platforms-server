package page.clab.api.domain.application.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;

import java.util.List;
import java.util.Optional;
import page.clab.api.domain.board.domain.Board;

public interface ApplicationRepository extends JpaRepository<Application, ApplicationId>, ApplicationRepositoryCustom, QuerydslPredicateExecutor<Application> {

    Page<Application> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Application> findByRecruitmentIdAndIsPass(Long recruitmentId, Boolean isPass);

    Optional<Application> findByRecruitmentIdAndStudentId(Long recruitmentId, String studentId);

    @Query(value = "SELECT a.* FROM application a WHERE a.is_deleted = true", nativeQuery = true)
    Page<Application> findAllByIsDeletedTrue(Pageable pageable);

}
