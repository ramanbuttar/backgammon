public class BackgammonEvaluation2 extends Evaluation {
	
	public BackgammonEvaluation2() {}

	public float boardScore(BackgammonBoard b, int player) {
		int otherPlayer = 1 - player;
		boolean block = false;
		
		int value = getBoardState(b, player);
		value -= checkMySingles(b, player);
		
		if (b.on_bar[otherPlayer] > 0) {
			value += blockPlayer(b, player);
			block = true;
		}
		
		if (value <= 2 * BackgammonBoard.NUM_POINTS * BackgammonBoard.NUM_POINTS && !block) {
			value += rewardPriming(b, player);
		}
		
		
		return (float) value;
	}
	
	public float boardScore2(BackgammonBoard b, int player) {
		float value = 0;
		
		for (int i = 0; i < BackgammonBoard.NUM_POINTS; i++) {
			if (player == 1) {
				if (b.board[i] == 1) {
					value--;
				} else if (b.board[i] == 2) {
					if (i <= 6) {
						value += 8;
					} else {
						value += 6;
					}
				} else if (b.board[i] > 2) {
					if (i <= 6) {
						value += 4;
					} else {
						value += 2;
					}
				}
			} else if (player == 0){
				if (b.board[i] == -1) {
					value--;
				} else if (b.board[i] == -2) {
					if (i >= 18) {
						value += 8;
					} else {
						value += 6;
					}
				} else if (b.board[i] < -2) {
					if (i >= 18) {
						value += 4;
					} else {
						value += 2;
					}
				}
			}
		}
		
		value += b.in_home[player];
		value -= b.on_bar[player];
		
		return (float) value * BackgammonBoard.NUM_POINTS;
	}
	
	private int getBoardState(BackgammonBoard b, int player) {
		int otherPlayer = 1 - player;
		int value = 0;
		
		/*for (int i = 0; i < BackgammonBoard.NUM_POINTS; i++) {
			if (b.board[i] > 0) {				
				value += (player * b.board[i] * i) - (otherPlayer * b.board[i] * i);
			} else {		
				value -= (player * b.board[i] * (BackgammonBoard.NUM_POINTS - i - 1))
						+ (otherPlayer * b.board[i] * (BackgammonBoard.NUM_POINTS - i - 1));
			}
		}*/
		value += b.in_home[player] * BackgammonBoard.NUM_POINTS;
		value -= b.in_home[otherPlayer] * BackgammonBoard.NUM_POINTS;
		value -= b.on_bar[player] * BackgammonBoard.NUM_POINTS;
		value += b.on_bar[otherPlayer] * BackgammonBoard.NUM_POINTS;
		return value;
	}

	private int checkMySingles(BackgammonBoard b, int player) {
		int singles = 0;
		for (int i = 0; i < BackgammonBoard.NUM_POINTS; i++) {
			if (player == 1 && b.board[i] == 1) {
				singles++;
			} else if (player == 0 && b.board[i] == -1) {
				singles++;
			}
		}
		return singles;
	}

	private int rewardPriming(BackgammonBoard b, int player) {
		int otherPlayer = 1 - player;
		int value = 0;

		//Player 0: 13 to 23
		//Player 1: 1 to 11

		for (int i = (player + 13 * otherPlayer); i < (11 * player + 23 * otherPlayer); i++) {
			/*if (b.board[i] >= 2 || (b.board[i] <= -2)) {
				value += ((11 - i) * b.board[i] * player) - (i * b.board[i] * otherPlayer);
			}*/
			if (player == 0 && b.board[player] <= -2) {
				value += i * b.board[i] * -1;
			} else if (player == 1 && b.board[player] >= 2) {
				value += i * b.board[i];
			}
		}
		return value * BackgammonBoard.NUM_POINTS;
	}
	
	private int blockPlayer(BackgammonBoard b, int player) {
		int otherPlayer = 1 - player;
		
		int i = otherPlayer * (BackgammonBoard.NUM_POINTS - 1);
		int j = 0;
		
		while ((b.board[i] * player + player - 1) > (player + b.board[i] * otherPlayer) && (j < 7)) {
			j++;
			i += 2 * player - 1;
		}
		return j * BackgammonBoard.NUM_POINTS;
	}

}
