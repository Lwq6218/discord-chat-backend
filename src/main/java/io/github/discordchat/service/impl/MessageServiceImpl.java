package io.github.discordchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.DatabaseConst;
import io.github.discordchat.dao.entity.*;
import io.github.discordchat.dao.mapper.*;
import io.github.discordchat.dto.req.MessageReqDto;
import io.github.discordchat.dto.resp.MemberInfoWithProfileRespDto;
import io.github.discordchat.dto.resp.MessageRespDto;
import io.github.discordchat.dto.resp.MessagesWithMemberWithProfileRespDto;
import io.github.discordchat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @classname MessageServiceImpl
 * @description TODO
 * @date 2024/6/20
 * @created by lwq
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final ServerInfoMapper serverInfoMapper;
    private final MessageMapper messageMapper;
    private final ChannelInfoMapper channelInfoMapper;
    private final MemberInfoMapper memberInfoMapper;

    private final UserProfileMapper userProfileMapper;

    @Override
    public RestResp<MessagesWithMemberWithProfileRespDto> listMessage(Long channelId, Long cursor) {
        final String MESSAGES_BATCH = "limit 10";
        List<MessageRespDto> collect;
        //判断是否有游标
        if (cursor == null) {
            //没有游标，直接查询最新的消息的前10条
            QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.MessageTable.COLUMN_CHANNEL_ID, channelId)
                    .orderByDesc(DatabaseConst.CommonColumnEnum.CREATE_TIME.getName())
                    .last(MESSAGES_BATCH);
            List<Message> messages = messageMapper.selectList(queryWrapper);

            //根据查询到的消息列表查询它门的成员信息
            collect = messages.stream().map(message -> {
                MemberInfo memberInfo = memberInfoMapper.selectById(message.getMemberId());
                UserProfile userProfile = userProfileMapper.selectById(memberInfo.getProfileId());
                return MessageRespDto.builder()
                        .id(message.getId())
                        .content(message.getContent())
                        .channelId(message.getChannelId())
                        .memberId(message.getMemberId())
                        .fileUrl(message.getFileUrl())
                        .deleted(message.getDeleted())
                        .createTime(message.getCreateTime())
                        .updateTime(message.getUpdateTime())
                        .member(MemberInfoWithProfileRespDto.builder()
                                .id(memberInfo.getId())
                                .profileId(memberInfo.getProfileId())
                                .role(memberInfo.getRole())
                                .profile(userProfile)
                                .build())
                        .build();

            }).toList();

        } else {
            //游标就是消息的ID
            //有游标，查询游标后的10条消息
            QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.MessageTable.COLUMN_CHANNEL_ID, channelId)
                    .lt(DatabaseConst.CommonColumnEnum.ID.getName(), cursor)
                    .orderByDesc(DatabaseConst.CommonColumnEnum.CREATE_TIME.getName())
                    .last(MESSAGES_BATCH);
            List<Message> messages = messageMapper.selectList(queryWrapper);
            //根据查询到的消息列表查询它门的成员信息
            collect = messages.stream().map(message -> {
                MemberInfo memberInfo = memberInfoMapper.selectById(message.getMemberId());
                UserProfile userProfile = userProfileMapper.selectById(memberInfo.getProfileId());
                return MessageRespDto.builder()
                        .id(message.getId())
                        .content(message.getContent())
                        .channelId(message.getChannelId())
                        .memberId(message.getMemberId())
                        .fileUrl(message.getFileUrl())
                        .deleted(message.getDeleted())
                        .createTime(message.getCreateTime())
                        .updateTime(message.getUpdateTime())
                        .member(MemberInfoWithProfileRespDto.builder()
                                .id(memberInfo.getId())
                                .profileId(memberInfo.getProfileId())
                                .role(memberInfo.getRole())
                                .profile(userProfile)
                                .build())
                        .build();

            }).toList();
        }
        Long nextCursor = null;
        if (collect.size() == 10) {
            nextCursor = collect.get(9).getId();
        }
        return RestResp.ok(MessagesWithMemberWithProfileRespDto.builder()
                .items(collect)
                .nextCursor(nextCursor)
                .build());
    }

    @Override
    public MessageRespDto create(Long serverId, Long channelId, MessageReqDto dto, Long userId) {
        //查询服务器是否存在
        ServerInfo serverInfo = serverInfoMapper.selectById(serverId);
        if (serverInfo == null) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        //查询用户是否是服务器成员
        QueryWrapper<MemberInfo> memberInfoQueryWrapper = new QueryWrapper<>();
        memberInfoQueryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_PROFILE_ID, userId)
                .eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID, serverInfo.getId())
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        MemberInfo memberInfo = memberInfoMapper.selectOne(memberInfoQueryWrapper);
        if (Objects.isNull(memberInfo)) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_NOT_EXIST);
        }
        //查询频道是否存在
        ChannelInfo channelInfo = channelInfoMapper.selectById(channelId);
        if (channelInfo == null) {
            throw new BusinessException(ErrorCodeEnum.CHANNEL_NOT_EXIST);
        }
        //创建消息
        Message message = new Message();
        message.setChannelId(channelId);
        message.setMemberId(memberInfo.getId());
        //判断消息是否是文件
        if (dto.getIsFile().equals("1") ) {
            message.setFileUrl(dto.getContent());
        }else {
            message.setContent(dto.getContent());
        }
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        int insert = messageMapper.insert(message);
        if (insert == 0) {
            throw new BusinessException(ErrorCodeEnum.USER_ERROR);
        }
        return MessageRespDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .channelId(message.getChannelId())
                .memberId(message.getMemberId())
                .fileUrl(message.getFileUrl())
                .deleted(message.getDeleted())
                .createTime(message.getCreateTime())
                .updateTime(message.getUpdateTime())
                .member(MemberInfoWithProfileRespDto.builder()
                        .id(message.getMemberId())
                        .profileId(memberInfo.getProfileId())
                        .role(memberInfo.getRole())
                        .profile(userProfileMapper.selectById(memberInfo.getProfileId()))
                        .build())
                .build();

    }

    @Override
    public MessageRespDto deleteMessage(Long userId, Long id, Long channelId, Long serverId) {
        //查询服务器是否存在
        ServerInfo serverInfo = serverInfoMapper.selectById(serverId);
        if (serverInfo == null) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        //查询用户是否是服务器成员
        QueryWrapper<MemberInfo> memberInfoQueryWrapper = new QueryWrapper<>();
        memberInfoQueryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_PROFILE_ID, userId)
                .eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID,serverId)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        MemberInfo memberInfo = memberInfoMapper.selectOne(memberInfoQueryWrapper);
        if (Objects.isNull(memberInfo)) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_NOT_EXIST);
        }
        //查询消息是否存在
        Message message = messageMapper.selectById(id);
        if (Objects.isNull(message)) {
            throw new BusinessException(ErrorCodeEnum.MESSAGE_NOT_EXIST);
        }
        //查询频道是否存在
        ChannelInfo channelInfo = channelInfoMapper.selectById(channelId);
        if (channelInfo == null) {
            throw new BusinessException(ErrorCodeEnum.CHANNEL_NOT_EXIST);
        }
        //删除消息,逻辑删除
        message.setDeleted(1);
        message.setUpdateTime(LocalDateTime.now());
        message.setContent("This message has been deleted");
        message.setFileUrl(null);
        messageMapper.updateById(message);

return MessageRespDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .channelId(message.getChannelId())
                .memberId(message.getMemberId())
                .fileUrl(message.getFileUrl())
                .deleted(message.getDeleted())
                .createTime(message.getCreateTime())
                .updateTime(message.getUpdateTime())
                .member(MemberInfoWithProfileRespDto.builder()
                        .id(message.getMemberId())
                        .profileId(memberInfo.getProfileId())
                        .role(memberInfo.getRole())
                        .profile(userProfileMapper.selectById(memberInfo.getProfileId()))
                        .build())
                .build();
    }

    @Override
    public MessageRespDto updateMessage(Long userId, Long id, Long channelId, Long serverId, String content) {
        //查询服务器是否存在
        ServerInfo serverInfo = serverInfoMapper.selectById(serverId);
        if (serverInfo == null) {
            throw new BusinessException(ErrorCodeEnum.SERVER_NOT_EXIST);
        }
        //查询用户是否是服务器成员
        QueryWrapper<MemberInfo> memberInfoQueryWrapper = new QueryWrapper<>();
        memberInfoQueryWrapper.eq(DatabaseConst.MemberInfoTable.COLUMN_PROFILE_ID, userId)
                .eq(DatabaseConst.MemberInfoTable.COLUMN_SERVER_ID,serverId)
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        MemberInfo memberInfo = memberInfoMapper.selectOne(memberInfoQueryWrapper);
        if (Objects.isNull(memberInfo)) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_NOT_EXIST);
        }
        //查询消息是否存在
        Message message = messageMapper.selectById(id);
        if (Objects.isNull(message)) {
            throw new BusinessException(ErrorCodeEnum.MESSAGE_NOT_EXIST);
        }
        //查询频道是否存在
        ChannelInfo channelInfo = channelInfoMapper.selectById(channelId);
        if (channelInfo == null) {
            throw new BusinessException(ErrorCodeEnum.CHANNEL_NOT_EXIST);
        }
        //查询消息的拥有者是否是当前用户
        if (!message.getMemberId().equals(memberInfo.getId())) {
            throw new BusinessException(ErrorCodeEnum.MESSAGE_NOT_EXIST);
        }
        //更新消息
        message.setContent(content);
        message.setUpdateTime(LocalDateTime.now());
        messageMapper.updateById(message);

        return MessageRespDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .channelId(message.getChannelId())
                .memberId(message.getMemberId())
                .fileUrl(message.getFileUrl())
                .deleted(message.getDeleted())
                .createTime(message.getCreateTime())
                .updateTime(message.getUpdateTime())
                .member(MemberInfoWithProfileRespDto.builder()
                        .id(message.getMemberId())
                        .profileId(memberInfo.getProfileId())
                        .role(memberInfo.getRole())
                        .profile(userProfileMapper.selectById(memberInfo.getProfileId()))
                        .build())
                .build();
    }
}
