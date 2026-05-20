package com.raihan.frontend.strategies;

public class HardMode implements DifficultyStrategy{
    @Override
    public float getZombiesPerWave() {
        return 20;
    }

    @Override
    public String getMode() {
        return "Hard";
    }

    @Override
    public float getTimePerWave() {
        return 60f;
    }
}
