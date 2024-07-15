package io.github.discordchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.DatabaseConst;
import io.github.discordchat.dao.entity.ChannelInfo;
import io.github.discordchat.dao.entity.MemberInfo;
import io.github.discordchat.dao.entity.Message;
import io.github.discordchat.dao.entity.ServerInfo;
import io.github.discordchat.dao.mapper.*;
import io.github.discordchat.dto.req.ServerInfoCreateReqDto;
import io.github.discordchat.dto.resp.*;
import io.github.discordchat.service.ChannelService;
import io.github.discordchat.service.MemberService;
import io.github.discordchat.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @classname ServerServiceImpl
 * @description TODO
 * @date 2024/6/17
 * @created by lwq
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final MemberService memberService;

    private final MemberInfoMapper memberInfoMapper;

    private final ChannelService channelService;

    private final ChannelInfoMapper channelInfoMapper;

    private final ServerInfoMapper serverInfoMapper;

    private final MessageMapper messageMapper;

    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResp<ServerInfoCreateRespDto> create(Long userId, ServerInfoCreateReqDto dto) {

        //校验服务器名称是否已经存在
        QueryWrapper<ServerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.ServerInfoTable.COLUMN_SERVERNAME, dto.getName())
                .eq(DatabaseConst.ServerInfoTable.COLUMN_PROFILE_ID, userId)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        ServerInfo serverInfo1 = serverInfoMapper.selectOne(queryWrapper);
        if (Objects.nonNull(serverInfo1)) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NAME_EXIST);
        }

        //保存服务器信息
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setName(dto.getName());
        log.info("userId:{}", userId);
        serverInfo.setProfileId(userId);
        serverInfo.setImageUrl(dto.getImageUrl());
        serverInfo.setInviteCode(IdWorker.get32UUID());
        serverInfo.setCreateTime(LocalDateTime.now());
        serverInfo.setUpdateTime(LocalDateTime.now());
        serverInfoMapper.insert(serverInfo);

        //创建成员信息,创建者默认为管理员
        memberService.create(userId, serverInfo.getId(), DatabaseConst.MemberRoleEnum.ADMIN.getValue());

        //创建默认频道,默认频道为general
        channelService.create(serverInfo.getId(), userId, DatabaseConst.ChannelInfoTable.DEFAULT_CHANNEL_NAME, DatabaseConst.ChannelTypeEnum.TEXT.getValue());

        return RestResp.ok(ServerInfoCreateRespDto.builder().id(serverInfo.getId()).name(serverInfo.getName()).build());

    }

    @Override
    public RestResp<ServerWithMembersWithProfileRespDto> getServerInfo(Long userId, Long serverId) {
        //校验服务器是否存在
        ServerInfo serverInfo = serverInfoMapper.selectById(serverId);
        if (Objects.isNull(serverInfo)) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        //查询服务器的所有成员信息
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, serverId);
        List<MemberInfo> memberInfos = memberInfoMapper.selectList(queryWrapper);

        return RestResp.ok(ServerWithMembersWithProfileRespDto.builder()
                .id(serverInfo.getId())
                .name(serverInfo.getName())
                .imageUrl(serverInfo.getImageUrl())
                .inviteCode(serverInfo.getInviteCode())
                .profileId(serverInfo.getProfileId())
                .members(memberInfos.stream().map(memberInfo -> {
                    return MemberInfoWithProfileRespDto.builder()
                            .id(memberInfo.getId())
                            .profileId(memberInfo.getProfileId())
                            .role(memberInfo.getRole())
                            .profile(userProfileMapper.selectById(memberInfo.getProfileId())).build();
                }).collect(Collectors.toList())).build());
    }

    @Override
    public RestResp<List<ServerInfoRespDto>> listServerInfo(Long userId) {
        //查询该成员的所有的服务器列表,包括创建的和加入的
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_PROFILE_ID, userId);
        //查询成员信息
        List <MemberInfo> memberInfos = memberInfoMapper.selectList(queryWrapper);
        //查询服务器信息
        return RestResp.ok(memberInfos.stream().map(memberInfo -> {
            ServerInfo serverInfo = serverInfoMapper.selectById(memberInfo.getServerId());
            return ServerInfoRespDto.builder()
                    .id(serverInfo.getId())
                    .profileId(serverInfo.getProfileId())
                    .name(serverInfo.getName())
                    .imageUrl(serverInfo.getImageUrl())
                    .inviteCode(serverInfo.getInviteCode()).build();
        }).collect(Collectors.toList()));

    }

    @Override
    public RestResp<ServerInfoRespDto> updateInviteCode(Long userId, Long id) {
        //校验服务器是否存在
        ServerInfo serverInfo = serverInfoMapper.selectById(id);
        if (Objects.isNull(serverInfo)) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        //更新邀请码
        serverInfo.setInviteCode(IdWorker.get32UUID());
        serverInfo.setUpdateTime(LocalDateTime.now());
        serverInfoMapper.updateById(serverInfo);
        return RestResp.ok(ServerInfoRespDto.builder()
                .id(serverInfo.getId())
                .name(serverInfo.getName())
                .profileId(serverInfo.getProfileId())
                .imageUrl(serverInfo.getImageUrl())
                .inviteCode(serverInfo.getInviteCode()).build());
    }

    @Override
    public RestResp<ServerInfoRespDto> getServerInfoByInviteCode(String inviteCode) {
        //根据邀请码查询服务器信息
        QueryWrapper<ServerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.ServerInfoTable.COLUMN_INVITE_CODE, inviteCode)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        ServerInfo serverInfo = serverInfoMapper.selectOne(queryWrapper);
        if (Objects.isNull(serverInfo)) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        return RestResp.ok(ServerInfoRespDto.builder()
                .id(serverInfo.getId())
                .profileId(serverInfo.getProfileId())
                .name(serverInfo.getName())
                .imageUrl(serverInfo.getImageUrl())
                .inviteCode(serverInfo.getInviteCode()).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResp<Void> leaveServer(Long userId, Long id) {
        //校验服务器是否存在
        ServerInfo serverInfo = serverInfoMapper.selectById(id);
        if (Objects.isNull(serverInfo)) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        //校验成员是否存在
        QueryWrapper<MemberInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_PROFILE_ID, userId)
                .eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, id)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        MemberInfo memberInfo = memberInfoMapper.selectOne(queryWrapper);
        if (Objects.isNull(memberInfo)) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_NOT_EXIST);
        }
        //成员不能是服务器的创建者
        if (Objects.equals(memberInfo.getProfileId(), serverInfo.getProfileId())) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }

        //删除成员的发送过的所有信息
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        messageQueryWrapper.eq(DatabaseConst.MessageTable.COLUMN_MEMBER_ID, memberInfo.getId());
        messageMapper.delete(messageQueryWrapper);

        //删除成员信息
        memberInfoMapper.deleteById(memberInfo.getId());
        return RestResp.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResp<Void> deleteServer(Long userId, Long id) {
        //校验服务器是否存在
        ServerInfo serverInfo = serverInfoMapper.selectById(id);
        if (Objects.isNull(serverInfo)) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        //校验用户是否是服务器的创建者
        if (!Objects.equals(serverInfo.getProfileId(), userId)) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
        //删除服务器
        serverInfoMapper.deleteById(id);

        //删除服务器的所有成员信息
        QueryWrapper<MemberInfo> memberInfoQueryWrapper = new QueryWrapper<>();
        memberInfoQueryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, id);
        memberInfoMapper.delete(memberInfoQueryWrapper);

        //删除服务器的所有频道信息
        QueryWrapper<ChannelInfo> channelInfoQueryWrapper = new QueryWrapper<>();
        channelInfoQueryWrapper.eq(DatabaseConst.ChannelInfoTable.COLUMN_SERVER_ID, id);
        //删除频道的所有消息
        channelInfoMapper.selectList(channelInfoQueryWrapper).forEach(channelInfo -> {
            QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
            messageQueryWrapper.eq(DatabaseConst.MessageTable.COLUMN_CHANNEL_ID, channelInfo.getId());
            messageMapper.delete(messageQueryWrapper);
        });
        channelInfoMapper.delete(channelInfoQueryWrapper);
        return RestResp.ok();

    }

    @Override
    public RestResp<ChannelInfoRespDto> getInitialChannel(Long userId, Long serverId) {

        QueryWrapper<ChannelInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.ChannelInfoTable.COLUMN_SERVER_ID, serverId)
                .eq(DatabaseConst.ChannelInfoTable.COLUMN_CHANNEL_NAME, DatabaseConst.ChannelInfoTable.DEFAULT_CHANNEL_NAME)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        ChannelInfo channelInfo = channelInfoMapper.selectOne(queryWrapper);
        if (Objects.isNull(channelInfo)) {
            throw new BusinessException(ErrorCodeEnum.CHANNEL_NOT_EXIST);
        }
        return RestResp.ok(ChannelInfoRespDto.builder()
                .id(channelInfo.getId())
                .name(channelInfo.getName())
                .serverId(channelInfo.getServerId())
                .type(channelInfo.getType()).build());
    }

    @Override
    public RestResp<ServerInfoRespDto> editServerInfo(Long userId, Long id, ServerInfoCreateReqDto dto) {
        //校验服务器是否存在
        ServerInfo serverInfo = serverInfoMapper.selectById(id);
        if (Objects.isNull(serverInfo)) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        //校验用户是否是服务器的创建者
        if (!Objects.equals(serverInfo.getProfileId(), userId)) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
        //校验服务器名称是否已经存在
        QueryWrapper<ServerInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.ServerInfoTable.COLUMN_SERVERNAME, dto.getName())
                .eq(DatabaseConst.ServerInfoTable.COLUMN_PROFILE_ID, userId)
                .ne(DatabaseConst.CommonColumnEnum.ID.getName(), id)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        ServerInfo serverInfo1 = serverInfoMapper.selectOne(queryWrapper);
        if (Objects.nonNull(serverInfo1)) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NAME_EXIST);
        }
        //更新服务器信息
        serverInfo.setName(dto.getName());
        serverInfo.setImageUrl(dto.getImageUrl());
        serverInfo.setUpdateTime(LocalDateTime.now());
        serverInfoMapper.updateById(serverInfo);
        return RestResp.ok(ServerInfoRespDto.builder()
                .id(serverInfo.getId())
                .name(serverInfo.getName())
                .profileId(serverInfo.getProfileId())
                .imageUrl(serverInfo.getImageUrl())
                .inviteCode(serverInfo.getInviteCode()).build());
    }

}
