package com.example.towerDefender.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.towerDefender.Card.CardUtilities;
import com.example.towerDefender.Card.Deck;
import com.example.towerDefender.Card.OwnedDecks;
import com.example.towerDefender.R;
import com.example.towerDefender.VolleyServices.JsonUtils;
import com.example.towerDefender.VolleyServices.VolleyResponseListener;
import com.example.towerDefender.VolleyServices.VolleyUtilities;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity {
    Toast featureUnderProductionToast;
    private PopupWindow mPopupWindow;
    private Button deck1;
    private Button deck2;
    private Button deck3;
    private ArrayList<OwnedDecks> decks;
    public static OwnedDecks selectedDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.towerDefender.R.layout.activity_navigation);

        featureUnderProductionToast = Toast.makeText(this.getApplicationContext(), "Sorry, feature still under production.", Toast.LENGTH_SHORT);
    }

    public void launchSinglePlayer(View view){
        //TODO: Open up a practice game
        featureUnderProductionToast.show();
    }

    public void launchMultiplayer(final View view){
        VolleyUtilities.getRequest(getApplicationContext(), "http://coms-309-ss-5.misc.iastate.edu:8080/users/test1/deck", new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                Log.e("deck", response.toString());
                decks = new ArrayList<>(JsonUtils.jsonToOwnedDecksArray(response.toString()));

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.deck_selection_view, null);
                mPopupWindow = new PopupWindow(
                        customView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                deck1 = customView.findViewById(R.id.select_deck_1);
                deck2 = customView.findViewById(R.id.select_deck_2);
                deck3 = customView.findViewById(R.id.select_deck_3);
                deck1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopupWindow.dismiss();
                        selectedDeck = decks.get(0);
                        Intent intent = new Intent(getApplicationContext(), MultiplayerGameActivity.class);
                        startActivity(intent);
                    }
                });
                deck2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopupWindow.dismiss();
                        selectedDeck = decks.get(1);
                        Intent intent = new Intent(getApplicationContext(), MultiplayerGameActivity.class);
                        startActivity(intent);
                    }
                });
                deck3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopupWindow.dismiss();
                        selectedDeck = decks.get(2);
                        Intent intent = new Intent(getApplicationContext(), MultiplayerGameActivity.class);
                        startActivity(intent);
                    }
                });
                mPopupWindow.showAtLocation(view.getRootView(), Gravity.CENTER, 0, 0);
            }});
    }

    public void openInventory(View view){
        Intent intent = new Intent(this, inventoryActivity.class);
        startActivity(intent);
    }

    public void openSettings(View view){
        //TODO: open up settings page
        featureUnderProductionToast.show();
    }
}
