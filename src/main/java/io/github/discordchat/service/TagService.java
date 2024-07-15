package io.github.discordchat.service;

import io.github.discordchat.core.common.req.PageReqDto;
import io.github.discordchat.core.common.resp.PageRespDto;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dto.resp.TagRespDto;

/**
 * @classname TagService
 * @description TODO
 * @date 2024/6/28
 * @created by lwq
 */

public interface TagService {
    RestResp<PageRespDto<TagRespDto>> listTags(PageReqDto dto);
}
