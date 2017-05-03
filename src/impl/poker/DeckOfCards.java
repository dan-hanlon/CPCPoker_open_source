package impl.poker;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class DeckOfCards implements interfaces.poker.DeckOfCards {

    static final int SIZE_OF_FULL_DECK = 52;

    private final int NUMBER_OF_SUITS = 4;
    private final int NUMBER_OF_CARDS_PER_SUIT = 13;
    private ArrayList<PlayingCard> deck;
    private int cardsReturned = 0;

    DeckOfCards(){
        deck =  new ArrayList<PlayingCard>();
        reset();
        shuffle();
    }

    @Override
    public synchronized void reset() {
        deck.clear();

        char[] suitsArray = { PlayingCard.CLUBS, PlayingCard.HEARTS, PlayingCard.DIAMONDS, PlayingCard.SPADES };
        String[] faceArray = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};

        for(int i = 0; i < NUMBER_OF_SUITS; i++){
            for(int j = 0; j < NUMBER_OF_CARDS_PER_SUIT; j++){
                // Add new card to the deck
                deck.add(new PlayingCard(faceArray[j], suitsArray[i], j+1, (j == 0) ? 14 : j+1) );
            }
        }
    }

    @Override
    public synchronized void shuffle() {
        Random rand = new Random();

        for(int i = 0; i < Math.pow(deck.size(), 2); i++){
            int pos1 = rand.nextInt(deck.size());
            int pos2 = rand.nextInt(deck.size());

            Collections.swap(deck, pos1, pos2);
        }

        cardsReturned = 0;
    }

    @Override
    public synchronized PlayingCard dealNext() {
        if (deck.size() == cardsReturned)
            return null;
        else
            return deck.remove(deck.size() - 1);
    }

    @Override
    public synchronized void returnCard(PlayingCard discarded) {
        if (discarded != null) {
            cardsReturned++;
            deck.add(0, discarded );
        }
    }

    @Override
    public int size() {
        return deck.size();
    }
}
