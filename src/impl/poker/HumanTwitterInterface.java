package impl.poker;

import twitter4j.*;

import java.util.*;

public class HumanTwitterInterface extends HumanInterface {

    private PokerPlayer player;
    private User twitterUser;
    private Queue<PokerPlayer> foldedPlayers;
    private Queue<PokerPlayer> hasDiscarded;
    private HashMap<PokerPlayer, Integer> amountOfCardsDiscarded;
    private TwitterGameInterface gameInterface;
    private Queue<PokerPlayer> hasMadeAMove;
    private HashMap<PokerPlayer, Character> moveMadeByPlayer;
    private Queue<PokerPlayer> hasBoughtIn;
    private int currentPot;
    private HashMap<String, String> emojiFaces;

    public HumanTwitterInterface(User user){
        twitterUser = user;
        foldedPlayers = new LinkedList<>();
        amountOfCardsDiscarded = new HashMap<>();
        hasDiscarded = new LinkedList<>();
        gameInterface = TwitterGameInterface.getInstance();
        hasMadeAMove = new LinkedList<>();
        moveMadeByPlayer = new HashMap<>();
        hasBoughtIn = new LinkedList<>();
        currentPot = 0;
        emojiFaces = new HashMap<>();
        createFaces();
    }

    @Override
    void playerHand() {
        String str = "@" + twitterUser.getScreenName() + " Your hand is:\n" + player.getHand();
        StatusUpdate stat = new StatusUpdate(str);
        gameInterface.sendTweet(stat);
    }


    @Override
    synchronized boolean willBuyIn() {
        tweetBuyIn();
        tweetFolded();

        StatusUpdate stat = new StatusUpdate("@" + twitterUser.getScreenName() + " Your hand is:\n"
                + player.getHand() + "\n\nWould you like to buy in? (Y/N)\n" + player.getBank() + " in the bank.");

        gameInterface.sendTweet(stat);

        boolean validInput = false;
        int loopCount = 0;
        Status response;
        String responseString = "";

        while (!validInput) {
            response = gameInterface.getUserResponse(twitterUser);

            System.out.println( twitterUser.getScreenName() + " Buy in thread woken.");
            responseString = removeUsernamesAndHashtags(response.getText()).trim();

            if(responseString.equalsIgnoreCase("y") || responseString.equalsIgnoreCase("n") ||
                    responseString.equalsIgnoreCase("yes") || responseString.equalsIgnoreCase("no")) {
                validInput = true;
            } else {
                System.out.println("Not valid input from user. Tweet: " + responseString);
                if(loopCount==0){
                    String str = "@" + twitterUser.getScreenName() + " Sorry that was not a valid input!\n" +
                            "Do you want to buy in?\n\nTweet Y if you want to buy in,\nN if you do not";
                    StatusUpdate help = new StatusUpdate(str);
                    gameInterface.sendTweet(help);
                }
            }
            loopCount++;
        }

        return responseString.equalsIgnoreCase("y") || responseString.equalsIgnoreCase("yes");
    }

    private String removeUsernamesAndHashtags(String str){
        return str.replaceAll("(#|@)\\w+", "");
    }

    @Override
    public void gameOver() {
        System.out.println("twitter game over");
        String overStr = "@" + twitterUser.getScreenName() + " your game is now over!\nYou can now start a new game!" +
                "\nThanks for playing :)";
        StatusUpdate stat = new StatusUpdate(overStr);
        reset();
        gameInterface.sendTweet(stat);
        gameInterface.terminatedThread(twitterUser.getId());
    }

    @Override
    void insufficientBank() {

    }

    @Override
    public void playerMove(int pot, PokerPlayer player, char playerChoice) {
        System.out.println(player.getPlayerName() + " has " + playerChoice);
        if(playerChoice == PokerPlayer.FOLD){
            foldedPlayer(player);
        }
        else {
            hasMadeAMove.add(player);
            moveMadeByPlayer.put(player, playerChoice);
        }
    }

    @Override
    void printPot(int pot) {
        currentPot = pot;
    }


    @Override
    public void otherPlayerDiscard(int num, PokerPlayer discardPlayer) {
        hasDiscarded.add(discardPlayer);
        amountOfCardsDiscarded.put(discardPlayer, num);
    }

    private int tweetPlayerMove() {
        if (hasMadeAMove.peek() !=  null) {
            String moveString = "@" + twitterUser.getScreenName() + "\n";

            PokerPlayer p;
            while(hasMadeAMove.peek() != null){
                p = hasMadeAMove.poll();
                char c = moveMadeByPlayer.get(p);
                if(c == PokerPlayer.CHECK){
                    moveString = moveString.concat(p.getPlayerName() + " has checked " + emojiFaces.get("PokerFace") + "\n");
                } else if(c == PokerPlayer.BET){
                    moveString = moveString.concat(p.getPlayerName() + " has raised " + emojiFaces.get("PokerFace") + "\n");
                } else if(c == PokerPlayer.ALL_IN){
                    moveString = moveString.concat(p.getPlayerName() + " is All In " + emojiFaces.get("PokerFace") + "\n");
                } else if(c == PokerPlayer.FOLD){
                    foldedPlayers.add(p);
                }
            }

            StatusUpdate discardStatus = new StatusUpdate(moveString);
            gameInterface.sendTweet(discardStatus);
        }

        return 0;
    }

    private void tweetFolded(){
        if (foldedPlayers.peek() != null) {

            String foldedString = "@" + twitterUser.getScreenName() + "\n";

            PokerPlayer p;

            while(foldedPlayers.peek() != null){
                p = foldedPlayers.poll();
                foldedString = foldedString.concat(p.getPlayerName() + " has folded " + emojiFaces.get("FoldFace") +"\n");
            }

            StatusUpdate foldedStatus = new StatusUpdate(foldedString);
            gameInterface.sendTweet(foldedStatus);
        }

    }

    private void tweetDiscard(){
        if (hasDiscarded.peek() != null) {
            String discardedString = "@" + twitterUser.getScreenName() + "\n";

            PokerPlayer p;
            while(hasDiscarded.peek() != null){
                p = hasDiscarded.poll();
                int num = amountOfCardsDiscarded.get(p);
                discardedString = discardedString.concat(p.getPlayerName() + " has discarded " + num + " cards "+ emojiFaces.get("PokerFace") +"\n");
            }

            StatusUpdate discardStatus = new StatusUpdate(discardedString);
            gameInterface.sendTweet(discardStatus);
        }
    }

    private void tweetBuyIn(){
        if (hasBoughtIn.peek() != null) {
            String boughtInString = "@" + twitterUser.getScreenName() + "\n";

            PokerPlayer p;
            while(hasBoughtIn.peek() != null){
                p = hasBoughtIn.poll();
                boughtInString = boughtInString.concat(p.getPlayerName() + " has bought in.\n");
            }

            StatusUpdate stat = new StatusUpdate(boughtInString);
            gameInterface.sendTweet(stat);
        }
    }

    @Override
    public boolean playAgain() {
        String str = "@" + twitterUser.getScreenName() + " do you want to play again? (Y/N)";
        StatusUpdate stat = new StatusUpdate(str);
        gameInterface.sendTweet(stat);

        boolean validInput = false;
        int loopCount = 0;
        Status response;
        String responseString = "";

        while (!validInput) {
            response = gameInterface.getUserResponse(twitterUser);

            System.out.println( twitterUser.getScreenName() + " Play again in thread woken.");
            responseString = removeUsernamesAndHashtags(response.getText()).trim();

            if(responseString.equalsIgnoreCase("y") || responseString.equalsIgnoreCase("n") ||
                    responseString.equalsIgnoreCase("yes") || responseString.equalsIgnoreCase("no")) {
                validInput = true;
            } else {
                System.out.println("Not valid input from user. Tweet: " + responseString);
                if(loopCount==0){
                    str = "@" + twitterUser.getScreenName() + " Sorry that was not a valid input!\n" +
                            "Do you want to play again?\n yes / no";
                    StatusUpdate help = new StatusUpdate(str);
                    gameInterface.sendTweet(help);
                }
            }
            loopCount++;
        }

        return responseString.equalsIgnoreCase("y") || responseString.equalsIgnoreCase("yes");

    }

    @Override
    int[] discardCards() {
        tweetFolded();
        tweetBuyIn();
        tweetDiscard();

        String tweetString = "@" + twitterUser.getScreenName() + " What cards would you like to discard? If none, reply"
                    + " with \"none\". \nYour hand:\n" + player.getHand();

        StatusUpdate stat = new StatusUpdate(tweetString);
        gameInterface.sendTweet(stat);

        Status response;

        int intArray[] = {-1,-1,-1};
        boolean validInput = false;
        int loopCount = 0;

        while(!validInput){
            response = gameInterface.getUserResponse(twitterUser);
            String str = removeUsernamesAndHashtags(response.getText()).trim();
            System.out.println("discard cards test: " + str);
            if (str.equalsIgnoreCase("none")){
                validInput = true;
            } else {
                intArray = checkIfDiscardValidInput(str, player);
                // If the intArray is all -1's that means that the input was invalid, and the player is asked to discard again.
                for (int i : intArray) {
                    if (i != -1) {
                        validInput = true;
                        break;
                    }
                }
            }

            if(!validInput){
                String helpStr = "@" + twitterUser.getScreenName() + " not a valid input!\n" + "Reply with \"none\" or"
                        + " up to 3 card identifiers (e.g AC or Ace of Hearts)";
                stat = new StatusUpdate(helpStr);
                gameInterface.sendTweet(stat);
            }
        }

        return intArray;
    }



    @Override
    PokerPlayer setPlayer(PokerPlayer player) {
        this.player = player;
        return player;
    }

    @Override
    char checkBetFold(int currentBet, int contributionToPot) {
        int checkAmount = currentBet - contributionToPot;
        int betAmount = (currentBet - contributionToPot) + 1;

        tweetDiscard();
        tweetFolded();
        int temp = tweetPlayerMove();

        String tweetString = "@" + twitterUser.getScreenName() + "\nBet, Check, Fold or All In? B/C/F/A\n" +
                "To check: " + checkAmount + " credit(s). To bet: "+ betAmount + " credits. Your bank: " +
                player.getBank() + " credit(s)\nPot:" + currentPot;

        tweetString = tweetString.trim();
        StatusUpdate stat = new StatusUpdate(tweetString);

        gameInterface.sendTweet(stat);

        Status response;
        String responseString;
        boolean validInput = false;
        int loopCount = 0;

        while (!validInput) {
            response = gameInterface.getUserResponse(twitterUser);

            System.out.println( twitterUser.getScreenName() + "\nBet/Check/Fold/All-In thread woke");
            responseString = removeUsernamesAndHashtags(response.getText()).trim();

            if(responseString.length() == 1) {
                if( responseString.equalsIgnoreCase("B") || responseString.equalsIgnoreCase("bet"))
                    return PokerPlayer.BET;
                else if (responseString.equalsIgnoreCase("F") || responseString.equalsIgnoreCase("fold"))
                    return PokerPlayer.FOLD;
                else if (responseString.equalsIgnoreCase("C") || responseString.equalsIgnoreCase("check"))
                    return PokerPlayer.CHECK;
                else if (responseString.equalsIgnoreCase("A") || responseString.equalsIgnoreCase("all in"))
                    return PokerPlayer.ALL_IN;
            }

            if(loopCount == 0){
                String str = "@" + twitterUser.getScreenName() + " not a valid input!\n"
                        + "Do you want to Bet, Check, Fold, or All In?\n" + "Tweet B, C, F or A!";
                StatusUpdate status = new StatusUpdate(str);
                gameInterface.sendTweet(status);
            }
            loopCount++;
        }

        return PokerPlayer.FOLD;
    }



    @Override
    boolean insufficientBankAllIn() {
        String str = "@" + twitterUser.getScreenName() + " To continue you must All In\n" + "Do you want to all in? " +
                "(Y/N)";
        StatusUpdate statusUpdate = new StatusUpdate(str);
        gameInterface.sendTweet(statusUpdate);

        boolean validInput = false;
        int loopCount = 0;
        Status response;
        String responseString = "";

        while (!validInput) {
            response = gameInterface.getUserResponse(twitterUser);

            responseString = removeUsernamesAndHashtags(response.getText()).trim();
            System.out.println( twitterUser.getScreenName() + " All In check thread woken. input: " + responseString);

            if(responseString.equalsIgnoreCase("y") || responseString.equalsIgnoreCase("n") ||
                    responseString.equalsIgnoreCase("yes") || responseString.equalsIgnoreCase("no")) {
                validInput = true;
            } else {
                System.out.println("Not valid All In input check from user. Tweet: " + responseString);
                if(loopCount==0){
                    String temp = "@" + twitterUser.getScreenName() + " Sorry that was not a valid input!\n" +
                            "Do you want to All In?\n\nReply with Yes or No.";
                    StatusUpdate help = new StatusUpdate(temp);
                    gameInterface.sendTweet(help);
                }
            }
            loopCount++;
        }

        return responseString.equalsIgnoreCase("y") || responseString.equalsIgnoreCase("yes");
    }



    @Override
    public void foldedPlayer(PokerPlayer player) {
        System.out.println(player.getPlayerName() + " has folded.");
        foldedPlayers.add(player);
    }

    @Override
    public void buyIn(int pot, PokerPlayer player) {
        System.out.println(player.getPlayerName() + " has bought in");
        hasBoughtIn.add(player);
        currentPot = pot;
    }

    @Override
    public void noPlayerCanOpen() {
        StatusUpdate stat = new StatusUpdate("@" + twitterUser.getScreenName() + " No player can open, a new " +
                "hand will be dealt!");
        TwitterGameInterface.getInstance().sendTweet(stat);
        this.reset();
    }

    @Override
    public void noPlayersBoughtIn() {
        System.out.println("no players bought in");
        this.reset();
    }

    @Override
    public void winningPlayer(PokerPlayer winningPlayer, int pot, ArrayList<PokerPlayer> players) {
        String str;

        if (!players.isEmpty()) {
            str = "@" + twitterUser.getScreenName() + "\n";
            for(PokerPlayer p : players){
                str = str.concat(p.getPlayerName() + " had: " + p.getHand() + "\n");
            }

            StatusUpdate stat = new StatusUpdate(str);
            gameInterface.sendTweet(stat);
        }

        System.out.println(winningPlayer.getPlayerName() + " has won.");
        str = "@" + twitterUser.getScreenName() + " " + winningPlayer.getPlayerName() + " has won the pot of " +
                pot + "!\n";
        if(winningPlayer == player){
            str = str.concat("Your bank is now " + player.getBank());
        } else{
            str += emojiFaces.get("WinFace");
        }
        StatusUpdate stat = new StatusUpdate(str);
        gameInterface.sendTweet(stat);

        this.reset();
    }

    @Override
    void newCard(PlayingCard card) {

    }

    private void createFaces(){
        emojiFaces.put("PokerFace", "\ud83d\ude11");
        emojiFaces.put("WinFace", "\ud83e\udd11");
        emojiFaces.put("FoldFace", "\ud83d\ude1e");
    }

    @Override
    public void reset(){
        foldedPlayers = new LinkedList<>();

        hasDiscarded = new LinkedList<>();
        amountOfCardsDiscarded = new HashMap<>();

        hasMadeAMove = new LinkedList<>();
        moveMadeByPlayer = new HashMap<>();

    }

}
