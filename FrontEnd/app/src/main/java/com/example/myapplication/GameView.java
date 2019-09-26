package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public GameView instance;
    private MainThread mainThread;
    private BackButton backButton;
    Paint paint;
    public GameManager manager;
    private Player player;

    public GameView(Context context, Player player){
        super(context);
        instance = this;
        this.player = player;
        getHolder().addCallback(this);
        mainThread = new MainThread(getHolder(), this);
        setFocusable(true);
        paint = new Paint(Color.LTGRAY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        mainThread.setRunning(true);
        mainThread.start();
        manager = new GameManager(this, player);
        manager.getPlayer().drawHand();
        backButton = new BackButton(BitmapFactory.decodeResource(getResources(),R.drawable.back_button));
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry){
            try {
                mainThread.setRunning(false);
                mainThread.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        if(canvas != null){
            canvas.drawColor(Color.BLUE);
            canvas.drawRect(new Rect(150, Resources.getSystem().getDisplayMetrics().heightPixels - 250, Resources.getSystem().getDisplayMetrics().widthPixels - 150, Resources.getSystem().getDisplayMetrics().heightPixels), paint);
            //Manager will draw the characters and hand
            manager.draw(canvas);
            backButton.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(manager.isPlayingCard){
                if(manager.cardToPlay != -1){
                    manager.playCard((int)event.getX(), (int)event.getY());
                    manager.setPlayingCard(-1, false);
                }
            } else {
                if(event.getX() <= backButton.getxEnd() &&
                    event.getX() >= backButton.getxStart() &&
                    event.getY() <= backButton.getyEnd() &&
                    event.getY() >= backButton.getyStart()){
                    this.backToNavigation(this);
                }
                //Is the event within the bounds of one of our CardInHand objects?
                for(int i = 0; i < 4; i++){
                    if (event.getX() <= manager.getPlayer().hand[i].card.getSprite().getxEnd() &&
                            event.getX() >= manager.getPlayer().hand[i].card.getSprite().getxStart() &&
                            event.getY() <= manager.getPlayer().hand[i].card.getSprite().getyEnd() &&
                            event.getY() >= manager.getPlayer().hand[i].card.getSprite().getyStart() &&
                            manager.getPlayer().hand[i].paint.getColor() == Color.GREEN) {
                        manager.setPlayingCard(i, true);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void update(){
        manager.update();
    }

    public void backToNavigation(View view){
        Intent intent = new Intent(this.getContext(), NavigationActivity.class);
        this.getContext().startActivity(intent);
    }


}
