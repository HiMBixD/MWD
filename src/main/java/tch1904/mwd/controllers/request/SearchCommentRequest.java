package tch1904.mwd.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.constant.components.Pagination;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchCommentRequest {
    private Integer productId;
    private Pagination pagination;
}
