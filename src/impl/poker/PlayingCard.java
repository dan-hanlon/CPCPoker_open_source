package impl.poker;

public class PlayingCard implements interfaces.poker.PlayingCard {

    // Attributes of a playing card
    private String face;    // What appears on the face of the card
    private char suit;      // The suit of the card
    private int faceValue;  // The face value of a card
    private int gameValue;  // The ranking of a card in the game

    // Constants defining the four suits of cards
    public static final char HEARTS = 'H';
    public static final char DIAMONDS = 'D';
    public static final char SPADES = 'S';
    public static final char CLUBS = 'C';
    public static final int ACE_GAME_VAL = 14;
    
    /* PlayingCard constructor
       Args: String cardFace: What appears on the face of the card
             char cardSuit: Suit of the card
             int cardFaceValue: Face value of the card, the value of the card as it would appear on the face of the card
             int cardGameValue: Value of the card rank in terms of the game, e.g. Ace is the highest-rank card in the
                                game, therefore it's Game Value is the highest
    */
    public PlayingCard( String cardFace, char cardSuit, int cardFaceValue, int cardGameValue){
        face = cardFace;
        suit = cardSuit;
        faceValue = cardFaceValue;
        gameValue = cardGameValue;
    }


    @Override
    public String getFace(){    return this.face;   }   //  Returns what appears on the face of the card

    @Override
    public char getSuit(){  return this.suit;   }   //  Returns the suit of the card

    @Override
    public int getFaceValue(){  return this.faceValue;  }   //  Returns the face value of the card

    @Override
    public int getGameValue(){  return this.gameValue;  }   // Returns the game value of the card

    @Override
    public String toString(){ return this.face + this.suit; } // Returns string representing specific card
}
