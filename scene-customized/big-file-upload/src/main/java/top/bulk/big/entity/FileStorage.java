package top.bulk.big.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;

/**
 * 文件存储表(FileStorage)表实体类
 *
 * @author 散装java
 * @since 2022-11-19 15:42:59
 */
@Data
public class FileStorage implements Serializable {
    /**主键**/
    private Long id;
    /**文件真实姓名**/
    private String realName;
    /**文件名**/
    private String fileName;
    /**文件后缀**/
    private String suffix;
    /**文件路径**/
    private String filePath;
    /**文件类型**/
    private String fileType;
    /**文件大小**/
    private Long size;
    /**检验码 md5**/
    private String identifier;
    /**创建者**/
    private String createBy;
    /**创建时间**/
    private LocalDateTime createTime;
    /**更新人**/
    private String updateBy;
    /**更新时间**/
    private LocalDateTime updateTime;
}

