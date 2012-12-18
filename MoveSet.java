import java.util.*;

/* A MoveSet is a set of Moves, returned by calling getValidMoves.
 * The Vector moves stores these Move objects, and is private.
 * Access it using Enumeration, Iterator, or getMove methods.
 */
public class MoveSet {
	
    private Vector moves;
    public MoveSet()
    {
	moves = new Vector();
    }

    public void reset()
    {
	moves = new Vector();
    }

    // Returns the number of moves in this MoveSet.
    public int getSize() {
	return moves.size();
    }

    public Enumeration getElements() {
	return moves.elements();
    }

    public Iterator getIterator() {
	return moves.iterator();
    }

    public Move getMove(int m_num) {
	try {
	    return (Move) moves.elementAt(m_num);
	} catch (ArrayIndexOutOfBoundsException e) {
	    return null;
	}
    }

    public boolean containsMove(Move move)
    {
	boolean contains = false;
	if (getSize()==0 && move==null) {
	    contains = true;
	}

	for (Iterator iter = this.getIterator(); iter.hasNext(); ) {
	    if (move.equals(iter.next())) {
		contains = true;
		break;
	    }
	}
	return contains;
    }
	
    // Adds a new "Move" to the beginning of the set of moves
    public void add(Move move)
    {
	moves.add(move);
    }

    // Appends the moves (uncloned) in mset to the end of the moves in this set.
    public void addSet(MoveSet mset) {
	moves.addAll(mset.moves);
    }
    
    // Remove all duplicate equivalent Moves from this set.
    public void uniquify() {
	for (int i=0; i<moves.size(); i++) {
	    // Check from end on down, so as to keep indexing correct.
	    for (int j=moves.size()-1; j>i; j--) {
		if (((Move) moves.elementAt(i)).equals(moves.elementAt(j))) {
		    moves.removeElementAt(j);
		}
	    }
	}
    }

    // Remove Moves that do not use the maximum number of dice.
    public void maxify() {
	// Find max dice possible.
	int max_dice = 0;
	for (int i=0; i<moves.size(); i++) {
	    int this_move_d = ((Move) moves.elementAt(i)).getNumAtomicMoves();
	    if (this_move_d > max_dice) {
		max_dice = this_move_d;
	    }
	}
	// Remove those less than max dice.
	// Check from end on down, so as to keep indexing correct.
	for (int i=moves.size()-1; i>=0; i--) {
	    int this_move_d = ((Move) moves.elementAt(i)).getNumAtomicMoves();
	    if (this_move_d < max_dice) {
		moves.removeElementAt(i);
	    }
	}
    }

    // If either die, but not both can be used, it must be the larger.
    // Assumes maxify has been called.
    public void bigify(int dc1, int dc2) {
	if (moves.size()>0) {
	    if (((Move) moves.elementAt(0)).getNumAtomicMoves()==1) {
		int big_die = 0;
		for (int i=0; i<moves.size(); i++) {
		    AtomicMove am = ((Move) moves.elementAt(i)).getAtomicMove(0);
		    int this_die = Math.abs(am.source_column - am.dest_column);
		    if (this_die > big_die) {
			big_die = this_die;
		    }
		}
		// Remove those not using big_die.
		// Check from end on down, so as to keep indexing correct.
		for (int i=moves.size()-1; i>=0; i--) {
		    AtomicMove am = ((Move) moves.elementAt(i)).getAtomicMove(0);
		    int this_die = Math.abs(am.source_column - am.dest_column);
		    if (this_die < big_die) {
			moves.removeElementAt(i);
		    }
		}
	    }
	}
    }
}
