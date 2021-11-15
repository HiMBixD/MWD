package tch1904.mwd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import tch1904.mwd.entity.UserProduct;
import tch1904.mwd.entity.dto.UserProductDTO;

import java.util.List;
import java.util.Optional;

public interface UserProductRepository extends JpaRepository<UserProduct, String> {
//    @Query("FROM RequestAddMoney r WHERE r.username LIKE CONCAT('%',:username,'%') " +
//            "and (:status is null or r.status = :status) order by r.create_time DESC ")
//    Page<Product> searchListRequestAddMoney(@Param("username")String username,
//                                                    @Param("status")Integer status, Pageable pageable);

    List<UserProduct> findAllByProductId(Integer fileId);

    List<UserProductDTO> findAllByUsername(String username);

    Optional<UserProduct> findByUsernameAndProductId(String username, Integer productId);

}
