package pl.edu.agh.kis.pz1;

import java.util.Objects;

public class Card {
    //zmienna przechowująca wartość karty
    Rank rank_;

    //zmienna przechowująca symbol karty
    Suit suit_;

    /*
    * konstruktor tworzący kartę na podstawie podanych pól enum
    *
    * @param  rank odpowiednia nazwa pola typu wyliczeniowego Rank
    * @param  suit odpowiednia nazwa pola typu wyliczeniowego Suit
    * @return      obiekt typu Card
    * */
    public Card(Rank rank, Suit suit){
        rank_ = rank;
        suit_ = suit;
    }

    //konstruktor tworzący kartę na podstawie podanych liczb
    public Card(int rank, int suit){
        switch (rank) {
            case 0 -> rank_ = Rank.TWO;
            case 1 -> rank_ = Rank.THREE;
            case 2 -> rank_ = Rank.FOUR;
            case 3 -> rank_ = Rank.FIVE;
            case 4 -> rank_ = Rank.SIX;
            case 5 -> rank_ = Rank.SEVEN;
            case 6 -> rank_ = Rank.EIGHT;
            case 7 -> rank_ = Rank.NINE;
            case 8 -> rank_ = Rank.TEN;
            case 9 -> rank_ = Rank.JACK;
            case 10 -> rank_ = Rank.QUEEN;
            case 11 -> rank_ = Rank.KING;
            case 12 -> rank_ = Rank.ACE;
            default -> throw new IllegalArgumentException("No Card rank provided");
        }
        switch (suit) {
            case 0 -> suit_ = Suit.SPADES;
            case 1 -> suit_ = Suit.HEARTS;
            case 2 -> suit_ = Suit.CLUBS;
            case 3 -> suit_ = Suit.DIAMONDS;
            default -> throw new IllegalArgumentException("No Card  provided");
        }
    }

    //funkcja porównująca dwie karty
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank_ == card.rank_ && suit_ == card.suit_;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank_, suit_);
    }

    //funkcja pomocnicza sprawdzająca czy dana karta jest większa od podanej
    public boolean is_greater_than(Card other){
        if (this.rank_.num_val > other.rank_.num_val) return true;
        else if (this.rank_.num_val == other.rank_.num_val){
            return this.suit_.num_val > other.suit_.num_val;
        }
        else return false;
    }
}
