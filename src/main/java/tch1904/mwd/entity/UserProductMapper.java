package tch1904.mwd.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_product")
@Data
public class UserProductMapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "mark")
    private Integer mark;

    @Column(name = "is_own")
    private Boolean isOwn;


}
