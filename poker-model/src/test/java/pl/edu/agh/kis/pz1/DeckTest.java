package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    @Test
    void sort() {
        Deck dToSort = new Deck();
        Deck dSorted = new Deck();
        dToSort.shuffle();

        assertNotEquals(dSorted, dToSort);
        dToSort.sort();
        assertEquals(dSorted, dToSort);
    }

    @Test
    void testShuffle() {
        Deck dToShuffle = new Deck();
        Deck dSorted = new Deck();

        assertEquals(dSorted, dToShuffle);
        dToShuffle.shuffle();
        assertNotEquals(dSorted, dToShuffle);
    }

    @Test
    void testEquals() {
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();

        assertEquals(deck1, deck2);
    }

    @Test
    void testHashCode() {
        Deck deck = new Deck();
        assertEquals(deck.hashCode(), Arrays.hashCode(deck.deck_));
    }
}