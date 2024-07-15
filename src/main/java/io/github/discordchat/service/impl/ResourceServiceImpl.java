package io.github.discordchat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.resp.ImageVerifyCodeRespDto;
import io.github.discordchat.manager.redis.VerifyCodeManager;
import io.github.discordchat.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @classname ResourceServiceImpl
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    private final VerifyCodeManager verifyCodeManager;

    @Value("${discordchat.file.upload.path}")
    private String fileUploadPath;

    @Override
    public RestResp<ImageVerifyCodeRespDto> getImgVerifyCode() throws IOException {
        //获取一个全局唯一的sessionId，标识该验证码属于哪一个浏览器会话,
        //该sessionId和验证码一并返回给前端，在用户提交验证码时，前端将sessionId和验证码一并提交用于校验
        String sessionId = IdWorker.get32UUID();
        return RestResp.ok(ImageVerifyCodeRespDto.builder()
                .sessionId(sessionId)
                .img(verifyCodeManager.genImgVerifyCode(sessionId))
                .build());
    }

    @SneakyThrows
    @Override
    public RestResp<String> uploadImage(@RequestParam("file") MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        String savePath =
                now.format(DateTimeFormatter.ofPattern("yyyy")) + File.separator
                        + now.format(DateTimeFormatter.ofPattern("MM")) + File.separator
                        + now.format(DateTimeFormatter.ofPattern("dd"));

        String oriName = file.getOriginalFilename();
        if (oriName == null) {
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
        }

        // 检查文件类型
        String fileExtension = oriName.substring(oriName.lastIndexOf(".")).toLowerCase();
        if (!(fileExtension.equals(".jpg") || fileExtension.equals(".jpeg") || fileExtension.equals(".png") || fileExtension.equals(".pdf"))) {
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_TYPE_NOT_MATCH);
        }

        String saveFileName = IdWorker.get32UUID() + fileExtension;
        File saveFile = new File(fileUploadPath + File.separator + savePath, saveFileName);

        try {
            if (!saveFile.getParentFile().exists() && !saveFile.getParentFile().mkdirs()) {
                throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
            }

            file.transferTo(saveFile);

            // 对于图片文件，验证文件内容
            if (!fileExtension.equals(".pdf")) {
                BufferedImage image = ImageIO.read(saveFile);
                if (image == null) {
                    Files.delete(saveFile.toPath());
                    throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_TYPE_NOT_MATCH);
                }
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
        }

        // 返回上传文件的路径
        String fileDownloadUri = "http://localhost" + File.separator + "uploads" + File.separator + savePath + File.separator + saveFileName;
        return RestResp.ok(fileDownloadUri.replace(File.separator, "/"));
    }

}
