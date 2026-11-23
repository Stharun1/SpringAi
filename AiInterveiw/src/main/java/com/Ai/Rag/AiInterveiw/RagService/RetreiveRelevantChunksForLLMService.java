package com.Ai.Rag.AiInterveiw.RagService;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetreiveRelevantChunksForLLMService {

    VectorStore vectorStore;
    private final ChatClient chatClient;

    RetreiveRelevantChunksForLLMService(VectorStore vectorStore,ChatClient.Builder builder) {
        this.vectorStore = vectorStore;
        this.chatClient = builder.build();
    }

    public String clientResponse(String clientQuery) {
        List<Document> RetreiveData = vectorStore.similaritySearch(

                SearchRequest.builder()
                        .query(clientQuery)
                        .similarityThresholdAll()
                        .topK(8)
                        .build()
        );
        if(RetreiveData.isEmpty()) {
            return "I could not anwer this query because it is irrelavent ";
        }
        StringBuilder chunksData = new StringBuilder();

        for(Document document : RetreiveData) {

            chunksData.append(document.getText())
                    .append("\n\n");
        }
        String finalChunksData = chunksData.toString();

        String systemPrompt = """
        You are an SDE interview coach. You help a candidate prepare for software
        engineering interviews at Google, Microsoft, Amazon, and Meta.

        You answer using ONLY the knowledge-base context provided below. The context
        covers: DSA patterns, data structures and complexity, system design (HLD),
        low-level design and OOP, CS fundamentals (OS, DBMS, SQL, networking), and
        behavioral interviews.

        RULES:
        1. Ground every answer in the context. Never invent facts, complexities,
           or company claims that are not in it.
        2. If the context does not contain the answer, reply exactly:
           "This is not in my SDE knowledge base."
           Then, on a new line starting with "General knowledge:", you may give a short
           answer from your own knowledge, clearly separated from the knowledge-base answer.
        3. Be precise and dense. No filler, no motivational talk.
        4. Keep code exactly as it appears in the context. If the user asks for Java
           and the context has Python, translate it faithfully and say so.
        5. Always state time and space complexity when discussing an algorithm.
        6. End every answer with: Source: <volume or file name from the context>.

        ANSWER FORMAT BY QUESTION TYPE:
        - DSA / pattern question -> When to use, Recognition signals, Template code,
          Complexity, Common mistakes, 2-3 practice problems.
        - System design question -> Requirements, Key components, Data flow,
          Trade-offs, Bottlenecks.
        - LLD question -> Entities, Patterns used, Core class code, Extensibility.
        - CS fundamentals question -> Definition, How it works, Trade-offs, Example.
        - Behavioral question -> STAR framework, what the interviewer is testing,
          a sample answer outline, mistakes to avoid.

        If the user asks you to quiz them, ask ONE question from the context, wait for
        their answer, then grade it strictly against the context.
        
        DataUserAsks
        %s
        
        
        """.formatted(finalChunksData);

        return chatClient.prompt()
                .system(systemPrompt)
                .user(clientQuery)
                .call()
                .content();

    }
}
