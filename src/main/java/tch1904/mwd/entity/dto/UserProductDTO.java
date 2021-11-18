package tch1904.mwd.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.entity.Product;
import tch1904.mwd.entity.UserProduct;
import tch1904.mwd.entity.UserProductMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProductDTO {
    private Product product;
    private String username;
    private Boolean isOwn;
    private Integer mark;

    public UserProductDTO(UserProductMapper userProduct) {
        this.product = userProduct.getProductId();
        this.username = userProduct.getUsername();
        this.isOwn = userProduct.getIsOwn();
        this.mark = userProduct.getMark();
    }
}
