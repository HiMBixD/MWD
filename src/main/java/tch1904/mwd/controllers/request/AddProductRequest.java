package tch1904.mwd.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.constant.components.Pagination;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddProductRequest {
    private String fileId;
    private String productAvatar;
    private String productName;
}
