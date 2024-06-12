package top.bulk.big.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.bulk.big.dto.FileChunkDto;
import top.bulk.big.entity.FileChunk;
import top.bulk.big.entity.FileStorage;
import top.bulk.big.mapper.FileStorageMapper;
import top.bulk.big.service.FileChunkService;
import top.bulk.big.service.FileStorageService;
import top.bulk.big.util.BulkFileUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 文件存储表(FileStorage)表服务实现类
 *
 * @author 散装java
 * @since 2022-11-15 17:49:41
 */
@Service("fileStorageService")
@Slf4j
public class FileStorageServiceImpl extends ServiceImpl<FileStorageMapper, FileStorage> implements FileStorageService {
    /**
     * 默认分块大小
     */
    @Value("${file.chunk-size}")
    public Long defaultChunkSize;
    /**
     * 上传地址
     */
    @Value("${file.path}")
    private String baseFileSavePath;

    @Resource
    FileChunkService fileChunkService;

    @Override
    public Boolean uploadFile(FileChunkDto dto) {
        // 简单校验，如果是正式项目，要考虑其他数据的校验
        if (dto.getFile() == null) {
            throw new RuntimeException("文件不能为空");
        }
        String fullFileName = baseFileSavePath + File.separator + dto.getFilename();
        Boolean uploadFlag;
        // 如果是单文件上传
        if (dto.getTotalChunks() == 1) {
            uploadFlag = this.uploadSingleFile(fullFileName, dto);
        } else {
            // 分片上传
            uploadFlag = this.uploadSharding(fullFileName, dto);
        }
        // 如果本次上传成功则存储数据到 表中
        if (uploadFlag) {
            this.saveFile(dto, fullFileName);
        }
        return uploadFlag;
    }

    @SneakyThrows
    @Override
    public void downloadByIdentifier(String identifier, HttpServletRequest request, HttpServletResponse response) {
        FileStorage file = this.getOne(new LambdaQueryWrapper<FileStorage>()
                .eq(FileStorage::getIdentifier, identifier));
        if (BeanUtil.isNotEmpty(file)) {
            File toFile = new File(baseFileSavePath + File.separator + file.getFilePath());
            BulkFileUtil.downloadFile(request, response, toFile);
        } else {
            throw new RuntimeException("文件不存在");
        }
    }

    /**
     * 分片上传方法
     * 这里使用 RandomAccessFile 方法，也可以使用 MappedByteBuffer 方法上传
     * 可以省去文件合并的过程
     *
     * @param fullFileName 文件名
     * @param dto          文件dto
     */
    private Boolean uploadSharding(String fullFileName, FileChunkDto dto) {
        // try 自动资源管理
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fullFileName, "rw")) {
            // 分片大小必须和前端匹配，否则上传会导致文件损坏
            long chunkSize = dto.getChunkSize() == 0L ? defaultChunkSize : dto.getChunkSize().longValue();
            // 偏移量, 意思是我从拿一个位置开始往文件写入，每一片的大小 * 已经存的块数
            long offset = chunkSize * (dto.getChunkNumber() - 1);
            // 定位到该分片的偏移量
            randomAccessFile.seek(offset);
            // 写入
            randomAccessFile.write(dto.getFile().getBytes());
        } catch (IOException e) {
            log.error("文件上传失败：" + e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @SneakyThrows
    private Boolean uploadSingleFile(String fullFileName, FileChunkDto dto) {
        File localPath = new File(fullFileName);
        dto.getFile().transferTo(localPath);
        return Boolean.TRUE;
    }

    private void saveFile(FileChunkDto dto, String fileName) {

        FileChunk chunk = BeanUtil.copyProperties(dto, FileChunk.class);
        chunk.setFileName(dto.getFilename());
        chunk.setTotalChunk(dto.getTotalChunks());
        fileChunkService.save(chunk);
        // 如果所有快都上传完成，那么在文件记录表中存储一份数据
        // todo 这里最好每次上传完成都存一下缓存，从缓存中查看是否所有块上传完成，这里偷懒了
        if (dto.getChunkNumber().equals(dto.getTotalChunks())) {
            String name = dto.getFilename();
            MultipartFile file = dto.getFile();

            FileStorage fileStorage = new FileStorage();
            fileStorage.setRealName(file.getOriginalFilename());
            fileStorage.setFileName(fileName);
            fileStorage.setSuffix(FileUtil.getSuffix(name));
            fileStorage.setFileType(file.getContentType());
            fileStorage.setSize(dto.getTotalSize());
            fileStorage.setIdentifier(dto.getIdentifier());
            fileStorage.setFilePath(dto.getRelativePath());
            this.save(fileStorage);
        }
    }
}

