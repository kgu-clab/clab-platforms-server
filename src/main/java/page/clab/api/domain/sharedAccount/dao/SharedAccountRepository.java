package page.clab.api.domain.sharedAccount.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.sharedAccount.domain.SharedAccount;

public interface SharedAccountRepository extends JpaRepository<SharedAccount, Long> {

    Page<SharedAccount> findAllByOrderByIdAsc(Pageable pageable);

    @Query(value = "SELECT s.* FROM shared_account s WHERE s.is_deleted = true", nativeQuery = true)
    Page<SharedAccount> findAllByIsDeletedTrue(Pageable pageable);

}
