package tch1904.mwd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "photos")
public class FileImg {
    @Id
    private String id;

    private String description;

    private Binary image;

    private String fileType;

    private String fileName;

    private String username;
}
