package com.bot.matsTestBot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//@Data
//@Configuration
//@PropertySource("application.properties")
@Component
@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "bot")
public class TgBotConfig {
////    @Value("${bot.name}")
//    String botName;
////    @Value("${bot.key}")
//    String token;
    private String name;
    private String key;
}
