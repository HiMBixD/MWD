package tch1904.mwd.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import tch1904.mwd.entity.Product;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
//    @Query("FROM RequestAddMoney r WHERE r.username LIKE CONCAT('%',:username,'%') " +
//            "and (:status is null or r.status = :status) order by r.create_time DESC ")
//    Page<Product> searchListRequestAddMoney(@Param("username")String username,
//                                                    @Param("status")Integer status, Pageable pageable);

    Optional<Product> findByFileId(String fileId);
    @Query("FROM Product r WHERE lower(r.username) LIKE lower(CONCAT('%',:username,'%')) " +
            "and lower(r.productType) LIKE lower(CONCAT('%',:productType,'%'))" +
            "and lower(r.productName) LIKE lower(CONCAT('%',:productName,'%'))" +
            "and r.isPublished = :isPublished ORDER BY r.publishTime DESC"
    )
    Page<Product> searchProduct(String username, String productName, String productType, Boolean isPublished, Pageable pageable);
    List<Product> findTop10ByIsPublishedIsTrueOrderByTotalBuyDesc();

    List<Product> findTop10ByUsernameAndIsPublishedIsTrueOrderByTotalBuyDesc(String username);

}
