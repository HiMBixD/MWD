package tch1904.mwd.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePassRequest {
    private String oldPass;
    private String newPass;
}
