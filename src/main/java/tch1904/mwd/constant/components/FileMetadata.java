package tch1904.mwd.constant.components;

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
}
