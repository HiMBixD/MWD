package tch1904.mwd.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.constant.components.FileMetadata;

import java.io.InputStream;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMusic {
    private FileMetadata metadata;
    private InputStream stream;
}
