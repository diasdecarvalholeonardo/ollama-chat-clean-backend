package com.leo.ai.ollamachat.agent.graph;

import com.leo.ai.ollamachat.agent.graph.nodes.ReActNode;
import com.leo.ai.ollamachat.agent.graph.nodes.ReflectionNode;
import com.leo.ai.ollamachat.agent.graph.nodes.TaskClassifierNode;
import com.leo.ai.ollamachat.agent.quality.AnswerQualityService;
import com.leo.ai.ollamachat.agent.state.AgentState;
import org.springframework.stereotype.Service;

@Service
public class AgentGraphEngine {

    private final TaskClassifierNode classifierNode;
    private final ReActNode reactNode;
    private final ReflectionNode reflectionNode;

    // Novo componente (Loop Quality)
    private final AnswerQualityService qualityService;

    // controle de loops
    private static final int MAX_ITERATIONS = 3;

    public AgentGraphEngine(
            TaskClassifierNode classifierNode,
            ReActNode reactNode,
            ReflectionNode reflectionNode,
            AnswerQualityService qualityService) {

        this.classifierNode = classifierNode;
        this.reactNode = reactNode;
        this.reflectionNode = reflectionNode;
        this.qualityService = qualityService;
    }

    public String run(String question) {

        AgentState state = new AgentState(question);

        // etapa 1 — classificar tarefa
        state = classifierNode.execute(state);

        // etapa 2 — loop de raciocínio
        for (int i = 0; i < MAX_ITERATIONS; i++) {

            // executar agente ReAct
            state = reactNode.execute(state);

            // reflexão da resposta
            state = reflectionNode.execute(state);

            // se não houver qualityService, manter compatibilidade
            if (qualityService == null) {
                break;
            }

            // verificar qualidade da resposta
            boolean good = qualityService.isAnswerGood(
                    state.getQuestion(),
                    state.getAnswer()
            );

            // se for boa, terminar
            if (good) {
                break;
            }
        }

        return state.getAnswer();
    }
}