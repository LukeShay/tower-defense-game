package com.example.towerDefender.Card;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.example.towerDefender.Game.GameObjectSprite;
import com.example.towerDefender.Game.Sprite;

import java.io.Serializable;

/**
 * The type Played card.
 */
public class PlayedCard implements Serializable {
    private static final long serialVersionUID = 69L;

    private String name;
    private String description;
    private int cost;
    private int damage;
    private int hitPoints;
    private int speed;
    private String type;
    private int range;
    private int xValue;
    private int yValue;
    private boolean attacking;
    private String cardAttacking;
    private int cardAttackingDistance;
    private String player;
    private static Paint textPaint;
    private GameObjectSprite sprite;

    /**
     * Constructs a new {@link PlayedCard} with the provided parameters.
     * @param cardToPlay the {@link Card} to play and construct this played card from
     * @param xValue x value of the current card
     * @param yValue y value of the current card
     * @param player the player who played the card
     */
    public PlayedCard(Card cardToPlay, int xValue, int yValue, String player){
        this.name = cardToPlay.cardName;
        this.description = cardToPlay.cardDescription;
        this.cost = cardToPlay.castingCost;
        this.damage = cardToPlay.damage;
        this.hitPoints = cardToPlay.hitPoints;
        this.speed = cardToPlay.speed;
        this.type =  cardToPlay.type;
        this.range = cardToPlay.range;
        this.xValue = xValue;
        this.yValue = yValue;
        this.player = player;
        this.sprite = null;
        textPaint = new Paint(Color.LTGRAY);
        textPaint.setTextSize(50);
    }

    /**
     * Gets serial version uid.
     *
     * @return the serial version uid
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets cost.
     *
     * @return the cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Sets cost.
     *
     * @param cost the cost
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Gets damage.
     *
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Sets damage.
     *
     * @param damage the damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Gets hit points.
     *
     * @return the hit points
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Sets hit points.
     *
     * @param hitPoints the hit points
     */
    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    /**
     * Gets speed.
     *
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets speed.
     *
     * @param speed the speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets range.
     *
     * @return the range
     */
    public int getRange() {
        return range;
    }

    /**
     * Sets range.
     *
     * @param range the range
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getxValue() {
        return xValue;
    }

    /**
     * Sets value.
     *
     * @param xValue the x value
     */
    public void setxValue(int xValue) {
        this.xValue = xValue;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getyValue() {
        return yValue;
    }

    /**
     * Sets value.
     *
     * @param yValue the y value
     */
    public void setyValue(int yValue) {
        this.yValue = yValue;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Sets player.
     *
     * @param player the player
     */
    public void setPlayer(String player) {
        this.player = player;
    }

    /**
     * Constructs a {@link Card} from the parameters of this played card
     */
    public Card getCard(){
        return new Card(this.name, this.description, this.cost, this.damage, this.hitPoints, this.speed, this.type, this.range);
    }

    /**
     * Updates and draws the wrapped {@link GameObjectSprite}, initializing it if necessary.
     * @param canvas the canvas to draw to
     */
    public void draw(Canvas canvas){
        if(this.sprite == null){
            Log.e("ERROR", "No sprite associated with card (" + this.name +"). Please addOrUpdate sprite.");
        } else if(hitPoints > 0){
            if(this.attacking){
                this.sprite.setStatus(Sprite.SPRITE_STATUS.ATTACKING);
            } else {
                this.sprite.setStatus(Sprite.SPRITE_STATUS.MOVING);
            }
            this.sprite.xStart = this.xValue;
            this.sprite.yStart = this.yValue;
            this.sprite.draw(canvas);
            Rect health = new Rect(this.sprite.xStart + (this.sprite.image.getWidth()/2), this.sprite.yStart, this.sprite.xStart + (100 / this.getHitPoints()), this.sprite.yStart + 10);
            Paint myPaint = new Paint();
            myPaint.setColor(Color.rgb(0, 255, 0));
            myPaint.setStrokeWidth(10);
            canvas.drawRect(health, myPaint);
            //canvas.drawText("HP: " + this.getHitPoints(), this.xValue, this.yValue - 20, textPaint);
        }
    }

    /**
     * Sets the sprite associated with this {@link PlayedCard} to the given card
     * @param sprite the sprite to set
     */
    public void setSprite(GameObjectSprite sprite){
        this.sprite = sprite;
    }

    /**
     * Sets this card's 'attacking' value to the provided boolean
     * @param attacking the value to set 'attacking' to
     */
    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    /**
     *
     * @return the value of the 'attacking' boolean
     */
    public boolean getAttacking(){
        return this.attacking;
    }

    @Override
    public String toString() {
        return "PlayedCard{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", damage=" + damage +
                ", hitPoints=" + hitPoints +
                ", speed=" + speed +
                ", type='" + type + '\'' +
                ", range=" + range +
                ", xValue=" + xValue +
                ", yValue=" + yValue +
                ", player='" + player + '\'' +
                '}';
    }
}