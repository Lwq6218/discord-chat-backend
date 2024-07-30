package io.github.discordchat.controller.front;

import io.github.discordchat.core.auth.UserHolder;
import io.github.discordchat.core.common.req.PageReqDto;
import io.github.discordchat.core.common.resp.PageRespDto;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.req.AnswerCreateReqDto;
import io.github.discordchat.dto.resp.AnswerWithProfileRespDto;
import io.github.discordchat.service.AnswerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @classname AnswerController
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */

@Tag(name = "AnswerController", description = "答复模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_ANSWER_URL_PREFIX)
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;
   @PostMapping
    public RestResp<Void> addAnswer(@Valid @RequestBody AnswerCreateReqDto dto){
         return answerService.addAnswer(UserHolder.getUserId(),dto);
    }
    @GetMapping("/list/question")
    public RestResp<List<AnswerWithProfileRespDto>> listAnswers(@PathParam("questionId") Long questionId){
        return answerService.listAnswer(UserHolder.getUserId(),questionId);
    }
    @GetMapping("/list/user")
    public RestResp<PageRespDto<AnswerWithProfileRespDto>> listAnswersByUserId(@PathParam("userId") Long userId,  PageReqDto dto){
        return answerService.listAnswersByUserId(userId,dto);
    }
    @DeleteMapping("{id}")
    public RestResp<Void> deleteAnswer(@PathVariable("id") Long id){
        return answerService.deleteAnswer(UserHolder.getUserId(),id);
    }
}
