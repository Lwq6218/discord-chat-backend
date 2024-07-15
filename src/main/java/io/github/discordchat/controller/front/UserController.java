package io.github.discordchat.controller.front;

import io.github.discordchat.core.auth.UserHolder;
import io.github.discordchat.core.common.req.PageReqDto;
import io.github.discordchat.core.common.resp.PageRespDto;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.req.UserLoginReqDto;
import io.github.discordchat.dto.req.UserProfileReqDto;
import io.github.discordchat.dto.req.UserRegisterReqDto;
import io.github.discordchat.dto.resp.UserLoginRespDto;
import io.github.discordchat.dto.resp.UserProfileRespDto;
import io.github.discordchat.dto.resp.UserRegisterRespDto;
import io.github.discordchat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @classname UserController
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Tag(name = "UserController", description = "用户模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_USER_URL_PREFIX)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * 用户注册接口
     */
    @Operation(summary = "用户注册接口")
    @PostMapping("sign-up")
    public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto dto) {
        return userService.register(dto);
    }


    /**
     * 用户登录接口
     */
    @Operation(summary = "用户登录接口")
    @PostMapping("sign-in")
    public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto dto) {
        return userService.login(dto);
    }

    @GetMapping("list")
    public RestResp<PageRespDto<UserProfileRespDto>> listUsers(PageReqDto dto) {
        return userService.listUsers(dto);
    }


    @GetMapping("{id}")
    public RestResp<UserProfileRespDto> getUserInfo( @PathVariable("id") Long id) {
        return userService.getUserInfo(id);
    }
    @PatchMapping("{id}")
    public RestResp<Void> updateUserInfo(@PathVariable("id") Long id, @Valid @RequestBody UserProfileReqDto dto) {
        return userService.updateUserInfo(id,dto);
    }
}
