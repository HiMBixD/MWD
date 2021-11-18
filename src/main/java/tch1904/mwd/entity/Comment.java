package tch1904.mwd.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Integer commentId;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "comment_data")
    private String commentData;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "create_time")
    private Instant createTime;

    @OneToMany(mappedBy = "parentId")
    private List<Comment> children = new ArrayList<Comment>();
}
