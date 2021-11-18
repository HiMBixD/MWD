package tch1904.mwd.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "play_list_product")
@Data
public class PlayListProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "list_id")
    private Integer listId;
//    @ManyToOne
//    @JoinColumn(name = "list_id")
//    private PlayList playList;
}
