package io.github.discordchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.DatabaseConst;
import io.github.discordchat.dao.entity.Action;
import io.github.discordchat.dao.entity.Answer;
import io.github.discordchat.dao.entity.Question;
import io.github.discordchat.dao.mapper.ActionMapper;
import io.github.discordchat.dao.mapper.AnswerMapper;
import io.github.discordchat.dao.mapper.QuestionMapper;
import io.github.discordchat.dto.req.ActionReqDto;
import io.github.discordchat.service.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @classname ActionServerImpl
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */
@Service
@RequiredArgsConstructor
public class ActionServerImpl implements ActionService {
    private final AnswerMapper answerMapper;
    private final QuestionMapper questionMapper;
    private final ActionMapper actionMapper;


    @Override
    public RestResp<Void> handleUpvote(Long userId, ActionReqDto dto) {

        if (dto.getType().equals("question")) {

            //校验问题是否存在
            questionMapper.selectById(dto.getQuestionId());
            if (questionMapper.selectById(dto.getQuestionId()) == null) {
                throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
            }
            if (dto.isHasUpVoted()) {

                //取消点赞
                QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_QUESTION_ID, dto.getQuestionId())
                        .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                        .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.UPVOTE.getValue());
                actionMapper.delete(queryWrapper);
                //更新问题点赞数
                Question question = questionMapper.selectById(dto.getQuestionId());
                question.setUpvotes(question.getUpvotes() - 1);
                questionMapper.updateById(question);
            } else {
                //点赞
                Action action = new Action();
                action.setQuestionId(dto.getQuestionId());
                action.setProfileId(userId);
                action.setActionType(DatabaseConst.ActionTypeEnum.UPVOTE.getValue());
                actionMapper.insert(action);
                //更新问题点赞数
                Question question = questionMapper.selectById(dto.getQuestionId());
                question.setUpvotes(question.getUpvotes() + 1);
                questionMapper.updateById(question);
            }

        }
        if (dto.getType().equals("answer")) {
            //校验回复是否存在
            answerMapper.selectById(dto.getAnswerId());
            if (answerMapper.selectById(dto.getAnswerId()) == null) {
                throw new BusinessException(ErrorCodeEnum.ANSWER_NOT_EXIST);
            }
            if (dto.isHasUpVoted()) {
                //取消点赞
                QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_ANSWER_ID, dto.getAnswerId())
                        .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                        .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.UPVOTE.getValue());
                actionMapper.delete(queryWrapper);
                //更新回复点赞数
                Answer answer = answerMapper.selectById(dto.getAnswerId());
                answer.setUpvotes(answer.getUpvotes() - 1);
                answerMapper.updateById(answer);
            } else {
                //点赞
                Action action = new Action();
                action.setAnswerId(dto.getAnswerId());
                action.setProfileId(userId);
                action.setActionType(DatabaseConst.ActionTypeEnum.UPVOTE.getValue());
                actionMapper.insert(action);
                //更新回复点赞数
                Answer answer = answerMapper.selectById(dto.getAnswerId());
                answer.setUpvotes(answer.getUpvotes() + 1);
                answerMapper.updateById(answer);

            }

        }
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> handleDownVote(Long userId, ActionReqDto dto) {


        if (dto.getType().equals("question")) {
            //校验问题是否存在
            questionMapper.selectById(dto.getQuestionId());
            if (questionMapper.selectById(dto.getQuestionId()) == null) {
                throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
            }
            if (dto.isHasDownVoted()) {
                //如果已经踩过了，取消踩
                QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_QUESTION_ID, dto.getQuestionId())
                        .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                        .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.DOWNVOTE.getValue());
                actionMapper.delete(queryWrapper);
                //更新问题踩数
                Question question = questionMapper.selectById(dto.getQuestionId());
                question.setDownvotes(question.getDownvotes() - 1);
                questionMapper.updateById(question);
            } else {

                //如果没有踩过，踩
                Action action = new Action();
                action.setQuestionId(dto.getQuestionId());
                action.setProfileId(userId);
                action.setActionType(DatabaseConst.ActionTypeEnum.DOWNVOTE.getValue());
                actionMapper.insert(action);
                //更新问题点赞数
                Question question = questionMapper.selectById(dto.getQuestionId());
                question.setDownvotes(question.getDownvotes() + 1);
                questionMapper.updateById(question);
            }

        }
        if (dto.getType().equals("answer")) {
            //校验回复是否存在
            answerMapper.selectById(dto.getAnswerId());
            if (answerMapper.selectById(dto.getAnswerId()) == null) {
                throw new BusinessException(ErrorCodeEnum.ANSWER_NOT_EXIST);
            }
            if (dto.isHasDownVoted()) {
                //如果已经踩过了，取消踩
                QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_ANSWER_ID, dto.getAnswerId())
                        .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                        .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.DOWNVOTE.getValue());
                actionMapper.delete(queryWrapper);
                //更新回复踩数
                Answer answer = answerMapper.selectById(dto.getQuestionId());
                answer.setDownvotes(answer.getDownvotes() - 1);
                answerMapper.updateById(answer);
            } else {

                //如果没有踩过，踩
                Action action = new Action();
                action.setAnswerId(dto.getAnswerId());
                action.setProfileId(userId);
                action.setActionType(DatabaseConst.ActionTypeEnum.DOWNVOTE.getValue());
                actionMapper.insert(action);
                //更新回复点赞数
                Answer answer = answerMapper.selectById(dto.getAnswerId());
                answer.setDownvotes(answer.getDownvotes() + 1);
                answerMapper.updateById(answer);
            }
        }
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> handleStar(Long userId, ActionReqDto dto) {
        //校验问题是否存在
        Question question = questionMapper.selectById(dto.getQuestionId());
        if (Objects.isNull(question)) {
            throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
        }
        if (dto.isHasSaved()) {
            //取消收藏
            QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_QUESTION_ID, dto.getQuestionId())
                    .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                    .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.FAVORITE.getValue());
            actionMapper.delete(queryWrapper);
        } else {
            //收藏
            Action action = new Action();
            action.setQuestionId(dto.getQuestionId());
            action.setProfileId(userId);
            action.setActionType(DatabaseConst.ActionTypeEnum.FAVORITE.getValue());
            actionMapper.insert(action);
        }
        return RestResp.ok();
    }

    @Override
    public RestResp<Boolean> hasUpVoted(Long userId, ActionReqDto dto) {
        if (dto.getType().equals("question")) {
            //校验问题是否存在
            questionMapper.selectById(dto.getQuestionId());
            if (questionMapper.selectById(dto.getQuestionId()) == null) {
                throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
            }
            //查询是否点赞
            QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_QUESTION_ID, dto.getQuestionId())
                    .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                    .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.UPVOTE.getValue())
                    .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
            Action action = actionMapper.selectOne(queryWrapper);
            return RestResp.ok(action != null);
        }
        if (dto.getType().equals("answer")) {
            //校验回复是否存在
            answerMapper.selectById(dto.getAnswerId());
            if (answerMapper.selectById(dto.getAnswerId()) == null) {
                throw new BusinessException(ErrorCodeEnum.ANSWER_NOT_EXIST);
            }
            //查询是否点赞
            QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_ANSWER_ID, dto.getAnswerId())
                    .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                    .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.UPVOTE.getValue())
                    .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
            Action action = actionMapper.selectOne(queryWrapper);
            return RestResp.ok(action != null);
        }
        return null;
    }

    @Override
    public boolean hasUpVoted(Long userId, String type, Long questionId, Long answerId) {
        if (type.equals("question")) {
            //校验问题是否存在
            if (questionMapper.selectById(questionId) == null) {
                throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
            }
            //查询是否点赞
            QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_QUESTION_ID, questionId)
                    .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                    .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.UPVOTE.getValue())
                    .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
            Action action = actionMapper.selectOne(queryWrapper);
            return action != null;
        }
        if (type.equals("answer")) {
            //校验回复是否存在
            if (answerMapper.selectById(answerId) == null) {
                throw new BusinessException(ErrorCodeEnum.ANSWER_NOT_EXIST);
            }
            //查询是否点赞
            QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_ANSWER_ID, answerId)
                    .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                    .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.UPVOTE.getValue())
                    .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
            Action action = actionMapper.selectOne(queryWrapper);
            return action != null;
        }
        return false;
    }

    @Override
    public RestResp<Boolean> hasDownVoted(Long userId, ActionReqDto dto) {
        if (dto.getType().equals("question")) {
            //校验问题是否存在
            if (questionMapper.selectById(dto.getQuestionId()) == null) {
                throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
            }
            //查询是否点赞
            QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_QUESTION_ID, dto.getQuestionId())
                    .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                    .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.DOWNVOTE.getValue())
                    .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
            Action action = actionMapper.selectOne(queryWrapper);

            return RestResp.ok(action != null);
        }
        if (dto.getType().equals("answer")) {
            //校验回复是否存在
            if (answerMapper.selectById(dto.getAnswerId()) == null) {
                throw new BusinessException(ErrorCodeEnum.ANSWER_NOT_EXIST);
            }
            //查询是否点赞
            QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_ANSWER_ID, dto.getAnswerId())
                    .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                    .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.DOWNVOTE.getValue())
                    .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
            Action action = actionMapper.selectOne(queryWrapper);
            return RestResp.ok(action != null);
        }
        return null;
    }

    @Override
    public boolean hasDownVoted(Long userId, String type, Long questionId, Long answerId) {
        if (type.equals("question")) {
            //校验问题是否存在
            if (questionMapper.selectById(questionId) == null) {
                throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
            }
            //查询是否点赞
            QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_QUESTION_ID, questionId)
                    .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                    .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.DOWNVOTE.getValue())
                    .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
            Action action = actionMapper.selectOne(queryWrapper);

            return action != null;
        }
        if (type.equals("answer")) {
            //校验回复是否存在
            if (answerMapper.selectById(answerId) == null) {
                throw new BusinessException(ErrorCodeEnum.ANSWER_NOT_EXIST);
            }
            //查询是否点赞
            QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_ANSWER_ID, answerId)
                    .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                    .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.DOWNVOTE.getValue())
                    .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
            Action action = actionMapper.selectOne(queryWrapper);
            return action != null;
        }
        return false;
    }

    @Override
    public RestResp<Boolean> hasSaved(Long userId, ActionReqDto dto) {
        //校验问题是否存在
        questionMapper.selectById(dto.getQuestionId());
        if (questionMapper.selectById(dto.getQuestionId()) == null) {
            throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
        }
        //查询是否收藏
        QueryWrapper<Action> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.ActionTable.COLUMN_QUESTION_ID, dto.getQuestionId())
                .eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.FAVORITE.getValue())
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        Action action = actionMapper.selectOne(queryWrapper);
        return RestResp.ok(action != null);
    }

}