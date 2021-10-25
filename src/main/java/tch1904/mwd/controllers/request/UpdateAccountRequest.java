package tch1904.mwd.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAccountRequest {
    private String username;
    private String email;
    private String phone;
    private String fullName;
    private String otp;
}
