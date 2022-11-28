package pl.edu.agh.kis.pz1;

import java.nio.channels.SelectionKey;
import java.util.*;

public class Player {
    //zmienna przechowująca liczbę kart jaką ma gracz
    int num_of_cards = 5;

    //tablica przechowująca karty które gracz posiada
    Card[] cards_;

    int balance;
    SelectionKey playerKey;
    String lastDecision;

    public Player(Card[] cards){
        cards_ = cards;
        num_of_cards = cards.length;
        balance = 100;
        playerKey = null;
    }
    public Player(Card[] cards, SelectionKey key){
        cards_ = cards;
        num_of_cards = cards.length;
        balance = 100;
        playerKey = key;
    }
    public Player(){
        cards_ = null;
        num_of_cards = 0;
        balance = 100;
    }

    //funkcja wypisująca karty gracza
    public String show_cards(){
        String returnString = "";
        for (int i = 0; i < cards_.length; i++)
            if (i < cards_.length - 1)
                returnString += (i + 1) + ": " + cards_[i].rank_ + " of " + cards_[i].suit_ + ", ";
            else
                returnString += (i + 1) + ": " + cards_[i].rank_ + " of " + cards_[i].suit_;
        return  returnString;
    }

    public void sortCards(){
        for (int j = 1; j < cards_.length; j++) {
            Card key = this.cards_[j];
            int i = j - 1;
            while ((i > -1) && (this.cards_[i].is_greater_than(key))) {
                this.cards_[i + 1] = this.cards_[i];
                i--;
            }
            this.cards_[i+1] = key;
        }
    }


    public HandRanking whatHandRanking(){
        if (cards_.length < 5) throw new IllegalArgumentException("Size of the given set of cards is not equal to 5");
        Set<HandRanking> retValue = new HashSet<>();
        retValue.add(HandRanking.HIGH_CARD);
        this.sortCards();
        Suit tempSuit = cards_[0].suit_;
        if (tempSuit == cards_[1].suit_ && tempSuit == cards_[2].suit_ &&
                tempSuit == cards_[3].suit_ && tempSuit == cards_[4].suit_){ // ten sam kolor
            // royal flush
            if (cards_[0].rank_ == Rank.TEN &&
                    cards_[1].rank_ == Rank.JACK &&
                    cards_[2].rank_ == Rank.QUEEN &&
                    cards_[3].rank_ == Rank.KING &&
                    cards_[4].rank_ == Rank.ACE){
                retValue.add(HandRanking.ROYAL_FLUSH);
            }
            // straight flush
            else if (cards_[1].rank_.num_val == cards_[0].rank_.num_val + 1 &&
                    cards_[2].rank_.num_val == cards_[1].rank_.num_val + 1 &&
                    cards_[4].rank_.num_val == cards_[3].rank_.num_val + 1){
                retValue.add(HandRanking.STRAIGHT_FLUSH);
            }
            // flush
            else retValue.add(HandRanking.FLUSH);
        }
        // straight
        if (cards_[1].rank_.num_val == cards_[0].rank_.num_val + 1 &&
                cards_[2].rank_.num_val == cards_[1].rank_.num_val + 1 &&
                cards_[4].rank_.num_val == cards_[3].rank_.num_val + 1){
            retValue.add(HandRanking.STRAIGHT);
        }

        int numSame = 0;
        Map<Rank, Integer> duplicatesMap = new HashMap<>();
        for (Card c : cards_) {
            if (duplicatesMap.containsKey(c.rank_)) {
                duplicatesMap.put(c.rank_, duplicatesMap.get(c.rank_) + 1);
            } else {
                duplicatesMap.put(c.rank_, 1);
            }
        }
        int num_of_pairs = 0;
        boolean triplet = false;
        for (int occ : duplicatesMap.values()){
            if (occ == 2) num_of_pairs++;
            if (occ == 3) triplet = true;
            if (occ == 4) retValue.add(HandRanking.FOUR_OF_A_KIND); // four of a kind
        }

        // full house
        if (triplet && num_of_pairs == 1)
            retValue.add(HandRanking.FULL_HOUSE);
        // three of a kind
        if (triplet && num_of_pairs == 0)
            retValue.add(HandRanking.THREE_OF_A_KIND);
        // two pair
        if (num_of_pairs == 2)
            retValue.add(HandRanking.TWO_PAIR);
        // one pair
        if (num_of_pairs == 1)
            retValue.add(HandRanking.ONE_PAIR);

        return Collections.max(retValue);
    }

    public boolean handRankingGreaterThan(Player otherPlayer){
        HandRanking thisHandRanking = whatHandRanking();
        HandRanking otherRanking = otherPlayer.whatHandRanking();
        if (thisHandRanking.value > otherRanking.value) return true;
        else if (thisHandRanking.value == otherRanking.value){
            return this.cards_[0].is_greater_than(otherPlayer.cards_[0]);
        }
        else return false;
    }
}
