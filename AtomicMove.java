// This class represents a single actual repositioning of a checker to a new place on the board.
public class AtomicMove {
    int source_column;
    int dest_column;	    
	
    public AtomicMove(int sc, int dc) {
	source_column=sc;
	dest_column=dc;
    }
	
    public String toString() {
	return "(" + source_column + "," + dest_column + ")";
    }

    // AtomicMoves are ordered by source column, then by destination column.
    // Returns true if this < am.
    public boolean lessThan(AtomicMove am) {
	if (source_column > am.source_column) {
	    return false;
	} else if (source_column < am.source_column) {
	    return true;
	} else {
	    return dest_column < am.dest_column;
	}
    }


    public boolean equals(Object o) {
	if ( this == o ) {
	    return true;
	} else if ( !(o instanceof AtomicMove) ) {
	    return false;
	} else {
	    AtomicMove amo = (AtomicMove) o;
	    
	    return (amo.source_column==source_column && amo.dest_column==dest_column);
	}
    }
}
