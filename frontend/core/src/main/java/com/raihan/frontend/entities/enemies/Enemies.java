package com.raihan.frontend.entities.enemies;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public abstract class Enemies {
    private float HP;
    private Circle detectionRadius;
    private Circle attackRadius;
    private Rectangle collider;

    public void takeDamage(float damage){
        if(this.HP > 0){
            this.HP -= damage;
            if (this.HP < 0) this.HP = 0;
        }
    }

    public Circle getDetectionRadius() {
        return detectionRadius;
    }
}
