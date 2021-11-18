package tch1904.mwd.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "request_publish_product")
@Data
public class RequestPublishProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Integer requestId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

//    @Column(name = "information")
//    private String information;

    @Column(name = "approve_details")
    private String approve_details;

    @Column(name = "price")
    private Double price;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    private Instant create_time;

    @Column(name = "approve_time")
    private Instant approve_time;
}
