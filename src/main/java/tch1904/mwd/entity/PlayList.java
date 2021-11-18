package tch1904.mwd.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "play_list")
@Data
public class PlayList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_id", nullable = false)
    private Integer listId;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;
}
