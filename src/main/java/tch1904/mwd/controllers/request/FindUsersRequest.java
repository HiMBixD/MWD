package tch1904.mwd.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindUsersRequest {
    private String username;
    private String fullName;
    private String email;
    private Integer roleId;
}
