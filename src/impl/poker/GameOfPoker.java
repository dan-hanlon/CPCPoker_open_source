package impl.poker;

import twitter4j.TwitterException;

import java.util.*;

public class GameOfPoker implements interfaces.poker.GameOfPoker, Runnable {

    private Long gameID;
    private ArrayList<PokerPlayer> players;
    private ArrayList<PokerPlayer> allPlayers;

    public GameOfPoker(Long ID, ArrayList<PokerPlayer> playerList){
        this.players = playerList;
        allPlayers = new ArrayList<>();
        allPlayers.addAll(playerList);
        this.gameID = ID;
    }

    @Override
    public void startGame() throws TwitterException, InterruptedException {
        while(players.size() > 1 && gameHasHumanPlayer()){
            ArrayList<PokerPlayer> playersInThisRound = players;
            HandOfPoker newRound = new HandOfPoker(playersInThisRound, new DeckOfCards() );
            newRound.startRound();
            Stack<PokerPlayer> bankrupt = new Stack<>();

            for(PokerPlayer player : players){
                if(player.getBank() == 0){
                    bankrupt.push(player);
                }
            }

            players.removeAll(bankrupt);

            Stack<PokerPlayer> notPlayingAgain = new Stack<>();
            for(PokerPlayer player : players){
                if (!player.playAgain()) {
                    notPlayingAgain.push(player);
                }
            }

            players.removeAll(notPlayingAgain);
        }

        for(PokerPlayer p : allPlayers){
            p.gameOver();
        }

    }

    private boolean gameHasHumanPlayer(){
        for(PokerPlayer p : players){
            if(p.isHuman())
                return true;
        }

        return false;
    }

    @Override
    public void run() {
        try {
            startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
