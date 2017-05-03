package impl.poker;

import twitter4j.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class TwitterGameInterface{

    private static TwitterGameInterface instance;
    private TwitterGameController controller;
    private HashMap<Long, Status> recentTweet;
    private HashMap<Long, Thread> gameThreads;
    private int runningGames;

    private TwitterGameInterface(){
        this.controller = TwitterGameController.getInstance();
        gameThreads = new HashMap<>();
        recentTweet = new HashMap<>();
        runningGames = 0;
    }

    public static TwitterGameInterface getInstance(){
        if(instance == null){
            synchronized (TwitterGameInterface.class) {
                instance = new TwitterGameInterface();
            }
        }
        return instance;
    }

    void sendTweet(StatusUpdate stat){
        Twitter twitter = TwitterFactory.getSingleton();

        stat = new StatusUpdate(stat.getStatus() + "\n\nID:" + getRandomIdentifier());
        try {
            twitter.updateStatus(stat);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private String getRandomIdentifier(){
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 8; i++){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, 8).toUpperCase();
    }

    public void streamTweets() throws TwitterException, IOException{
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                try {
                    Twitter t = TwitterFactory.getSingleton();
                    if (status.getUser().getId() !=  t.getId()) {
                        tweetParse(status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

            @Override
            public void onTrackLimitationNotice(int i) {}

            @Override
            public void onScrubGeo(long l, long l1) {}

            @Override
            public void onStallWarning(StallWarning stallWarning) {}

            @Override
            public void onException(Exception e) {}
        };

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        FilterQuery filter = new FilterQuery("#CPPjoin", "@cosypepepoker");
        twitterStream.addListener(listener);
        twitterStream.filter(filter);
    }

    private synchronized void tweetParse(Status status) throws TwitterException, InterruptedException {

        User tweetUser = status.getUser();

        if (gameThreads.containsKey(tweetUser.getId()) ) {
            synchronized (gameThreads.get(tweetUser.getId())) {
                recentTweet.put(tweetUser.getId(), status);
                gameThreads.get(tweetUser.getId()).notifyAll();
            }

        } else if (status.getText().contains("#cppjoin") && runningGames < 50){
            StatusUpdate reply = new StatusUpdate("@" + tweetUser.getScreenName() + " You have successfully joined a game!");
            sendTweet(reply);
            gameThreads.put(tweetUser.getId(), controller.newGame(status));
            runningGames += 1;
        } else if(runningGames > 50){
            StatusUpdate reply = new StatusUpdate("@" + tweetUser.getScreenName() + " Sorry but there are too " +
                    "many games running at the moment, try again later!");
            sendTweet(reply);
        }
    }

    synchronized void terminatedThread(Long id){
        gameThreads.remove(id);
        runningGames -= 1;
    }

    Status getUserResponse(User user){
        Status tweet;
        synchronized (gameThreads.get(user.getId())){
            while (recentTweet.get(user.getId()) == null) {
                try {
                    gameThreads.get(user.getId()).wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tweet = recentTweet.get(user.getId());
        }
        recentTweet.put(user.getId(), null);
        return tweet;
    }
}
