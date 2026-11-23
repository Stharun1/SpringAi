# SDE Prep Bot

I built this to prepare for SDE interviews. It's a RAG chatbot that answers questions from a knowledge base of six PDFs I put together on DSA patterns, data structures, system design, LLD, CS fundamentals, and behavioral rounds.

Instead of letting the model answer from memory, it searches the PDFs for relevant sections first and answers only from those. If the answer isn't in the knowledge base, it says so.

<img width="1247" height="767" alt="SDE Prep Bot" src="https://github.com/user-attachments/assets/a87d1d90-d1a5-4b6e-a06d-4b2fa940ef40" />
(./docs/example-output.png)

## How it works

1. On startup, the PDFs in `src/main/resources/SDE_RAG/` are split into 300-token chunks.
2. Each chunk is converted to an embedding with OpenAI `text-embedding-3-small` and stored in Pinecone.
3. When a question comes in, the 8 most similar chunks are retrieved from Pinecone.
4. Those chunks are added to the system prompt and sent to `gpt-4o-mini` along with the question.
5. The answer comes back with the source volume it was taken from.

The prompt also controls the answer format. DSA questions get the pattern, template code, complexity and practice problems. System design questions get components, data flow and trade-offs. Behavioral questions get a STAR outline.

## Tech used

- Java 21
- Spring Boot 4.1.1
- Spring AI 2.0.1
- OpenAI (gpt-4o-mini, text-embedding-3-small)
- Pinecone
- Plain HTML/JS frontend

## Running it locally

You need Java 21, an OpenAI API key, and a Pinecone account (the free tier works).

**1. Create a Pinecone index**

Name: `sde-rag-index`
Dimensions: `1024`
Metric: `cosine`

The dimensions have to be 1024 or inserts will fail.

**2. Clone the repo**

```
git clone https://github.com/Stharun1/SpringAi.git
cd SpringAi/AiInterveiw
```

**3. Set your API keys**

Windows (PowerShell):
```
$env:OPENAI_API_KEY="your-key"
$env:PINECONE_API_KEY="your-key"
```

Mac/Linux:
```
export OPENAI_API_KEY="your-key"
export PINECONE_API_KEY="your-key"
```

In IntelliJ you can add them under Run > Edit Configurations > Environment variables.

**4. Run**

Windows:
```
.\mvnw.cmd spring-boot:run
```

Mac/Linux:
```
./mvnw spring-boot:run
```

The first start takes a little while because all six PDFs get embedded. Once you see `Started AiInterveiwApplication`, it's ready.

**5. Open the app**

Go to http://localhost:8080

You can pick a topic tab, click one of the suggested questions, or type your own. "Quiz me" makes the bot ask you a question and grade your answer. The "Java" toggle returns code in Java.

## API

There's one endpoint if you want to call it directly:

```
POST /api/query
Content-Type: text/plain
```

Example:
```
curl -X POST http://localhost:8080/api/query -H "Content-Type: text/plain" -d "Explain the sliding window pattern"
```

The response is the answer as plain text.

## Project structure

```
AiInterveiw/
├── src/main/java/com/Ai/Rag/AiInterveiw/
│   ├── RagController/clientController.java
│   └── RagService/
│       ├── PdfToChunksService.java
│       └── RetreiveRelevantChunksForLLMService.java
├── src/main/resources/
│   ├── application.properties
│   ├── SDE_RAG/          (knowledge base PDFs)
│   └── static/index.html (frontend)
└── pom.xml
```

## Common issues

- **"Could not resolve placeholder OPENAI_API_KEY"**: the environment variable isn't set. Set it and restart your terminal or IDE.
- **Pinecone dimension error**: the index wasn't created with 1024 dimensions. Recreate it.
- **Port 8080 in use**: stop whatever is running on it, or add `server.port=8081` to `application.properties`.

## What I'd improve next

- Right now the PDFs are re-embedded every time the app starts, which creates duplicate vectors. I want it to skip ingestion when the index already has data.
- Add chat memory so follow-up questions keep context.
