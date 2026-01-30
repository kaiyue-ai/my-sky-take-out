package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private WeChatProperties wx;
    @Autowired
    private UserMapper UserMapper;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    public User wxLogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO.getCode());
        //判断openid是否为空，如果为空表示登录失败，抛出业务异常
        if(openid==null) throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        //判断当前用户是否为新用户
        User user = UserMapper.getByOpenid(openid);
        //如果是新用户，自动完成注册
        if(user==null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            UserMapper.insert(user);
        }
        //返回登录成功的用户对象
        return user;
    }
    /**
     * 调用微信接口服务，获取openid
     * @param code
     * @return
     */
    private String getOpenid(String code) {
        //调用微信接口服务，获取用户openid
        Map<String, String> query = new HashMap<>();
        query.put("appid", wx.getAppid());
        query.put("secret", wx.getSecret());
        query.put("js_code", code);//js_code就是临时登录凭证，用来调用微信的接口获取用户openid
        String json = HttpClientUtil.doGet(WX_LOGIN_URL, query);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
