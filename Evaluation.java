/* An Evaluation must provide a method for scoring a board for a given
 * player, and max/min bounds on this score. */
public abstract class Evaluation {
    abstract float boardScore(BackgammonBoard b, int player);
	
    protected float MAX_SCORE;
    protected float MIN_SCORE;

    public float getMaxScore() {
	return MAX_SCORE;
    }

    public float getMinScore() {
	return MIN_SCORE;
    }
}