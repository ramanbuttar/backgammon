public class AlphaBetaBackgammonAgent extends BackgammonAgent {
    private Evaluation evaluation;

    public AlphaBetaBackgammonAgent(int my_player_num) {
	super(my_player_num);
	evaluation = new NaiveEvaluation();
    }

    public AlphaBetaBackgammonAgent(int my_player_num, Evaluation e) {
	super(my_player_num);
	evaluation = e;
    }

    public Move chooseMove(BackgammonBoard b) {
	/* Fill in this code. */
	return null;
    }
}