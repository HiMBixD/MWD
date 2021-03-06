package tch1904.mwd.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublishProductRequest {
    private Double price;
    private Integer productId;
    private String productName;
    private String productType;
}
