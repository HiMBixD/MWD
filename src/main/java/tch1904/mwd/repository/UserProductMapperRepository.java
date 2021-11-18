package tch1904.mwd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tch1904.mwd.entity.UserProductMapper;
import tch1904.mwd.entity.dto.UserProductDTO;

import java.util.List;

public interface UserProductMapperRepository extends JpaRepository<UserProductMapper, Integer> {
    List<UserProductDTO> findAllByUsername(String username);

}
