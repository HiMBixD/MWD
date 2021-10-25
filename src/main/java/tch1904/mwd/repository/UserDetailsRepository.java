package tch1904.mwd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tch1904.mwd.entity.UserDetailToken;
import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetailToken, Integer> {
    @Query("from UserDetailToken u where u.username = :username")
    Optional<UserDetailToken> findByUsernameToken(@Param("username")String username);
}
