import java.util.*;

// A Move consists of a series of AtomicMoves (usually 2 of them, but could be any number from 1 to 4).
public class Move implements Cloneable{
    private Vector moves;
    protected Object clone() {
	Move m = null;
	try {
	    m = (Move) super.clone();
	}
	catch (CloneNotSupportedException e) {
	    System.err.println("I caught it");
	}
	m.moves = (Vector)moves.clone();
	return m;
    }
        
        
    public Move() {
	moves = new Vector();
    }
	
    public Move(int s1, int d1, int s2, int d2) {
	moves = new Vector();
	moves.add(new AtomicMove(s1,d1));
	moves.add(new AtomicMove(s2,d2));
    }
	
    public Move(int s1, int d1) {
	moves = new Vector();
	moves.add(new AtomicMove(s1,d1));
    }

    public void addMove(AtomicMove am) {
	moves.add(am);
    }
	
    public Iterator getAtomicMoves() {
	return moves.iterator();
    }

    public AtomicMove getAtomicMove(int m_num) {
	try {
	    return (AtomicMove) moves.elementAt(m_num);
	} catch (ArrayIndexOutOfBoundsException e) {
	    return null;
	}
    }
	
    public String toString() {
	String rtn_string = "";
	for (Iterator iter = moves.iterator(); iter.hasNext(); ) {
	    rtn_string = rtn_string + iter.next().toString() + " ";
	}
	return rtn_string;
    }

    public int getNumAtomicMoves() {
	return moves.size();
    }

    // Default is to sort in ascending order.
    public void sort() {
	sortAscend();
    }

    // Sort this list in ascending order by atomic move space numbers.
    public void sortAscend() {
	for (int i=0; i<moves.size(); i++) {
	    int small_ind=i;
	    for (int j=i+1; j<moves.size(); j++) {
		if (((AtomicMove) moves.elementAt(j)).lessThan((AtomicMove) moves.elementAt(small_ind))) {
		    small_ind = j;
		}
	    }
	    // Swap these elements.
	    AtomicMove tmp_am = (AtomicMove) moves.elementAt(i);
	    moves.setElementAt(moves.elementAt(small_ind),i);
	    moves.setElementAt(tmp_am,small_ind);
	}
    }

    // Same, in descending.
    public void sortDescend() {
	for (int i=0; i<moves.size(); i++) {
	    int large_ind=i;
	    for (int j=i+1; j<moves.size(); j++) {
		if (!((AtomicMove) moves.elementAt(j)).lessThan((AtomicMove) moves.elementAt(large_ind))) {
		    large_ind = j;
		}
	    }
	    // Swap these elements.
	    AtomicMove tmp_am = (AtomicMove) moves.elementAt(i);
	    moves.setElementAt(moves.elementAt(large_ind),i);
	    moves.setElementAt(tmp_am,large_ind);
	}
    }

    public boolean equals(Object o) {
	if ( this == o ) {
	    return true;
	} else if ( !(o instanceof Move) ) {
	    return false;
	} else {
	    Move mo = (Move) o;

	    // Check that each Atomic Move is the same.
	    // Make copies so as not to modify state.
	    Move this_copy = (Move) this.clone();
	    Move mo_copy = (Move) mo.clone();

	    this_copy.sort();
	    mo_copy.sort();
	    if (this_copy.moves.size() != mo_copy.moves.size()) {
		return false;
	    } else {
		for (int i=0; i<this_copy.moves.size(); i++) {
		    if (!((AtomicMove) this_copy.moves.elementAt(i)).equals(mo_copy.moves.elementAt(i))) {
			return false;
		    }
		}
	    }
	    return true;
	}
    }
}
