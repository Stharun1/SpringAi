package com.ai.pluginAi.Service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
    public EmbeddingService(EmbeddingModel embeddingModel) {

        this.embeddingModel = embeddingModel;
    }

    public float[] embeddings(String query) {

        float[] embd = embeddingModel.embed(query);

        return embd;
    }

}
