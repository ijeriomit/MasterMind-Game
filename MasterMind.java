package mastermind;

import java.util.*;
import static java.util.stream.Collectors.toList;

public class MasterMind {
    public enum Result{ INPOSITION, MATCH, NOMATCH }
    public enum GameStatus { INPROGRESS, WON, LOST }

    private List<Integer> selectedColorIndices;
    int round = 0;

    private GameStatus gameStatus = GameStatus.INPROGRESS;
    public MasterMind(){setSelectedColorIndices(List.of(1, 2, 3, 4, 5, 6));}
    public void setSelectedColorIndices(List<Integer> colorIndices) {
       selectedColorIndices = colorIndices;
    }

    public Map<Result, Integer> guess(List<Integer> guessIndices) {
        int inposition = 0;
        int match = 0;

        for(int i = 0; i < selectedColorIndices.size(); i++) {
            if(selectedColorIndices.get(i).equals(guessIndices.get(i))) {
            inposition++;
            continue;
          }

          if(guessIndices.contains(selectedColorIndices.get(i))) {
              match++;
          }
        }  

        updateGameStatus(inposition);
        
        return Map.of(
          Result.INPOSITION, inposition,
          Result.MATCH, match,
          Result.NOMATCH, selectedColorIndices.size() - inposition - match);
    }

    public GameStatus getGameStatus() { return gameStatus; }

    public List<Integer> createRandomColorIndicies(int poolSize, int size, int randomSeed){

       return new Random(randomSeed)
         .ints(0, poolSize)
         .distinct()
         .boxed()
         .limit(size)
         .collect(toList());
    }

    private void updateGameStatus(int inposition) {
        if (gameStatus != GameStatus.INPROGRESS) return;

        round++;

        if (round == 20) gameStatus = GameStatus.LOST;

        if (inposition == 6) gameStatus = GameStatus.WON;
    }
}