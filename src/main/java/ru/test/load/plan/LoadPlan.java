package ru.test.load.plan;

public enum LoadPlan {

    STANDARD("debug-plan", "acdd7cfc-ccd9-11ea-87d0-0242ac130005");

    private final String planName;
    private final String planId;

    LoadPlan(String planName, String planId) {
        this.planName = planName;
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public String getPlanId() {
        return planId;
    }

    public String getPlanDescription() {
        return "Модуль нагрузочного тестирования";
    }
}
