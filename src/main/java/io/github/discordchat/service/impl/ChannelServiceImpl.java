package io.github.discordchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.DatabaseConst;
import io.github.discordchat.dao.entity.ChannelInfo;
import io.github.discordchat.dao.entity.MemberInfo;
import io.github.discordchat.dao.entity.ServerInfo;
import io.github.discordchat.dao.mapper.ChannelInfoMapper;
import io.github.discordchat.dao.mapper.MemberInfoMapper;
import io.github.discordchat.dao.mapper.ServerInfoMapper;
import io.github.discordchat.dto.req.ChannelCreateReqDto;
import io.github.discordchat.dto.resp.ChannelCreateRespDto;
import io.github.discordchat.dto.resp.ChannelInfoRespDto;
import io.github.discordchat.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @classname ChannelServiceImpl
 * @description TODO
 * @date 2024/6/17
 * @created by lwq
 */
@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final ChannelInfoMapper channelInfoMapper;

    private final ServerInfoMapper serverInfoMapper;


    private final MemberInfoMapper memberInfoMapper;

    @Override
    public RestResp<ChannelCreateRespDto> create(Long serverId, Long profileId, String channelName, int type) {
        validateServer(serverId);
        validateMemberRole(serverId, profileId);
        //校验频道是否存在
        QueryWrapper<ChannelInfo> channelInfoQueryWrapper = new QueryWrapper<>();
        channelInfoQueryWrapper.eq(DatabaseConst.ChannelInfoTable.COLUMN_SERVER_ID, serverId).eq(DatabaseConst.ChannelInfoTable.COLUMN_CHANNEL_NAME, channelName).last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        if (Objects.nonNull(channelInfoMapper.selectOne(channelInfoQueryWrapper))) {
            throw new BusinessException(ErrorCodeEnum.CHANNEL_EXIST);
        }
        //创建频道
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setServerId(serverId);
        channelInfo.setName(channelName);
        channelInfo.setProfileId(profileId);
        channelInfo.setType(DatabaseConst.ChannelTypeEnum.fromValue(type).getValue());
        channelInfo.setCreateTime(LocalDateTime.now());
        channelInfo.setUpdateTime(LocalDateTime.now());
        channelInfoMapper.insert(channelInfo);
        return RestResp.ok(ChannelCreateRespDto.builder().id(channelInfo.getId()).build());
    }


    @Override
    public RestResp<List<ChannelInfoRespDto>> listChannelByServerId(Long serverId) {
        //校验服务器是否存在
        validateServer(serverId);
        QueryWrapper<ChannelInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.ChannelInfoTable.COLUMN_SERVER_ID, serverId);
        List<ChannelInfo> channelInfos = channelInfoMapper.selectList(queryWrapper);
        List<ChannelInfoRespDto> channelInfoRespDtos = channelInfos.stream()
                .map(channelInfo ->
                        ChannelInfoRespDto.builder()
                                .id(channelInfo.getId())
                                .name(channelInfo.getName())
                                .serverId(channelInfo.getServerId())
                                .type(DatabaseConst.ChannelTypeEnum.fromValue(channelInfo.getType()).getValue())
                                .build())
                .toList();

        return RestResp.ok(channelInfoRespDtos);
    }

    @Override
    public RestResp<ChannelCreateRespDto> create(Long serverId, Long userId, ChannelCreateReqDto dto) {
        return create(serverId, userId, dto.getName(), dto.getType());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResp<Void> delete(Long id, Long serverId, Long userId) {
        //校验服务器是否存在
        validateServer(serverId);
        //校验频道是否存在
        ChannelInfo channelInfo = channelInfoMapper.selectById(id);
        if (Objects.isNull(channelInfo)) {
            throw new BusinessException(ErrorCodeEnum.CHANNEL_NOT_EXIST);
        }
        //校验用户是否有权限 MODERATOR ADMIN有权限，GUEST没有权限
        validateMemberRole(serverId, userId);

        //删除频道,不删除默认的‘general’频道
        if (channelInfo.getName().equals(DatabaseConst.ChannelInfoTable.DEFAULT_CHANNEL_NAME)) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
        channelInfoMapper.deleteById(id);

        //删除频道消息
        //TODO
        return RestResp.ok();
    }

    @Override
    public RestResp<ChannelInfoRespDto> getChannelById(Long id) {
        ChannelInfo channelInfo = channelInfoMapper.selectById(id);
        if (Objects.isNull(channelInfo)) {
            throw new BusinessException(ErrorCodeEnum.CHANNEL_NOT_EXIST);
        }
        return RestResp.ok(ChannelInfoRespDto.builder()
                .id(channelInfo.getId())
                .name(channelInfo.getName())
                .serverId(channelInfo.getServerId())
                .type(DatabaseConst.ChannelTypeEnum.fromValue(channelInfo.getType()).getValue())
                .build());
    }

    @Override
    public RestResp<Void> updateChannel(Long id, Long serverId, Long userId, ChannelCreateReqDto dto) {
        //校验服务器是否存在
        validateServer(serverId);
        //校验频道是否存在
        ChannelInfo channelInfo = channelInfoMapper.selectById(id);
        if (Objects.isNull(channelInfo)) {
            throw new BusinessException(ErrorCodeEnum.CHANNEL_NOT_EXIST);
        }
        //校验用户是否有权限 MODERATOR ADMIN有权限，GUEST没有权限
        validateMemberRole(serverId, userId);
        //修改频道
        channelInfo.setName(dto.getName());
        channelInfo.setType(DatabaseConst.ChannelTypeEnum.fromValue(dto.getType()).getValue());
        channelInfo.setUpdateTime(LocalDateTime.now());
        channelInfoMapper.updateById(channelInfo);
        return RestResp.ok();
    }

    private void validateServer(Long serverId) {
        //校验服务器是否存在
        ServerInfo serverInfo = serverInfoMapper.selectById(serverId);
        if (Objects.isNull(serverInfo)) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
    }

    private void validateMemberRole(Long serverId, Long profileId) {
        //校验用户是否有权限 MODERATOR ADMIN有权限，GUEST没有权限
        QueryWrapper<MemberInfo> memberInfoQueryWrapper = new QueryWrapper<>();
        memberInfoQueryWrapper.eq(DatabaseConst.ChannelInfoTable.COLUMN_PROFILE_ID, profileId).eq(DatabaseConst.ChannelInfoTable.COLUMN_SERVER_ID, serverId).last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        MemberInfo memberInfo = memberInfoMapper.selectOne(memberInfoQueryWrapper);
        if (memberInfo.getRole().equals(DatabaseConst.MemberRoleEnum.GUEST.getValue())) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
    }
}
