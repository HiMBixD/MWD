package tch1904.mwd.entity.dto;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.constant.components.FileMetadata;
import tch1904.mwd.entity.FileImg;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private String id;

    private FileMetadata metadata;

    public FileDTO(GridFSFile gridFSFile) {
        this.id = gridFSFile.getObjectId().toString();
        this.metadata = new FileMetadata(gridFSFile);
    }
}
