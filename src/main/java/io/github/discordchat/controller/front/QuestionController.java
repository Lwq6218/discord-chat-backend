package io.github.discordchat.controller.front;

import io.github.discordchat.core.auth.UserHolder;
import io.github.discordchat.core.common.req.PageReqDto;
import io.github.discordchat.core.common.resp.PageRespDto;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.req.QuestionCreateReqDto;
import io.github.discordchat.dto.req.QuestionUpdateReqDto;
import io.github.discordchat.dto.resp.QuestionWithTagsRespDto;
import io.github.discordchat.service.QuestionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @classname QuestionController
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */


@Tag(name = "QuestionController", description = "问题模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_QUESTION_URL_PREFIX)
@RequiredArgsConstructor
public class QuestionController {

   private final QuestionService questionService;

   @GetMapping("list")
   public RestResp<PageRespDto<QuestionWithTagsRespDto>> listQuestions( PageReqDto dto){
         return questionService.listQuestion(dto);
   }

   @GetMapping("list/saved-question")
   public RestResp<PageRespDto<QuestionWithTagsRespDto>> listSavedQuestions(PageReqDto dto){
         return questionService.listSavedQuestion(UserHolder.getUserId(),dto);
   }

    @GetMapping("list/user")
    public RestResp<PageRespDto<QuestionWithTagsRespDto>> listQuestionsByUserId(@PathParam("userId") Long userId, PageReqDto dto){
        return questionService.listQuestion(userId,dto);
    }
    @GetMapping("{id}")
    public RestResp<QuestionWithTagsRespDto> getQuestionById(@PathVariable("id") Long id){
        return questionService.getQuestionById(UserHolder.getUserId(),id);
    }

   @PostMapping
    public RestResp<Void> addQuestion(@Valid @RequestBody QuestionCreateReqDto dto){
        return questionService.addQuestion(UserHolder.getUserId(),dto);
    }
    @PatchMapping
    public RestResp<Void> updateQuestion(@Valid @RequestBody QuestionUpdateReqDto dto){
        return questionService.updateQuestion(UserHolder.getUserId(),dto);
    }
    @DeleteMapping("{id}")
    public RestResp<Void> deleteQuestion(@PathVariable("id") Long id){
      return questionService.deleteQuestion(UserHolder.getUserId(),id);
    }
}
