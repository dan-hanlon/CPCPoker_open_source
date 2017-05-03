package impl.poker;

import twitter4j.TwitterException;

import java.util.*;

class HandOfPoker implements interfaces.poker.HandOfPoker {

    private ArrayList<PokerPlayer> players;
    private DeckOfCards deck;
    private int pot;
    private HashMap<PokerPlayer, Integer> contributionToPot;
    private HashMap<PokerPlayer, Boolean> isAllIn;
    Stack<PokerPlayer> folded;

    HandOfPoker(ArrayList<PokerPlayer> allPlayers, DeckOfCards deck){
        players = new ArrayList<>();
        players.addAll(allPlayers);

        for(PokerPlayer player : players){
            player.reset();
        }

        this.deck = deck;
        pot = 0;
        contributionToPot = new HashMap<>(5);
        isAllIn = new HashMap<>();

        for(PokerPlayer player : players){
            contributionToPot.put(player, 0);
            isAllIn.put(player, false);
        }

        deck.reset();
        deck.shuffle();
        folded = new Stack<>();
    }

    @Override
    public int startRound() throws TwitterException, InterruptedException {

        dealCardsToAllPlayers();
        for(PokerPlayer player: players){
            player.getRoundInformation(this);
        }
        int openingPlayer = aPlayerCanOpen();
        if (openingPlayer != -1) {
            startBettingRoundOne(openingPlayer);
            if(players.size() <= 1) {
                finishHand();
                return 0;
            } else {
                startDiscardRound();
                startBettingRoundTwo();
                if(!folded.empty())
                    foldPlayers(folded);
                finishHand();
                return 0;
            }
        } else {
            noPlayerCanOpen();
        }
        finishHand();
        return 0;
    }

    private void noPlayerCanOpen() {
        for(PokerPlayer player : players){
            player.updateNoPlayerCanOpen();
        }
    }

    private void dealCardsToAllPlayers(){
        for (int i = 0; i < HandOfCards.SIZE_OF_HAND; i++) {
            for(PokerPlayer player : players){
                    player.giveCard(deck.dealNext());
            }
        }
    }

    //TODO check if there is a player with at least a pair
    private int aPlayerCanOpen() {
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).canOpen())
                return i;
        }

        return -1;
    }

    private void startBettingRoundOne( int openingPlayer ) throws TwitterException, InterruptedException {

        int betAmount = 0; // first betting round is buy in, starting bet is 0. when a player buys in they bet 1.
        int playerPotContribution = 0; // at buy in every player has not put anything in the pot

        for(int i = 0; i < players.size(); i++){
            PokerPlayer player = players.get((i + openingPlayer) % players.size());

            if (players.size() - folded.size() != 1) {
                if( player.willPlayerBuyIn(betAmount, playerPotContribution) ){
                    pot += 1 ;
                    contributionToPot.put(player, 1);

                    for(PokerPlayer p : players){
                        if(!folded.contains( p ) && p != player){
                            p.updatePlayerBuyIn(pot, player);
                        }
                        else if(p == player)
                            p.updatePotDetails( pot );
                    }

                } else {
                    folded.push(player);

                    for(PokerPlayer p : players){
                        if(!folded.contains( p )){
                            p.updateWithFoldedPlayer(player);
                        }
                    }
                }
            }
        }

        foldPlayers(folded);

    }

    private void foldPlayers(Stack<PokerPlayer> folders){
        folders.forEach(PokerPlayer::reset);
        players.removeAll( folders );

        while(!folders.empty())
            folders.pop();
    }

    private void startDiscardRound(){
        for(PokerPlayer player : players){
            int num = player.discard();

            for(PokerPlayer p : players) {
                if ( p != player) {
                    p.updateWithDiscard(num, player);
                }
            }

            for(int i = 0; i < num; i++)
                    player.giveCard(deck.dealNext());
        }
    }

    private void startBettingRoundTwo(){
        int betAmount = 1; // as anyone left in the game has bought in at 1 chip, we start betting at 1 chip.

        boolean equalPotContribution = false;
        int loopCount = 0;
        while (!equalPotContribution) {
            for(PokerPlayer player : players){
                if (players.size() - folded.size() > 0) {
                    if (!isAllIn.get(player)) {
                        int allInValue = player.getBank();
                        char playerChoice;

                        if (loopCount > 0 && betAmount - contributionToPot.get(player) == 0){
                            continue;
                        } else {
                            playerChoice = player.willPlayerBetCheckFold(betAmount, contributionToPot.get(player));

                            // if a player bets, they raise the current bet by one
                            if (playerChoice == PokerPlayer.BET) {
                                pot += ((betAmount - contributionToPot.get(player)) + 1);
                                betAmount += 1;
                                contributionToPot.put(player, betAmount);
                            }
                            // if a player checks, they match the current bet exactly.
                            else if (playerChoice == PokerPlayer.CHECK) {
                                pot += (betAmount - contributionToPot.get(player));
                                contributionToPot.put(player, betAmount);
                            }
                            // if a player all ins, they put all their chips in the pot
                            else if (playerChoice == PokerPlayer.ALL_IN) {
                                pot += allInValue;
                                if (allInValue + contributionToPot.get(player) > betAmount) {
                                    betAmount = allInValue + contributionToPot.get(player);
                                }
                                isAllIn.put(player, true);
                                int oldContribution = contributionToPot.get(player);
                                contributionToPot.put(player, oldContribution + allInValue);
                            }
                            // if a player does none of the above, they fold
                            else {
                                folded.push(player);
                                if (players.size() == folded.size() + 1) {
                                    return;
                                }
                            }
                        }

                        for(PokerPlayer p : players){
                            if(!folded.contains( p ) && p != player){
                                p.updateWithPlayerChoice(pot, player, playerChoice);
                            }
                            else if(p == player)
                                p.updatePotDetails( pot );
                        }
                    }
                }
            }
            foldPlayers(folded);

            equalPotContribution = true;

            for(PokerPlayer player : players){
                if(contributionToPot.get(player) != betAmount && !isAllIn.get(player))
                    equalPotContribution = false;
            }
            loopCount++;
        }

        for(PokerPlayer player : players){
            player.updatePotDetails(pot);
        }
    }

    private void finishHand(){
        int winningHandRank = -1;
        PokerPlayer winningPlayer = null;

        if (pot > 0) {
            if (players.size() == 1) {
                winningPlayer = players.get(0);
            } 
            else {
                for(PokerPlayer player : players){
                    if(player.getHandRank()> winningHandRank) {
                        winningHandRank = player.getHandRank();
                        winningPlayer = player;
                    }
                }
            }
            winningPlayer.giveCoins(pot);

            for (PokerPlayer player : players) {
                if (players.size() > 1)
                    player.updateWithWinner(winningPlayer, pot, players);
                else
                    player.updateWithWinner(winningPlayer, pot, new ArrayList <PokerPlayer>(  ));
            }
        } else {
            for (PokerPlayer player : players) {
                player.updateNoPlayersBoughtIn();
            }
        }
    }

    public int playerCount(){
        return players.size();
    }

}
