package tch1904.mwd.constant.components.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.constant.components.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse {
    private Boolean success;
    private Message responseMessage;
    private Object data;
}
