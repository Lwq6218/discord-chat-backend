package io.github.discordchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@MapperScan("io.github.discordchat.dao.mapper")
@ComponentScan(basePackages = {"io.github.discordchat", "com.gearwenxin"}
        ,excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = com.gearwenxin.config.WebSocketConfig.class)})
@EnableCaching
public class DiscordChatApplication {

    public static void main(String[] args) {
    /*    System.setProperty("proxyType", "4");
        System.setProperty("proxyHost", "127.0.0.1");
        System.setProperty("proxyPort", "7890");
        System.setProperty("proxySet", "true");*/
        SpringApplication.run(DiscordChatApplication.class, args);

    }

}
