package com.raihan.frontend.strategies;

public class EasyMode implements DifficultyStrategy {
    @Override
    public float getZombiesPerWave() {
        return 15;
    }

    @Override
    public String getMode() {
        return "Easy";
    }

    @Override
    public float getTimePerWave() {
        return 90f;
    }
}
