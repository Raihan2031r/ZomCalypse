package com.raihan.frontend.strategies;

public class EasyMode implements DifficultyStrategy {
    @Override
    public float getZombiesPerWave() {
        return 15f;
    }

    @Override
    public String getMode() {
        return "Easy";
    }

    @Override
    public float getTimePerWave() {
        return 20f;
    }
}
