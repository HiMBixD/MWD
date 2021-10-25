package tch1904.mwd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tch1904.mwd.entity.RequestAddMoney;
import tch1904.mwd.entity.User;

import java.util.List;
import java.util.Optional;

public interface RequestAddMoneyRepository extends JpaRepository<RequestAddMoney, Integer> {
//    @Query("from User u where u.username = :username")
//    Optional<User> findByUsername(@Param("username")String username);
//
//    @Query("FROM User u WHERE u.username LIKE CONCAT('%',:username,'%')")
//    List<User> searchByUsername(@Param("username")String username);

}
