package tch1904.mwd.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "file_id")
    private String fileId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private Double price;

    @Column(name = "product_avatar")
    private String productAvatar;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "is_published")
    private Boolean isPublished;

    @Column(name = "mark")
    private Double mark;

    @Column(name = "total_mark")
    private Integer totalMark;

    @Column(name = "total_view")
    private Integer totalView;

    @Column(name = "total_buy")
    private Integer totalBuy;

    @Column(name = "total_comment")
    private Integer totalComment;

    @Column(name = "publish_time")
    private Instant publishTime;
}
