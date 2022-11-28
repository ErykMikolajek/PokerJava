package pl.edu.agh.kis.pz1;

public enum Suit {
    SPADES(1), HEARTS(2), CLUBS(3), DIAMONDS(4);

    public final int num_val;
    private Suit(int num_val){
        this.num_val = num_val;
    }
}
