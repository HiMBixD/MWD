package tch1904.mwd.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApproveRequest {
    private Integer requestId;
    private Integer type; //0 default, 1 approve, 2 deny
    private String reason;
}
