package chiragtailor.tech.filestorageuserservice.repositories;


import chiragtailor.tech.filestorageuserservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    User save(User user);
}
