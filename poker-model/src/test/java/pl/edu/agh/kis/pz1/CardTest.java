package pl.edu.agh.kis.pz1;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @org.junit.jupiter.api.Test
    void testEquals() {
        Card ace_spades = new Card(Rank.ACE, Suit.SPADES);
        Card ace_spades2 = new Card(Rank.ACE, Suit.SPADES);
        Card two_diamonds = new Card(Rank.TWO, Suit.DIAMONDS);
        Card ace_clubs = new Card(Rank.ACE, Suit.CLUBS);

        assertEquals(ace_spades, ace_spades);
        assertEquals(two_diamonds, two_diamonds);
        assertEquals(ace_spades, ace_spades2);
        assertNotEquals(ace_clubs, two_diamonds);
    }

    @org.junit.jupiter.api.Test
    void testHashCode() {
        Card ace_spades = new Card(Rank.ACE, Suit.SPADES);
        Card two_diamonds = new Card(Rank.TWO, Suit.DIAMONDS);

        assertEquals(ace_spades.hashCode(), Objects.hash(Rank.ACE, Suit.SPADES));
        assertEquals(two_diamonds.hashCode(), Objects.hash(Rank.TWO, Suit.DIAMONDS));
    }

    @org.junit.jupiter.api.Test
    void testIs_greater_than() {
        Card ace_spades = new Card(Rank.ACE, Suit.SPADES);
        Card ace_spades2 = new Card(Rank.ACE, Suit.SPADES);
        Card two_diamonds = new Card(Rank.TWO, Suit.DIAMONDS);
        Card ace_clubs = new Card(Rank.ACE, Suit.CLUBS);

        assertTrue(ace_spades.is_greater_than(two_diamonds));
        assertTrue(ace_clubs.is_greater_than(ace_spades));
        assertFalse(two_diamonds.is_greater_than(ace_clubs));
        assertFalse(ace_spades.is_greater_than(ace_spades2));
    }
}