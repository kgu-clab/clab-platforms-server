package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
