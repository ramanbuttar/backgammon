/* A BackgammonAgent consists of a method to choose a move. */
abstract class BackgammonAgent {
    int player_num;
    
    public BackgammonAgent(int my_player_num) {		
	player_num = my_player_num;
    }

    // Returns the move that this agent selects given a current board state b.	
    public abstract Move chooseMove(BackgammonBoard b);
}
