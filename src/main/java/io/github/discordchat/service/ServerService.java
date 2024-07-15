package io.github.discordchat.service;

import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dto.req.ServerInfoCreateReqDto;
import io.github.discordchat.dto.resp.ChannelInfoRespDto;
import io.github.discordchat.dto.resp.ServerInfoCreateRespDto;
import io.github.discordchat.dto.resp.ServerInfoRespDto;
import io.github.discordchat.dto.resp.ServerWithMembersWithProfileRespDto;

import java.util.List;

public interface ServerService {

    /**
     * 服务器创建
     *
     * @param dto 创建参数
     * @return
     */
    RestResp<ServerInfoCreateRespDto> create(Long userId, ServerInfoCreateReqDto dto);


    RestResp<ServerWithMembersWithProfileRespDto> getServerInfo(Long userId, Long serverId);

    RestResp<List<ServerInfoRespDto>> listServerInfo(Long userId);

    RestResp<ServerInfoRespDto> updateInviteCode(Long userId, Long id);

    RestResp<ServerInfoRespDto> getServerInfoByInviteCode(String inviteCode);

    RestResp<Void> leaveServer(Long userId, Long id);

    RestResp<Void> deleteServer(Long userId, Long id);

    RestResp<ChannelInfoRespDto> getInitialChannel(Long userId, Long serverId);

    RestResp<ServerInfoRespDto> editServerInfo(Long userId, Long id, ServerInfoCreateReqDto dto);
}
