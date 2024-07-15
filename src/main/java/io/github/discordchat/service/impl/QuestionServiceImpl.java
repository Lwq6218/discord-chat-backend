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
import io.github.discordchat.dao.entity.*;
import io.github.discordchat.dao.mapper.*;
import io.github.discordchat.dto.req.QuestionCreateReqDto;
import io.github.discordchat.dto.req.QuestionUpdateReqDto;
import io.github.discordchat.dto.resp.QuestionWithTagsRespDto;
import io.github.discordchat.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @classname QuestionServiceImpl
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;

    private final TagMapper tagMapper;

    private final AnswerMapper answerMapper;

    private final UserProfileMapper userProfileMapper;

    private final QuestionTagMapper questionTagMapper;

    private final ActionMapper actionMapper;

    @Override
    public RestResp<PageRespDto<QuestionWithTagsRespDto>> listQuestion(PageReqDto dto) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        //根据filter构建queryWrapper
        switch (dto.getFilter()) {
            case "recommended":
                //默认按照创建时间倒序
                break;
            case "frequent":
                //按照回答数倒序
                queryWrapper.orderByDesc(DatabaseConst.QuestionTable.COLUMN_UPVOTES);
                break;
            case "unanswered":
                //查询没有回答的问题
                queryWrapper.eq(DatabaseConst.QuestionTable.COLUMN_ANSWER_COUNT, 0L);
                break;
            case "newest":
                //按照创建时间倒序
                queryWrapper.orderByDesc(DatabaseConst.CommonColumnEnum.CREATE_TIME.getName());
                break;
            default:
                throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
        //关键字搜索
        if (Objects.nonNull(dto.getQ())) {
            queryWrapper.like(DatabaseConst.QuestionTable.COLUMN_TITLE, dto.getQ());
        }
        IPage<Question> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        IPage<Question> questionPage = questionMapper.selectPage(page, queryWrapper);
        List<Question> questions = questionPage.getRecords();

        if (CollectionUtils.isEmpty(questions)) {
            return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), Collections.emptyList()));
        }
        List<QuestionWithTagsRespDto> questionWithTagsRespDtos = questions.stream().map(question -> {
            //对于每一个问题，获取其所有的标签
            QueryWrapper<QuestionTag> questionTagQueryWrapper = new QueryWrapper<>();
            questionTagQueryWrapper.eq(DatabaseConst.QuestionTagTable.COLUMN_QUESTION_ID, question.getId());
            List<Long> tagIds = questionTagMapper.selectList(questionTagQueryWrapper)
                    .stream().map(QuestionTag::getTagId).toList();
            List<Tag> tags;
            if (CollectionUtils.isEmpty(tagIds)) {
                tags = Collections.emptyList();
            } else {
                tags = tagMapper.selectBatchIds(tagIds);
            }

            return QuestionWithTagsRespDto.builder()
                    .id(question.getId())
                    .title(question.getTitle())
                    .content(question.getContent())
                    .upvotes(question.getUpvotes())
                    .downvotes(question.getDownvotes())
                    .author(userProfileMapper.selectById(question.getProfileId()))
                    .answerCount(question.getAnswerCount())
                    .tags(tags)
                    .createTime(question.getCreateTime())
                    .updateTime(question.getUpdateTime())
                    .build();
        }).toList();
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), questionWithTagsRespDtos));
    }

    @Override
    public RestResp<PageRespDto<QuestionWithTagsRespDto>> listQuestion(Long userId, PageReqDto dto) {
        IPage<Question> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.QuestionTable.COLUMN_PROFILE_ID, userId);
        queryWrapper.orderByDesc(DatabaseConst.CommonColumnEnum.CREATE_TIME.getName());
        IPage<Question> questionPage = questionMapper.selectPage(page, queryWrapper);
        List<Question> questions = questionPage.getRecords();

        if (CollectionUtils.isEmpty(questions)) {
            return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), Collections.emptyList()));
        }
        List<QuestionWithTagsRespDto> questionWithTagsRespDtos = questions.stream().map(question -> {
            //对于每一个问题，获取其所有的标签
            QueryWrapper<QuestionTag> questionTagQueryWrapper = new QueryWrapper<>();
            questionTagQueryWrapper.eq(DatabaseConst.QuestionTagTable.COLUMN_QUESTION_ID, question.getId());
            List<Long> tagIds = questionTagMapper.selectList(questionTagQueryWrapper)
                    .stream().map(QuestionTag::getTagId).toList();
            List<Tag> tags;
            if (CollectionUtils.isEmpty(tagIds)) {
                tags = Collections.emptyList();
            } else {
                tags = tagMapper.selectBatchIds(tagIds);
            }
            return QuestionWithTagsRespDto.builder()
                    .id(question.getId())
                    .title(question.getTitle())
                    .content(question.getContent())
                    .upvotes(question.getUpvotes())
                    .downvotes(question.getDownvotes())
                    .author(userProfileMapper.selectById(question.getProfileId()))
                    .answerCount(question.getAnswerCount())
                    .tags(tags)
                    .createTime(question.getCreateTime())
                    .updateTime(question.getUpdateTime())
                    .build();
        }).toList();
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), questionWithTagsRespDtos));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResp<Void> addQuestion(Long userId, QuestionCreateReqDto dto) {
        //插入问题
        Question question = new Question();
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        question.setProfileId(userId);
        question.setAnswerCount(0L);
        question.setCreateTime(LocalDateTime.now());
        question.setUpdateTime(LocalDateTime.now());
        questionMapper.insert(question);

        //先判断标签是否存在
        List<String> tagsName = dto.getTagsName();
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.in(DatabaseConst.TagTable.COLUMN_TAG_NAME, tagsName);

        List<Tag> existingTags = tagMapper.selectList(tagQueryWrapper);
        List<Long> existingTagIds = existingTags.stream().map(Tag::getId).toList();
        //对于已经存在的标签,则直接插入问题-标签表
        if (!CollectionUtils.isEmpty(existingTagIds)) {
            existingTagIds.forEach(tagId -> {
                QuestionTag questionTag = new QuestionTag();
                questionTag.setQuestionId(question.getId());
                questionTag.setTagId(tagId);
                questionTag.setCreateTime(LocalDateTime.now());
                questionTag.setUpdateTime(LocalDateTime.now());
                questionTagMapper.insert(questionTag);
                //更新标签的问题数
                Tag tag = tagMapper.selectById(tagId);
                tag.setQuestionCount(tag.getQuestionCount() + 1);
                tagMapper.updateById(tag);
            });
        }
        if (CollectionUtils.isEmpty(existingTags)) {
            existingTags = Collections.emptyList();
        }
        //对于不存在的标签，先创建标签，再插入问题-标签表
        List<String> existingTagsName = existingTags.stream().map(Tag::getName).toList();
        //获取不存在的标签
        List<String> notExistingTagsName = tagsName.stream().filter(tagName -> !existingTagsName.contains(tagName)).toList();
        if (!CollectionUtils.isEmpty(notExistingTagsName)) {
            notExistingTagsName.forEach(tagName -> {
                Tag tag = new Tag();
                tag.setName(tagName);
                tag.setQuestionCount(1L);
                tag.setCreateTime(LocalDateTime.now());
                tag.setUpdateTime(LocalDateTime.now());
                tagMapper.insert(tag);
                QuestionTag questionTag = new QuestionTag();
                questionTag.setQuestionId(question.getId());
                questionTag.setTagId(tag.getId());
                questionTag.setCreateTime(LocalDateTime.now());
                questionTag.setUpdateTime(LocalDateTime.now());
                questionTagMapper.insert(questionTag);
            });
        }
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> updateQuestion(Long userId, QuestionUpdateReqDto dto) {
        //判断问题是否存在
        Question question = questionMapper.selectById(dto.getId());
        if (Objects.isNull(question)) {
            throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
        }
        //判断问题的作者是不是当前用户
        if (!Objects.equals(question.getProfileId(), userId)) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
        //更新问题
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        question.setUpdateTime(LocalDateTime.now());
        questionMapper.updateById(question);

        return RestResp.ok();
    }

    @Override
    public RestResp<QuestionWithTagsRespDto> getQuestionById(Long userId, Long id) {
        //判断问题是否存在
        Question question = questionMapper.selectById(id);
        if (Objects.isNull(question)) {
            throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
        }
        //获取问题的标签
        QueryWrapper<QuestionTag> questionTagQueryWrapper = new QueryWrapper<>();
        questionTagQueryWrapper.eq(DatabaseConst.QuestionTagTable.COLUMN_QUESTION_ID, id);
        List<Long> tagIds = questionTagMapper.selectList(questionTagQueryWrapper)
                .stream().map(QuestionTag::getTagId).toList();
        List<Tag> tags;
        if (CollectionUtils.isEmpty(tagIds)) {
            tags = Collections.emptyList();
        } else {
            tags = tagMapper.selectBatchIds(tagIds);
        }
        //获取问题的回复数
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.eq(DatabaseConst.AnswerTable.COLUMN_QUESTION_ID, id);
        Long answerCount = answerMapper.selectCount(answerQueryWrapper);

        return RestResp.ok(QuestionWithTagsRespDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .upvotes(question.getUpvotes())
                .downvotes(question.getDownvotes())
                .answerCount(answerCount)
                .author(userProfileMapper.selectById(question.getProfileId()))
                .tags(tags)
                .createTime(question.getCreateTime())
                .updateTime(question.getUpdateTime())
                .build());

    }

    @Override
    public RestResp<PageRespDto<QuestionWithTagsRespDto>> listSavedQuestion(Long userId, PageReqDto dto) {
        IPage<Question> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        //查询用户收藏的问题
        QueryWrapper<Action> actionQueryWrapper = new QueryWrapper<>();
        actionQueryWrapper.eq(DatabaseConst.ActionTable.COLUMN_PROFILE_ID, userId)
                .eq(DatabaseConst.ActionTable.COLUMN_ACTION_TYPE, DatabaseConst.ActionTypeEnum.FAVORITE.getValue())
                .select(DatabaseConst.ActionTable.COLUMN_QUESTION_ID);
        List<Long> questionIds = actionMapper.selectList(actionQueryWrapper)
                .stream().map(Action::getQuestionId).toList();
        if (CollectionUtils.isEmpty(questionIds)) {
            return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), Collections.emptyList()));
        }
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.in(DatabaseConst.CommonColumnEnum.ID.getName(), questionIds);

        switch (dto.getFilter()) {
            case "recommended":
                break;
            case "most_upvoted":
                questionQueryWrapper.orderByDesc(DatabaseConst.QuestionTable.COLUMN_UPVOTES);
                break;
            case "most_downvoted":
                questionQueryWrapper.orderByDesc(DatabaseConst.QuestionTable.COLUMN_DOWNVOTES);
                break;
            case "most_answered":
                questionQueryWrapper.orderByDesc(DatabaseConst.QuestionTable.COLUMN_ANSWER_COUNT);
            default:
                throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
        if (Objects.nonNull(dto.getQ())) {
            questionQueryWrapper.like(DatabaseConst.QuestionTable.COLUMN_TITLE, dto.getQ());
        }
        IPage<Question> questionPage = questionMapper.selectPage(page, questionQueryWrapper);
        List<Question> questions = questionPage.getRecords();
        List<QuestionWithTagsRespDto> questionWithTagsRespDtos = questions.stream().map(question -> {
            //对于每一个问题，获取其所有的标签
            QueryWrapper<QuestionTag> questionTagQueryWrapper = new QueryWrapper<>();
            questionTagQueryWrapper.eq(DatabaseConst.QuestionTagTable.COLUMN_QUESTION_ID, question.getId());
            List<Long> tagIds = questionTagMapper.selectList(questionTagQueryWrapper)
                    .stream().map(QuestionTag::getTagId).toList();
            List<Tag> tags;
            if (CollectionUtils.isEmpty(tagIds)) {
                tags = Collections.emptyList();
            } else {
                tags = tagMapper.selectBatchIds(tagIds);
            }
            //对于每一个问题，查询其回答数
            QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
            answerQueryWrapper.eq(DatabaseConst.AnswerTable.COLUMN_QUESTION_ID, question.getId());
            Long answerCount = answerMapper.selectCount(answerQueryWrapper);
            if (Objects.isNull(answerCount)) {
                answerCount = 0L;
            }

            return QuestionWithTagsRespDto.builder()
                    .id(question.getId())
                    .title(question.getTitle())
                    .content(question.getContent())
                    .upvotes(question.getUpvotes())
                    .downvotes(question.getDownvotes())
                    .author(userProfileMapper.selectById(question.getProfileId()))
                    .answerCount(answerCount)
                    .tags(tags)
                    .createTime(question.getCreateTime())
                    .updateTime(question.getUpdateTime())
                    .build();
        }).toList();
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), questionWithTagsRespDtos));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResp<Void> deleteQuestion(Long userId, Long id) {
        //判断问题是否存在
        Question question = questionMapper.selectById(id);
        if (Objects.isNull(question)) {
            throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
        }
        //判断问题的作者是不是当前用户
        if (!Objects.equals(question.getProfileId(), userId)) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
        //删除问题
        questionMapper.deleteById(id);
        //删除问题-标签表
        QueryWrapper<QuestionTag> questionTagQueryWrapper = new QueryWrapper<>();
        questionTagQueryWrapper.eq(DatabaseConst.QuestionTagTable.COLUMN_QUESTION_ID, id);
        questionTagMapper.delete(questionTagQueryWrapper);
        //更新标签的问题数
        QueryWrapper<QuestionTag> questionTagQueryWrapper1 = new QueryWrapper<>();
        questionTagQueryWrapper1.eq(DatabaseConst.QuestionTagTable.COLUMN_QUESTION_ID, id);
        List<Long> tagIds = questionTagMapper.selectList(questionTagQueryWrapper1)
                .stream().map(QuestionTag::getTagId).toList();
        tagIds.forEach(tagId -> {
            Tag tag = tagMapper.selectById(tagId);
            tag.setQuestionCount(tag.getQuestionCount() - 1);
            tagMapper.updateById(tag);
        });
        //删除问题的回答
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.eq(DatabaseConst.AnswerTable.COLUMN_QUESTION_ID, id);
        answerMapper.delete(answerQueryWrapper);
        return RestResp.ok();
    }
}
