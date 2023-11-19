package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByMember(Member member, Pageable pageable);

    Page<Review> findAllByIsPublic(boolean isPublic, Pageable pageable);

    Page<Review> findAllByMember_Id(String memberId, Pageable pageable);

    Page<Review> findAllByMember_Name(String name, Pageable pageable);

}
