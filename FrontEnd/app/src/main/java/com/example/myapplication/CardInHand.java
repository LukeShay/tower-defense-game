package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import static com.example.myapplication.Sprite.normalizedInventorySize;

public class CardInHand {

    public enum Status {
        READY, PLACING, NOT_READY
    }
    //Used to create the inventory box behind our Card's sprite
    private Rect background;
    public Paint paint;
    public int color;
    public Status status;
    public Card card;
    //The time (in seconds) it will take this inventory item to recharge
    public static int loadTime = 3;
    //The time that this card started recharging
    public long startRechargeTime;

    public CardInHand(Card card, int itemNumber){
        this.card = card;
        this.card.sprite.image = Bitmap.createScaledBitmap(card.sprite.image, normalizedInventorySize, normalizedInventorySize, false);
        this.card.sprite.xStart = 150 + itemNumber * normalizedInventorySize;
        this.card.sprite.xEnd = this.card.sprite.xStart + normalizedInventorySize;
        this.card.sprite.yStart = Resources.getSystem().getDisplayMetrics().heightPixels - 250;
        this.card.sprite.yEnd = this.card.sprite.yStart + this.card.sprite.image.getHeight();
        background = new Rect(this.card.sprite.xStart, this.card.sprite.yStart, this.card.sprite.xStart + this.card.sprite.image.getWidth(), this.card.sprite.yStart + this.card.sprite.image.getHeight());
        color = Color.GREEN;
        paint = new Paint(color);
    }

    public void update(){
        if(this.status == Status.NOT_READY){
            if(System.currentTimeMillis() - startRechargeTime >= loadTime * 1000 ){
                this.setStatus(Status.READY);
            }
        }
    }

    public void setStatus(Status status){
        this.status = status;
        switch(status){
            case READY:
                this.color = Color.GREEN;
                break;
            case PLACING:
                this.color = Color.CYAN;
                break;
            case NOT_READY:
                this.color = Color.RED;
                this.startRechargeTime = System.currentTimeMillis();
                break;
        }
    }

    public void draw(Canvas canvas) {
        paint.setColor(color);
        canvas.drawRect(background, paint);
        card.draw(canvas);
    }

}
