package tch1904.mwd.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.constant.components.Pagination;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchRequestListRequest {
    private String username;
    private Integer status;
    private Pagination pagination;
}
