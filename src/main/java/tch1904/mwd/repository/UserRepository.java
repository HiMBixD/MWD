package tch1904.mwd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tch1904.mwd.entity.RoleAccount;
import tch1904.mwd.entity.User;
import tch1904.mwd.entity.UserDetailToken;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("from User u where u.username = :username and u.password = :password")
    Optional<User> findByUserNameAndPassword(@Param("username") String username,
                                                         @Param("password") String password);
    @Query("from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username")String username);

    @Query("FROM User u WHERE u.username LIKE CONCAT('%',:username,'%')")
    List<User> searchByUsername(@Param("username")String username);

    @Query("FROM User u WHERE u.username LIKE CONCAT('%',:username,'%') and u.fullName LIKE CONCAT('%',:fullName,'%') " +
            "and u.email LIKE CONCAT('%',:email,'%') and (:roleId is null or u.roleId = :roleId)")
    List<User> searchListUsers(@Param("username")String username,@Param("fullName")String fullName,@Param("email")String email,@Param("roleId")Integer roleId);

    List<User> findAllByUsernameLikeAndFullNameLikeAndEmailLikeAndRoleIdLike(
            String username,
           String fullName,
            String email,
            Integer roleId
    );

}
