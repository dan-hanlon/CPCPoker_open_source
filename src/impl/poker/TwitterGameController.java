package impl.poker;


import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TwitterGameController{

    private static TwitterGameController instance;
    private HashMap<User, PokerPlayer> twitterPlayers;

    private TwitterGameController(){
        twitterPlayers = new HashMap<>();
    }

    public PokerPlayer getTwitterPlayer(User twitterUser){
        return twitterPlayers.get(twitterUser);
    }

    public Thread newGame(Status tweet) throws TwitterException, InterruptedException {
        PokerPlayer player = new AutomatedPokerPlayer(getName(), 10);
        PokerPlayer player2 = new AutomatedPokerPlayer(getName(), 10);
        PokerPlayer player3 = new AutomatedPokerPlayer(getName(), 10);
        PokerPlayer player4 = new HumanPokerPlayer(tweet.getUser().getScreenName(), 10, new HumanTwitterInterface(tweet.getUser()));

        ArrayList<PokerPlayer> players = new ArrayList<>();
        players.add(player);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        Thread game = new Thread(new GameOfPoker(tweet.getUser().getId(), players));

        game.start();
        return game;
    }

    public static TwitterGameController getInstance(){
        if(instance == null){
            synchronized (TwitterGameController.class) {
                instance = new TwitterGameController();
            }
        }
        return instance;
    }

    private static ArrayList<String> botNames = new ArrayList<>(
            Arrays.asList(
                    "Bob", "Adam", "Conor", "James", "Philip", "Daniel",
                    "Kyle", "Sam", "Tom", "John", "Robert", "Michael",
                    "David", "Jim", "Gary", "Matthew", "Kevin", "Jason",
                    "Scott", "Frank", "Harold", "Larry", "Peter", "Ryan",
                    "Arthur", "Mary", "Susan", "Lauren", "Emma", "Amy",
                    "Betty", "Helen", "Nancy", "Maria", "Alice", "Julia",
                    "Anna", "Sarah", "Lisa", "Jennifer", "Jane", "Cindy",
                    "Kim", "Robin", "Diana", "Rachel", "April", "Emily",
                    "Megan", "Kate"));

    private static String getName() {
        int random = (int)(Math.random() * botNames.size());
        return botNames.remove(random);
    }

}
