package tch1904.mwd.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import tch1904.mwd.entity.RequestAddMoney;

import org.springframework.data.domain.Pageable;


public interface RequestAddMoneyRepository extends PagingAndSortingRepository<RequestAddMoney, Integer> {
    @Query("FROM RequestAddMoney r WHERE r.username LIKE CONCAT('%',:username,'%') " +
            "and (:status is null or r.status = :status) order by r.create_time DESC ")
    Page<RequestAddMoney> searchListRequestAddMoney(@Param("username")String username,
                                                    @Param("status")Integer status, Pageable pageable);

}
