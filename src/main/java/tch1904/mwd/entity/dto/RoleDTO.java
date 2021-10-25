package tch1904.mwd.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.entity.RoleAccount;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String roleName;
    private String description;

    public RoleDTO(RoleAccount roleEntity) {
        this.roleName = roleEntity.getRoleName();
        this.description = roleEntity.getDescription();
    }
}
