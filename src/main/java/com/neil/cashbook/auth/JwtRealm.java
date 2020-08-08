package com.neil.cashbook.auth;
import com.neil.cashbook.dao.entity.User;
import com.neil.cashbook.dao.repository.UserRepository;
import com.neil.cashbook.service.UserService;
import com.neil.cashbook.util.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String token = (String) authenticationToken.getPrincipal();
        if (token == null) {
            throw new NullPointerException("jwtToken 不允许为空");
        }
        //判断
        JwtUtil jwtUtil = new JwtUtil();
        if (!jwtUtil.isVerify(token)) {
            throw new UnknownAccountException("请重新登录");
        }
        String openId = (String) jwtUtil.decode(token).get("sub");
        User user = userRepository.findByOpenId(openId);
        userService.setCurrentUser(openId);
        if (user == null) {
            // 这里返回后会报出对应异常
            return null;
        } else {
            // 这里验证authenticationToken和simpleAuthenticationInfo的信息
            return new SimpleAuthenticationInfo(openId, token, getName());
        }
    }
}