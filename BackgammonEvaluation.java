public class BackgammonEvaluation extends Evaluation {
	
	public BackgammonEvaluation() {
		this.MAX_SCORE = BackgammonBoard.NUM_POINTS * BackgammonBoard.NUM_POINTS * BackgammonBoard.NUM_POINTS;
		this.MIN_SCORE = 0;
	}

	public float boardScore(BackgammonBoard b, int player) {
		
		int otherPlayer = 1 - player;
		
		int value = blotState(b, player);
			
		if ((b.on_bar[otherPlayer] > 0) || (worthBlocking(b, player))) {
			value += goAfterSingles(b, player);
			value += rewardPriming(b, player);
		}
			value += runHome(b, player);
			//value += b.in_home[player] * BackgammonBoard.NUM_POINTS * BackgammonBoard.NUM_POINTS;
		return (float) value;
	}
	
	private int blotState(BackgammonBoard b, int player) {
		int otherPlayer = 1 - player;
		int value = 0;
		
		for (int i = 0; i < BackgammonBoard.NUM_POINTS; i++) {
			if (player == 0) {
				if (b.board[i] == -1) {
					value -= (BackgammonBoard.NUM_POINTS - i) * BackgammonBoard.NUM_POINTS;
				} else {
					value += b.board[i] * -1 * (BackgammonBoard.NUM_POINTS - i) * BackgammonBoard.NUM_POINTS;
				}
			} else if (player == 1) {
				if (b.board[i] == 1) {
					value -= i * BackgammonBoard.NUM_POINTS;
				} else {
					value += b.board[i] * i * BackgammonBoard.NUM_POINTS;
				}
			}
		}
		
		value += b.in_home[player] * BackgammonBoard.NUM_POINTS;
		value -= b.in_home[otherPlayer] * BackgammonBoard.NUM_POINTS;
		value -= b.on_bar[player] * BackgammonBoard.NUM_POINTS;
		value += b.on_bar[otherPlayer] * BackgammonBoard.NUM_POINTS;
		
		return value;
	}
	
	private int rewardPriming(BackgammonBoard b, int player) {
		int value = 0;

		for (int i = 0; i < BackgammonBoard.NUM_POINTS; i++) {
			if (player == 0) {
				if (b.board[i] <= -2) {
					value += BackgammonBoard.NUM_POINTS - i - ((b.board[i] * -1) - 2);
					if ((i > 1) && (b.board[i-1] <= -2)) {
						value += BackgammonBoard.NUM_POINTS - i - ((b.board[i] * -1) - 2);
					}
				}
			} else if (player == 1) {
				if (b.board[i] >= 2) {
					value += i - (b.board[i] - 2);
					if ((i < (BackgammonBoard.NUM_POINTS-1)) && (b.board[i+1] >= 2)) {
						value += i - (b.board[i] - 2);
					}
				}
			}
		}
		return value *  BackgammonBoard.NUM_POINTS;
	}
	
	private boolean worthBlocking(BackgammonBoard b, int player) {
		if (player == 0) {
			for (int i = 0; i <= 6; i++) {
				if (b.board[i] > 0) {
					return true;
				}
			}
		} else if (player == 1) {
			for (int i = BackgammonBoard.NUM_POINTS - 6; i < BackgammonBoard.NUM_POINTS; i++) {
				if (b.board[i] < 0) {
					return true;
				}
			}
		}
		return true;
	}
	
	private int runHome(BackgammonBoard b, int player) {
		int value = 0;
		if (player == 0) {
			for (int i=6; i<BackgammonBoard.NUM_POINTS; i++) {
				if (b.board[i] < 0) {
					value += b.board[i] * -1 * (BackgammonBoard.NUM_POINTS - i) * BackgammonBoard.NUM_POINTS;
					//value += b.board[i] * -1 * i * BackgammonBoard.NUM_POINTS;
				}
				if (b.board[i] == -1) {
					value += b.board[i] * -1 * (BackgammonBoard.NUM_POINTS - i) * BackgammonBoard.NUM_POINTS;
					//value += b.board[i] * -1 * i * BackgammonBoard.NUM_POINTS;
				}
			}
		} else if (player == 1) {
			for (int i=0; i<BackgammonBoard.NUM_POINTS-6; i++) {
				if (b.board[i] > 0) {
					//value += b.board[i] * i * BackgammonBoard.NUM_POINTS;
					value += b.board[i] * (BackgammonBoard.NUM_POINTS - i) * BackgammonBoard.NUM_POINTS;
				}
				if (b.board[i] == 1) {
					//value += b.board[i] * i * BackgammonBoard.NUM_POINTS;
					value += b.board[i] * (BackgammonBoard.NUM_POINTS - i) * BackgammonBoard.NUM_POINTS;
				}
			}
		}
		return value;
	}
	
	private int goAfterSingles(BackgammonBoard b, int player){
		int value = 0;
		for (int i=0; i<BackgammonBoard.NUM_POINTS; i++) {
			if (player == 0) {
				if (b.board[i] == 1) {
					value += BackgammonBoard.NUM_POINTS - i;
				}
			} else if (player == 1) {
				if (b.board[i] == -1) {
					value += i;
				}
			}
			
		}
		
		return value * BackgammonBoard.NUM_POINTS;
	}
}
