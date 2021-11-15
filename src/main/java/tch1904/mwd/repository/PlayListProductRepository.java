package tch1904.mwd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.PagingAndSortingRepository;
import tch1904.mwd.entity.PlayListProduct;
import tch1904.mwd.entity.Product;

import java.util.List;
import java.util.Optional;


public interface PlayListProductRepository extends JpaRepository<PlayListProduct, Integer> {

    List<PlayListProduct> findAllByListId(Integer listId);

    Optional<PlayListProduct> findByProductAndAndListId(Product product, Integer listId);
}
