package io.github.discordchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.DatabaseConst;
import io.github.discordchat.dao.entity.Conversation;
import io.github.discordchat.dao.entity.DirectMessage;
import io.github.discordchat.dao.entity.MemberInfo;
import io.github.discordchat.dao.entity.UserProfile;
import io.github.discordchat.dao.mapper.ConversationMapper;
import io.github.discordchat.dao.mapper.DirectMessageMapper;
import io.github.discordchat.dao.mapper.MemberInfoMapper;
import io.github.discordchat.dao.mapper.UserProfileMapper;
import io.github.discordchat.dto.resp.DirectMessageRespDto;
import io.github.discordchat.dto.resp.DirectMessagesWithMemberWithProfileRespDto;
import io.github.discordchat.dto.resp.MemberInfoWithProfileRespDto;
import io.github.discordchat.dto.resp.MessageRespDto;
import io.github.discordchat.service.DirectMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @classname DirectMessageServiceImpl
 * @description TODO
 * @date 2024/6/23
 * @created by lwq
 */
@Service
@RequiredArgsConstructor
public class DirectMessageServiceImpl implements DirectMessageService {
    private final DirectMessageMapper directMessageMapper;

    private final ConversationMapper conversationMapper;

    private final MemberInfoMapper memberInfoMapper;

    private final UserProfileMapper userProfileMapper;

    @Override
    public RestResp<DirectMessagesWithMemberWithProfileRespDto> listDirectMessage(Long conversationId, Long cursor) {
        final String MESSAGES_BATCH = "limit 10";

        //根据会话id查询会话信息
        Conversation conversation = conversationMapper.selectById(conversationId);
        if (conversation == null) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_NOT_EXIST);
        }
        List<DirectMessageRespDto> collect;
        //判断是否有游标
        if (cursor == null) {
            //没有游标，直接查询最新的消息的前10条
            QueryWrapper<DirectMessage> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.DirectMessageTable.COLUMN_CONVERSATION_ID, conversationId)
                    .orderByDesc(DatabaseConst.CommonColumnEnum.CREATE_TIME.getName())
                    .last(MESSAGES_BATCH);
            List<DirectMessage> messages = directMessageMapper.selectList(queryWrapper);

            //根据查询到的消息列表查询它门的成员信息
            collect = messages.stream().map(message -> {
                MemberInfo memberInfo = memberInfoMapper.selectById(message.getMemberId());
                UserProfile userProfile = userProfileMapper.selectById(memberInfo.getProfileId());
                return DirectMessageRespDto.builder()
                        .id(message.getId())
                        .content(message.getContent())
                        .conversationId(message.getConversationId())
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
            QueryWrapper<DirectMessage> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.DirectMessageTable.COLUMN_CONVERSATION_ID, conversationId)
                    .lt(DatabaseConst.CommonColumnEnum.ID.getName(), cursor)
                    .orderByDesc(DatabaseConst.CommonColumnEnum.CREATE_TIME.getName())
                    .last(MESSAGES_BATCH);
            List<DirectMessage> messages = directMessageMapper.selectList(queryWrapper);
            //根据查询到的消息列表查询它门的成员信息
            collect = messages.stream().map(message -> {
                MemberInfo memberInfo = memberInfoMapper.selectById(message.getMemberId());
                UserProfile userProfile = userProfileMapper.selectById(memberInfo.getProfileId());
                return DirectMessageRespDto.builder()
                        .id(message.getId())
                        .content(message.getContent())
                        .conversationId(message.getConversationId())
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
        return RestResp.ok(DirectMessagesWithMemberWithProfileRespDto.builder()
                .items(collect)
                .nextCursor(nextCursor)
                .build());
    }

    @Override
    public DirectMessageRespDto create( Long conversationId, String content, Long userId) {
        //校验会话是否存在
        Conversation conversation = conversationMapper.selectById(conversationId);
        if (conversation == null) {
            throw new BusinessException(ErrorCodeEnum.CONVERSATION_NOT_EXIST);
        }
        //根据会话ID查询会话的成员信息
        MemberInfo memberOneInfo = memberInfoMapper.selectById(conversation.getMemberOne());
        MemberInfo memberTwoInfo = memberInfoMapper.selectById(conversation.getMemberTwo());
        if (memberOneInfo == null || memberTwoInfo == null) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_NOT_EXIST);
        }
        //查询当前用户的成员信息
        MemberInfo memberInfo = memberOneInfo.getProfileId().equals(userId) ? memberOneInfo : memberTwoInfo;

        //插入消息
        DirectMessage directMessage = new DirectMessage();
        directMessage.setConversationId(conversationId);
        directMessage.setMemberId(memberInfo.getId());
        directMessage.setContent(content);
        directMessage.setCreateTime(LocalDateTime.now());
        directMessage.setUpdateTime(LocalDateTime.now());
        directMessageMapper.insert(directMessage);

        return DirectMessageRespDto.builder()
                .id(directMessage.getId())
                .content(directMessage.getContent())
                .conversationId(directMessage.getConversationId())
                .memberId(directMessage.getMemberId())
                .fileUrl(directMessage.getFileUrl())
                .deleted(directMessage.getDeleted())
                .member(MemberInfoWithProfileRespDto.builder()
                        .id(memberInfo.getId())
                        .profileId(memberInfo.getProfileId())
                        .role(memberInfo.getRole())
                        .profile(userProfileMapper.selectById(memberInfo.getProfileId()))
                        .build())
                .createTime(directMessage.getCreateTime())
                .updateTime(directMessage.getUpdateTime())
                .build();

    }

    @Override
    public DirectMessageRespDto deleteMessage(Long userId, Long id, Long conversationId) {
        //校验消息是否存在
        DirectMessage directMessage = directMessageMapper.selectById(id);
        if (directMessage == null) {
            throw new BusinessException(ErrorCodeEnum.MESSAGE_NOT_EXIST);
        }
        //校验会话是否存在
        Conversation conversation = conversationMapper.selectById(conversationId);
        if (conversation == null) {
            throw new BusinessException(ErrorCodeEnum.CONVERSATION_NOT_EXIST);
        }
        //逻辑删除消息
        directMessage.setDeleted(1);
        directMessage.setContent("This message has been deleted");
        directMessage.setUpdateTime(LocalDateTime.now());
        directMessageMapper.updateById(directMessage);

        return DirectMessageRespDto.builder()
                .id(directMessage.getId())
                .content(directMessage.getContent())
                .conversationId(directMessage.getConversationId())
                .memberId(directMessage.getMemberId())
                .fileUrl(directMessage.getFileUrl())
                .deleted(directMessage.getDeleted())
                .member(MemberInfoWithProfileRespDto.builder()
                        .id(directMessage.getMemberId())
                        .profileId(memberInfoMapper.selectById(directMessage.getMemberId()).getProfileId())
                        .role(memberInfoMapper.selectById(directMessage.getMemberId()).getRole())
                        .profile(userProfileMapper.selectById(memberInfoMapper.selectById(directMessage.getMemberId()).getProfileId()))
                        .build())
                .createTime(directMessage.getCreateTime())
                .updateTime(directMessage.getUpdateTime())
                .build();
    }

    @Override
    public DirectMessageRespDto updateMessage(Long userId, Long id, Long conversationId, String content) {
        //校验消息是否存在
        DirectMessage directMessage = directMessageMapper.selectById(id);
        if (directMessage == null) {
            throw new BusinessException(ErrorCodeEnum.MESSAGE_NOT_EXIST);
        }
        //校验会话是否存在
        Conversation conversation = conversationMapper.selectById(conversationId);
        if (conversation == null) {
            throw new BusinessException(ErrorCodeEnum.CONVERSATION_NOT_EXIST);
        }
        //校验消息是否是当前用户的消息
        if (!directMessage.getMemberId().equals(userId)) {
            throw new BusinessException(ErrorCodeEnum.MESSAGE_NOT_EXIST);
        }
        //修改消息
        directMessage.setContent(content);
        directMessage.setUpdateTime(LocalDateTime.now());
        directMessageMapper.updateById(directMessage);

        return DirectMessageRespDto.builder()
                .id(directMessage.getId())
                .content(directMessage.getContent())
                .conversationId(directMessage.getConversationId())
                .memberId(directMessage.getMemberId())
                .fileUrl(directMessage.getFileUrl())
                .deleted(directMessage.getDeleted())
                .member(MemberInfoWithProfileRespDto.builder()
                        .id(directMessage.getMemberId())
                        .profileId(memberInfoMapper.selectById(directMessage.getMemberId()).getProfileId())
                        .role(memberInfoMapper.selectById(directMessage.getMemberId()).getRole())
                        .profile(userProfileMapper.selectById(memberInfoMapper.selectById(directMessage.getMemberId()).getProfileId()))
                        .build())
                .createTime(directMessage.getCreateTime())
                .updateTime(directMessage.getUpdateTime())
                .build();
    }
}

