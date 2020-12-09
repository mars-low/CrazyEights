import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rules taken from this source:
 * https://bicyclecards.com/how-to-play/crazy-eights/
 */

// mikolaj komentarz marek komentarz
public class GameModel {
    /**
     * All players.
     */
    public List<Player> players = new ArrayList<>();

    /**
     * PLayer selected at random
     * who starts the game.
     */
    public Player dealer;

    /**
     * Player that has turn.
     */
    public Player turnPlayer;

    /**
     * Cards put on the table face down.
     */
    public List<Card> stock = new ArrayList<>();

    /**
     * Cards put on the table face up.
     * First card is starter and is
     * turned up after everyone
     * took his cards. Starter cannot be eight.
     */
    public List<Card> pile = new ArrayList<>();

    /**
     * Actually played suit.
     * Usually the same as top card on the pile
     * but changes when crazy eight is played
     */
    private Suit suit;

    /**
     * Prepare 52-card deck to put on table.
     * Shuffle cards at random.
     */
    public void prepareCardDeck(){
        for (Suit suit: Suit.values()) {
            for (Denomination denomination: Denomination.values()) {
                stock.add(new Card(suit, denomination));
            }
        }

        Collections.shuffle(stock);
    }

    /**
     * Invite players to game.
     * @param playersGroup custom number of players
     */
    public void invitePlayers(Player... playersGroup){
        for (Player player: playersGroup ) {
            if(players.size() >= 7)
                break;

            players.add(player);
        }
    }

    /**
     * Dealer starts the game.
     */
    public void drawDealer(){
        dealer = players.get(ThreadLocalRandom.current().nextInt(0,players.size()));
    }

    /**
     * Every player deals 8 cards from stock.
     */
    public void beginTheDeal(){
        for (Player player: players){
            for (int i = 0; i < 8; i++) {
                player.dealCard(stock.remove(stock.size()-1));
            }
        }
    }

    /**
     * Puts card from stock top as starter card.
     * Starter card cannot be eight.
     */
    public void putStarterOnPile(){
        while (stock.get(stock.size()-1).denomination == Denomination.EIGHT){
            // Put eight somewhere in the pile, but not near to the top
            stock.add(ThreadLocalRandom.current().nextInt(0,stock.size()-5),
                    stock.remove(stock.size()-1));
        }

        pile.add(stock.remove(stock.size()-1));
        suit = pile.get(0).suit;
    }

    /**
     * Deal card from stock.
     * Only player that have turn
     * can make this action.
     */
    public int dealCard(){
        if(stock.size()>0){
        turnPlayer.dealCard(stock.remove(stock.size()-1));
        return 0;}
        else return 1;
    }

    /**
     * Play cards and put it on the pile.
     * Only player that have turn
     * can make this action.
     * @return true if player played any card
     */
    public boolean playCards(){
        if (turnPlayer.getSelectedCards().isEmpty()){
            return false;
        }
        else if (turnPlayer.getSelectedCards().get(0).denomination == Denomination.EIGHT){
            // Crazy eight is here!!! Player can select any suit!
            turnPlayer.getSelectedCards().forEach(card -> pile.add(turnPlayer.putCardOnPile(card)));
            suit = turnPlayer.getSelectedSuit();
            return true;
        }
        else if (turnPlayer.getSelectedCards().get(0).denomination == pile.get(pile.size()-1).denomination
                || turnPlayer.getSelectedCards().get(0).suit == suit){
            // Player can play many cards with the same denomination at once
            // Player can play card with the same suit as card on the pile's top
            turnPlayer.getSelectedCards().forEach(card -> pile.add(turnPlayer.putCardOnPile(card)));
            suit = pile.get(pile.size()-1).suit;
            return true;
        }

        return false;
    }

    /**
     * The player who is the first
     * to have no cards left wins the game.
     * Check if current player is winner.
     * Usually called after playCards method
     * returns true.
     */
    public boolean turnPlayerIsWinner(){
        return turnPlayer.getCardsNumber() == 0;
    }

    /**
     * Control which player has turn.
     * Usually called after turnPlayerIsWinner
     * returns false
     */
    public void nextPlayerTurn(){
        if(players.indexOf(turnPlayer) == players.size()-1){
            // Set current player to first in the players list
            turnPlayer = players.get(0);
        } else {
            // Set current player to next in the players list
            turnPlayer = players.get(players.indexOf(turnPlayer)+1);
        }
    }

    /**
     * The winning player collects
     * from each other player
     * the value of the cards
     * remaining in that player's
     * hand as follows:
     * Each eight = 50 points
     * Each K,Q,J or 10 = 10 points
     * Each ace = 1 point
     * @return points scored by winner
     */
    public int winnerPoints(){
        AtomicInteger points = new AtomicInteger();

        players.forEach(player -> player.getAllCards().forEach(card -> {
            switch (card.denomination){
                case ACE -> points.addAndGet(1);
                case TEN, JACK, QUEEN, KING -> points.addAndGet(10);
                case EIGHT -> points.addAndGet(50);
            }
        }));

        return points.get();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getDealer() {
        return dealer;
    }

    public Player getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(Player turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    public List<Card> getStock() {
        return stock;
    }

    public List<Card> getPile() {
        return pile;
    }

    public Suit getSuit() {
        return suit;
    }

}