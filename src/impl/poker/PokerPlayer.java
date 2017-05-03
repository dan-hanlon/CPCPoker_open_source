package impl.poker;

import java.util.ArrayList;

public abstract class PokerPlayer{

    static final char BET = 'B';
    static final char CHECK = 'C';
    static final char FOLD = 'F';
    static final char ALL_IN = 'A';

    public abstract boolean canOpen();

    public abstract void getRoundInformation(HandOfPoker currentHand);

    public abstract boolean willPlayerBuyIn(int totalBetAmount, int contributionToPot);

    public abstract char willPlayerBetCheckFold(int betAmount, int contributionToPot);

    public abstract int discard();

    public abstract HandOfCards getHand();

    public abstract void giveCard(PlayingCard card);

    public abstract String getPlayerName();

    public abstract int getBank();

    public abstract void updatePotDetails( int pot);

    public abstract int giveCoins(int coins);

    public abstract int getHandRank();

    public abstract void updateWithFoldedPlayer(PokerPlayer player);

    public abstract void updatePlayerBuyIn(int pot, PokerPlayer bettingPlayer);

    public abstract void updateWithDiscard(int num, PokerPlayer discardPlayer);

    public abstract void updateWithWinner(PokerPlayer player, int pot, ArrayList<PokerPlayer> players);

    public abstract void updateNoPlayersBoughtIn();

    public abstract void updateNoPlayerCanOpen();

    public abstract void reset();

    public abstract boolean isHuman();

    public abstract void updateWithPlayerChoice(int pot, PokerPlayer player, char playerChoice);

    public abstract boolean playAgain();

    public abstract void gameOver();
}

