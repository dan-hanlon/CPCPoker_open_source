import impl.poker.*;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String args[]) throws TwitterException, IOException, InterruptedException {

        TwitterGameController controller = TwitterGameController.getInstance();
        TwitterGameInterface t = TwitterGameInterface.getInstance();
        t.streamTweets();

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
