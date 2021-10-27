package tch1904.mwd.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMusic {
    private String description;
    private InputStream stream;
}
