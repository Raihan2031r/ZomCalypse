package com.raihan.frontend.network;

import com.raihan.frontend.entities.item.Weapon;

public class NetworkPackets {
    public NetworkPackets(){}
    public static class PlayerMove{
        public String username;
        public float x;
        public float y;
    }

    public static class PlayerAttack {
        public String username;
        public Weapon weapon;
        public float targetX;
        public float targetY;
    }
}
