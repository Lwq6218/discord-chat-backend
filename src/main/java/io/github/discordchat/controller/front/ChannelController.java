package io.github.discordchat.controller.front;

import io.github.discordchat.core.auth.UserHolder;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.req.ChannelCreateReqDto;
import io.github.discordchat.dto.resp.ChannelCreateRespDto;
import io.github.discordchat.dto.resp.ChannelInfoRespDto;
import io.github.discordchat.service.ChannelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @classname ChannelController
 * @description TODO
 * @date 2024/6/17
 * @created by lwq
 */
@Tag(name = "ChannelController", description = "频道模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_CHANNEL_URL_PREFIX)
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping("list/{serverId}")
    public RestResp<List<ChannelInfoRespDto>> listChannelByServerId(@PathVariable("serverId") Long serverId) {
        return channelService.listChannelByServerId(serverId);
    }

    @PostMapping
    public RestResp<ChannelCreateRespDto> create(@Valid @RequestBody ChannelCreateReqDto dto, @RequestParam(value = "serverId",required = true) Long serverId){
        return channelService.create(serverId, UserHolder.getUserId(),dto);
    }
    @DeleteMapping("{id}")
    public RestResp<Void> delete(@PathVariable("id") Long id, @RequestParam(value = "serverId",required = true) Long serverId){
        return channelService.delete(id,serverId,UserHolder.getUserId());
    }

    @GetMapping("{id}")
    public RestResp<ChannelInfoRespDto> getChannelById(@PathVariable("id") Long id){
        return channelService.getChannelById(id);
    }

    @PatchMapping("{id}")
    public RestResp<Void> updateChannel(@PathVariable("id") Long id, @RequestParam(value = "serverId",required = true) Long serverId, @Valid @RequestBody ChannelCreateReqDto dto){
        return channelService.updateChannel(id,serverId,UserHolder.getUserId(),dto);
    }
}
