package interfaces.poker;

import twitter4j.TwitterException;

public interface GameOfPoker {

    void startGame() throws TwitterException, InterruptedException;
}
