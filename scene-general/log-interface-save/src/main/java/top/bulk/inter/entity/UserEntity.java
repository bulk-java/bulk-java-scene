package top.bulk.inter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private String name;
    private Integer age;
}
