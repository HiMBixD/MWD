package tch1904.mwd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class UserDetailToken {
    @Id
    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "PASSWORD", length = 100)
    private String password;

    @Column(name = "Full_NAME", length = 200)
    private String fullName;

    @Column(name = "PHONE", length = 15)
    private String phone;

    @Column(name = "EMAIL", length = 200)
    private String email;

    @Column(name = "otp", length = 200)
    private String otp;

    @Column(name = "otp_expire")
    private Instant otpExpire;

    @Column(name = "money", length = 20)
    private Double money;


    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private RoleAccount roleAccount;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", otp='" + otp + '\'' +
                ", otpExpire=" + otpExpire +
                ", money=" + money +
                ", roleAccount=" + roleAccount +
                '}';
    }
}
