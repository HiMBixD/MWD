package tch1904.mwd.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import tch1904.mwd.entity.RequestBeSinger;
import tch1904.mwd.entity.RequestPublishProduct;

import java.util.Optional;

public interface RequestPublishProductRepository extends JpaRepository<RequestPublishProduct, Integer> {
    @Query("FROM RequestPublishProduct r WHERE r.username LIKE CONCAT('%',:username,'%') " +
            "and (:status is null or r.status = :status) order by r.create_time DESC ")
    Page<RequestPublishProduct> searchListRequestPublishProduct(@Param("username")String username,
                                                    @Param("status")Integer status, Pageable pageable);

    Optional<RequestPublishProduct> findByProductIdAndStatus(Integer productId, Integer status);
}
