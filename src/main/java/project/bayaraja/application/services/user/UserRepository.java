package project.bayaraja.application.services.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Query("SELECT u FROM UserEntity u WHERE u.phone_number = :phoneNumber")
    Optional<UserEntity> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
