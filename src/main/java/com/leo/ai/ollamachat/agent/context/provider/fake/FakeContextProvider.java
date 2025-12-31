package com.leo.ai.ollamachat.agent.context.provider.fake;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.leo.ai.ollamachat.agent.context.model.ContextRequest;
import com.leo.ai.ollamachat.agent.context.model.ContextResponse;
import com.leo.ai.ollamachat.agent.context.provider.ContextProvider;

@Component
@Profile("fake")
public class FakeContextProvider implements ContextProvider {

    @Override
    public ContextResponse resolve(ContextRequest request) {
        return new ContextResponse(
            List.of("fake"),
            "Fake context for input: " + request.userInput()
        );
    }
}
