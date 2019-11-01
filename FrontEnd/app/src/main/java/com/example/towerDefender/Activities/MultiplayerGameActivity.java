package com.example.towerDefender.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.towerDefender.Card.Card;
import com.example.towerDefender.SocketServices.Message;
import com.example.towerDefender.SocketServices.SocketListener;
import com.example.towerDefender.SocketServices.SocketUtilities;
import com.example.towerDefender.VolleyServices.CardRestServices;
import com.example.towerDefender.Game.GameView;
import com.example.towerDefender.VolleyServices.JsonUtils;
import com.example.towerDefender.Game.Player;
import com.example.towerDefender.VolleyServices.VolleyResponseListener;
import com.example.towerDefender.VolleyServices.VolleyUtilities;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.Random;

import javax.websocket.OnMessage;

public class MultiplayerGameActivity extends AppCompatActivity {

    private String lastSocketMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        VolleyUtilities.getRequest(this.getApplicationContext(), CardRestServices.BASE_URL, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("ERROR", "Encountered an error while grabbing cards from database. " + message);
            }

            @Override
            public void onResponse(Object response) {
                startGame(new ArrayList<>(JsonUtils.jsonToCardArray(response.toString())));
            }
        });
    }
    public void startGame(ArrayList<Card> cards){
        final Context ctx = this.getApplicationContext();
        final ArrayList<Card> passed = cards;
        final GameView gameView = new GameView(ctx, new Player(ctx, passed));
        SocketUtilities.connect(this.getApplicationContext(), "ws://coms-309-ss-5.misc.iastate.edu:8080/socket/%s", new SocketListener() {
            @OnMessage
            @Override
            public void onMessage(String message) {
//                if (lastSocketMessage == null || !lastSocketMessage.equals(message)){
                    gameView.getManager().passMessageToManager(message);
                    Log.i("GAME_MESSAGE", message);
//                    lastSocketMessage = message;
//                }
            }


            @Override
            public void onOpen(ServerHandshake handshake) {
                try {
                    MultiplayerGameActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(gameView);
                        }
                    });
                }
                catch(Exception e){e.printStackTrace();}
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
