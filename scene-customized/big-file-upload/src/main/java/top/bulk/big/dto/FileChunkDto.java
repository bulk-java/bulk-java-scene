package top.bulk.big.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 接收前端传过来的参数
 * 配合前端上传方法接收参数，参考官方文档
 * https://github.com/simple-uploader/Uploader/blob/develop/README_zh-CN.md#%E6%9C%8D%E5%8A%A1%E7%AB%AF%E5%A6%82%E4%BD%95%E6%8E%A5%E5%8F%97%E5%91%A2
 *
 * @author 散装java
 * @version 1.0.0
 * @date 2022-11-19
 */

@Data
public class FileChunkDto {
    /**
     * 当前块的次序，第一个块是 1，注意不是从 0 开始的
     */
    private Integer chunkNumber;
    /**
     * 文件被分成块的总数。
     */
    private Integer totalChunks;
    /**
     * 分块大小，根据 totalSize 和这个值你就可以计算出总共的块数。注意最后一块的大小可能会比这个要大
     */
    private Long chunkSize;
    /**
     * 当前块的大小，实际大小
     */
    private Long currentChunkSize;
    /**
     * 文件总大小
     */
    private Long totalSize;
    /**
     * 这个就是每个文件的唯一标示
     */
    private String identifier;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 文件夹上传的时候文件的相对路径属性
     */
    private String relativePath;
    /**
     * 文件
     */
    private MultipartFile file;
}
