package pl.edu.agh.kis.pz1;

public enum Rank {
        TWO(1),
        THREE(2),
        FOUR(3),
        FIVE(4),
        SIX(5),
        SEVEN(6),
        EIGHT(7),
        NINE(8),
        TEN(9),
        JACK(10),
        QUEEN(11),
        KING(12),
        ACE(13);

        public final int num_val;
        private Rank(int num_val){
                this.num_val = num_val;
        }

}
