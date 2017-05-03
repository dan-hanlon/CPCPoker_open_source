package interfaces.poker;

import impl.poker.PlayingCard;

public interface HandOfCards {

    int size();

    String toString();

    void addCard(PlayingCard card);

    int getRank();

    int removeCardsAtPositions(int[] array);

}
