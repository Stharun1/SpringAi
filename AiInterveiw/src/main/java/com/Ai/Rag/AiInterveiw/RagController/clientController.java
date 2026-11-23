package com.Ai.Rag.AiInterveiw.RagController;

import com.Ai.Rag.AiInterveiw.RagService.PdfToChunksService;
import com.Ai.Rag.AiInterveiw.RagService.RetreiveRelevantChunksForLLMService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class clientController {

    PdfToChunksService pdfToChunksService;
    RetreiveRelevantChunksForLLMService retreiveRelevantChunksForLLMService;

    clientController(PdfToChunksService pdfToChunksService,
                     RetreiveRelevantChunksForLLMService retreiveRelevantChunksForLLMService) {
        this.pdfToChunksService = pdfToChunksService;
        this.retreiveRelevantChunksForLLMService = retreiveRelevantChunksForLLMService;
    }

    @PostMapping("/query")
    public String query(@RequestBody String query) {

        return retreiveRelevantChunksForLLMService.clientResponse(query);
    }
}
