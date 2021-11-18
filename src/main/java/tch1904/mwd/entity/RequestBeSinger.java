package tch1904.mwd.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "request_be_singer")
@Data
public class RequestBeSinger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Integer requestId;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "link_file")
    private String linkFile;

    @Column(name = "approve_details")
    private String approve_details;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    private Instant create_time;

    @Column(name = "approve_time")
    private Instant approve_time;
}
