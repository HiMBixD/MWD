package tch1904.mwd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tch1904.mwd.entity.RequestAddMoney;

import java.time.Instant;
import java.util.List;

public interface RequestAddMoneyRepository extends JpaRepository<RequestAddMoney, Integer> {
    @Query("FROM RequestAddMoney r WHERE r.username LIKE CONCAT('%',:username,'%') " +
            "and (:status is null or r.status = :status) order by r.create_time DESC ")
    List<RequestAddMoney> searchListRequestAddMoney(@Param("username")String username,
                                                    @Param("status")Integer status);

}
