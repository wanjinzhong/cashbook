package com.neil.cashbook.config;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class WechatProperties {

    @Value("${wx.appid}")
    private String appId;

    @Value(("${wx.appsecret}"))
    private String appSecret;
}
