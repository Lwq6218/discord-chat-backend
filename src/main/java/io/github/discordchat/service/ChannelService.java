package io.github.discordchat.service;

import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dto.req.ChannelCreateReqDto;
import io.github.discordchat.dto.resp.ChannelCreateRespDto;
import io.github.discordchat.dto.resp.ChannelInfoRespDto;

import java.util.List;

public interface ChannelService {

    RestResp<ChannelCreateRespDto> create(Long serverId, Long profileId, String channelName, int type);

    RestResp<List<ChannelInfoRespDto>> listChannelByServerId(Long serverId);

    RestResp<ChannelCreateRespDto> create(Long serverId, Long userId, ChannelCreateReqDto dto);

    RestResp<Void> delete(Long id, Long serverId, Long userId);

    RestResp<ChannelInfoRespDto> getChannelById(Long id);

    RestResp<Void> updateChannel(Long id, Long serverId, Long userId, ChannelCreateReqDto dto);
}
