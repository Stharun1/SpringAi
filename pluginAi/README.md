# pluginAi — Tool-Calling AI Agent

A Spring AI app where the LLM decides on its own which tools to call to answer a question. You can ask something that needs live weather, a currency conversion and some math in one sentence, and it works out the steps, calls the right tools in order, and combines the results into one answer.

## Example

Request:
```
What's the current temperature in Hyderabad? Also convert 5000 INR to USD,
then multiply that USD amount by 3 and add 25.
```

To answer this, the model calls the weather tool for Hyderabad, the currency tool for INR to USD, and the calculator tool twice (multiply, then add), then writes a short answer that puts it all together.

## Tools

| Tool | What it does | Data source |
|---|---|---|
| `WeatherTool` | Looks up the city's coordinates, then fetches current weather | OpenWeather API |
| `CurrencyConverter` | Converts an amount between currencies | FastForex API |
| `CalculatorTool` | Arithmetic, powers, roots, trig, logs, rounding | Local (no API) |

Each tool is a Spring bean with methods annotated `@Tool`. Spring AI sends the tool descriptions to the model, and the model decides when to call them and with what arguments.

## Tech used

- Java 17+
- Spring Boot
- Spring AI
- OpenAI gpt-4o-mini
- Spring WebFlux (for the streaming endpoint)

## Running it locally

You need an OpenAI API key, an [OpenWeather](https://openweathermap.org/api) API key, and a [FastForex](https://www.fastforex.io/) API key.

**1. Clone the repo**
```
git clone https://github.com/Stharun1/SpringAi.git
cd SpringAi/pluginAi
```

**2. Set your API keys**

Windows (PowerShell):
```
$env:OPENAI_API_KEY="your-key"
$env:WEATHER_API_KEY="your-key"
$env:CURRENCY_API_KEY="your-key"
```

Mac/Linux:
```
export OPENAI_API_KEY="your-key"
export WEATHER_API_KEY="your-key"
export CURRENCY_API_KEY="your-key"
```

In IntelliJ you can add them under Run > Edit Configurations > Environment variables.

**3. Run**

Open the project in IntelliJ and run `PluginAiApplication`, or from the terminal:
```
mvn spring-boot:run
```

The app starts on http://localhost:8080

## API

### Chat with tools
```
POST /api/summarize
Content-Type: text/plain
```
Returns the full answer at once. Keeps the conversation history, so follow-up questions work.

### Streaming chat with tools
```
POST /api/stream
Content-Type: text/plain
```
Same as above, but streams the answer back as it's generated.

Example:
```
curl -X POST http://localhost:8080/api/summarize -H "Content-Type: text/plain" -d "What's the weather in London and what is 25 * 4?"
```

### Embeddings
```
POST /embeding
Content-Type: text/plain
```
Returns the embedding vector (array of floats) for the given text.

## Project structure

```
pluginAi/
├── src/main/java/com/ai/pluginAi/
│   ├── AiTools/
│   │   ├── CalculatorTool.java
│   │   ├── CurrencyConverter.java
│   │   └── WeatherTool.java
│   ├── Controller/
│   │   ├── SummarizeController.java
│   │   └── EmbeddingController.java
│   └── Service/
│       ├── SummarizeService.java
│       └── EmbeddingService.java
├── src/main/resources/application.properties
└── pom.xml
```

## Common issues

- **"Could not resolve placeholder"**: one of the API keys isn't set. Set it and restart your terminal or IDE.
- **Currency conversion fails**: the FastForex key is invalid or the free trial has ended. Get a new key.
- **Weather says "Could not find location"**: check the city spelling.

## What I'd improve next

- Chat history is currently one shared list for all users. I want to move it to Spring AI's chat memory with a conversation ID per user.
- The streaming endpoint doesn't use the conversation history yet.
- Swap FastForex for a free exchange-rate API that doesn't need a key.
