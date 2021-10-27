package tch1904.mwd.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchRequestAddMoneyRequest {
    private String username;
    private Integer status;
}
