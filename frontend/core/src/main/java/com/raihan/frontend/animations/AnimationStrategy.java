package com.raihan.frontend.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.raihan.frontend.entities.item.Weapon;
import com.raihan.frontend.entities.item.Rifle;
import com.raihan.frontend.states.playerStates.*;

import java.util.HashMap;
import java.util.Map;

public class AnimationStrategy {
    private final Map<String, Animation> animations;

    public AnimationStrategy() {
        animations = new HashMap<>();

        animations.put("IDLE_SPEAR", new SixDirectionAnimation("Animations/Idle_Spear.png", 8, 6, 1/12f, true));
        animations.put("IDLE_GUN", new SixDirectionAnimation("Animations/Idle_Gun.png", 8, 6, 1/12f, true));

        animations.put("WALK_SPEAR", new SixDirectionAnimation("Animations/walk_Spear.png", 8, 6, 1/12f, true));
        animations.put("WALK_GUN", new SixDirectionAnimation("Animations/walk_Gun.png", 8, 6, 1/12f, true));

        animations.put("RUN_SPEAR", new SixDirectionAnimation("Animations/Run_Spear.png", 8, 6, 1/12f, true));
        animations.put("RUN_GUN", new EightDirectionAnimation("Animations/Run_Gun.png", 8, 8, 1/12f, true));

        animations.put("ATTACK_SPEAR", new EightDirectionAnimation("Animations/Attack_Spear.png", 8, 8, 1/12f, true));
        animations.put("SHOOT_GUN", new EightDirectionAnimation("Animations/Shooting.png", 8, 8, 1/12f, true));

        animations.put("WALK_SHOOT_GUN", new EightDirectionAnimation("Animations/walk_Shooting.png", 8, 8, 1/12f, true));
        animations.put("RUN_SHOOT_GUN", new EightDirectionAnimation("Animations/Run_while_shooting.png", 8, 8, 1/12f, true));

        animations.put("RELOAD_GUN", new SixDirectionAnimation("Animations/Reloading.png", 8, 6, 1/12f, true));
        animations.put("WALK_RELOAD_GUN", new EightDirectionAnimation("Animations/walk_reloading.png", 8, 8, 1/12f, true));

        animations.put("DEATH", new SixDirectionAnimation("Animations/death.png", 8, 6, 1/12f, false));
    }

    public TextureRegion getCurrentFrame(PlayerState state, Weapon weapon, Vector2 velocity, boolean isAttacking, boolean isReloading, float stateTime, float facingX, float facingY) {
        String animationKey = determineKey(state, weapon, velocity, isAttacking, isReloading);
        Animation currentAnim = animations.get(animationKey);

        if (currentAnim != null) {
            return currentAnim.getFrame(stateTime, facingX, facingY);
        }
        return animations.get("IDLE_" + (weapon instanceof Rifle ? "GUN" : "SPEAR")).getFrame(stateTime, facingX, facingY);
    }

    private String determineKey(PlayerState state, Weapon weapon, Vector2 velocity, boolean isAttacking, boolean isReloading) {
        if (state instanceof DyingState) return "DEATH";

        String statePart = "IDLE";
        String weaponPart = (weapon instanceof Rifle) ? "GUN" : "SPEAR";

        boolean isMoving = !velocity.isZero();

        if (isMoving) {
            if (state instanceof RunningState) {
                statePart = "RUN";
            } else {
                statePart = "WALK";
            }
        }

        if (isReloading && weapon instanceof Rifle) {
            if (statePart.equals("IDLE")) statePart = "RELOAD";
            else if (statePart.equals("WALK")) statePart = "WALK_RELOAD";
            else statePart = "RELOAD";
        }
        else if (isAttacking) {
            if (statePart.equals("IDLE")) {
                statePart = (weapon instanceof Rifle) ? "SHOOT" : "ATTACK";
            } else {
                statePart = statePart + "_SHOOT";
            }
        }

        return statePart + "_" + weaponPart;
    }

    public void dispose() {
        for (Animation anim : animations.values()) {
            anim.dispose();
        }
    }
}
