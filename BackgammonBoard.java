import java.util.*;
import java.io.*;

public class BackgammonBoard implements Cloneable {
    // Each cell in our model is free, or has a checker from player 0 or
    // has a checker from player 1
    // (represented respectively by 0, negative, positive)
    
    // Player 0 is negative counts and moves in negative direction of board index.
    // Player 1 is opposite.
    int[] board;

    // Checkers on bar (that have been hit).  on_bar[0] is first player, on_bar[1] is second player.
    int[] on_bar;
    // Checkers in home.  in_home[0] is first player, in_home[1] is second player.
    int[] in_home;
    
    // The current dice rolls.
    int dc1;
    int dc2;
    
    // Print lots 'o stuff or not.
    boolean verbose;
    
    // Where to print the output.
    PrintStream out_stream;

    public static int CHECKERS_PER_PLAYER = 15;
    public static int NUM_POINTS = 24;
    public static int BAR_LOC0 = NUM_POINTS;
    public static int BAR_LOC1 = -1;
    // Set use_doubles to false if you would like to turn off the quadruple moves for testing.
    public static boolean use_doubles = true;
    protected Object clone() {
        BackgammonBoard b = null;
        try {
            b = (BackgammonBoard) super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("I caught it");
        }
        b.board = (int[])board.clone();
        b.on_bar = (int[])on_bar.clone();
        b.in_home = (int[])in_home.clone();
        return b;
    }
    
    public BackgammonBoard(PrintStream ps) {
	out_stream = ps;
	initialize();
    }

    public BackgammonBoard() {
	out_stream = System.out;
        initialize();
    }
    
    private void initialize() {
        // Allocating space for the array cells
        board = new int[NUM_POINTS];
        on_bar = new int[2];
        in_home = new int[2];
        
        verbose = true;
        
        // Initializing the cells to the "free" state (i.e. ZERO)
        for (int i = 0; i < NUM_POINTS; i = i + 1)
            board[i] = 0;

	// Initilizing the checkers for player 0
	board[5]  = -5;
	board[7]  = -3;
	board[12] = -5;
	board[23] = -2;
        
	// Initilizing the checkers for player 1
	board[0]  = 2;
	board[11] = 5;
	board[16] = 3;
	board[18] = 5;

        
        on_bar[0] = 0;
        on_bar[1] = 0;
        in_home[0] = 0;
        in_home[1] = 0;
    }

    // Apply Move m for player to the board.
    public void applyMove(int player, Move m)
    {
	if (m != null) {
	    if (player==0)
		m.sortDescend();
	    else
		m.sortAscend();

	    Iterator iter = m.getAtomicMoves();

	    try {
		while (iter.hasNext()) {
		    AtomicMove am = (AtomicMove) iter.next();
		    if (verbose) {
			out_stream.println("Agent"+ player +" selects move:  (" + am.source_column + "," + am.dest_column + ") (source, dest)");
		    }
		    applyAtomicMove(player,am);
		}
	    } catch (NoSuchElementException e) {
		System.err.println("Error in applying move for player " + player);
	    }
	}
    }
    
    private void applyAtomicMove(int player, AtomicMove am) {
        pickChecker(player, am.source_column);
        putChecker(player, am.dest_column);
    }
    
    // Remove a checker from a location.
    private void pickChecker(int player, int column) {
        if ((column < -1) || (column > NUM_POINTS)) {
            System.err.println("Error, out of range.");
        } else {
            // column -1 means the table!
            // So if a checker is transfered from the column -1, it is simply put in the game.
            if (column == BAR_LOC1 || column == BAR_LOC0) {
                if (on_bar[player] < 1)
                    System.err.println("Error, no pieces on bar.");
                else
                    on_bar[player]--;
            } else {
                int p_type = pType(player);
                if (((p_type) * board[column]) >= p_type) {
                    board[column] -= p_type;
                } else {
                    System.err.println("Error, no pieces at this location.");
                }
            }
        }
        
    }

    // Wrapper to get count type (-1/+1) from player number (0/1)
    public int pType(int player) {
        return player==0 ? -1 : 1;
    }
    

    // Place one of player's checkers at location column.
    // Handles hitting of blots, prints error if this is an invalid move (should never happen).
    private void putChecker(int player, int column) {
        if ((column < -1) || (column > NUM_POINTS)) {
            System.err.println("Error, out of range.");
        } else {
            int p_type = pType(player);
            if (column==-1 || column==NUM_POINTS) {
                in_home[player]++;
            } else if (p_type * board[column] >= 0) {
                board[column] += p_type;
            } else if (board[column] == -p_type) {
                board[column] = p_type;
                on_bar[((player+1) % 2)]++;
            } else {
                System.err.println("Error, too many opponent pieces at this locations.");
            }
        }
        
    }

    // Get a set of valid moves for a player, assuming it's his/her
    // turn, given the current dice roll.
    public MoveSet getValidMoves(int player) {
        Move empty_move = new Move();
	MoveSet rtn_mset = null;
        int[] dice;
        int dice_left;
        if ((dc1==dc2)&&(use_doubles)) {
            dice_left = 4;
            dice = new int[4];
            dice[0]=dc1;
            dice[1]=dc1;
            dice[2]=dc1;
            dice[3]=dc1;

	    int s_pos = player==1 ? 0 : NUM_POINTS-1;
	    rtn_mset = getValidMovesDoublesRec(empty_move, player, dice_left, dice, s_pos);
	    // Doubles version does not guarrantee maximum number of dice is used.
	    if (rtn_mset != null)
		rtn_mset.maxify();
        } else {
            dice_left = 2;
            dice = new int[2];
	    
	    // Make two calls to getValidMovesRec, one for each ordering of the dice.
	    // Remove duplicate moves later.
	    dice[0]=dc1;
	    dice[1]=dc2;
	    rtn_mset = getValidMovesRec(empty_move, player, dice_left, dice);
	    
	    dice[0]=dc2;
	    dice[1]=dc1;
	    rtn_mset.addSet(getValidMovesRec(empty_move, player, dice_left, dice));

	    if (rtn_mset != null) {
		// Must use as many dice as possible.
		rtn_mset.maxify();
		// If you can only move one die, the larger must be used.
		rtn_mset.bigify(dc1,dc2);
	    }
        }
	// Remove duplicate moves from MoveSet.
	if (rtn_mset != null) {
	    rtn_mset.uniquify();
	    // Moves must be in descending order for player 0, ascending order for player 1.
	    for (Iterator iter = rtn_mset.getIterator(); iter.hasNext(); ) {
		if (player==0)
		    ((Move) iter.next()).sortDescend();
		else
		    ((Move) iter.next()).sortAscend();
	    }
	}
	return rtn_mset;
    }

    // Get moves when using doubles, reduce repetitions of moves.
    // s_pos is position to start trying moves from.
    // Start at 0/NUM_POINTS and increase/decrease from there.
    // Does not guarrantee maximum number of dice is used.
    private MoveSet getValidMovesDoublesRec(Move current_move, int player, int dice_left, int[] dice, int s_pos) {
	// Do not make a move that is "before" the move of one at a previous recursive level.
	MoveSet rtn_mset = new MoveSet();
	int p_type = pType(player);
	if (dice_left==0) {
	    // Stop if we're out of dice, return the current move.
	    rtn_mset.add(current_move);
	} else {
	    // Find all atomic moves with first die in list.
	    int the_die = dice[dice_left-1];
	    boolean moved=false;
	    // For player 0, run from s_pos down to 0, for player 1, run from s_pos up to NUM_POINTS-1.
	    for (int b_loc=s_pos; b_loc<NUM_POINTS && b_loc>=0; b_loc+=p_type) {
		// Check that the player has a checker here, and that
		// it can move it the_die spaces.
		if (board[b_loc]*p_type>0 && isValidMove(b_loc,the_die,player)) {
		    moved = true;
		    Move rec_move = (Move) current_move.clone();

		    // For each such move, make a recursive call with
		    // that atomic move added to the move, with an
		    // updated board.
		    int to_loc = b_loc+p_type*the_die;
		    if (to_loc>NUM_POINTS) {
			to_loc = NUM_POINTS;
		    } else if (to_loc<-1) {
			to_loc = -1;
		    }
		    AtomicMove a_move = new AtomicMove(b_loc, to_loc);
		    rec_move.addMove(a_move);
		    BackgammonBoard rec_board = (BackgammonBoard) this.clone();
		    rec_board.applyAtomicMove(player,a_move);
		    MoveSet rec_mset = rec_board.getValidMovesDoublesRec(rec_move,player,dice_left-1,dice,b_loc);

		    // Add rec_mset to rtn_mset.
		    rtn_mset.addSet(rec_mset);
		}
	    }
	    // Check the bar.
	    int bar_loc = player==0 ? BAR_LOC0 : BAR_LOC1;
	    if (on_bar[player] > 0 && isValidMove(bar_loc,the_die,player)) {
		// Same as above, with bar location.
		moved = true;
		Move rec_move = (Move) current_move.clone();

		AtomicMove a_move = new AtomicMove(bar_loc, bar_loc+p_type*the_die);
		rec_move.addMove(a_move);
		BackgammonBoard rec_board = (BackgammonBoard) this.clone();
		rec_board.applyAtomicMove(player,a_move);
		// TO DO:: Could do the bar moves non-duplicate using s_pos too.
		MoveSet rec_mset = rec_board.getValidMovesDoublesRec(rec_move,player,dice_left-1,dice,s_pos);
		rtn_mset.addSet(rec_mset);
	    }
	    if (!moved && current_move.getNumAtomicMoves()>0) {
		rtn_mset.add(current_move);
	    }
	}
	return rtn_mset;
    }


    // Find all moves possible on current board, append to current_move.
    // Assumes dice are sorted in non-decreasing order, we will find
    // moves starting with largest die first.
    // TO DO:: Slow because of clone calls?  Just modify board??
    // Note: could use the "not before", but it gets tricky with the "use some dice" rule.
    private MoveSet getValidMovesRec(Move current_move, int player, int dice_left, int[] dice) {
	MoveSet rtn_mset = new MoveSet();
	int p_type = pType(player);
	if (dice_left==0) {
	    // Stop if we're out of dice, return the current move.
	    rtn_mset.add(current_move);
	} else {
	    // Find all atomic moves with first die in list.
	    int the_die = dice[dice_left-1];
	    boolean moved=false;
	    for (int b_loc=0; b_loc<NUM_POINTS; b_loc++) {
		// Check that the player has a checker here, and that
		// it can move it the_die spaces.
		if (board[b_loc]*p_type>0 && isValidMove(b_loc,the_die,player)) {
		    moved = true;
		    Move rec_move = (Move) current_move.clone();

		    // For each such move, make a recursive call with
		    // that atomic move added to the move, with an
		    // updated board.
		    int to_loc = b_loc+p_type*the_die;
		    if (to_loc>NUM_POINTS) {
			to_loc = NUM_POINTS;
		    } else if (to_loc<-1) {
			to_loc = -1;
		    }
		    AtomicMove a_move = new AtomicMove(b_loc, to_loc);
		    rec_move.addMove(a_move);
		    BackgammonBoard rec_board = (BackgammonBoard) this.clone();
		    rec_board.applyAtomicMove(player,a_move);
		    MoveSet rec_mset = rec_board.getValidMovesRec(rec_move,player,dice_left-1,dice);

		    // Add rec_mset to rtn_mset.
		    rtn_mset.addSet(rec_mset);
		}
	    }
	    // Check the bar.
	    int bar_loc = player==0 ? BAR_LOC0 : BAR_LOC1;
	    if (on_bar[player] > 0 && isValidMove(bar_loc,the_die,player)) {
		// Same as above, with bar location.
		moved = true;
		Move rec_move = (Move) current_move.clone();

		AtomicMove a_move = new AtomicMove(bar_loc, bar_loc+p_type*the_die);
		rec_move.addMove(a_move);
		BackgammonBoard rec_board = (BackgammonBoard) this.clone();
		rec_board.applyAtomicMove(player,a_move);
		MoveSet rec_mset = rec_board.getValidMovesRec(rec_move,player,dice_left-1,dice);
		rtn_mset.addSet(rec_mset);
	    }

	    // If we didn't find any atomic moves, just add the current move.
	    if (!moved) {
		if (current_move.getNumAtomicMoves()!=0) {
		    rtn_mset.add(current_move);
		}
	    }
	}
	return rtn_mset;
    }


    /* Returns true iff player has won. */
    public boolean hasWon(int player) {
	return in_home[player] == CHECKERS_PER_PLAYER;
    }

    /* Print a visualization of the board to out_stream */
    public void showBoard() {
        int absx;
        int [][] dummy;
        
        dummy = new int [20][24];
        
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 24; j++)
                dummy[i][j] = 0;
        
        for (int j = 0; j < 12; j++) {
            absx = board[j] >= 0 ? board[j] : -board[j];
            for (int i = 0; i < absx; i++)
                if (board[j] > 0)
                    dummy[19 - i][22 - 2 * j] = 1;
                else
                    dummy[19 - i][22 - 2 * j] = -1;
        }
        
        for (int j = 0; j < 12; j++) {
            absx = board[j + 12] >= 0 ? board[j+12] : -board[j+12];
            for (int i = 0; i < absx; i++)
                if (board[j+12] > 0)
                    dummy[i][2*j] = 1;
                else
                    dummy[i][2*j] = -1;
        }
        
        out_stream.println();
        
        // Print point numbers.
        out_stream.println("12 13 14 15 16 17 18 19 20 21 22 23");
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 24; j++) {
                if (dummy[i][j] == 1) {
                    out_stream.print("O");
                } else if (dummy[i][j] == -1) {
                    out_stream.print("X");
                } else {
                    out_stream.print("_");
                }
                if (j % 2==1) {
                    out_stream.print(" ");
                }
                
                
            }
            out_stream.println();
        }
        
        // Print point numbers.
        out_stream.println("11 10 09 08 07 06 05 04 03 02 01 00");
        
        out_stream.println("Player X on bar: " + on_bar[0] + "  in home: " + in_home[0]);
        out_stream.println("Player O on bar: " + on_bar[1] + "  in home: " + in_home[1]);
        
        out_stream.println();
        
    }

    // Checks if player can start "bearing off" (moving checkers into home).
    private boolean canBearOff(int player) {
        boolean can_off = true;
        
        if (player==0) {
            for (int loc=6; loc < NUM_POINTS; loc++) {
                can_off = can_off & board[loc]>=0;
            }
        } else {
            for (int loc=0; loc < NUM_POINTS-6; loc++) {
                can_off = can_off & board[loc]<=0;
            }
        }
        
        return can_off;
    }
    
    // Assumes there is one of player's tiles at space.
    private boolean isValidMove(int space, int steps, int player) {
        boolean is_valid = true;
        int p_type = pType(player);

	// Can't move other tiles unless bar is clear.
	if (on_bar[player]>0 && ((space != BAR_LOC0 && player==0) || (space != BAR_LOC1 && player==1))) {
	    return false;
	}
        
        // Check not a move into home without proper positioning of tiles.
        // Bounds check for next step happens here.
        int move_to = space+steps*p_type;
        if (move_to < 0 | move_to > NUM_POINTS-1) {
            // Assumes moves are positive.
            is_valid = is_valid & canBearOff(player);

	    // Check that either this is an exact bearing off, or that this is the maximally distant checker.
	    if (move_to < -1) {
		// Check all locations higher than space.
		for (int s_i=space+1; s_i<NUM_POINTS; s_i++) {
		    if (board[s_i]*p_type > 0)
			is_valid = false;
		}
	    }
	    if (move_to > NUM_POINTS) {
		// Check all locations lower than space.
		for (int s_i=space-1; s_i>=0; s_i--) {
		    if (board[s_i]*p_type > 0)
			is_valid = false;
		}
	    }
	} else {
	    // Check no opponent's block there.
	    if (board[move_to]*(-p_type) >= 2)
		is_valid = false;
	}
	return is_valid;
    }
}