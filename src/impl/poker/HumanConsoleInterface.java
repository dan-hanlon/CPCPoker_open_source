package impl.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HumanConsoleInterface extends HumanInterface {

    private PokerPlayer player;

    public HumanConsoleInterface(PokerPlayer humanPlayer) {
        player = humanPlayer;
    }

    public HumanConsoleInterface() {}

    @Override
    boolean insufficientBankAllIn() {
        System.out.println("To continue you must All In. Do you want to all in? (Y/N)");

        return checkYesNoInput();
    }

    @Override
    public void otherPlayerDiscard(int num, PokerPlayer discardPlayer) {
        System.out.println( discardPlayer.getPlayerName() + " has discarded " + num + " cards." );
    }

    @Override
    public void buyIn(int pot, PokerPlayer bettingPlayer) {
        System.out.println( bettingPlayer.getPlayerName() + " has bought in. Pot is now: " + pot );
    }

    @Override
    public void foldedPlayer(PokerPlayer foldedPlayer) {
        System.out.println( foldedPlayer.getPlayerName() + " has folded." );
    }

    @Override
    void newCard(PlayingCard card) {
        System.out.println(player.getPlayerName() + ", you have been dealt: " + card);
    }

    @Override
    char checkBetFold(int currentBet, int contributionToPot) {
        int checkAmount = currentBet - contributionToPot;
        int betAmount = (currentBet - contributionToPot) + 1;

        System.out.println( player.getPlayerName() + ", do you want to Bet, Check, Fold or All In? (B/C/F/A)");
        System.out.println("Checking costs " + checkAmount + " credit(s). Betting costs "+ betAmount + " credits. You have " + player.getBank() + " credits in the bank.");


        String str;
        boolean validInput = false;

        while (!validInput) {
            str = getUserInput().trim();

            if(str.length() == 1) {
                if( str.equalsIgnoreCase("B") ) return PokerPlayer.BET;
                else if (str.equalsIgnoreCase("F") ) return PokerPlayer.FOLD;
                else if (str.equalsIgnoreCase("C")) return PokerPlayer.CHECK;
                else if (str.equalsIgnoreCase("A")) return PokerPlayer.ALL_IN;
            }

            System.out.println("Invalid input. Please enter B, C, F or A (bet, check, fold, all in): ");
        }

        return 0;
    }

    @Override
    public PokerPlayer setPlayer(PokerPlayer humanPlayer){
        player = humanPlayer;
        return player;
    }

    @Override
    void playerHand(){
        System.out.println(player.getPlayerName()+ ", your hand is: " + player.getHand() + " " + player.getHand().getRank());
    }

    @Override
    public void noPlayerCanOpen() {
        System.out.println(player.getPlayerName() + ", no players were able to open. Round over.");
    }

    @Override
    public void noPlayersBoughtIn() {
        System.out.println(player.getPlayerName() + ", no players bought in. Round over.");
    }

    @Override
    public void winningPlayer(PokerPlayer winningPlayer, int pot, ArrayList<PokerPlayer> players) {
        System.out.println(player.getPlayerName() + ", " + winningPlayer.getPlayerName() + " has won the pot of " + pot);

        for(PokerPlayer p : players){
            System.out.println( player.getPlayerName() + ", " + p.getPlayerName() + " had the hand: " + p.getHand() );
        }
    }

    @Override
    boolean willBuyIn(){

        System.out.println(player.getPlayerName() + ", do you want to buy in? Your current bank balance is " +
                player.getBank() + " (Y/N) : ");

        return checkYesNoInput();
    }

    @Override
    void insufficientBank(){
        System.out.println(player.getPlayerName() + ", Insufficient funds to perform action.");
    }

    @Override
    void printPot(int pot){
        System.out.println(player.getPlayerName() + ", the pot is: " + pot );
    }


    @Override
    int[] discardCards(){
        /*
            Originally we asked for indexes of cards, this method converts user inputted names back to indexes so we can discard them.
            First Ask the player if they wish to discard, then what cards we want them to discard.
         */
        String str;
        boolean validInput = false;
        int intArray[] = {-1,-1,-1};
        System.out.println( player.getPlayerName() + ", your hand is " + player.getHand() );
        System.out.println("Would you like to discard? Yes or No?");
        // Ask if the user wishes to discard cards or not
        while(!validInput){
            str = getUserInput().trim();
            for(int i = 0; i < discardYesNoStrings.length; i++){
                if(discardYesNoStrings[i].equals(str.toLowerCase()) && i+1 <= discardYesNoStrings.length/2){
                    validInput = true;
                    break;
                }
                else if(discardYesNoStrings[i].equals(str.toLowerCase()) && i+1 > discardYesNoStrings.length/2){
                    return intArray;
                }
                else {
                    continue;
                }
            }
            if(validInput == true){
                break;
            }
            System.out.println("Please Enter a valid input, Y or Yes for yes, N or No for no.\nYour hand is " + player.getHand());

        }
        System.out.println("What cards would you like to discard? Enter the names of the cards you would like to discard");
        validInput = false;

        while (!validInput) {
            str = getUserInput().trim();

            intArray = checkIfDiscardValidInput(str, player);

            // If the intArray is all -1's that means that the input was invalid, and the player is asked to discard again.
            for(int i: intArray){
                if (i != -1){
                    validInput = true;
                    break;
                }
            }

            if (!validInput){
                System.out.println("Not valid input. Please enter the name(s) of the cards you wish to discard.\nYour hand is " + player.getHand());
            }
        }

        Arrays.sort(intArray);
        System.out.println(Arrays.toString(intArray));
        return intArray;
    }

    private String getUserInput(){
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        String str = reader.nextLine();
        return str;
    }

    @Override
    public void gameOver() {
        System.out.println("Your game is now over!");
    }

    @Override
    public boolean playAgain() {
        System.out.println( player.getPlayerName() + ", Do you want to play again? (Y/N)");
        return checkYesNoInput();
    }

    private boolean checkYesNoInput(){
        boolean validInput = false;

        String str = null;

        while (!validInput) {
            str = getUserInput();
            if(str.equalsIgnoreCase("y") || str.equalsIgnoreCase("n"))
                validInput = true;
            else
                System.out.println("Invalid input. Please enter Y or N: ");
        }

        return str.equalsIgnoreCase("y");
    }

    @Override
    public void playerMove(int pot, PokerPlayer p, char playerChoice) {
        System.out.println(player.getPlayerName() + ", " + p.getPlayerName() + " has " + playerChoice + ".");
        System.out.println("Pot is now: " + pot + ".");
    }

    @Override
    public void reset(){

    }
}
