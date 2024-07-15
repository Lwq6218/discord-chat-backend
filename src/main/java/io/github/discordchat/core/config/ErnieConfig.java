package io.github.discordchat.core.config;

import com.gearwenxin.client.ChatClient;
import com.gearwenxin.config.ModelConfig;
import com.gearwenxin.model.ChatModel;
import com.gearwenxin.schedule.entity.ModelHeader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @classname ErnieConfig
 * @description TODO
 * @date 2024/7/8
 * @created by lwq
 */
@Configuration
public class ErnieConfig {
    @Bean
    // 对应的模型名称，建议与modelConfig.setModelName("Ernie")保持一致 （允许不一致）
    @Qualifier("Ernie")
    public ChatClient ernieClient() {
        ModelConfig modelConfig = new ModelConfig();
        // 模型名称，必须跟设置的QPS数值的名称一致 (强烈建议与官网名称一致)
        modelConfig.setModelName("Ernie");
        // 模型url
        modelConfig.setModelUrl("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions");
        // 单独设置某个模型的access-token, 优先级高于全局access-token, 统一使用全局的话可以不设置
//        modelConfig.setAccessToken("xx.xx.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        // 请求头，可选，不设置则使用默认值
        ModelHeader modelHeader = new ModelHeader();
        // 一分钟内允许的最大请求次数
        modelHeader.set_X_Ratelimit_Limit_Requests(100);
        // 一分钟内允许的最大tokens消耗，包含输入tokens和输出tokens
        modelHeader.set_X_Ratelimit_Limit_Tokens(2000);
        // 达到RPM速率限制前，剩余可发送的请求数配额，如果配额用完，将会在0-60s后刷新
        modelHeader.set_X_Ratelimit_Remaining_Requests(1000);
        // 达到TPM速率限制前，剩余可消耗的tokens数配额，如果配额用完，将会在0-60s后刷新
        modelHeader.set_X_Ratelimit_Remaining_Tokens(5000);

        modelConfig.setModelHeader(modelHeader);

        return new ChatClient(modelConfig);
    }
}
