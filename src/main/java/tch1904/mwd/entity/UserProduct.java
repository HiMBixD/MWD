package tch1904.mwd.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_product")
@Data
public class UserProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "mark")
    private Integer mark;

    @Column(name = "is_own")
    private Boolean isOwn;


}
