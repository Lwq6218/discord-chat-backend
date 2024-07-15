package io.github.discordchat.service;

import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dto.resp.MemberInfoCreateRespDto;
import io.github.discordchat.dto.resp.MemberInfoRespDto;
import io.github.discordchat.dto.resp.MemberInfoWithProfileRespDto;
import io.github.discordchat.dto.resp.ServerWithMembersWithProfileRespDto;

import java.util.List;

/**
 * @classname MemberService
 * @description TODO
 * @date 2024/6/17
 * @created by lwq
 */
public interface MemberService {

    /**
     * 成员创建
     *
     * @param
     * @return
     */
    RestResp<MemberInfoCreateRespDto> create(Long profileId, Long serverId, int role);

    RestResp<List<MemberInfoRespDto>> listMemberByServerId(Long userId, Long serverId);

    RestResp<ServerWithMembersWithProfileRespDto> updateMemberRole(Long userId, Long serverId, Long id, Integer role);

    RestResp<Void> deleteMember(Long userId, Long serverId, Long id);

    RestResp<Long> createMemberByInviteCode(Long userId, String inviteCode);

    RestResp<List<MemberInfoWithProfileRespDto>> listMemberWithProfile(Long userId, Long serverId);

    RestResp<MemberInfoRespDto> getMemberByServerId(Long userId, Long serverId);
}
