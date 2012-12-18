/* A very naive evaluation function.
 * Returns the number of tiles in the player's home.
 * Max is 15, min is 0.
 */
public class NaiveEvaluation extends Evaluation {
    NaiveEvaluation() {
	MAX_SCORE = 15;
	MIN_SCORE = 0;
    }

    public float boardScore(BackgammonBoard b, int player) {
	return (float) b.in_home[player];
    }
}