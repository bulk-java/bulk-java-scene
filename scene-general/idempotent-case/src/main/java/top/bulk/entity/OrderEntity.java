package top.bulk.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 订单实体类，模拟测试使用
 *
 * @author 散装java
 * @date 2024-06-14
 */
@Data
@Accessors(chain = true)
public class OrderEntity {
    private Long id;
    /**
     * 下单用户id
     */
    private Long userId;
    /**
     * 产品id
     */
    private Long productId;

    // ... 其他属性省略


    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", userId:" + userId +
                ", productId:" + productId +
                '}';
    }
}
