package tch1904.mwd.constant.components;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata {
    private String username;
    private String fileType;
    private String description;
    private String contentType;
    private String fileName;

    public FileMetadata(GridFSFile gridFSFile) {
        this.username = gridFSFile.getMetadata().get("username").toString();
        this.fileType = gridFSFile.getMetadata().get("fileType").toString();
        this.description = gridFSFile.getMetadata().get("description").toString();
        this.contentType = gridFSFile.getMetadata().get("contentType").toString();
        this.fileName = gridFSFile.getMetadata().get("fileName").toString();
    }
}
