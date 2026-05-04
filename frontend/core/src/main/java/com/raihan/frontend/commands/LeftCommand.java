package com.raihan.frontend.commands;

import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;

public class LeftCommand implements Command{
    private final Player player;
    public LeftCommand(Player player){ this.player = player; }

    @Override
    public void execute() {
        player.moveLeft();
    }

    @Override
    public void execute(Enemies enemy) {

    }
}
