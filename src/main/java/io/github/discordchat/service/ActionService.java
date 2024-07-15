package io.github.discordchat.service;

import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dto.req.ActionReqDto;

public interface ActionService {
    RestResp<Void> handleUpvote(Long userId, ActionReqDto dto);

    RestResp<Void> handleDownVote(Long userId, ActionReqDto dto);

    RestResp<Void> handleStar(Long userId, ActionReqDto dto);

    RestResp<Boolean> hasUpVoted(Long userId, ActionReqDto dto);

    boolean hasUpVoted(Long userId, String type, Long questionId, Long answerId);

    RestResp<Boolean> hasDownVoted(Long userId, ActionReqDto dto);

    boolean hasDownVoted(Long userId, String type, Long questionId, Long answerId);

    RestResp<Boolean> hasSaved(Long userId, ActionReqDto dto);
}
