package io.github.discordchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.discordchat.core.common.req.PageReqDto;
import io.github.discordchat.core.common.resp.PageRespDto;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.DatabaseConst;
import io.github.discordchat.dao.entity.Tag;
import io.github.discordchat.dao.mapper.QuestionTagMapper;
import io.github.discordchat.dao.mapper.TagMapper;
import io.github.discordchat.dto.resp.TagRespDto;
import io.github.discordchat.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @classname TagServiceImpl
 * @description TODO
 * @date 2024/6/28
 * @created by lwq
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagMapper tagMapper;
    private final QuestionTagMapper questionTagMapper;

    @Override
    public RestResp<PageRespDto<TagRespDto>> listTags(PageReqDto dto) {
        IPage<Tag> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        switch (dto.getFilter()) {
            case "recommended":
                break;
            case "popular":
                wrapper.orderByDesc(DatabaseConst.TagTable.COLUMN_QUESTION_COUNT);
                break;
            case "newest":
                wrapper.orderByDesc(DatabaseConst.CommonColumnEnum.CREATE_TIME.getName());
                break;
            default:
                break;
        }
        if (!dto.getQ().isEmpty()) {
            wrapper.like(DatabaseConst.TagTable.COLUMN_TAG_NAME, dto.getQ());
        }
        IPage<Tag> tagPage = tagMapper.selectPage(page, wrapper);
        List<Tag> records = tagPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), Collections.emptyList()));
        }
        List<TagRespDto> tagRespDtos = records.stream().map(tag -> TagRespDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .questionCount(tag.getQuestionCount())
                .createTime(tag.getCreateTime())
                .updateTime(tag.getUpdateTime())
                .build()).toList();
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), tagRespDtos));
    }
}
