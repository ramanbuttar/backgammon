/* A backgammon agent that just randomly chooses a possible move.
 */
import java.util.Random;

public class RandomBackgammonAgent extends BackgammonAgent {
    private Random generator;

    public RandomBackgammonAgent(int my_player_num) {
	super(my_player_num);

	generator = new Random();
    }
	
    public Move chooseMove(BackgammonBoard b) {
	MoveSet ms = b.getValidMoves(player_num);
	if (ms.getSize() > 0) {
	    return ms.getMove(generator.nextInt(ms.getSize()));
	} else {
	    return null;
	}
    }
}
