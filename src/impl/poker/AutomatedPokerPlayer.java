package impl.poker;
import java.util.ArrayList;
import java.util.Arrays;

public class AutomatedPokerPlayer extends PokerPlayer{

    private HandOfCards hand;
    private String playerName;
    private int bank;
    private double riskModifier, bluffModifier;
    private boolean isHuman;
    private boolean isBluffing = false;
    private static double[] handProbabilities = {.501177, .422569, .047539, .021128, 0.003925, 0.001965, 0.001441, 0.000240, 0.0000139, 0.00000154};
    private static ArrayList<Double> personalityModifier = new ArrayList<Double>(Arrays.asList( .8,.8,.9,1.0, 1.0,1.0,1.1,1.2,1.2));
    private HandOfPoker currentRound;

    @Override
    public boolean isHuman() {
        return isHuman;
    }

    public void getRoundInformation (HandOfPoker currentHand){
        currentRound = currentHand;
    }

    public AutomatedPokerPlayer(String name, int initialBank){
        playerName = name;
        hand = new HandOfCards();
        bank = initialBank;
        riskModifier = assignModifiers();
        bluffModifier = assignModifiers();
        isHuman = false;
    }

    private double assignModifiers(){
        int random = (int)(Math.random() * personalityModifier.size());
        return personalityModifier.get(random);
    }
    @Override
    public void updateWithFoldedPlayer(PokerPlayer player) {

    }

    @Override
    public void updatePlayerBuyIn(int pot, PokerPlayer bettingPlayer) {

    }

    @Override
    public void updateWithDiscard(int num, PokerPlayer discardPlayer) {

    }

    @Override
    public void reset() {
        hand.removeAll();
    }

    @Override
    public void updateNoPlayerCanOpen() {

    }

    @Override
    public void updateNoPlayersBoughtIn() {

    }

    @Override
    public void updateWithWinner(PokerPlayer player, int pot, ArrayList<PokerPlayer> p ) {

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
    public void gameOver() {

    }

    @Override
    public boolean playAgain() {
        return true;
    }

    @Override
    public boolean willPlayerBuyIn(int totalBetAmount, int contributionToPot) {
        isBluffing = false;
        System.out.println(playerName + ": " + hand);
        if(hand.getRank() > 1000000) {
            bank -= 1;
            return true;
        }
        else{
            double buyInChance = bank * 10 * riskModifier;
            int chance = (int)(Math.random() * 100);
            if(buyInChance >= chance){
                if(bank  > 0) {
                    bank -= 1;
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public int giveCoins(int coins) {
        bank += coins;
        return bank;
    }
    private boolean aiBluff(int remainingPlayers, int betAmount, int contributionToPot){
        double bluffChance = Math.pow(bluffModifier, 4) * 7; // chance to bluff
        int chance = (int)(Math.random() * 100);
        if(bank > 3 && bluffChance > chance){
            bank -= 2;
            return true;
        }
        return false;
    }
    private boolean aiAllIn(int remainingPlayers, int betAmount, int contributionToPot){
        double winChance = 0;
        for(int i = 0; i <  hand.getRank() / 1000000; i++){
            winChance += handProbabilities[i];
        }
        winChance = Math.pow(winChance, remainingPlayers) * riskModifier;
        int chance = (int)(Math.random() * 100);
        if((contributionToPot > 3 || bank < 4) && winChance > chance){
            bank -= bank;
            return true;
        }
        return false;
    }
    private boolean aiBet(int remainingPlayers, int betAmount, int contributionToPot) {
        double winChance = 0;
        if(isBluffing && bank > 3){ // if the player is bluffing, high chance to raise
            winChance = 30 * bluffModifier;
        }
        else{
            for(int i = 0; i < (hand.getRank() / 1000000); i++){
                winChance += handProbabilities[i];
                // we don't want to raise with a high hand, only check.
            }
        }
        // finds the probability of another player having a worse hand than the player
        winChance = Math.pow(winChance, remainingPlayers) * riskModifier * 100;
        System.out.println(playerName + " bet chance: " + winChance);
        int chance = (int)(Math.random() * 100);
        if(winChance >= chance && bank - ((betAmount - contributionToPot) + 1) >= 0){
            bank -= ((betAmount - contributionToPot) + 1);
            return true;
        }
        return false;
    }

    private boolean aiCheck (int remainingPlayers, int betAmount, int contributionToPot){
        double winChance = 0;
        for(int i = 0; i < (hand.getRank() / 1000000); i++){
            winChance += handProbabilities[i];
        }
        if(isBluffing && bank > 3){ // if the player is bluffing, high chance to raise
            winChance = 50 * bluffModifier;
        }
        else{

            winChance = Math.pow(winChance, remainingPlayers) * (riskModifier + (bank / 10)) * 100;
            if(winChance <= 20.0){   // with a high hand, your chance of checking is based on your bank amount
                winChance = 5 + bank * 5 * riskModifier;
            }
            System.out.println(playerName + " check chance: " + winChance);
        }
        int chance = (int)(Math.random() * 100);
        if(winChance >= chance){
            if(bank - (betAmount - contributionToPot) >= 0) {
                bank -= (betAmount - contributionToPot);
                return true;
            }
        }
        return false;
    }

    @Override
    public char willPlayerBetCheckFold(int betAmount, int contributionToPot) {
        System.out.println(playerName + ": " + hand);

        if(aiBluff(currentRound.playerCount(), betAmount, contributionToPot) && isBluffing == false){
            System.out.println(playerName + " IS BLUFFING! SHHHH DONT TELL ANYONE!!!");
            return PokerPlayer.BET;
        }
        else if(aiAllIn(currentRound.playerCount(), betAmount, contributionToPot)){
            return PokerPlayer.ALL_IN;
        }
        else if(aiBet(currentRound.playerCount(), betAmount, contributionToPot)){
            return PokerPlayer.BET;
        }
        else if(aiCheck(currentRound.playerCount(), betAmount, contributionToPot)){
            return PokerPlayer.CHECK;
        }
        isBluffing = false;
        return PokerPlayer.FOLD;
    }

    @Override
    public int discard() {
        return hand.discard(riskModifier);
    }

    @Override
    public HandOfCards getHand() {
        return hand;
    }

    @Override
    public void giveCard(PlayingCard card) {
        hand.addCard(card);
    }

    @Override
    public void updateWithPlayerChoice(int pot, PokerPlayer player, char playerChoice) {

    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public int getBank() {
        return bank;
    }

    @Override
    public void updatePotDetails(int pot) {

    }
}
