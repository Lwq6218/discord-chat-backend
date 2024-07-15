package io.github.discordchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.DatabaseConst;
import io.github.discordchat.dao.entity.Conversation;
import io.github.discordchat.dao.entity.MemberInfo;
import io.github.discordchat.dao.mapper.ConversationMapper;
import io.github.discordchat.dao.mapper.MemberInfoMapper;
import io.github.discordchat.dao.mapper.UserProfileMapper;
import io.github.discordchat.dto.resp.ConversationRespDto;
import io.github.discordchat.dto.resp.MemberInfoWithProfileRespDto;
import io.github.discordchat.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @classname ConversationServiceImpl
 * @description TODO
 * @date 2024/6/23
 * @created by lwq
 */
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationMapper conversationMapper;

    private final MemberInfoMapper memberInfoMapper;

    private final UserProfileMapper userProfileMapper;

    @Override
    public RestResp<ConversationRespDto> getConversation(Long memberOneId, Long memberTwoId) {

        MemberInfo memberOneInfo = memberInfoMapper.selectById(memberTwoId);
        MemberInfo memberTwoInfo = memberInfoMapper.selectById(memberOneId);
        if (Objects.isNull(memberOneInfo) || Objects.isNull(memberTwoInfo)) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_NOT_EXIST);
        }

        //查询交流信息
        QueryWrapper<Conversation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.ConversationTable.COLUMN_CONVERSATION_ONE_ID, memberOneId).eq(DatabaseConst.ConversationTable.COLUMN_CONVERSATION_TWO_ID, memberTwoId)
                .or().eq(DatabaseConst.ConversationTable.COLUMN_CONVERSATION_ONE_ID, memberTwoId).eq(DatabaseConst.ConversationTable.COLUMN_CONVERSATION_TWO_ID, memberOneId);
        Conversation conversation = conversationMapper.selectOne(queryWrapper);
        if (Objects.isNull(conversation)) {
            //如果没有交流信息，创建一条
            conversation = new Conversation();
            conversation.setMemberOne(memberOneId);
            conversation.setMemberTwo(memberTwoId);
            conversationMapper.insert(conversation);
        }
        return RestResp.ok(ConversationRespDto.builder()
                .id(conversation.getId())
                .memberOne(MemberInfoWithProfileRespDto.builder()
                        .id(memberOneId)
                        .role(memberOneInfo.getRole())
                        .profileId(memberOneInfo.getProfileId())
                        .profile(userProfileMapper.selectById(memberOneInfo.getProfileId()))
                        .build())
                .memberTwo(MemberInfoWithProfileRespDto.builder()
                        .id(memberTwoId)
                        .role(memberTwoInfo.getRole())
                        .profileId(memberTwoInfo.getProfileId())
                        .profile(userProfileMapper.selectById(memberTwoInfo.getProfileId()))
                        .build())
                .build()
        );

    }
}
