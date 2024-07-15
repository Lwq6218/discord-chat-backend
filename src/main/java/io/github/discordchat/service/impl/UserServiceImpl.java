package io.github.discordchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.common.req.PageReqDto;
import io.github.discordchat.core.common.resp.PageRespDto;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.common.util.MD5Util;
import io.github.discordchat.core.constant.DatabaseConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.core.util.JwtUtil;
import io.github.discordchat.dao.entity.UserProfile;
import io.github.discordchat.dao.mapper.UserProfileMapper;
import io.github.discordchat.dto.UserProfileDto;
import io.github.discordchat.dto.req.UserLoginReqDto;
import io.github.discordchat.dto.req.UserProfileReqDto;
import io.github.discordchat.dto.req.UserRegisterReqDto;
import io.github.discordchat.dto.resp.UserLoginRespDto;
import io.github.discordchat.dto.resp.UserProfileRespDto;
import io.github.discordchat.dto.resp.UserRegisterRespDto;
import io.github.discordchat.manager.redis.VerifyCodeManager;
import io.github.discordchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.runtime.DotClass;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @classname UserServiceImpl
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final VerifyCodeManager verifyCodeManager;

    private final UserProfileMapper userProfileMapper;

    private final JwtUtil jwtUtil;

    @Override
    public RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto) {
        //校验图形验证码
        if (!verifyCodeManager.imgVerifyCodeOk(dto.getSessionId(), dto.getVerifyCode())) {
            throw new BusinessException(ErrorCodeEnum.USER_VERIFY_CODE_ERROR);
        }
        //校验手机号码是否已经注册
        QueryWrapper<UserProfile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.UserProfileTable.COLUMN_USERNAME, dto.getName())
                .last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        if (userProfileMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(ErrorCodeEnum.PHONE_NUMBER_EXIST);
        }
        //成功保存用户信息
        UserProfile userProfile = new UserProfile();
        userProfile.setName(dto.getName());
        userProfile.setPassword(MD5Util.encrypt(dto.getPassword()));
        userProfile.setCreateTime(LocalDateTime.now());
        userProfile.setUpdateTime(LocalDateTime.now());
        userProfileMapper.insert(userProfile);

        //删除图形验证码
        verifyCodeManager.removeImgVerifyCode(dto.getSessionId());

        //生成JWT并返回
        return RestResp.ok(UserRegisterRespDto.builder()
                .token(jwtUtil.generateToken(userProfile.getId(), SystemConfigConst.DISCORDCHAT_FRONT_KEY))
                .uid(userProfile.getId())
                .build());
    }

    @Override
    public RestResp<UserLoginRespDto> login(UserLoginReqDto dto) {
        //查询用户信息
        QueryWrapper<UserProfile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConst.UserProfileTable.COLUMN_USERNAME, dto.getName()).last(DatabaseConst.SqlEnum.LIMIT_1.getSql());
        UserProfile userProfile = userProfileMapper.selectOne(queryWrapper);
        //校验用户是否存在
        if (Objects.isNull(userProfile)){
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }
        //校验密码是否正确
        if (!Objects.equals(userProfile.getPassword(),MD5Util.encrypt(dto.getPassword()))){
            throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR);
        }
        //生成JWT并返回
        return  RestResp.ok(UserLoginRespDto.builder()
                .token(jwtUtil.generateToken(userProfile.getId(),SystemConfigConst.DISCORDCHAT_FRONT_KEY))
                .uid(userProfile.getId())
                .build());
    }

    @Override
    public RestResp<PageRespDto<UserProfileRespDto>> listUsers(PageReqDto dto) {
        IPage<UserProfile> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<UserProfile> queryWrapper = new QueryWrapper<>();
        switch (dto.getFilter()){
            case "recommended":
                break;
            case "old_user":
                queryWrapper.orderByAsc(DatabaseConst.CommonColumnEnum.CREATE_TIME.getName());
                break;
            case "new_user":
                queryWrapper.orderByDesc(DatabaseConst.CommonColumnEnum.CREATE_TIME.getName());
                break;
            default:
                throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
        if (!dto.getQ().isEmpty()){
            queryWrapper.like(DatabaseConst.UserProfileTable.COLUMN_USERNAME,dto.getQ());
        }
        queryWrapper.orderByDesc(DatabaseConst.CommonColumnEnum.CREATE_TIME.getName());
        IPage<UserProfile> profilePage = userProfileMapper.selectPage(page, queryWrapper);
        List<UserProfile> userProfiles = profilePage.getRecords();

        if (CollectionUtils.isEmpty(userProfiles)) {
            return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), Collections.emptyList()));
        }

        List<UserProfileRespDto> userProfileRespDtos = userProfiles.stream().map(profile ->{
            return UserProfileRespDto.builder()
                    .id(profile.getId())
                    .name(profile.getName())
                    .imageUrl(profile.getImageUrl())
                    .userSex(profile.getUserSex())
                    .nickName(profile.getNickName())
                    .email(profile.getEmail())
                    .phone(profile.getPhone())
                    .createTime(profile.getCreateTime())
                    .updateTime(profile.getUpdateTime())
                    .build();
        }).toList();
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(), userProfileRespDtos));
    }

    @Override
    public RestResp<UserProfileRespDto> getUserInfo(Long userId) {
        UserProfile userProfile = userProfileMapper.selectById(userId);
        if (Objects.isNull(userProfile)){
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }
        return RestResp.ok(UserProfileRespDto.builder()
                .id(userProfile.getId())
                .name(userProfile.getName())
                .imageUrl(userProfile.getImageUrl())
                .userSex(userProfile.getUserSex())
                .nickName(userProfile.getNickName())
                .email(userProfile.getEmail())
                .phone(userProfile.getPhone())
                .createTime(userProfile.getCreateTime())
                .updateTime(userProfile.getUpdateTime())
                .build());
    }

    @Override
    public RestResp<Void> updateUserInfo(Long id, UserProfileReqDto dto) {
        //判断用户是否存在
        UserProfile userProfile = userProfileMapper.selectById(id);
        if (Objects.isNull(userProfile)){
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }
        //更新用户信息
        userProfile.setName(dto.getName());
        userProfile.setNickName(dto.getNickName());
        userProfile.setEmail(dto.getEmail());
        userProfile.setPhone(dto.getPhone());
        if (Objects.nonNull(dto.getImageUrl())){
            userProfile.setImageUrl(dto.getImageUrl());
        }
        userProfile.setUpdateTime(LocalDateTime.now());
        userProfileMapper.updateById(userProfile);
        return RestResp.ok();
    }
}
