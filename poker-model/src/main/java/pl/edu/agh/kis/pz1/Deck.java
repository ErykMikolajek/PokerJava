package pl.edu.agh.kis.pz1;

import java.util.Arrays;


/**
*   Klasa przechowująca talie kart, zbiór obiektów Card.
*   Domyślnie tworzona jest z 52 posortowanymi kartami
*
 */
public class Deck {
    /**
     * tablica przechowująca talię kart
     */
    Card[] deck_;

    /**
     * kostruktor tworzący talię kart z 52 kartami
     */
    public Deck(){
        deck_ = new Card[52];
        int index = 0;
        for (int i = 0; i < 13; i++){
            for (int j = 0; j < 4; j++){
                deck_[index++] = new Card(i, j);
            }
        }
    }

    /**
     * funkcja wypisująca karty w talii na ekran
     */
    public void print(){
        for (Card card : deck_)
            System.out.print(card.rank_ + " of " + card.suit_ + ",\n");
    }

    /**
     * funkcja porządkująca talie
     */
    public void sort(){
        for (int j = 1; j < 52; j++) {
            Card key = this.deck_[j];
            int i = j - 1;
            while ((i > -1) && (this.deck_[i].is_greater_than(key))) {
                this.deck_[i + 1] = this.deck_[i];
                i--;
            }
            this.deck_[i+1] = key;
        }
    }

    /**
     * funkcja mieszająca talie obiekt
     */
    public void shuffle(){
        for (int i = 0; i < 52; i++){
            int swap_pos = (int)(Math.random() * 51);
            Card temp = this.deck_[swap_pos];
            this.deck_[swap_pos] = this.deck_[i];
            this.deck_[i] = temp;
        }
    }

    /**
     * funkcja wydająca podaną liczbę kart dla gracza
     */
    public Card[] dealCards(int numOfCards){
        Card[] returnCards = new Card[numOfCards];
        for (int i = 0; i < numOfCards; i++){
            int temp = (int)(Math.random() * 51);
            while (this.deck_[temp] == null)
                temp = (int)(Math.random() * 51);
            returnCards[i] = this.deck_[temp];
            this.deleteCard(temp);
        }
        return returnCards;
    }

    private void deleteCard(int indexOfCard){
        this.deck_[indexOfCard] = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return Arrays.equals(deck_, deck.deck_);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(deck_);
    }

}
