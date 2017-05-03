package interfaces.poker;

public interface PlayingCard {

    String toString(); // Returns string representing specific card

    String getFace(); //  Returns what appears on the face of the card

    char getSuit();   //  Returns the suit of the card

    int getFaceValue();   //  Returns the face value of the card

    int getGameValue();   // Returns the game value of the card

}
