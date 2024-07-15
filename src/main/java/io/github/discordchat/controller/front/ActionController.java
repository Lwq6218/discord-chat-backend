package io.github.discordchat.controller.front;

import io.github.discordchat.core.auth.UserHolder;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.req.ActionReqDto;
import io.github.discordchat.service.ActionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @classname ActionController
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */

@Tag(name = "ActionController", description = "动作模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_ACTION_URL_PREFIX)
@RequiredArgsConstructor
public class ActionController {

    private final ActionService actionService;

    @PostMapping("/upvote")
    public RestResp<Void> handleUpvote(@RequestBody ActionReqDto dto) {
          return actionService.handleUpvote(UserHolder.getUserId(),dto);
    }

    @PostMapping("/hasUpVoted")
    public RestResp<Boolean> hasUpVoted(@RequestBody ActionReqDto dto) {
        return actionService.hasUpVoted(UserHolder.getUserId(),dto);
    }

    @PostMapping("/hasDownVoted")
    public RestResp<Boolean> hasDownVoted(@RequestBody ActionReqDto dto) {
        return actionService.hasDownVoted(UserHolder.getUserId(),dto);
    }
    @PostMapping("/downvote")
    public RestResp<Void> handleDownVote(@RequestBody ActionReqDto dto) {
        return actionService.handleDownVote(UserHolder.getUserId(),dto);
    }
    @PostMapping("/star")
    public RestResp<Void> handleStar(@RequestBody ActionReqDto dto) {
        return actionService.handleStar(UserHolder.getUserId(),dto);
    }

    @PostMapping("/hasSaved")
    public RestResp<Boolean> hasSaved(@RequestBody ActionReqDto dto) {
        return actionService.hasSaved(UserHolder.getUserId(), dto);
    }
}
