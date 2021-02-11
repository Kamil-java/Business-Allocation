package pl.bak.businessallocationapp.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.bak.businessallocationapp.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findUserByPinCode(int pin);

    @Query("SELECT DISTINCT au FROM User au JOIN au.tasks t WHERE t.readyToBeChecked=false")
    List<User> findAllByTasksIsFalse();

    Optional<User> findByPinCode(int pin);
}
