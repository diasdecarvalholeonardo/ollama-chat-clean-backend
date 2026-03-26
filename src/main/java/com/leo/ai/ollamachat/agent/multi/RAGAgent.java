package com.leo.ai.ollamachat.agent.multi;

import com.leo.ai.ollamachat.memory.vector.service.VectorMemoryService;
import com.leo.ai.ollamachat.model.router.ModelRouterService;

import org.springframework.stereotype.Service;

@Service
public class RAGAgent {

    private final VectorMemoryService memoryService;
    private final ModelRouterService modelRouter;

    public RAGAgent(
            VectorMemoryService memoryService,
            ModelRouterService modelRouter) {

        this.memoryService = memoryService;
        this.modelRouter = modelRouter;
    }

    public String handle(String question) {

        String context = memoryService.retrieveRelevantMemory(question);

        String prompt =
                "Answer using this knowledge:\n"
                + context
                + "\n\nQuestion:\n"
                + question;

        return modelRouter.generate(prompt);
    }
}
