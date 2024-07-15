package io.github.discordchat.controller.front;

import io.github.discordchat.core.common.req.PageReqDto;
import io.github.discordchat.core.common.resp.PageRespDto;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.resp.TagRespDto;
import io.github.discordchat.service.TagService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @classname TagController
 * @description TODO
 * @date 2024/6/28
 * @created by lwq
 */

@Tag(name = "TagController", description = "标签模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_TAG_URL_PREFIX)
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    @GetMapping("list")
    public RestResp<PageRespDto<TagRespDto>> listTags(PageReqDto dto){
        return tagService.listTags(dto);
    }
}
