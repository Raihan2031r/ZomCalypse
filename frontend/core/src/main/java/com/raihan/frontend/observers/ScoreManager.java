package com.raihan.frontend.observers;

import java.util.ArrayList;
import java.util.List;

public class ScoreManager implements Subject {
    private int zombieKills;
    private int daysSurvived;
    private int spentScore;

    private final List<Observer> observers;

    public ScoreManager() {
        this.zombieKills = 0;
        this.daysSurvived = 0;
        this.spentScore = 0;
        this.observers = new ArrayList<>();
    }

    public void addKill() {
        zombieKills++;
        notifyObservers(getTotalScore());
    }

    public void addDay() {
        daysSurvived++;
        notifyObservers(getTotalScore());
    }

    public int getTotalScore() {
        return ((zombieKills * 10) + (daysSurvived * 100)) - spentScore;
    }

    public int getZombieKills() {
        return zombieKills;
    }

    public int getDaysSurvived() {
        return daysSurvived;
    }

    public int getSpentScore() {
        return spentScore;
    }

    public boolean spendScore(int cost) {
        if (getTotalScore() >= cost) {
            spentScore += cost;
            notifyObservers(getTotalScore());
            return true;
        }
        return false;
    }

    @Override
    public void addObserver(Observer observer) { observers.add(observer); }

    @Override
    public void removeObserver(Observer observer) { observers.remove(observer); }

    @Override
    public void notifyObservers(int score) {
        for (Observer observer : observers) {
            observer.update(score);
        }
    }

    public void resetScore() {
        this.zombieKills = 0;
        this.daysSurvived = 0;
        this.spentScore = 0;
    }

    public void loadGame(int zombieKills, int daysSurvived, int spentScore){
        this.zombieKills = zombieKills;
        this.daysSurvived = daysSurvived;
        this.spentScore = spentScore;
    }
}
