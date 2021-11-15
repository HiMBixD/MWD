package tch1904.mwd.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tch1904.mwd.entity.UserProduct;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProductDTO {
    private Integer productId;
    private String username;
    private Boolean isOwn;
    private Integer mark;

    public UserProductDTO(UserProduct userProduct) {
        this.productId = userProduct.getProductId();
        this.username = userProduct.getUsername();
        this.isOwn = userProduct.getIsOwn();
        this.mark = userProduct.getMark();
    }
}
