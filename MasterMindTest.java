package mastermind;

import java.util.*;
import mastermind.MasterMind.Result;
import mastermind.MasterMind.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MasterMindTest {

    private MasterMind mastermind;

    @BeforeEach
    public void init() {
        mastermind = new MasterMind();
    }

    @Test
    void canary(){
        assert(true);
    }

    @Test
    void playerMakesIncorrectGuess() {
        assertEquals(Map.of(
            Result.INPOSITION, 0,
            Result.MATCH, 0,
            Result.NOMATCH, 6),
            mastermind.guess(List.of(0, 0, 0, 0, 0, 0)));
    }

    @Test
    void playerMakesCorrectGuess(){
        assertEquals(Map.of(
            Result.INPOSITION, 6,
            Result.MATCH, 0,
            Result.NOMATCH, 0),
            mastermind.guess(List.of(1, 2, 3, 4, 5, 6)));
    }

    @Test
    void playerMakesOneColorNonpositionalMatch(){
        assertEquals(Map.of(
            Result.INPOSITION, 0,
            Result.MATCH, 1,
            Result.NOMATCH, 5),
            mastermind.guess(List.of(2, 0, 0, 0, 0, 0)));
    }

    @Test
    void playerMakesTwoColorNonpositionalMatch(){
        assertEquals(Map.of(
            Result.INPOSITION, 0,
            Result.MATCH, 2,
            Result.NOMATCH, 4),
            mastermind.guess(List.of(2, 3, 0, 0, 0, 0)));
    }

    @Test
    void playerMakesOneColorPositionalMatchOneNonPositionalMatch(){
        assertEquals(Map.of(
            Result.INPOSITION, 1,
            Result.MATCH, 1,
            Result.NOMATCH, 4),
            mastermind.guess(List.of(1, 3, 0, 0, 0, 0)));
    }

    @Test
    void playerMakesTwoSameMatches(){
        assertEquals(Map.of(
            Result.INPOSITION, 1,
            Result.MATCH, 0,
            Result.NOMATCH, 5),
            mastermind.guess(List.of(1, 1, 0, 0, 0, 0)));
    }

    @Test
    void playerMakesThreeCorrectMatches(){
        assertEquals(Map.of(
            Result.INPOSITION, 3,
            Result.MATCH, 0,
            Result.NOMATCH, 3),
            mastermind.guess(List.of(1, 2, 3, 0, 0, 0)));
    }

    @Test
    void playerMakesFirstRightSecondWrongMatchOfSameColor(){
        assertEquals(Map.of(
            Result.INPOSITION, 1,
            Result.MATCH, 0,
            Result.NOMATCH, 5),
            mastermind.guess(List.of(2, 2, 0, 0, 0, 0)));
    }
            
    @Test
    void gameStartsInProgress(){
        assertEquals(GameStatus.INPROGRESS, mastermind.getGameStatus());
    }

    @Test
    void playerMakesCorrectGuessAndWins(){
        mastermind.guess(List.of(1, 2, 3, 4, 5, 6));
        assertEquals(GameStatus.WON, mastermind.getGameStatus());
    }

    @Test
    void playerMakesAnIncorrectGuessAndDoesNotWin(){
        mastermind.guess(List.of(1, 2, 3, 4, 5, 0));
        assertEquals(GameStatus.INPROGRESS, mastermind.getGameStatus());
    }

    @Test
    void playerRunsOutOfTurns(){
        mastermind.round = 19;
        mastermind.guess(List.of(1, 2, 3, 4, 5, 0));
        assertEquals(GameStatus.LOST, mastermind.getGameStatus());
    }

    @Test
    void lostStaysLost(){
        mastermind.round = 19;
        mastermind.guess(List.of(1, 2, 3, 4, 5, 0));
        mastermind.guess(List.of(1, 2, 3, 4, 5, 6));
        assertEquals(GameStatus.LOST,mastermind.getGameStatus());
    }

    @Test
    void wonStaysWon(){
        mastermind.guess(List.of(1, 2, 3, 4, 5, 6));
        mastermind.guess(List.of(1, 2, 3, 4, 5, 0));
        assertEquals(GameStatus.WON, mastermind.getGameStatus());
    }


    @Test
    void randomlySelectedIndicesHaveSixValues() {
      assertEquals(6, mastermind.createRandomColorIndicies(10, 6, 1).size());
    }

    @Test
    void randomlySelectedIndicesHaveDistinctValues() {
      List<Integer> selectedIndices = mastermind.createRandomColorIndicies(10, 6, 1);
      assertEquals(6, new HashSet<>(selectedIndices).size());
    }
    
    @Test
    void randomlySelectedIndicesAreSameForSameSeed() {
      assertEquals(
        mastermind.createRandomColorIndicies(10, 6, 1),
        mastermind.createRandomColorIndicies(10, 6, 1)
      );
    }

    @Test
    void randomlySelectedIndicesAreDifferentForDifferentSeed() {
      assertNotEquals(
        mastermind.createRandomColorIndicies(10, 6, 1),
        mastermind.createRandomColorIndicies(10, 6, 2)
      );
    }

    @Test
    void selectedValuesNotNegative() {
      assertEquals(0, mastermind.createRandomColorIndicies(10, 6, 1)
        .stream()
        .filter(value -> value < 0)
        .count());
    }
}
