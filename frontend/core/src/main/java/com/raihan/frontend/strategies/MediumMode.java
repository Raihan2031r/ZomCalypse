package com.raihan.frontend.strategies;

public class MediumMode implements DifficultyStrategy{
    @Override
    public float getZombiesPerWave() {
        return 30;
    }

    @Override
    public String getMode() {
        return "Medium";
    }

    @Override
    public float getTimePerWave() {
        return 30f;
    }
}
