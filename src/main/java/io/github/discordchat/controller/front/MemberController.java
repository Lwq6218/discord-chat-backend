package io.github.discordchat.controller.front;

import io.github.discordchat.core.auth.UserHolder;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.config.WebSocketHandler;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.req.MemberUpdateRoleReqDto;
import io.github.discordchat.dto.resp.MemberInfoRespDto;
import io.github.discordchat.dto.resp.MemberInfoWithProfileRespDto;
import io.github.discordchat.dto.resp.ServerWithMembersWithProfileRespDto;
import io.github.discordchat.service.MemberService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @classname MemberController
 * @description TODO
 * @date 2024/6/17
 * @created by lwq
 */

@Tag(name = "MemberController", description = "成员模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_MEMBER_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final WebSocketHandler webSocketHandler;

    private final MemberService memberService;


    @GetMapping("list/{serverId}")
    public RestResp<List<MemberInfoRespDto>> listMemberByServerId(@PathVariable("serverId") Long serverId) {
        return memberService.listMemberByServerId(UserHolder.getUserId(), serverId);
    }

    @GetMapping("{serverId}")
    public RestResp<MemberInfoRespDto> getMemberByServerId(@PathVariable("serverId") Long serverId) {
        return memberService.getMemberByServerId(UserHolder.getUserId(), serverId);
    }

    @GetMapping("list/with_profile/{serverId}")
    public RestResp<List<MemberInfoWithProfileRespDto>> listMemberWithProfile(@PathVariable("serverId") Long serverId) {
        return memberService.listMemberWithProfile(UserHolder.getUserId(), serverId);
    }

    @PatchMapping("{id}")
    public RestResp<ServerWithMembersWithProfileRespDto> updateMemberRole(@PathVariable("id") Long id, @RequestParam(value = "serverId", required = true) Long serverId, @Valid @RequestBody MemberUpdateRoleReqDto dto) {

        RestResp<ServerWithMembersWithProfileRespDto> serverWithMembersWithProfileRespDtoRestResp = memberService.updateMemberRole(UserHolder.getUserId(), serverId, id, dto.getRole());
        try {
            String key = String.format("server:%s:member:update", serverId);
            webSocketHandler.broadcastToChannel(key, serverId.toString());
            log.info("broadcastToChannel memberId:{} role update", id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return serverWithMembersWithProfileRespDtoRestResp;
    }


    @DeleteMapping("{id}")
    public RestResp<Void> deleteMember(@PathVariable("id") Long id, @RequestParam(value = "serverId", required = true) Long serverId) {

        return memberService.deleteMember(UserHolder.getUserId(), serverId, id);
    }

    @PostMapping("/invite/{inviteCode}")
    public RestResp<Long> createMemberByInviteCode(@PathVariable("inviteCode") String inviteCode) {
        RestResp<Long> memberByInviteCode = memberService.createMemberByInviteCode(UserHolder.getUserId(), inviteCode);
        try {
            String key = String.format("server:%s:member:add", inviteCode);
            webSocketHandler.broadcastToChannel(key, "add");
            log.info("broadcastToChannel inviteCode:{}", inviteCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return memberByInviteCode;
    }
}
