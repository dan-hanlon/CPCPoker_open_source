package impl.poker;

import twitter4j.TwitterException;

import java.util.ArrayList;

abstract class HumanInterface {
    private static final int DECK_SIZE = 52;
    static final String[] discardYesNoStrings = {"yes", "y", "no", "n"};
    private static final String[] cardNamesFormat1 ={  "Ace of Hearts", "Two of Hearts", "Three of Hearts", "Four of Hearts", "Five of Hearts", "Six of Hearts", "Seven of Hearts", "Eight of Hearts",
            "Nine of Hearts", "Ten of Hearts", "J of Hearts", "Q of Hearts", "K of Hearts",
            "Ace of Diamonds", "Two of Diamonds", "Three of Diamonds", "Four of Diamonds", "Five of Diamonds", "Six of Diamonds", "Seven of Diamonds", "Eight of Diamonds",
            "Nine of Diamonds", "Ten of Diamonds", "J of Diamonds", "Q of Diamonds", "K of Diamonds",
            "Ace of Spades", "Two of Spades", "Three of Spades", "Four of Spades", "Five of Spades", "Six of Spades", "Seven of Spades", "Eight of Spades",
            "Nine of Spades", "Ten of Spades", "J of Spades", "Q of Spades", "K of Spades",
            "Ace of Clubs", "Two of Clubs", "Three of Clubs", "Four of Clubs", "Five of Clubs", "Six of Clubs", "Seven of Clubs", "Eight of Clubs",
            "Nine of Clubs", "Ten of Clubs", "J of Clubs", "Q of Clubs", "K of Clubs"
    };
    private static final String[] cardNamesFormat2 = {
            "Ace Hearts", "2 Hearts", "3 Hearts", "4 Hearts", "5 Hearts", "6 Hearts", "7 Hearts", "8 Hearts", "9 Hearts", "10 Hearts",
            "Jack Hearts", "Queen Hearts", "King Hearts",
            "Ace Diamonds", "2 Diamonds", "3 Diamonds", "4 Diamonds", "5 Diamonds", "6 Diamonds", "7 Diamonds", "8 Diamonds", "9 Diamonds", "10 Diamonds",
            "Jack Diamonds", "Queen Diamonds", "King Diamonds",
            "Ace Spades", "2 Spades", "3 Spades", "4 Spades", "5 Spades", "6 Spades", "7 Spades", "8 Spades", "9 Spades", "10 Spades",
            "Jack Spades", "Queen Spades", "King Spades",
            "Ace Clubs", "2 Clubs", "3 Clubs", "4 Clubs", "5 Clubs", "6 Clubs", "7 Clubs", "8 Clubs", "9 Clubs", "10 Clubs",
            "Jack Clubs", "Queen Clubs", "King Clubs"
    };
    private static final String[] cardNamesFormat3 ={"Ace of Hearts", "2 of Hearts", "3 of Hearts", "4 of Hearts", "5 of Hearts", "6 of Hearts", "7 of Hearts", "8 of Hearts",
            "9 of Hearts", "10 of Hearts", "Jack of Hearts", "Queen of Hearts", "King of Hearts",
            "Ace of Diamonds", "2 of Diamonds", "3 of Diamonds", "4 of Diamonds", "5 of Diamonds", "6 of Diamonds", "7 of Diamonds", "8 of Diamonds",
            "9 of Diamonds", "10 of Diamonds", "Jack of Diamonds", "Queen of Diamonds", "King of Diamonds",
            "Ace of Spades", "2 of Spades", "3 of Spades", "4 of Spades", "5 of Spades", "6 of Spades", "7 of Spades", "8 of Spades",
            "9 of Spades", "10 of Spades", "Jack of Spades", "Queen of Spades", "King of Spades",
            "Ace of Clubs", "2 of Clubs", "3 of Clubs", "4 of Hearts", "5 of Clubs", "6 of Clubs", "7 of Clubs", "8 of Clubs",
            "9 of Clubs", "10 of Clubs", "Jack of Clubs", "Queen of Clubs", "King of Clubs"
    };
    private static final String[] cardNamesFormat4 ={  "Ace Hearts", "Two Hearts", "Three Hearts", "Four Hearts", "Five Hearts", "Six Hearts", "Seven Hearts", "Eight Hearts",
            "Nine Hearts", "Ten Hearts", "J Hearts", "Q Hearts", "K Hearts",
            "Ace Diamonds", "Two Diamonds", "Three Diamonds", "Four Diamonds", "Five Diamonds", "Six Diamonds", "Seven Diamonds", "Eight Diamonds",
            "Nine Diamonds", "Ten Diamonds", "J Diamonds", "Q Diamonds", "K Diamonds",
            "Ace Spades", "Two Spades", "Three Spades", "Four Spades", "Five Spades", "Six Spades", "Seven Spades", "Eight Spades",
            "Nine Spades", "Ten Spades", "J Spades", "Q Spades", "K Spades",
            "Ace Clubs", "Two Clubs", "Three Clubs", "Four Clubs", "Five Clubs", "Six Clubs", "Seven Clubs", "Eight Clubs",
            "Nine Clubs", "Ten Clubs", "J Clubs", "Q Clubs", "K Clubs"
    };
    private static final String[] cardNamesFormat5 = {"ah", "2h", "3h", "4h", "5h", "6h", "7h", "8h", "9h", "10h", "Jh", "Qh", "Kh",
            "ad", "2d", "3d", "4d", "5d", "6d", "7d", "8d", "9d", "10d", "Jd", "Qd", "Kd",
            "as", "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s", "Js", "Qs", "Ks",
            "ac", "2c", "3c", "4c", "5c", "6c", "7c", "8c", "9c", "10c", "Jc", "Qc", "Kc"
    };


    int[] checkIfDiscardValidInput(String str, PokerPlayer player){
        int cardsToDiscard = 0;
        int intArray[] = {-1,-1,-1};

        for(int i = 0; i < DECK_SIZE; i++){ //Loops through each format checking for a recognised String.
            if(cardsToDiscard == 3){
                break;
            }
            if(str.toLowerCase().contains(cardNamesFormat1[i].toLowerCase()) || str.toLowerCase().contains(cardNamesFormat2[i].toLowerCase()) ||
                    str.toLowerCase().contains(cardNamesFormat3[i].toLowerCase()) || str.toLowerCase().contains(cardNamesFormat4[i].toLowerCase()) ||
                    str.toLowerCase().contains(cardNamesFormat5[i].toLowerCase())){
                //Once the card is recognised as valid, check if that card is present in the player's hand.
                for(int j = 0; j < player.getHand().size(); j++){
                    if(player.getHand().getHand().get(j).getFaceValue() == (i%13) +1){  // gets the face value of the card
                        //These if/elseif statements find the suit of the card.
                        if(player.getHand().getHand().get(j).getSuit() == 'H' && i/13 == 0){    // Hearts
                            intArray[cardsToDiscard] = j;
                            cardsToDiscard++;
                            break;
                        }
                        else if(player.getHand().getHand().get(j).getSuit() == 'D' && i/13 == 1){   //Diamonds
                            intArray[cardsToDiscard] = j;
                            cardsToDiscard++;
                            break;
                        }
                        else if(player.getHand().getHand().get(j).getSuit() == 'S' && i/13 == 2){   //Spades
                            intArray[cardsToDiscard] = j;
                            cardsToDiscard++;
                            break;
                        }
                        else if(player.getHand().getHand().get(j).getSuit() == 'C' && i/13 == 3){   //Clubs
                            intArray[cardsToDiscard] = j;
                            cardsToDiscard++;
                            break;
                        }
                        intArray[cardsToDiscard] = -1;
                    }
                }
            }
        }

        return intArray;
    }
    // TODO more methods necessary e.g. asking if player wants to check, all in or fold
    abstract void playerHand();

    abstract boolean willBuyIn() throws TwitterException, InterruptedException;

    abstract void insufficientBank();

    abstract void printPot(int pot);

    abstract int[] discardCards();

    abstract PokerPlayer setPlayer(PokerPlayer player);

    abstract char checkBetFold(int betAmount, int contributionToPot);

    abstract boolean insufficientBankAllIn();

    abstract void newCard(PlayingCard card);

    public abstract void foldedPlayer(PokerPlayer player);

    public abstract void buyIn(int pot, PokerPlayer bettingPlayer);

    public abstract void otherPlayerDiscard(int num, PokerPlayer discardPlayer);

    public abstract void winningPlayer(PokerPlayer winningPlayer, int pot, ArrayList<PokerPlayer> players);

    public abstract void noPlayersBoughtIn();

    public abstract void noPlayerCanOpen();

    public abstract void playerMove(int pot, PokerPlayer player, char playerChoice);

    public abstract void reset();

    public abstract boolean playAgain();

    public abstract void gameOver();
}


