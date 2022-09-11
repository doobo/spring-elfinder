package cn.yours.web.config;

import cn.yours.elfinder.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource(value = "classpath:manager-default.yml"
        , encoding = "utf-8", factory = YamlPropertySourceFactory.class)
public class ElFinderDefaultConfig {

    @Bean
    @ConfigurationProperties("manager-default")
    @ConditionalOnMissingBean(name = "defaultElfinderConfiguration")
    public ElfinderConfiguration defaultElfinderConfiguration() {
        return new ElfinderConfiguration();
    }
}
