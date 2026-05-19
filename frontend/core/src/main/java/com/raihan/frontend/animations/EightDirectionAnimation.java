package com.raihan.frontend.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EightDirectionAnimation implements com.raihan.frontend.animations.Animation {
    private final Texture spriteSheet;
    private final Animation<TextureRegion>[] directionalAnimations;

    public EightDirectionAnimation(String filePath, int frameCols, int frameRows, float frameDuration, boolean LOOP) {
        spriteSheet = new Texture(filePath);

        int frameWidth = spriteSheet.getWidth() / frameCols;
        int frameHeight = spriteSheet.getHeight() / frameRows;

        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, frameWidth, frameHeight);

        directionalAnimations = new Animation[frameRows];
        for (int i = 0; i < frameRows; i++) {
            directionalAnimations[i] = new Animation<>(frameDuration, tmp[i]);
            if (LOOP) directionalAnimations[i].setPlayMode(Animation.PlayMode.LOOP);
            else directionalAnimations[i].setPlayMode(Animation.PlayMode.NORMAL);
        }
    }

    public TextureRegion getFrame(float stateTime, float facingX, float facingY) {
        int dirIndex = getDirectionIndex(facingX, facingY);
        if (dirIndex >= directionalAnimations.length) {
            dirIndex = 0;
        }

        return directionalAnimations[dirIndex].getKeyFrame(stateTime);
    }

    private int getDirectionIndex(float facingX, float facingY) {
        if (facingX > 0 && facingY > 0) return 4;
        else if (facingX > 0 && facingY < 0) return 5;
        else if (facingX < 0 && facingY > 0) return 2;
        else if (facingX < 0 && facingY < 0) return 1;
        else if (facingX > 0) return 6;
        else if (facingX < 0) return 7;
        else if (facingY > 0) return 3;
        else if (facingY < 0) return 0;

        return 0;
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
