package io.github.discordchat.service;

import io.github.discordchat.core.common.req.PageReqDto;
import io.github.discordchat.core.common.resp.PageRespDto;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dto.req.AnswerCreateReqDto;
import io.github.discordchat.dto.resp.AnswerWithProfileRespDto;

import java.util.List;

/**
 * @classname AnswerService
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */
public interface AnswerService {
    RestResp<Void> addAnswer(Long userId, AnswerCreateReqDto dto);

    RestResp<List<AnswerWithProfileRespDto>> listAnswer(Long userId, Long questionId);

    RestResp<PageRespDto<AnswerWithProfileRespDto>> listAnswersByUserId(Long userId, PageReqDto dto);

    RestResp<Void> deleteAnswer(Long userId, Long id);
}
