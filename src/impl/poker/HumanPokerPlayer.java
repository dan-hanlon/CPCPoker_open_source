package impl.poker;

import java.util.ArrayList;
import java.util.Stack;

public class HumanPokerPlayer extends PokerPlayer {

    private HandOfCards hand;
    private String playerName;
    private int bank;
    private HumanInterface communicationInterface;
    private boolean isHuman;

    public HumanPokerPlayer(String name, int initialBank, HumanInterface commInterface){
        playerName = name;
        hand = new HandOfCards();
        bank = initialBank;
        communicationInterface = commInterface;
        communicationInterface.setPlayer(this);
        isHuman = true;
    }


    @Override
    public void updateNoPlayersBoughtIn() {
        communicationInterface.noPlayersBoughtIn();
    }

    @Override
    public void getRoundInformation(HandOfPoker currentHand){}

    @Override
    public void updateWithWinner(PokerPlayer player, int pot, ArrayList<PokerPlayer> players) {
        communicationInterface.winningPlayer(player, pot, players);
    }

    @Override
    public int getHandRank() {
        return hand.getRank();
    }

    @Override
    public boolean canOpen() {
        return hand.getRank() >= 1000000;
    }

    @Override
    public boolean isHuman() {
        return isHuman;
    }

    @Override
    public int giveCoins(int coins) {
        bank += coins;
        return bank;
    }

    @Override
    public void updateWithPlayerChoice(int pot, PokerPlayer player, char playerChoice) {
        communicationInterface.playerMove(pot, player, playerChoice);
    }

    @Override
    public char willPlayerBetCheckFold(int betAmount, int contributionToPot) {
        char input = communicationInterface.checkBetFold(betAmount, contributionToPot);

        // TODO keep asking player to enter a command that is valid (e.g) if they don't have enough to bet, ask if they
        // want to check or all in instead.
        if (input == PokerPlayer.BET){
           if ((bank -(betAmount - contributionToPot) +1) > (betAmount - contributionToPot) +1){
               bank -= (betAmount - contributionToPot) +1;
               return PokerPlayer.BET;
           } else if(communicationInterface.insufficientBankAllIn()){
               bank -= bank;
               return PokerPlayer.ALL_IN;
           } else if(bank < 2){
               if(communicationInterface.insufficientBankAllIn()){
                   bank -= bank;
               }
           }else
               return PokerPlayer.FOLD;
        }

        else if (input == PokerPlayer.CHECK){
            if ((bank -(betAmount - contributionToPot)) > 0){
                bank -= (betAmount - contributionToPot);
                return PokerPlayer.CHECK;
            } else if(communicationInterface.insufficientBankAllIn()){
                bank -= bank;
                return PokerPlayer.ALL_IN;
            } else
                return PokerPlayer.FOLD;
        }

        else if (input == PokerPlayer.ALL_IN){
            bank -= bank;
            return PokerPlayer.ALL_IN;
        }


        return PokerPlayer.FOLD;
    }

    @Override
    public boolean willPlayerBuyIn(int totalBetAmount, int contributionToPot) {
        try {
            if ( communicationInterface.willBuyIn() ) {
                if ( (bank - ( (totalBetAmount - contributionToPot) + 1 )) >= 0){
                    bank -= ((totalBetAmount - contributionToPot) + 1);
                    return true;
                } else
                    communicationInterface.insufficientBank();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void updatePotDetails(int pot) {
        communicationInterface.printPot(pot);
    }

    @Override
    public void gameOver() {
        communicationInterface.gameOver();
    }

    @Override
    public boolean playAgain() {
        return communicationInterface.playAgain();
    }

    @Override
    public int discard() {
        int intArray[] = communicationInterface.discardCards();

        return hand.removeCardsAtPositions(intArray);

    }

    @Override
    public HandOfCards getHand() {return hand; }

    @Override
    public void updatePlayerBuyIn(int pot, PokerPlayer bettingPlayer) {
        communicationInterface.buyIn(pot, bettingPlayer);
    }

    @Override
    public void updateWithFoldedPlayer(PokerPlayer player) {
        communicationInterface.foldedPlayer(player);
    }

    @Override
    public void updateWithDiscard(int num, PokerPlayer discardPlayer) {
        communicationInterface.otherPlayerDiscard(num, discardPlayer);
    }

    @Override
    public void reset() {
        hand.removeAll();
    }

    @Override
    public void updateNoPlayerCanOpen() {
        communicationInterface.noPlayerCanOpen();
    }

    @Override
    public void giveCard(PlayingCard card) {
        hand.addCard(card);
        //communicationInterface.newCard(card);
        if(hand.size() == 5)
            communicationInterface.playerHand();
    }

    public String getPlayerName(){
        return playerName;
    }


    @Override
    public int getBank() {
        return bank;
    }
}
