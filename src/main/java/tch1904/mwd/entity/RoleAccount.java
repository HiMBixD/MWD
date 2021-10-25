package tch1904.mwd.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class RoleAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "ROLE_NAME", length = 25, nullable = false)
    private String roleName;

    @Column(name = "description")
    private String description;
}
