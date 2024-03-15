package org.sici.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.sici.custom.CustomUser;
import org.sici.result.ResponseUtil;
import org.sici.result.Result;
import org.sici.result.ResultCodeEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import util.JwtHelper;
import org.sici.vo.system.LoginVo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @projectName: oa-parent
 * @package: org.sici.filter
 * @className: TokenLoginFilter
 * @author: 749291
 * @description: TODO
 * @date: 3/9/2024 4:53 PM
 * @version: 1.0
 */

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private RedisTemplate redisTemplate;
    // 构造方法
    public TokenLoginFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate) {
        super.setAuthenticationManager(authenticationManager);
        super.setPostOnly(false);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login", "POST"));

        this.redisTemplate = redisTemplate;
    }

    // 登录认证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            UsernamePasswordAuthenticationToken unauthenticated = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            super.setDetails(request, unauthenticated);
            return super.getAuthenticationManager().authenticate(unauthenticated);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 认证成功调用
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//         获取当前用户
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        // 生成token
        String token = JwtHelper.createToken(customUser.getSysUser().getId(), customUser.getSysUser().getUsername());
        // 返回token
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("token", token);

        redisTemplate.opsForValue().set(customUser.getSysUser().getUsername(), JSON.toJSONString(customUser.getAuthorities()));

        ResponseUtil.out(response, Result.ok(stringObjectHashMap));
    }

    // 认证失败调用
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException, IOException {
        ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
    }
}
