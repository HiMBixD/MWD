package tch1904.mwd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import tch1904.mwd.entity.FileImg;
import tch1904.mwd.entity.dto.FileImgDTO;

public interface FileRepository extends MongoRepository<FileImg, String> {
    Page<FileImgDTO> findByUsernameAndFileType(String username, String fileType, Pageable pageable);
}
