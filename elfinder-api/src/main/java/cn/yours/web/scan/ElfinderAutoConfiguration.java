package cn.yours.web.scan;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;


/**
 * 基本配置类
 */
@Configuration
@AutoConfigureOrder(1024)
@ComponentScans({@ComponentScan("cn.yours.web.config")
        , @ComponentScan("cn.yours.web.controller")
        , @ComponentScan("cn.yours.elfinder.factory")
})
public class ElfinderAutoConfiguration {
}
