package com.raihan.frontend.commands;

import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;
import com.raihan.frontend.entities.item.Rifle;
import com.raihan.frontend.factories.BulletFactory;

import java.util.List;

public class AttackCommand  implements Command{
    private final Player player;
    public AttackCommand(Player player){ this.player = player; }
    @Override
    public void execute() {

    }

    @Override
    public void execute(Enemies enemy) {

    }

    public void execute(BulletFactory factory, List<Enemies> enemies){
        if(player.getWeapon() instanceof Rifle ) player.attack(factory);
        else {
            for (Enemies e: enemies){
                player.attack(e);
            }
        }
    }
}
