package com.example.towerDefender.Game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.towerDefender.Card.Card;
import com.example.towerDefender.Card.CardInHand;
import com.example.towerDefender.SocketServices.SocketUtilities;
import com.example.towerDefender.VolleyServices.JsonUtils;
//import com.example.towerDefender.SocketServices.WebSocketClientConnection;

import java.util.ArrayList;
import java.util.Collection;

import com.example.towerDefender.Card.PlayedCard;

/**
 * The GameManager handles all the {@link Player}s and {@link GameObjectSprite}s for the {@link GameView} to streamline code.
 */
public class GameManager {
    private String playerSide;
    private Player player;
    private boolean isConnected = false;
    private PlayedCardsHolder playedCards;
    //whether or not a card in the player's hand currently has status CardInHand.Status.PLACING
    private boolean isPlayingCard;
    //The index of the CardInHand to play from the player's CardInHand
    private int cardToPlayIndex;
    private long lastUpdate;
    private int cardsSent = 0;
    private boolean gameOver = false;
    private Paint textPaint;

    public GameManager(GameView gameView, Player player){
        this.player = player;
        playedCards = new PlayedCardsHolder(new ArrayList<PlayedCard>(), this.player);
        isPlayingCard = false;
        cardToPlayIndex = 0;
        initializeDeck();
        SocketUtilities.sendMessage("Hello from " + this.player.getUserId());
        lastUpdate = System.currentTimeMillis();
        textPaint = new Paint(Color.BLACK);
        textPaint.setTextSize(250);
        playerSide = "left";
    }

    //TODO: these pulls should be randomized, pulled from the server
    /**
     * Initializes the {@link Player}'s deck.
     */
    public void initializeDeck(){
        player.drawHand();
    }

    public Player getPlayer(){
        return player;
    }

    /**
     * Draws the {@link GameObjectSprite}s and {@link CardInHand}s on the provided canvas
     * @param canvas the canvas to drawLeftFacing on
     */
    public void draw(Canvas canvas){
        if(!gameOver){
            for(PlayedCard playedCard : playedCards.getPlayedCards()){
                if(playedCard.getPlayer().equals(this.getPlayer().getUserId()) && playerSide != null && playerSide.equals("left")){
                    playedCard.drawLeftFacing(canvas);
                } else{
                    playedCard.drawRightFacing(canvas);
                }
            }
            for(CardInHand card : player.getHand()){
                card.draw(canvas);
            }
            player.draw(canvas);
        } else{
            canvas.drawText("GAME OVER", 0, Sprite.screenHeight / 2, textPaint);
        }

    }

    /**
     * Updates the {@link GameObjectSprite}s and {@link CardInHand}s.
     */
    public void update(){
        player.update();
        for(CardInHand card :  getPlayer().getHand()){
            card.update();
        }
    }

    /**
     * Updates the card that is about to played (has been clicked on but not deployed)
     * @param index the index of this {@link GameManager}'s {@link Player}'s {@link CardInHand} to play
     * @param currentlyPlaying whether or not there is a {@link Card} in the process of being played
     */
    public void setPlayingCard(int index, boolean currentlyPlaying){
        this.cardToPlayIndex = index;
        if(index != -1){
            this.player.getCardInHand(cardToPlayIndex).setStatus(CardInHand.Status.PLACING);
        }
        this.isPlayingCard = currentlyPlaying;
    }

    /**
     * @return the {@link CardInHand} the manager is set to play
     */
    public CardInHand getPlayingCard(){
        return this.player.getCardInHand(cardToPlayIndex);
    }

    /**
     * Plays the {@link Card} represented by cardToPlayIndex.
     * @param eventX the X value of the event causing the card to be played
     * @param eventY the Y value of the event playing this card
     */
    public void playCard(int eventX, int eventY){
        try {
            Card toSend = new Card(player.getCardInHand(cardToPlayIndex).getCard());
            toSend.cardName = toSend.cardName + "@" + cardsSent++;
            SocketUtilities.sendMessage(JsonUtils.playedCardToJson(new PlayedCard(toSend, eventX, eventY, this.player.getUserId())).toString()  );
            player.setCurrentMana(player.getCurrentMana() - player.getCardInHand(cardToPlayIndex).getCardManaCost());
            player.getCardInHand(cardToPlayIndex).setStatus(CardInHand.Status.PLAYED);
        } catch (Exception e){
            Log.e("ERROR", "Encountered an error sending card over socket.");
        }

    }

    /**
     * @return true if a card is currently set to {@link CardInHand.Status} = Status.PLACING
     */
    public boolean isPlayingCard(){
        return isPlayingCard;
    }

    /**
     * @return the index of the {@link CardInHand} from the {@link Player}'s hand to play
     */
    public int getCardToPlayIndex(){
        return cardToPlayIndex;
    }

    /**
     * Sends a message to the game manager
     * @param message the message to send to the game manager
     */
    public void passMessageToManager(String message){
        //have we gone 5 seconds without server communication?
        if(System.currentTimeMillis() - lastUpdate >= 5000){
            this.gameOver = true;
        }
        if(message.contains("true")){
            Log.i("SOCKET_INFO", "Connected.");
            isConnected = true;
            if(message.contains("left")){
                playerSide = "left";
            } else if(message.contains("right")){
                playerSide = "right";
            }
        } else if(message.contains("win") || message.contains("loss")){
            Log.i("SOCKET_INFO", "GAME OVER: " + message);
            this.gameOver = true;
        } else{
            try {
                //If message is not formatted yet it will be an array and contain "PlayedCard"
                if(message.contains("PlayedCard")){
                    Collection<PlayedCard> playedCards = JsonUtils.socketCardsToPlayedCards(message);
                    for(PlayedCard playedCard : playedCards){
                        this.playedCards.addOrUpdate(playedCard, this);
                    }
                    lastUpdate = System.currentTimeMillis();
                } else if(message.contains("name")){
                    playedCards.addAll(JsonUtils.jsonToPlayedCardArray(message), this);
                    lastUpdate = System.currentTimeMillis();
                }
            } catch (Exception e){
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    /**
     * @return this game manager's player's side (left or right)
     */
    public String getPlayerSide(){
        return playerSide;
    }

    /**
     * @return true if the socket has sent over a message confirming the connection
     */
    public boolean isConnected(){
        return isConnected;
    }

    /**
     * @return the {@link PlayedCardsHolder} containing all the played cards
     */
    public PlayedCardsHolder getPlayedCards(){
        return playedCards;
    }

    /**
     *
     * @return true if the game is over
     */
    public boolean isGameOver(){
        return gameOver;
    }
}
