package com.raihan.frontend.commands;

import com.raihan.frontend.entities.enemies.Enemies;

public interface Command {
    void execute();
    void execute(Enemies enemy);
}
