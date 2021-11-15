package tch1904.mwd.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.entity.FileImg;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileImgDTO {
    private String id;

    private String description;

    private String fileType;

    private String fileName;

    private String username;

    public FileImgDTO(FileImg fileImg) {
        this.id = fileImg.getId();
        this.description = fileImg.getDescription();
        this.fileType = fileImg.getFileType();
        this.fileName = fileImg.getFileName();
        this.username = fileImg.getUsername();
    }
}
