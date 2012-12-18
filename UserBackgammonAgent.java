/*
 * An interactive backgammon agent.  A list of possible moves is
 * presented, and the user is asked to select one.
 */
import java.io.*;
import java.util.*;
public class UserBackgammonAgent extends BackgammonAgent {
    public UserBackgammonAgent(int my_player_num) {
	super(my_player_num);
    }
	
    public Move chooseMove(BackgammonBoard b)
    {
	MoveSet ms = b.getValidMoves(player_num);

	if (ms.getSize()==0) {
	    /* No valid moves. */
	    System.out.println("No valid moves.");
	    return null;
	} else {

	    /* Show list of moves to user. */
	    int move_i=0;
	    for (Iterator iter = ms.getIterator(); iter.hasNext(); move_i++) {
		System.out.println(move_i + ": " + iter.next());
	    }
	    System.out.println("Select a move by typing number in 0-" + (ms.getSize()-1));

	    /* Get input from the user. */
	    InputStreamReader isr = new InputStreamReader ( System.in );
	    BufferedReader br = new BufferedReader ( isr );
	    String str;
	    int move_num=0;
	    boolean got_move = false;
	    try {
		while ( !got_move ) {
		    str = br.readLine();
		    try {
			move_num = Integer.parseInt(str);
			if (move_num >= 0 & move_num < ms.getSize()) {
			    got_move = true;
			} else {
			    System.out.println("Move number out of range.");
			}
		    } catch (NumberFormatException nfe) {
			System.out.println("Invalid input.");
		    }
		}
	    } catch ( IOException ioe ) {
		System.err.println("Error reading move.");
	    }

	    return ms.getMove(move_num);
	}
    }
}
