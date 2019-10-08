package com.example.towerDefender.Game;

import android.graphics.Canvas;

import com.example.towerDefender.Card.Card;
import com.example.towerDefender.Card.CardInHand;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameManager handles all the {@link Player}s and {@link Character}s for the {@link GameView} to streamline code.
 */
public class GameManager {
    private GameView gameView;
    private Player player;
    private List<Character> characters;
    private boolean isPlayingCard;
    //The index of the CardInHand to play from the player's CardInHand
    private int cardToPlayIndex;

    public GameManager(GameView gameView, Player player){
        this.gameView = gameView;
        this.player = player;
        characters = new ArrayList<>();
        isPlayingCard = false;
        cardToPlayIndex = 0;
       initializeDeck();
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
     * Draws the {@link Character}s and {@link CardInHand}s on the provided canvas
     * @param canvas the canvas to draw on
     */
    public void draw(Canvas canvas){
        for(Character sprite : characters){
            sprite.draw(canvas);
        }
        for(CardInHand card : player.getHand()){
            card.draw(canvas);
        }
    }

    /**
     * Adds a {@link Character} to manage
     * @param character the {@link Character} to add
     */
    public void addCharacter(Character character){
        characters.add(character);
    }

    /**
     * Updates the {@link Character}s and {@link CardInHand}s.
     */
    public void update(){
        for(Character character : characters){
            character.update();
        }
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
     * Plays the {@link Card} represented by cardToPlayIndex.
     * @param eventX the X value of the event causing the card to be played
     * @param eventY the Y value of the event playing this card
     */
    public void playCard(int eventX, int eventY){
        //if(player.hand[cardToPlayIndex].getCard().getCardType() == Card.CardType.UNIT){
            this.addCharacter(new Character(player.getCardInHand(cardToPlayIndex).getSprite().image, eventX, eventY));
        //}
        player.getCardInHand(cardToPlayIndex).setStatus(CardInHand.Status.NOT_READY);
        //for debugging
        //player.hand[cardToPlayIndex].card.cardName = "hello from front end";
        //CardRestServices.sendCardToDB(this.gameView.getContext(), player.hand[cardToPlayIndex].card);
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

}