package io.github.discordchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.common.req.PageReqDto;
import io.github.discordchat.core.common.resp.PageRespDto;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.DatabaseConst;
import io.github.discordchat.dao.entity.Answer;
import io.github.discordchat.dao.entity.Question;
import io.github.discordchat.dao.mapper.AnswerMapper;
import io.github.discordchat.dao.mapper.QuestionMapper;
import io.github.discordchat.dao.mapper.UserProfileMapper;
import io.github.discordchat.dto.req.ActionReqDto;
import io.github.discordchat.dto.req.AnswerCreateReqDto;
import io.github.discordchat.dto.resp.AnswerWithProfileRespDto;
import io.github.discordchat.service.ActionService;
import io.github.discordchat.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @classname AnswerServiceImpl
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;
    private final UserProfileMapper userProfileMapper;

      private final ActionService actionService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResp<Void> addAnswer(Long userId, AnswerCreateReqDto dto) {
        //校验问题是否存在
        questionMapper.selectById(dto.getQuestionId());
        if (Objects.isNull(questionMapper.selectById(dto.getQuestionId()))) {
            throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
        }
        //保存答复
        Answer answer = new Answer();
        answer.setContent(dto.getContent());
        answer.setQuestionId(dto.getQuestionId());
        answer.setProfileId(userId);
        answer.setCreateTime(LocalDateTime.now());
        answer.setUpdateTime(LocalDateTime.now());
        answer.setUpvotes(0L);
        answer.setDownvotes(0L);
        answerMapper.insert(answer);
        //更新问题的答复数
        Question question = questionMapper.selectById(dto.getQuestionId());
        question.setAnswerCount(question.getAnswerCount() + 1);
        questionMapper.updateById(question);
        return RestResp.ok();


    }

    @Override
    public RestResp<List<AnswerWithProfileRespDto>> listAnswer(Long userId, Long questionId) {
        //校验问题是否存在
        questionMapper.selectById(questionId);
        if (Objects.isNull(questionMapper.selectById(questionId))) {
            throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
        }
        //查询问题下所有的答复
        QueryWrapper<Answer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.AnswerTable.COLUMN_QUESTION_ID, questionId);
        List<Answer> answers = answerMapper.selectList(queryWrapper);
        return RestResp.ok(answers.stream().map(answer -> {
            //对于每一个答复，查看当前用户是否点赞或者踩
            boolean hasupvoted = actionService.hasUpVoted(userId,"answer", 0L,answer.getId());
           boolean hasdownvoted = actionService.hasDownVoted(userId,"answer", 0L,answer.getId());
            return AnswerWithProfileRespDto.builder()
                    .id(answer.getId())
                    .questionId(answer.getQuestionId())
                    .content(answer.getContent())
                    .createTime(answer.getCreateTime())
                    .updateTime(answer.getUpdateTime())
                    .upvotes(answer.getUpvotes())
                    .downvotes(answer.getDownvotes())
                    .hasDownVoted(hasdownvoted)
                    .hasUpVoted(hasupvoted)
                    .author(userProfileMapper.selectById(answer.getProfileId()))
                    .build();
        }).toList());
    }

    @Override
    public RestResp<PageRespDto<AnswerWithProfileRespDto>> listAnswersByUserId(Long userId, PageReqDto dto) {
        IPage<Answer> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<Answer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.AnswerTable.COLUMN_PROFILE_ID, userId);
        IPage<Answer> answerPage = answerMapper.selectPage(page, queryWrapper);
        List<Answer> answers = answerPage.getRecords();
        if (CollectionUtils.isEmpty(answers)) {
            return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), List.of()));
        }

        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), answers.stream().map(answer -> AnswerWithProfileRespDto.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .questionId(answer.getQuestionId())
                .createTime(answer.getCreateTime())
                .updateTime(answer.getUpdateTime())
                .upvotes(answer.getUpvotes())
                .downvotes(answer.getDownvotes())
                .author(userProfileMapper.selectById(answer.getProfileId()))
                .build()).toList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResp<Void> deleteAnswer(Long userId, Long id) {
        Answer answer = answerMapper.selectById(id);
        if (Objects.isNull(answer)) {
            throw new BusinessException(ErrorCodeEnum.ANSWER_NOT_EXIST);
        }
        if (!answer.getProfileId().equals(userId)) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
        //问题答复数减一
        Question question = questionMapper.selectById(answer.getQuestionId());
        question.setAnswerCount(question.getAnswerCount() - 1);
        questionMapper.updateById(question);
        //删除答复
        answerMapper.deleteById(id);

        return RestResp.ok();
    }
}
