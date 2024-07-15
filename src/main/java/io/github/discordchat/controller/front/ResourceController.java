package io.github.discordchat.controller.front;

import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.dto.resp.ImageVerifyCodeRespDto;
import io.github.discordchat.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @classname ResourceController
 * @description 资源(图片 / 视频 / 文档)模块 API 控制器
 * @date 2024/6/13
 * @created by lwq
 */


@Tag(name = "ResourceController", description = "资源模块")
@RestController
@RequestMapping(ApiRouterConst.API_RESOURCE_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class ResourceController {

    private final ResourceService resourceService;

    /**
     * 获取图片验证码接口
     */
    @Operation(summary = "获取图片验证码接口")
    @GetMapping("img_verify_code")
    public RestResp<ImageVerifyCodeRespDto> getImgVerifyCode() throws IOException {
        RestResp<ImageVerifyCodeRespDto> imgVerifyCode = resourceService.getImgVerifyCode();
        return imgVerifyCode;

    }

    /**
     * 图片上传接口
     */
    @Operation(summary = "图片上传接口")
    @PostMapping("file")
    RestResp<String> uploadImage(
            @Parameter(description = "上传文件") @RequestParam("file") MultipartFile file) {
        return resourceService.uploadImage(file);
    }

}
