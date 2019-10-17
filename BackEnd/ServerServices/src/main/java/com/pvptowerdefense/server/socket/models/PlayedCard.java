package com.pvptowerdefense.server.socket.models;

import com.pvptowerdefense.server.spring.models.Card;

public class PlayedCard extends Card {
    private int xValue;
    private int yValue;
    private int player;


    /**
     * Instantiates a new Card.
     *
     * @param name        the name
     * @param description the description
     * @param cost        the cost
     * @param damage      the damage
     * @param hitPoints   the hit points
     * @param speed       the speed
     * @param type        the type
     * @param range       the range
     */
    public PlayedCard(String name, String description, int cost, int damage, int hitPoints, int speed, String type, int range, int xValue, int yValue, int player) {
        super(name, description, cost, damage, hitPoints, speed, type, range);
        this.xValue = xValue;
        this.yValue = yValue;
        this.player = player;
    }


    public int getxValue() {
        return xValue;
    }

    public void setxValue(int xValue) {
        this.xValue = xValue;
    }

    public int getyValue() {
        return yValue;
    }

    public void setyValue(int yValue) {
        this.yValue = yValue;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}
