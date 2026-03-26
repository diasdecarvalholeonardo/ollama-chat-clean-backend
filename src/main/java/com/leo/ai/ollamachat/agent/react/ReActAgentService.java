package com.leo.ai.ollamachat.agent.react;

import com.leo.ai.ollamachat.agent.tool.ToolExecutorService;
import com.leo.ai.ollamachat.model.router.ModelRouterService;
import com.leo.ai.ollamachat.memory.vector.service.VectorMemoryService;
import org.springframework.stereotype.Service;

@Service
public class ReActAgentService {

    private final ToolExecutorService toolExecutor;
    private final ModelRouterService modelRouter;
    private final VectorMemoryService vectorMemoryService;

    private static final int MAX_STEPS = 5;

    public ReActAgentService(
            ToolExecutorService toolExecutor,
            ModelRouterService modelRouter,
            VectorMemoryService vectorMemoryService) {

        this.toolExecutor = toolExecutor;
        this.modelRouter = modelRouter;
        this.vectorMemoryService = vectorMemoryService;
    }

    /**
     * Método principal do agente
     */
    public String runAgent(String userQuestion) {

        // 🔎 Buscar memória relevante
        String memoryContext =
                vectorMemoryService.retrieveRelevantMemory(userQuestion);

        String context =
                buildPrompt(userQuestion)
                + "\nRelevant Memory:\n"
                + memoryContext + "\n";

        for (int step = 0; step < MAX_STEPS; step++) {

            String llmResponse = modelRouter.generate(context);

            // terminou o raciocínio
            if (!llmResponse.contains("Action:")) {

                String finalAnswer;

                if (llmResponse.contains("Final Answer:")) {
                    finalAnswer = extractFinalAnswer(llmResponse);
                } else {
                    finalAnswer = llmResponse;
                }

                // 💾 salvar memória
                vectorMemoryService.storeMemory(userQuestion, finalAnswer);

                return finalAnswer;
            }

            // 🔧 executar ação
            AgentAction action = parseAction(llmResponse);

            String toolResult =
                    toolExecutor.executeTool(
                            action.getTool(),
                            action.getInput()
                    );

            // adicionar observação ao contexto
            context += "\nObservation: " + toolResult + "\n";
        }

        return "Agent stopped after max steps.";
    }

    /**
     * Compatibilidade com outros serviços
     */
    public String run(String question) {
        return runAgent(question);
    }

    private String buildPrompt(String question) {

        return """
You are an intelligent AI agent.

You can think, act, and observe.

You may use tools if necessary.

Use the format:

Thought: reasoning
Action: tool_name
Action Input: input
Observation: tool result

Repeat the loop if necessary.

When you know the answer:

Final Answer: answer

User Question:
""" + question;

    }

    private AgentAction parseAction(String response) {

        String tool = "";
        String input = "";

        for (String line : response.split("\n")) {

            if (line.startsWith("Action:")) {
                tool = line.replace("Action:", "").trim();
            }

            if (line.startsWith("Action Input:")) {
                input = line.replace("Action Input:", "").trim();
            }
        }

        return new AgentAction(tool, input);
    }

    private String extractFinalAnswer(String response) {

        for (String line : response.split("\n")) {

            if (line.startsWith("Final Answer:")) {
                return line.replace("Final Answer:", "").trim();
            }
        }

        return response;
    }
}