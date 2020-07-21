package cn.yours.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;


/**
 * 基本配置类
 */
@Configuration
@ComponentScans({@ComponentScan("cn.yours.web.config"),@ComponentScan("cn.yours.web.controller")})
public class ElfinderAutoConfiguration {
}
