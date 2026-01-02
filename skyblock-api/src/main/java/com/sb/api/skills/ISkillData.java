package com.sb.api.skills;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public interface ISkillData extends ComponentV3 {
    double getXp(SBSkill skill);
    int getLevel(SBSkill skill);
    void addXp(SBSkill skill, double amount);
    void setXp(SBSkill skill, double amount);
    double getXpForNextLevel(int currentLevel);
}
