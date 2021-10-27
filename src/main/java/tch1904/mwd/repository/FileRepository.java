package tch1904.mwd.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tch1904.mwd.entity.FileImg;

public interface FileRepository extends MongoRepository<FileImg, String> {
}
