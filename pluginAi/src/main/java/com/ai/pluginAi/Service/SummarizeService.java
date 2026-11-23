package com.ai.pluginAi.Service;

import com.ai.pluginAi.AiTools.CalculatorTool;
import com.ai.pluginAi.AiTools.CurrencyConverter;
import com.ai.pluginAi.AiTools.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummarizeService {

    private ChatClient chatClient;
    private CalculatorTool calculatorTool;
    private WeatherTool weatherTool;
    private CurrencyConverter currencyConverter;


    List<Message> history = new ArrayList<>();
    private String SYSTEM_PROMPT = """
            dont give resposne more than 5 line and humanize the text if used external tools as response
            and give resposnse in proper readable format line by line not in single one line
            """;

    public SummarizeService(ChatClient.Builder builder,
                            CalculatorTool calculatorTool,
                            WeatherTool weatherTool,
                            CurrencyConverter currencyConverter) {

        this.chatClient = builder.build();
        this.calculatorTool = calculatorTool;
        this.weatherTool = weatherTool;
        this.currencyConverter = currencyConverter;
    }

    public String summarize(String ticket) {

        history.add(new UserMessage(ticket));
        //System.out.println(MessageType.USER);
        String output = chatClient.prompt()
              //  .user("kf")
                .messages(history)
                .system(SYSTEM_PROMPT)
                .tools(calculatorTool, weatherTool, currencyConverter)
                .call()
                .content();

        history.add(new AssistantMessage(output));

        return output;
    }
    public Flux<String> streamOutput(String ticket){

        StringBuilder streams = new StringBuilder();
        history.add(new UserMessage(ticket));

        Flux<String> output = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(ticket)
                .tools(calculatorTool, weatherTool, currencyConverter)
                .stream()
                .content()
                .doOnNext(streams::append)
                .doOnComplete(() -> history.add(new AssistantMessage(streams.toString())));

        return output;
    }

}
