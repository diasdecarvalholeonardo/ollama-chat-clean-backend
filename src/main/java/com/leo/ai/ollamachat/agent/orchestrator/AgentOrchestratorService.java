package com.leo.ai.ollamachat.agent.orchestrator;

import com.leo.ai.ollamachat.agent.graph.AgentGraphEngine;
import com.leo.ai.ollamachat.agent.memory.AgentScratchpad;
import com.leo.ai.ollamachat.agent.memory.AgentScratchpadService;
import com.leo.ai.ollamachat.agent.multi.SupervisorAgent;
import com.leo.ai.ollamachat.agent.plan.PlanningAgentService;
import com.leo.ai.ollamachat.agent.task.TaskClassifierService;
import com.leo.ai.ollamachat.agent.task.TaskType;
import org.springframework.stereotype.Service;

@Service
public class AgentOrchestratorService {

    private final TaskClassifierService classifier;
    private final PlanningAgentService planner;
    private final AgentScratchpadService scratchpadService;
    private final SupervisorAgent supervisor;
    private final AgentGraphEngine agentGraph;

    public AgentOrchestratorService(
            TaskClassifierService classifier,
            PlanningAgentService planner,
            AgentScratchpadService scratchpadService,
            SupervisorAgent supervisor,
            AgentGraphEngine agentGraph) {

        this.classifier = classifier;
        this.planner = planner;
        this.scratchpadService = scratchpadService;
        this.supervisor = supervisor;
        this.agentGraph = agentGraph;
    }

    public String handleQuestion(String question) {

        // 1️⃣ Criar scratchpad
        AgentScratchpad scratchpad = scratchpadService.create();

        scratchpad.addThought("User asked: " + question);

        // 2️⃣ Classificar tarefa
        TaskType taskType = classifier.classify(question);

        scratchpad.addThought("Task classified as: " + taskType);

        // 3️⃣ Planejamento (caso necessário)
        try {

            String plan = planner.plan(question);

            scratchpad.addThought("Execution plan: " + plan);

        } catch (Exception e) {

            scratchpad.addThought("No planning required.");

        }

        // 4️⃣ Delegar ao SupervisorAgent
        String response = agentGraph.run(question);

        // 5️⃣ Registrar observação
        scratchpad.addObservation(response);

        // 6️⃣ Retornar resposta
        return response;
    }
}