package tch1904.mwd.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.entity.PlayList;
import tch1904.mwd.entity.PlayListProduct;
import tch1904.mwd.entity.Product;
import tch1904.mwd.entity.UserProduct;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayListProductDTO {
    private String username;
    private String description;
    private String title;
    private List<Product> products;

    public PlayListProductDTO(List<PlayListProduct> playListProduct, PlayList playList) {
        this.username = playList.getUsername();
        this.description = playList.getDescription();
        this.title = playList.getTitle();
        this.products = playListProduct.stream().map(PlayListProduct::getProduct).collect(Collectors.toList());
    }
    public PlayListProductDTO(PlayList playList) {
        this.username = playList.getUsername();
        this.description = playList.getDescription();
        this.title = playList.getTitle();
    }

    public PlayListProductDTO(List<PlayListProduct> playListProduct) {
        this.products = playListProduct.stream().map(PlayListProduct::getProduct).collect(Collectors.toList());
    }
}
