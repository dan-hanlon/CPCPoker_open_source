package impl.poker;

//import interfaces.poker.PlayingCard;

import java.util.*;

import impl.poker.PlayingCard;

class HandOfCards implements interfaces.poker.HandOfCards {

    public static final int SIZE_OF_HAND = 5;
    private static final int ROYAL_FLUSH = 9000000;
    private static final int STRAIGHT_FLUSH = 8000000;
    private static final int FOUR_OF_A_KIND = 7000000;
    private static final int FULL_HOUSE = 6000000;
    private static final int FLUSH = 5000000;
    private static final int STRAIGHT = 4000000;
    private static final int THREE_OF_A_KIND = 3000000;
    private static final int TWO_PAIR = 2000000;
    private static final int PAIR = 1000000;
    private static final int HAND_SIZE = 5;
    private ArrayList<PlayingCard> hand;

    HandOfCards(){
        hand = new ArrayList<>(SIZE_OF_HAND);
    }

    @Override
    public int size() {
        return hand.size();
    }

    @Override
    public void addCard(PlayingCard card) {
        if(hand.size() < HAND_SIZE){
            hand.add(card);
            sortHand();
        }
    }

    public ArrayList<PlayingCard> getHand(){
        return hand;
    }
  //returns if an ace is in the hand
  	private boolean handContainsAce(){
  		boolean found = false;
  		for(int i=0; i<SIZE_OF_HAND; i++){
  			if(hand.get(i).getGameValue() == PlayingCard.ACE_GAME_VAL){
  				found = true;
  			}
  		}
  		return found;
  	}
    
    private int findKicker() {
        if (isFourOfAKind()) {
            if (hand.get(0).getGameValue() == hand.get(1).getGameValue()) {
                return 4;
            }
            else return 0;
        }

        else if (isTwoPair()) {
            if (hand.get(0).getGameValue() == hand.get(1).getGameValue()) {
                if (hand.get(3).getGameValue() == hand.get(4).getGameValue()) {
                    return 2;
                }
                else {
                    return 4;
                }
            }
            else {
                return 0;
            }
        }

        else return 0;
    }

    private int[] findKickers() {
        if (hand.get(0).getGameValue() == hand.get(1).getGameValue()) {
            return new int[]{3, 4};
        }

        else if (hand.get(1).getGameValue() == hand.get(3).getGameValue()) {
            return new int[]{0, 4};
        }

        else if (hand.get(3).getGameValue() == hand.get(4).getGameValue()) {
            return new int[]{0, 1};
        }

        else return null;
    }

    private int[] countSuitFrequency() {
        int heartsFrequency = 0;
        int diamondsFrequency = 0;
        int clubsFrequency = 0;
        int spadesFrequency = 0;
        for (int i = 0; i < SIZE_OF_HAND; i++) {
            switch (hand.get(i).getSuit()) {
                case PlayingCard.DIAMONDS:
                    diamondsFrequency++;
                    break;
                case PlayingCard.HEARTS:
                    heartsFrequency++;
                    break;
                case PlayingCard.SPADES:
                    spadesFrequency++;
                    break;
                case PlayingCard.CLUBS:
                    clubsFrequency++;
                    break;
            }
        }

        // alphabetical :^)
        return new int[]{clubsFrequency, diamondsFrequency, heartsFrequency, spadesFrequency};
    }

    private boolean isOneOffFlush() {
        int[] suits = countSuitFrequency();

        if (suits[0] == 4 || suits[1] == 4 ||
                suits[2] == 4 || suits[3] == 4)
            return true;
        else return false;
    }
    
    private boolean isTwoOffFlush(){
    	int[] suits = countSuitFrequency();

        if (suits[0] == 3 || suits[1] == 3 ||
                suits[2] == 3 || suits[3] == 3)
            return true;
        else return false;
    }
    
    // gets index of card busting the flush - only called in the case of isOneOffFlush()
    private int findSuitKicker() {
        int[] suits = countSuitFrequency();

        if (suits[0] == 4) {
            for (int i = 0; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.CLUBS) {
                    return i;
                }
            }
        }
        else if (suits[1] == 4) {
            for (int i = 0; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.DIAMONDS) {
                    return i;
                }
            }
        }
        else if (suits[2] == 4) {
            for (int i = 0; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.HEARTS) {
                    return i;
                }
            }
        }
        else {
            for (int i = 0; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.SPADES) {
                    return i;
                }
            }
        }

        return 0;
    }

    //find 2 cards busting flush - only called if isTwoOffFlush() is true
    private int[] findSuitKickers(){
    	int[] suits = countSuitFrequency();
    	int index1 = 0, index2 = 0;
    	
        if (suits[0] == 3) {
            for (int i = 0; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.CLUBS) {
                    index1 = i;
                    break;
                }
            }
            for (int i = index1+1; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.CLUBS) {
                    index2 = i;
                    break;
                }
            }
            return new int[]{index1, index2};
        }
        else if (suits[1] == 3) {
            for (int i = 0; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.CLUBS) {
                    index1 = i;
                    break;
                }
            }
            for (int i = index1+1; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.CLUBS) {
                    index2 = i;
                    break;
                }
            }
            return new int[]{index1, index2};
        }
        else if (suits[2] == 3) {
            for (int i = 0; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.CLUBS) {
                    index1 = i;
                    break;
                }
            }
            for (int i = index1+1; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.CLUBS) {
                    index2 = i;
                    break;
                }
            }
            return new int[]{index1, index2};
        }
        else {
            for (int i = 0; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.CLUBS) {
                    index1 = i;
                    break;
                }
            }
            for (int i = index1+1; i < SIZE_OF_HAND; i++) {
                if (hand.get(i).getSuit() != PlayingCard.CLUBS) {
                    index2 = i;
                    break;
                }
            }
            return new int[]{index1, index2};
        }
    }
    
    
    
    private boolean isOneOffStraight(){
    	for(int i = 0; i < SIZE_OF_HAND; i++){
			if(isBreakingStraight(i)){
				return true;
			}
		}
		return false;
    }
    
    //will return lowest value card that is breaking the straight - will only be called in the case of isOneOffStraight
    private int getIndexOfCardBreakingStraight(){
    	int index = 0;
    	for(int i = 0; i < SIZE_OF_HAND; index++){
			if(isBreakingStraight(index)){
				index = i;
				break;
			}
		}
		return index;
    }
    
    //checks if card at position is breaking straight
    private boolean isBreakingStraight(int cardPos){
    	boolean breaking = false;
		
		ArrayList<PlayingCard> temp = new ArrayList<PlayingCard>();
		
		//get list of cards excluding the one at the cardPosition
		for(int i = 0; i < hand.size(); i++){
			if(i == cardPos) {
				continue;
			}
			temp.add(hand.get(i));
		}
		
		int total_diff = Math.abs(temp.get(0).getGameValue() - temp.get(1).getGameValue()) + Math.abs(temp.get(1).getGameValue() - temp.get(2).getGameValue())
				+ Math.abs(temp.get(2).getGameValue() - temp.get(3).getGameValue());
		
		//if the total difference is 3 or 4 then this signifies the card is breaking a straight
		if(total_diff <= 4){
			breaking = true;
		}
		
		//in case of ace low broken straight - use face value to check total_diff
		if(breaking == false) {
			total_diff = Math.abs(temp.get(0).getFaceValue() - temp.get(3).getFaceValue()) + Math.abs(temp.get(1).getFaceValue() - temp.get(2).getFaceValue())
					+ Math.abs(temp.get(2).getFaceValue() - temp.get(3).getFaceValue());
			if(total_diff <= 4){
				breaking = true;
			}
		}
		return breaking;
    }
    
    private boolean isTwoOffStraight(){
		for(int i = 0; i < SIZE_OF_HAND - 1; i++){
			for(int j = i+1; j < SIZE_OF_HAND; j++){
				if(isTwoCardsBreakingStraight(i, j)){
					return true;
				}
			}
		}
		return false;
	}
    
    //checks if two cards at positions are together breaking a straight
    private boolean isTwoCardsBreakingStraight(int pos1, int pos2){
    	boolean yes = false;
		ArrayList<PlayingCard> temp = new ArrayList<PlayingCard>();
		//gets list of cards excluding the two at the arg positions
		for(int i = 0; i < hand.size(); i++){
			if(hand.get(i).getGameValue() == hand.get(pos1).getGameValue() || hand.get(i).getGameValue() == hand.get(pos2).getGameValue()) {
				continue;
			}
			temp.add(hand.get(i));
		}
		//get the total difference between the other 3 cards
		int total_diff = Math.abs(temp.get(0).getGameValue() - temp.get(1).getGameValue()) + Math.abs(temp.get(1).getGameValue() - temp.get(2).getGameValue());
		
		//if the total difference is 3 or 4 then this signifies the two cards are breaking a straight
		if(total_diff <= 4){
			yes = true;
		}
		
		//in case of ace low broken straight - use face value
		if(yes == false) {
			total_diff = Math.abs(temp.get(2).getFaceValue() - temp.get(0).getFaceValue()) + Math.abs(temp.get(1).getFaceValue() - temp.get(2).getFaceValue());
			
			if(total_diff <= 4){
				yes = true;
			}
		}
		return yes;
    }
    
    private int[] getIndexOfTwoCardsBreakingStraight(){
        	int index1 = 0, index2 = 0;
    		for(int i = 0; i < SIZE_OF_HAND - 1; i++){
    			for(int j = i + 1; j < SIZE_OF_HAND; j++){
    				if(isTwoCardsBreakingStraight(i, j)){
    					index1 = i;
    					index2 = j;
    					break;
    				}
    			}
    		}
    		return new int[]{index1, index2};
    }
    
    //only called in case of worst possible high hand - 2 cards will have same suit
    private int[] getIndexOfThreeCardsBustingFlush(){
    	int index1 = 0, index2 = 0;
    	//find index of 2 cards that have same suit
    	for (int i = 0; i < SIZE_OF_HAND; i++){
    		for (int j = i+1; j < SIZE_OF_HAND; j++){
    			if (hand.get(i).getSuit() == hand.get(j).getSuit()){
    				index1 = i;
    				index2 = j;
    				break;
    			}
    		}
    	}

        ArrayList<Integer> indices = new ArrayList<Integer>();
    	for (int i = 0; i < SIZE_OF_HAND; i++){
    		if (i != index1 || i != index2){
    			indices.add(i);
    		}
    	}
		return new int[]{indices.get(0), indices.get(1), indices.get(2)};
    }
    
    
    private int[] findNonPairIndices() {
        if (hand.get(0).getGameValue() == hand.get(1).getGameValue()) {
            return new int[]{2, 3, 4};
        }
        else if (hand.get(1).getGameValue() == hand.get(2).getGameValue()) {
            return new int[]{0, 3, 4};
        }
        else if (hand.get(2).getGameValue() == hand.get(3).getGameValue()) {
            return new int[]{0, 1, 4};
        }
        else if (hand.get(3).getGameValue() == hand.get(4).getGameValue()) {
            return new int[]{0, 1, 2};
        }
        else return null;
    }

    private int[] calculateHighHandDiscard(double riskModifier) {
    	int[] indices;
    	if (isOneOffFlush()){
    		int index = findSuitKicker();
            indices =  new int[]{index};
    	}
    	else if (isOneOffStraight()){
    		int index = getIndexOfCardBreakingStraight();
            indices =  new int[]{index};
    	}
    	else if (isTwoOffFlush()){
    		indices = findSuitKickers();
    		return indices;
    	}
    	else if (isTwoOffStraight()){
    		indices = getIndexOfTwoCardsBreakingStraight();
    	}
    	//is 3 off flush
    	else {
    		indices = getIndexOfThreeCardsBustingFlush();
    	}
    	return indices;
    }

    //uses riskModifier to decide what cards, if any, to discard
    public int discard(double riskModifier) {
    	int chance = (int)(Math.random() * 100);

        if (isRoyalFlush() || isStraightFlush() ||
                isFullHouse() || isStraight() || isFlush())
            return 0;

        else if (isFourOfAKind()) {
        	
        	//discard last card 50% of time
        	if(chance >= 50){
        		int index = findKicker();
        		removeCardsAtPositions(new int[]{index});
        		return 1;
        	}
        	else {
        		return 0;
        	}
        }
        else if (isThreeOfAKind()) {
            int[] indices = findKickers();
            removeCardsAtPositions(indices);
            return 2;
        }
        else if (isTwoPair()) {
            int index = findKicker();
            removeCardsAtPositions(new int[]{index});
            return 1;
        }
        else if (isPair()) {
        	double prob = 0;
        	//probability with riskiness taken into account
        	double chancePlayerWillDiscard = 0.0;
        	
            if (isOneOffFlush()) {
            	//10 possible cards to make a flush
            	prob = (10.0/47.0) * 100.0;
            	
            	chancePlayerWillDiscard = prob *riskModifier;   // The higher the risk modifier, the higher the chance of going for discard
            	
            	if(chance <= chancePlayerWillDiscard){
            		int index = findSuitKicker();
            		removeCardsAtPositions(new int[]{index});
                    return 1;
            	}
            }
            else if(isOneOffStraight()){
            	//4 possible cards to make a straight
            	if(handContainsAce()){
					prob = (4.0/47.0) * 100.0;
				}
				//8 possible cards to make a straight
				else{
					prob = (int) ((8.0/47.0) * 100.0);
				}
            	
            	chancePlayerWillDiscard = prob * riskModifier;
            	if(chance <= chancePlayerWillDiscard){
            		int index = getIndexOfCardBreakingStraight();
            		removeCardsAtPositions(new int[]{index});
                    return 1;
            	}
            }
            //discards 3 cards if not one off flush or straight, or chance simulation fails
            int[] indices = findNonPairIndices();
            removeCardsAtPositions(indices);
            return 3;
        }

        else if (isHighHand()) {
            int[] indices = calculateHighHandDiscard(riskModifier);
            removeCardsAtPositions(indices);
            return indices.length;
        }

        else return 0;
    }

    @Override
    public int removeCardsAtPositions(int[] array) {
        Stack<PlayingCard> cardsToRemove = new Stack<>();
        int count = 0;

        for(int i=array.length-1; i>=0; i--){
    	    if(array[i] == -1) {continue;}

    		PlayingCard temp = hand.get(array[i]);
            cardsToRemove.push(temp);
            count++;
        }
    	hand.removeAll(cardsToRemove);
        return count;
    }

    @Override
    public String toString(){
        if (hand != null){
            return hand.toString();
        } else
            return "Hand is empty.";
    }

    public int getRank(){
        sortHand();
        if(isRoyalFlush()){
            // all hands are of equal value
            return ROYAL_FLUSH;
        }
        else if(isStraightFlush()){
            // High card wins
            return STRAIGHT_FLUSH + getHighCard().getGameValue();
        }
        else if(isFourOfAKind()){
            // High card wins
            return FOUR_OF_A_KIND;
        }
        else if(isFullHouse()){
            // High card wins
            return FULL_HOUSE + getHighCard().getGameValue();
        }
        else if(isFlush()){
            // High card wins
            return FLUSH + getHighCard().getGameValue();
        }
        else if(isStraight()){
            // High card wins
            return STRAIGHT + getHighCard().getGameValue();
        }
        else if(isThreeOfAKind()){
            // Highest set of 3 cards wins
            return THREE_OF_A_KIND + getHighCard().getGameValue();
        }
        else if(isTwoPair()){
            // Highest pair wins. If there is a tie, the next highest pair wins. If there is a tie, the remaining high card wins
            // Each card is multiplied by a value ensuring that the lesser set of cards is only taken into account if there is a tie.
            // The first pair's minimum value is 400, while the second pair's highest possible value is 14*20 =280. This ensures that the lesser pair
            // is only taken into account if there is a tie, otherwise the greater pair always wins. The same is true for the remaining card, as its max
            // value is 14, while the minimum value for the lesser pair is 40.
            // This same principal is used in all formulas
            if(hand.get(0).getGameValue() == hand.get(1).getGameValue() && hand.get(2).getGameValue() == hand.get(3).getGameValue()){
                return TWO_PAIR + 200 * hand.get(2).getGameValue() + 20*hand.get(1).getGameValue() + hand.get(4).getGameValue();
            }
            else if(hand.get(0).getGameValue() == hand.get(1).getGameValue() && hand.get(3).getGameValue() == hand.get(4).getGameValue()){
                return TWO_PAIR + 200 * hand.get(3).getGameValue() + 20*hand.get(1).getGameValue() + hand.get(2).getGameValue();
            }
            else{
                return TWO_PAIR + 200 * hand.get(3).getGameValue() + 20*hand.get(1).getGameValue() + hand.get(0).getGameValue();
            }
        }
        else if (isPair()){
            // Highest pair wins. If there is a tie, the next highest card wins, if there is a tie, this is repeated until all cards have been looked at
            if(hand.get(0).getGameValue() ==  hand.get(1).getGameValue()){
                return PAIR + 4000*hand.get(0).getGameValue() + 200*hand.get(4).getGameValue() + 20*hand.get(3).getGameValue() + hand.get(2).getGameValue();
            }
            else if (hand.get(1).getGameValue() == hand.get(2).getGameValue()){
                return PAIR + 4000*hand.get(1).getGameValue() + 200*hand.get(4).getGameValue() + 20*hand.get(3).getGameValue() + hand.get(0).getGameValue();
            }
            else if (hand.get(2).getGameValue() == hand.get(3).getGameValue()){
                return PAIR + 4000*hand.get(2).getGameValue() + 200*hand.get(4).getGameValue() + 20*hand.get(1).getGameValue() + hand.get(0).getGameValue();
            }
            else {
                return PAIR + 4000*hand.get(3).getGameValue() + 200*hand.get(2).getGameValue() + 20*hand.get(1).getGameValue() + hand.get(0).getGameValue();
            }
        }
        else{
            // Highest card wins. If there is a tie, the next highest card wins. This continues until all card have been looked at
            return 30000 * hand.get(4).getGameValue() + 4000 * hand.get(3).getGameValue() + 200 * hand.get(2).getGameValue()
                    + 20 * hand.get(1).getGameValue()  + hand.get(0).getGameValue();
        }
    }

    public boolean isRoyalFlush() {
        // A,K,Q,J,10 all same suit
        if (hand.get(0).getGameValue() == 10 && hand.get(1).getGameValue() == 11 && hand.get(2).getGameValue() == 12
                && hand.get(3).getGameValue() == 13 && hand.get(4).getGameValue() == 14 && sameSuit()) {
            return true;
        }
        return false;
    }

    public boolean isStraightFlush() {
        if(isRoyalFlush())
        {
            return false;
        }
        // 5 card sequence same suit
        if (hand.get(0).getGameValue() == hand.get(1).getGameValue() - 1
                && hand.get(1).getGameValue() == hand.get(2).getGameValue() - 1
                && hand.get(2).getGameValue() == hand.get(3).getGameValue() - 1
                && hand.get(3).getGameValue() == hand.get(4).getGameValue() - 1 && sameSuit()) {
            return true;
        }
        else if(hand.get(0).getGameValue() == hand.get(1).getGameValue() + 12
                && hand.get(1).getGameValue() == hand.get(2).getGameValue() - 1
                && hand.get(2).getGameValue() == hand.get(3).getGameValue() - 1
                && hand.get(3).getGameValue() == hand.get(4).getGameValue() - 1 && sameSuit()){
            return true;
        }
        return false;
    }

    public boolean isFourOfAKind() {
        if(isStraightFlush())
        {
            return false;
        }
        for (int i = 0; i < SIZE_OF_HAND - 3; i++) {
            if (hand.get(i).getGameValue() == hand.get(i + 1).getGameValue()
                    && hand.get(i).getGameValue() == hand.get(i + 2).getGameValue()
                    && hand.get(i).getGameValue() == hand.get(i + 3).getGameValue()) {
                return true;
            }
        }
        return false;
    }

    public boolean isFullHouse() {
        if(isFourOfAKind())
        {
            return false;
        }
        if (hand.get(0).getGameValue() == hand.get(1).getGameValue()
                && hand.get(0).getGameValue() == hand.get(2).getGameValue()
                && hand.get(3).getGameValue() == hand.get(4).getGameValue()) {
            return true;
        } else if (hand.get(0).getGameValue() == hand.get(1).getGameValue()
                && hand.get(2).getGameValue() == hand.get(3).getGameValue()
                && hand.get(2).getGameValue() == hand.get(4).getGameValue()) {
            return true;
        }
        // 3 of a kind and pair
        return false;
    }

    public boolean isFlush() {
        if(isFullHouse())
        {
            return false;
        }
        if (sameSuit()) {
            return true;
        }
        // same suit
        return false;
    }

    public boolean isStraight() {
        if(isFlush())
        {
            return false;
        }
        if (hand.get(0).getGameValue() == hand.get(1).getGameValue() - 1
                && hand.get(1).getGameValue() == hand.get(2).getGameValue() - 1
                && hand.get(2).getGameValue() == hand.get(3).getGameValue() - 1
                && hand.get(3).getGameValue() == hand.get(4).getGameValue() - 1) {
            return true;
        }
        else if(hand.get(0).getGameValue() == hand.get(1).getGameValue() + 12
                && hand.get(1).getGameValue() == hand.get(2).getGameValue() - 1
                && hand.get(2).getGameValue() == hand.get(3).getGameValue() - 1
                && hand.get(3).getGameValue() == hand.get(4).getGameValue() - 1){
            sortFaceValue();
            return true;
        }
        // 5 cards in sequence, different suit
        return false;
    }

    public boolean isThreeOfAKind() {
        if(isStraight())
        {
            return false;
        }
        for (int i = 0; i < SIZE_OF_HAND - 2; i++) {
            if (hand.get(i).getGameValue() == hand.get(i + 1).getGameValue()
                    && hand.get(i).getGameValue() == hand.get(i + 2).getGameValue()) {
                return true;
            }
        }
        // three of a kind
        return false;
    }

    public boolean isTwoPair() {
        if(isThreeOfAKind())
        {
            return false;
        }
        for (int i = 0; i < SIZE_OF_HAND - 3; i++) {
            if (hand.get(i).getFaceValue() == hand.get(i + 1).getFaceValue()) {
                for (int j = i+2; j < SIZE_OF_HAND-1; j++) {
                    j += 0;
                    if (hand.get(j).getFaceValue() == hand.get(j + 1).getFaceValue()) {
                        return true;
                    }
                }
            }
        }
        // two separate pairs
        return false;
    }

    public boolean isPair() {
        if(isTwoPair()){
            return false;
        }
        for (int i = 0; i < SIZE_OF_HAND - 1; i++) {
            if (hand.get(i).getFaceValue() == hand.get(i + 1).getFaceValue()) {
                return true;
            }
        }
        return false;
    }

    public boolean isHighHand(){
        if(isPair()){
            return false;
        }
        else{
            return true;
        }
    }
    private PlayingCard getHighCard(){	// returns the highest card from the hand
        if(isFullHouse() || isThreeOfAKind()){	// For these three hands, the highest card is always the card in the middle
            return hand.get(2);
        }
        return hand.get(4);
    }
    private boolean sameSuit() {	// checks if the cards are of the same suit
        if (hand.get(0).getSuit() == hand.get(1).getSuit() && hand.get(1).getSuit() == hand.get(2).getSuit()
                && hand.get(2).getSuit() == hand.get(3).getSuit() && hand.get(3).getSuit() == hand.get(4).getSuit()) {
            return true;
        }
        return false;
    }
    private void sortFaceValue(){
        if(hand.get(0).getFaceValue() == 2 && hand.get(1).getFaceValue() == 3 && hand.get(2).getFaceValue() == 4 &&
                hand.get(3).getFaceValue() == 5 && hand.get(4).getFaceValue() == 1)
        {
            Collections.sort(hand, new Comparator<PlayingCard>() {
                public int compare(PlayingCard card1, PlayingCard card2) {
                    return Integer.compare(card1.getFaceValue(), card2.getFaceValue());
                }
            });
        }
    }
    // Uses collections.sort based on the game value of each card
    private void sortHand() {
        // use collections.sort
        Collections.sort(hand, new Comparator<PlayingCard>() {
            public int compare(PlayingCard card1, PlayingCard card2) {
                return Integer.compare(card1.getGameValue(), card2.getGameValue());
            }
        });
    }

    public void removeAll(){
        hand.removeAll(hand);
    }
}
