package com.raihan.frontend.commands;

import com.raihan.frontend.entities.Player;

public class RunCommand implements Command{
    private  final Player player;
    public RunCommand(Player player){ this.player = player; }

    @Override
    public void execute() { player.run(); }
}
