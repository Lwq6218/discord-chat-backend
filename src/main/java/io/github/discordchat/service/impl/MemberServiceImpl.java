package io.github.discordchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.DatabaseConst;
import io.github.discordchat.dao.entity.ChannelInfo;
import io.github.discordchat.dao.entity.MemberInfo;
import io.github.discordchat.dao.entity.Message;
import io.github.discordchat.dao.entity.ServerInfo;
import io.github.discordchat.dao.mapper.*;
import io.github.discordchat.dto.resp.MemberInfoCreateRespDto;
import io.github.discordchat.dto.resp.MemberInfoRespDto;
import io.github.discordchat.dto.resp.MemberInfoWithProfileRespDto;
import io.github.discordchat.dto.resp.ServerWithMembersWithProfileRespDto;
import io.github.discordchat.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @classname MemberServiceImpl
 * @description TODO
 * @date 2024/6/17
 * @created by lwq
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final UserProfileMapper userProfileMapper;

    private final ServerInfoMapper serverInfoMapper;

    private final MemberInfoMapper memberInfoMapper;

    private final MessageMapper messageMapper;
    private final ChannelInfoMapper channelInfoMapper;

    @Override
    public RestResp<MemberInfoCreateRespDto> create(Long profileId, Long serverId, int role) {
        //查询服务器是否存在
        ServerInfo serverInfo = serverInfoMapper.selectById(serverId);
        if (serverInfo == null) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        //查询用户是否已经是服务器成员
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_PROFILE_ID, profileId)
                .eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, serverId)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        if (Objects.nonNull(memberInfoMapper.selectOne(queryWrapper))) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_EXIST);
        }
        //创建成员
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setProfileId(profileId);
        memberInfo.setServerId(serverId);
        memberInfo.setRole(DatabaseConst.MemberRoleEnum.fromValue(role).getValue());
        memberInfo.setCreateTime(LocalDateTime.now());
        memberInfo.setUpdateTime(LocalDateTime.now());
        memberInfoMapper.insert(memberInfo);

        return RestResp.ok(MemberInfoCreateRespDto.builder().id(memberInfo.getId()).build());
    }

    @Override
    public RestResp<List<MemberInfoRespDto>> listMemberByServerId(Long userId, Long serverId) {
        //查询服务器是否存在
        validateServerExisted(serverId);
        //查询该服务器的所有成员
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, serverId);
        List<MemberInfo> memberInfos = memberInfoMapper.selectList(queryWrapper);
        return RestResp.ok(memberInfos.stream().map(memberInfo -> MemberInfoRespDto.builder()
                .id(memberInfo.getId())
                .profileId(memberInfo.getProfileId())
                .name(userProfileMapper.selectById(memberInfo.getProfileId()).getName())
                .role(memberInfo.getRole())
                .build()).collect(Collectors.toList()));
    }

    @Override
    public RestResp<ServerWithMembersWithProfileRespDto> updateMemberRole(Long userId, Long serverId, Long id, Integer role) {
        //查询服务器是否存在
        ServerInfo serverInfo = validateServerExisted(serverId);
        //查询成员是否存在
        MemberInfo memberInfo = memberInfoMapper.selectById(id);
        if (Objects.isNull(memberInfo)) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_NOT_EXIST);
        }
        //校验用户是否有权限修改成员角色,ADMIN有权限
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_PROFILE_ID, userId)
                .eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, serverId)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        MemberInfo adminMemberInfo = memberInfoMapper.selectOne(queryWrapper);
        if (adminMemberInfo.getRole() != DatabaseConst.MemberRoleEnum.ADMIN.getValue()) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
        //修改成员角色
        memberInfo.setRole(DatabaseConst.MemberRoleEnum.fromValue(role).getValue());
        memberInfo.setUpdateTime(LocalDateTime.now());
        memberInfoMapper.updateById(memberInfo);
        //查询该服务器的所有成员，并返回所有成员信息
        QueryWrapper<MemberInfo> memberInfoQueryWrapper = new QueryWrapper<>();
        memberInfoQueryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, serverId);
        List<MemberInfo> memberInfos = memberInfoMapper.selectList(memberInfoQueryWrapper);
        return RestResp.ok(ServerWithMembersWithProfileRespDto.builder()
                .id(serverId)
                .name(serverInfo.getName())
                .profileId(serverInfo.getProfileId())
                .inviteCode(serverInfo.getInviteCode())
                .members(memberInfos.stream().map(memberInfo1 -> MemberInfoWithProfileRespDto.builder()
                        .id(memberInfo1.getId())
                        .role(memberInfo1.getRole())
                        .profileId(memberInfo1.getProfileId())
                        .profile(userProfileMapper.selectById(memberInfo1.getProfileId()))
                        .build()).collect(Collectors.toList())).build());
    }

    private ServerInfo validateServerExisted(Long serverId) {
        //查询服务器是否存在
        ServerInfo serverInfo = serverInfoMapper.selectById(serverId);
        if (Objects.isNull(serverInfo)) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        return serverInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResp<Void> deleteMember(Long userId, Long serverId, Long id) {
        //查询服务器是否存在
        validateServerExisted(serverId);
        //查询成员是否存在
        MemberInfo memberInfo = memberInfoMapper.selectById(id);
        if (Objects.isNull(memberInfo)) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_NOT_EXIST);
        }
        //校验用户是否有权限删除成员,ADMIN有权限
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_PROFILE_ID, userId)
                .eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, serverId)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        MemberInfo adminMemberInfo = memberInfoMapper.selectOne(queryWrapper);
        if (adminMemberInfo.getRole() != DatabaseConst.MemberRoleEnum.ADMIN.getValue()) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
        //删除成员在服务器下有发过消息的频道下的所有消息
        //先查询该成员在服务器下有发过消息的频道
        QueryWrapper<ChannelInfo> channelInfoQueryWrapper = new QueryWrapper<>();
        channelInfoQueryWrapper.eq(DatabaseConst.ChannelInfoTable.COLUMN_SERVER_ID, serverId)
                .eq(DatabaseConst.ChannelInfoTable.COLUMN_PROFILE_ID, userId)
                .select(DatabaseConst.CommonColumnEnum.ID.getName());
        List<ChannelInfo> channelIds = channelInfoMapper.selectList(channelInfoQueryWrapper);

        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        //删除成员在服务器下有发过消息的频道下的所有消息
        channelIds.forEach(channelId -> {
            messageQueryWrapper.eq(DatabaseConst.MessageTable.COLUMN_MEMBER_ID, id)
                    .eq(DatabaseConst.MessageTable.COLUMN_CHANNEL_ID, channelId.getId());
            messageMapper.delete(messageQueryWrapper);
        });
        //删除成员
        memberInfoMapper.deleteById(id);

        return RestResp.ok();
    }

    @Override
    public RestResp<Long> createMemberByInviteCode(Long userId, String inviteCode) {
        //根据邀请码查询服务器信息
        QueryWrapper<ServerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.ServerInfoTable.COLUMN_INVITE_CODE, inviteCode)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        ServerInfo serverInfo = serverInfoMapper.selectOne(queryWrapper);
        if (Objects.isNull(serverInfo)) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        //查询用户是否已经是服务器成员
        QueryWrapper<MemberInfo> memberInfoQueryWrapper = new QueryWrapper<>();
        memberInfoQueryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_PROFILE_ID, userId)
                .eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, serverInfo.getId())
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        if (Objects.nonNull(memberInfoMapper.selectOne(memberInfoQueryWrapper))) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_EXIST);
        }
        //创建成员
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setProfileId(userId);
        memberInfo.setServerId(serverInfo.getId());
        memberInfo.setRole(DatabaseConst.MemberRoleEnum.GUEST.getValue());
        memberInfo.setCreateTime(LocalDateTime.now());
        memberInfo.setUpdateTime(LocalDateTime.now());
        memberInfoMapper.insert(memberInfo);

        return RestResp.ok(memberInfo.getServerId());
    }

    @Override
    public RestResp<List<MemberInfoWithProfileRespDto>> listMemberWithProfile(Long userId, Long serverId) {
        //查询服务器是否存在
        validateServerExisted(serverId);
        //查询该服务器的所有成员
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, serverId);
        List<MemberInfo> memberInfos = memberInfoMapper.selectList(queryWrapper);
        return RestResp.ok(memberInfos.stream().map(memberInfo -> MemberInfoWithProfileRespDto.builder()
                .id(memberInfo.getId())
                .role(memberInfo.getRole())
                .profileId(memberInfo.getProfileId())
                .profile(userProfileMapper.selectById(memberInfo.getProfileId()))
                .build()).collect(Collectors.toList()));
    }

    @Override
    public RestResp<MemberInfoRespDto> getMemberByServerId(Long userId, Long serverId) {
        //查询服务器是否存在
        validateServerExisted(serverId);
        //查询该服务器的成员
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_PROFILE_ID, userId)
                .eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, serverId)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        MemberInfo memberInfo = memberInfoMapper.selectOne(queryWrapper);
        if (Objects.isNull(memberInfo)) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_NOT_EXIST);
        }
        return RestResp.ok(MemberInfoRespDto.builder()
                .id(memberInfo.getId())
                .profileId(memberInfo.getProfileId())
                .name(userProfileMapper.selectById(memberInfo.getProfileId()).getName())
                .role(memberInfo.getRole())
                .build());
    }
}
