package com.raihan.frontend.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface Animation {
    public TextureRegion getFrame(float stateTime, float facingX, float facingY);
    public void dispose();
}
