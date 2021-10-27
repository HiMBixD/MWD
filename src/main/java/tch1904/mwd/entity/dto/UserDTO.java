package tch1904.mwd.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.entity.RoleAccount;
import tch1904.mwd.entity.User;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userName;

    private String fullName;

    private String phone;

    private String email;

    private Double money;

    private String avatar;

    private Boolean isActive;

    private Integer roleId;

    public UserDTO(User user) {
        this.userName = user.getUsername();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.money = user.getMoney();
        this.isActive = user.isActive();
        this.roleId = user.getRoleId();
        this.avatar = user.getAvatar();
    }
}
