package io.github.discordchat.controller.front;

import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.livekit.server.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @classname LivekitController
 * @description TODO
 * @date 2024/6/23
 * @created by lwq
 */
@Tag(name = "LiveKitController", description = "livekit模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_LIVE_KIT_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class LiveKitController {
    final String apiKey = "APIxk9rTJaHHRgG";
    final String screctKey = "Wdo4nAvwT91yFwI7f56mD3uLV2QgaoYZ7ilgxqR7lNX";

    @GetMapping
    public RestResp<String> getLiveKit(@RequestParam String username, @RequestParam String room) {
        AccessToken accessToken = new AccessToken(apiKey, screctKey);
        accessToken.setIdentity(username);
        accessToken.addGrants(new RoomName(room), new RoomJoin(true), new CanPublish(true), new CanSubscribe(true));
        return RestResp.ok(accessToken.toJwt());
    }
}
