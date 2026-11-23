package com.ai.pluginAi.Controller;

import com.ai.pluginAi.Service.SummarizeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class SummarizeController {

    private SummarizeService summarizeService;
    public SummarizeController(SummarizeService summarizeService){
        this.summarizeService = summarizeService;
    }


    @PostMapping("/summarize")
    public String summarize(@RequestBody String ticket) {
        return summarizeService.summarize(ticket);
    }

    @PostMapping("/stream")
    public Flux<String> stream(@RequestBody String ticket) {

        return summarizeService.streamOutput(ticket);
    }
}
