package io.github.discordchat.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gearwenxin.client.ChatClient;
import com.gearwenxin.entity.response.ChatResponse;
import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dao.entity.Question;
import io.github.discordchat.dao.mapper.QuestionMapper;
import io.github.discordchat.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Objects;

/**
 * @classname OpenAIServiceImpl
 * @description TODO
 * @date 2024/7/8
 * @created by lwq
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIServiceImpl implements OpenAIService {


    private final QuestionMapper questionMapper;

    private final ChatClient chatClient;


    @Override
    public Flux<String> generateAnswer(Long questionId, Long userId) {
       //   查找问题
        Question question = questionMapper.selectById(questionId);
        if (Objects.isNull(question)) {
            throw new BusinessException(ErrorCodeEnum.QUESTION_NOT_EXIST);
        }
        //解析html
        Document document = Jsoup.parse(question.getContent());
        String content = document.text();
        //调用openai接口
        Flux<ChatResponse> responseFlux = chatClient.chatStream(content);
        return responseFlux.map(ChatResponse::getResult);
    }

    public static final String API_KEY = "LoklTaOjodM5kqVrx7PhD2Gl";
    public static final String SECRET_KEY = "zdU2ER2qhKxA0tNMFfv1knlhV4TLS0iL";

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public RestResp<String> getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        JsonNode jsonNode = OBJECT_MAPPER.readTree(response.body().string());
        return RestResp.ok(jsonNode.get("access_token").asText());
    }


}
