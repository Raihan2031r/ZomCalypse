package com.raihan.frontend.commands;

import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;

public class RightCommand implements Command{
    private final Player player;
    public RightCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.moveRight();
    }

    @Override
    public void execute(Enemies enemy) {

    }
}
