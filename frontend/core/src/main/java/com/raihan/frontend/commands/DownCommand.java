package com.raihan.frontend.commands;

import com.raihan.frontend.entities.Player;

public class DownCommand implements Command{
    private final Player player;
    public DownCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.moveDown();
    }
}
