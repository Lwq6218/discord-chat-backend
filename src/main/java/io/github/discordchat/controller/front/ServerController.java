package io.github.discordchat.controller.front;

import io.github.discordchat.core.auth.UserHolder;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.config.WebSocketHandler;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.req.ServerInfoCreateReqDto;
import io.github.discordchat.dto.resp.ChannelInfoRespDto;
import io.github.discordchat.dto.resp.ServerInfoCreateRespDto;
import io.github.discordchat.dto.resp.ServerInfoRespDto;
import io.github.discordchat.dto.resp.ServerWithMembersWithProfileRespDto;
import io.github.discordchat.service.ServerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @classname ServerController
 * @description TODO
 * @date 2024/6/17
 * @created by lwq
 */

@Tag(name = "ServerController", description = "服务器模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_SERVER_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class ServerController {
    private final ServerService serverService;

     private final WebSocketHandler webSocketHandler;
    @PostMapping
    public RestResp<ServerInfoCreateRespDto> create(@Valid @RequestBody ServerInfoCreateReqDto dto) {
        return serverService.create(UserHolder.getUserId(), dto);
    }

    @GetMapping("{id}")
    public RestResp<ServerWithMembersWithProfileRespDto> getServerInfo(@PathVariable("id") Long serverId) {
        return serverService.getServerInfo(UserHolder.getUserId(), serverId);
    }

    @GetMapping("/initial-channel/{id}")
    public RestResp<ChannelInfoRespDto> getInitialChannel(@PathVariable("id") Long serverId) {
        return serverService.getInitialChannel(UserHolder.getUserId(), serverId);
    }

    @GetMapping("/invite/{inviteCode}")
    public RestResp<ServerInfoRespDto> getServerInfoByInviteCode(@PathVariable("inviteCode") String inviteCode) {
        return serverService.getServerInfoByInviteCode(inviteCode);
    }

    @GetMapping("list")
    public RestResp<List<ServerInfoRespDto>> listServerInfo() {
        return serverService.listServerInfo(UserHolder.getUserId());
    }

    @PatchMapping("{id}/invite-code")
    public RestResp<ServerInfoRespDto> getInviteCode(@PathVariable Long id) {
        return serverService.updateInviteCode(UserHolder.getUserId(), id);
    }

    @PatchMapping("{id}")
    public RestResp<ServerInfoRespDto> editServerInfo(@PathVariable Long id,@Valid @RequestBody ServerInfoCreateReqDto dto) {
        return serverService.editServerInfo(UserHolder.getUserId(), id ,dto);
    }

    @PatchMapping("leave/{id}")
    public RestResp<Void> leaveServer(@PathVariable Long id) {
        RestResp<Void> voidRestResp = serverService.leaveServer(UserHolder.getUserId(), id);
           try {
            String key = String.format("server:%s:member:leave", id);
            webSocketHandler.broadcastToChannel(key, "leave");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return voidRestResp;
    }

    @DeleteMapping("{id}")
    public RestResp<Void> deleteServer(@PathVariable Long id) {
        return serverService.deleteServer(UserHolder.getUserId(), id);
    }
}
