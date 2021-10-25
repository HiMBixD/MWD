package tch1904.mwd.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "request_add_money")
@Data
public class RequestAddMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Integer requestId;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "information")
    private String information;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    private Instant create_time;

    @Column(name = "approve_time")
    private Instant approve_time;
}
