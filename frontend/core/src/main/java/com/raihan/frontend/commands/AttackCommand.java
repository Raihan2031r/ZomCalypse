package com.raihan.frontend.commands;

import com.raihan.frontend.entities.Player;

public class AttackCommand  implements Command{
    private final Player player;
    public AttackCommand(Player player){ this.player = player; }
    @Override
    public void execute() {
        
    }
}
