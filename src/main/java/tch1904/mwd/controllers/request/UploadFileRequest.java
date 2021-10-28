package tch1904.mwd.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadFileRequest {
    private String description;
    private String fileName;
    private String fileType;
    private MultipartFile file;
}
