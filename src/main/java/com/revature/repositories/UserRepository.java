package com.revature.repositories;

import com.revature.models.User;
import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
  /**
   * Optional throws error if more than 1 result found
   */
  Optional<User> findByEmail(String email);

  Optional<User> findByEmailAndPassword(String email, String password);
  Optional<User> findById(Integer id);
}
