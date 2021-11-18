package tch1904.mwd.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tch1904.mwd.entity.PlayList;

import java.util.List;
import java.util.Optional;

public interface PlayListRepository extends JpaRepository<PlayList, Integer> {

    Optional<PlayList> findByTitle(String title);

    List<PlayList> findAllByUsername(String username);
}
