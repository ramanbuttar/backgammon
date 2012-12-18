/* A backgammon agent that just takes the first possible move. 
 * Modify as you see fit.
 */
public class TestBackgammonAgent extends BackgammonAgent {
    NaiveEvaluation nh;
    public TestBackgammonAgent(int my_player_num) {
	super(my_player_num);

	nh = new NaiveEvaluation();
    }
	
    public Move chooseMove(BackgammonBoard b) {
	System.out.println("BS=" + nh.boardScore(b,player_num) + " out of " + nh.getMaxScore());

	MoveSet ms = b.getValidMoves(player_num);
	return ms.getMove(0);
    }
}
