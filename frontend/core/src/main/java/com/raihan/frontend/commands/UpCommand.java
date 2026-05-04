package com.raihan.frontend.commands;

import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;

public class UpCommand implements Command{
    private final Player player;
    public UpCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.moveUp();
    }

    @Override
    public void execute(Enemies enemy) {

    }
}
