package pl.edu.agh.kis.pz1;

import jdk.jshell.Snippet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void sortCards() {
        Card[] testCards = {new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.TWO, Suit.DIAMONDS),
                new Card(Rank.NINE, Suit.CLUBS),
                new Card(Rank.KING, Suit.DIAMONDS)};
        Card[] sortedCards = {new Card(Rank.TWO, Suit.DIAMONDS),
                new Card(Rank.NINE, Suit.CLUBS),
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.KING, Suit.DIAMONDS)};
        Player testPlayer = new Player(testCards);
        testPlayer.sortCards();

        assertArrayEquals(sortedCards, testPlayer.cards_);

        Card[] testCards2 = {new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.TWO, Suit.DIAMONDS)};
        Card[] sortedCards2 = {new Card(Rank.TWO, Suit.DIAMONDS),
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.QUEEN, Suit.SPADES)};
        Player testPlayer2 = new Player(testCards2);
        testPlayer2.sortCards();

        assertArrayEquals(sortedCards2, testPlayer2.cards_);
    }

    @Test
    void whatHandRanking() {
        // royal flush test
        Card[] test_cards = {new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.HEARTS), new Card(Rank.JACK, Suit.HEARTS), new Card(Rank.TEN, Suit.HEARTS)};
        Player player = new Player(test_cards);
        assertEquals(HandRanking.ROYAL_FLUSH, player.whatHandRanking());

        // straight flush test
        test_cards = new Card[]{new Card(Rank.KING, Suit.HEARTS), new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.JACK, Suit.HEARTS), new Card(Rank.TEN, Suit.HEARTS), new Card(Rank.NINE, Suit.HEARTS)};
        player = new Player(test_cards);
        assertEquals(HandRanking.STRAIGHT_FLUSH, player.whatHandRanking());

        // four od a kind test
        test_cards = new Card[]{new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.KING, Suit.HEARTS), new Card(Rank.ACE, Suit.SPADES)};
        player = new Player(test_cards);
        assertEquals(HandRanking.FOUR_OF_A_KIND, player.whatHandRanking());

        // full house test
        test_cards = new Card[]{new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.KING, Suit.DIAMONDS),
                new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.KING, Suit.HEARTS), new Card(Rank.ACE, Suit.SPADES)};
        player = new Player(test_cards);
        assertEquals(HandRanking.FULL_HOUSE, player.whatHandRanking());

        // flush test
        test_cards = new Card[]{new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.HEARTS), new Card(Rank.JACK, Suit.HEARTS), new Card(Rank.NINE, Suit.HEARTS)};
        player = new Player(test_cards);
        assertEquals(HandRanking.FLUSH, player.whatHandRanking());

        // straight test
        test_cards = new Card[]{new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.KING, Suit.DIAMONDS),
                new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.JACK, Suit.SPADES), new Card(Rank.TEN, Suit.HEARTS)};
        player = new Player(test_cards);
        assertEquals(HandRanking.STRAIGHT, player.whatHandRanking());

        // three od a kind test
        test_cards = new Card[]{new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.KING, Suit.HEARTS), new Card(Rank.JACK, Suit.SPADES)};
        player = new Player(test_cards);
        assertEquals(HandRanking.THREE_OF_A_KIND, player.whatHandRanking());

        // two pair test
        test_cards = new Card[]{new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.KING, Suit.DIAMONDS), new Card(Rank.KING, Suit.HEARTS), new Card(Rank.JACK, Suit.SPADES)};
        player = new Player(test_cards);
        assertEquals(HandRanking.TWO_PAIR, player.whatHandRanking());

        // one pair test
        test_cards = new Card[]{new Card(Rank.KING, Suit.CLUBS), new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.JACK, Suit.SPADES), new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.NINE, Suit.DIAMONDS)};
        player = new Player(test_cards);
        assertEquals(HandRanking.ONE_PAIR, player.whatHandRanking());

        // high card test
        test_cards = new Card[]{new Card(Rank.KING, Suit.HEARTS), new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.JACK, Suit.HEARTS), new Card(Rank.TWO, Suit.HEARTS), new Card(Rank.NINE, Suit.DIAMONDS)};
        player = new Player(test_cards);
        assertEquals(HandRanking.HIGH_CARD, player.whatHandRanking());

    }

    @Test
    void showCards() {
        Card[] test_cards = {new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.KING, Suit.HEARTS), new Card(Rank.JACK, Suit.SPADES)};
        Player player = new Player(test_cards);

        String playerCards = player.show_cards();
        String realCards = "1: ACE of HEARTS, 2: ACE of CLUBS, 3: ACE of DIAMONDS, 4: KING of HEARTS, 5: JACK of SPADES";
        assertEquals(realCards, playerCards);
    }

}