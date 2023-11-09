package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByMember(Member member);

    List<Review> findAllByIsPublic(boolean isPublic);

    List<Review> findAllByMember_Id(String memberId);

    List<Review> findAllByMember_Name(String name);

}
