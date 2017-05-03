package interfaces.poker;



public interface DeckOfCards {

    void reset();

    void shuffle();

    PlayingCard dealNext();

    void returnCard(impl.poker.PlayingCard card);

    int size();
}
