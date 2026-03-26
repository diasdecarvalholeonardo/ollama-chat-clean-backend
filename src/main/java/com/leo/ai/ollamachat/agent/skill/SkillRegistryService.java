package com.leo.ai.ollamachat.agent.skill;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkillRegistryService {

    private final Map<String, AgentSkill> skills = new HashMap<>();

    public SkillRegistryService(List<AgentSkill> skillList) {

        if (skillList != null) {
            for (AgentSkill skill : skillList) {
                skills.put(skill.getName(), skill);
            }
        }
    }

    public AgentSkill getSkill(String name) {
        return skills.get(name);
    }

    public Map<String, AgentSkill> getAllSkills() {
        return skills;
    }
}
