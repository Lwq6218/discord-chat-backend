package io.github.discordchat.service;

import io.github.discordchat.core.common.req.PageReqDto;
import io.github.discordchat.core.common.resp.PageRespDto;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dto.req.QuestionCreateReqDto;
import io.github.discordchat.dto.req.QuestionUpdateReqDto;
import io.github.discordchat.dto.resp.QuestionWithTagsRespDto;

public interface QuestionService {
    RestResp<PageRespDto<QuestionWithTagsRespDto>> listQuestion(PageReqDto dto);

    RestResp<PageRespDto<QuestionWithTagsRespDto>> listQuestion(Long userId, PageReqDto dto);

    RestResp<Void> addQuestion(Long userId, QuestionCreateReqDto dto);

    RestResp<Void> updateQuestion(Long userId, QuestionUpdateReqDto dto);

    RestResp<QuestionWithTagsRespDto> getQuestionById(Long userId, Long id);

    RestResp<PageRespDto<QuestionWithTagsRespDto>> listSavedQuestion(Long userId, PageReqDto dto);

    RestResp<Void> deleteQuestion(Long userId, Long id);
}
