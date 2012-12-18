import java.io.*;

public class BackgammonGame {
	
    BackgammonAgent agent0, agent1;
    BackgammonBoard board;
    Dice d;	
    PrintStream out_stream;

    // Number of moves taken by each agent.
    int moves0, moves1;
    // Time taken by each agent, in milliseconds.
    long time0, time1;
	
    BackgammonGame() {
	agent0 = new SimpleBackgammonAgent(0);
	agent1 = new SimpleBackgammonAgent(1);

	out_stream = System.out;
	board = new BackgammonBoard();
	d = new Dice();
    }

    BackgammonGame(BackgammonAgent a0, BackgammonAgent a1, PrintStream ps) {
	agent0 = a0;
	agent1 = a1;

	a0.player_num = 0;
	a1.player_num = 1;

	out_stream = ps;
	board = new BackgammonBoard(out_stream);
	d = new Dice();
    }

    // After creating a BackgammonGame, run this method to play it.
    public int playGame() {
	Move m;
	int winner, turn;
	long s_time;

	moves0 = 0;
	moves1 = 0;
	time0 = 0;
	time1 = 0;
		
	// Show the initial status of the board.
	if (board.verbose)
		board.showBoard();
		
	winner = -1;
	turn = 0;
		
	while (winner == -1) {
			
	    // Roll the dice.
	    d.toss();
	    if (board.verbose)
	    	out_stream.println("The Toss: " + d.dc1 + " " + d.dc2);	
			
	    board.dc1 = d.dc1;
	    board.dc2 = d.dc2;
			
	    // Get an agent to make a move.
	    BackgammonBoard b_copy = (BackgammonBoard) board.clone();
	    b_copy.verbose = false;
	    if (turn == 0) {
		s_time = System.currentTimeMillis();
		m = agent0.chooseMove(b_copy);
		time0 = time0 + System.currentTimeMillis()-s_time;
		moves0++;
	    } else {
		s_time = System.currentTimeMillis();
		m = agent1.chooseMove(b_copy);
		time1 = time1 + System.currentTimeMillis()-s_time;
		moves1++;
	    }
			
	    // Check that m is a valid move.
	    MoveSet valid_moves = board.getValidMoves(turn);
	    if (!valid_moves.containsMove(m)) {
		board.applyMove(turn, m);
		if (board.verbose)
			board.showBoard();
		out_stream.println("Invalid move by Agent" + turn);
		winner = (++turn) % 2;
	    }
		    

	    // Apply the move to the board.
	    board.applyMove(turn, m);
			
	    // Check to see if the player won.
	    if (board.hasWon(turn))
		winner = turn;
		
	    if (board.verbose)
	    	board.showBoard();
			
	    // Take turns.
	    turn = (++turn) % 2;
	}
	
	if (board.verbose) {
		out_stream.println("Agent"+winner+" wins");
		out_stream.println("Agent0 (X) made " + moves0 + " moves in " + time0/1000F + " seconds, " + (time0/1000F)/moves0 + " seconds per move");
		out_stream.println("Agent1 (O) made " + moves1 + " moves in " + time1/1000F + " seconds, " + (time1/1000F)/moves1 + " seconds per move");
	}
	return winner;
    }
	
	
}
