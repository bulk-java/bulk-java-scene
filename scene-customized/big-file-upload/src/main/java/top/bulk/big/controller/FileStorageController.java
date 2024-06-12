package top.bulk.big.controller;


import org.springframework.web.bind.annotation.*;
import top.bulk.big.common.Res;
import top.bulk.big.common.Result;
import top.bulk.big.dto.FileChunkDto;
import top.bulk.big.service.FileChunkService;
import top.bulk.big.service.FileStorageService;
import top.bulk.big.vo.CheckResultVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件存储表(FileStorage)表控制层
 *
 * @author 散装java
 * @since 2022-11-15 17:49:39
 */
@RestController
@RequestMapping("fileStorage")
public class FileStorageController {
    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private FileChunkService fileChunkService;

    /**
     * 本接口为校验接口，即上传前，先根据本接口查询一下 服务器是否存在该文件
     *
     * @param dto 入参
     * @return vo
     */
    @GetMapping("/upload")
    public Result<CheckResultVo> checkUpload(FileChunkDto dto) {
        return Res.ok(fileChunkService.check(dto));
    }

    /**
     * 本接口为实际上传接口
     *
     * @param dto      入参
     * @param response response 配合前端返回响应的状态码
     * @return boolean
     */
    @PostMapping("/upload")
    public Result<Boolean> upload(FileChunkDto dto, HttpServletResponse response) {
        try {
            Boolean status = fileStorageService.uploadFile(dto);
            if (status) {
                return Res.ok();
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return Res.error("上传失败");
            }
        } catch (Exception e) {
            // 这个code 是根据前端组件的特性来的，也可以自己定义规则
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Res.error("上传失败");
        }
    }

    /**
     * 下载接口，这里只做了普通的下载
     *
     * @param request    req
     * @param response   res
     * @param identifier md5
     * @throws IOException 异常
     */
    @GetMapping(value = "/download/{identifier}")
    public void downloadByIdentifier(HttpServletRequest request, HttpServletResponse response, @PathVariable("identifier") String identifier) throws IOException {
        fileStorageService.downloadByIdentifier(identifier, request, response);
    }
}

