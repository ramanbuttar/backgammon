/* A very naive evaluation function.
 * Returns the number of tiles in the player's home.
 * Max is 15, min is 0.
 */
public class BackgammonEvaluationMM extends Evaluation {
	private static int BLOCKBONUS = 3;
	private static float BLOTBONUS = (float).25;
	private static int ANCHORBONUS = 5;
	private static int DISTRIBUTIONBONUS = 2;
	private static float GETGOINGBONUS = (float)0;
	BackgammonEvaluationMM() {
	MAX_SCORE = 15;
	MIN_SCORE = 0;
    }

    public float boardScore(BackgammonBoard b, int player) 
    {
    	float sum = (float) 0.0;
    	int anchorsinhome = 0;
    	int anchorsinopponenthome = 0;
    	int primebonus = 0;
    	int opponentprimebonus = 0;
    	int proximitybonus = 0;
    	int opponentproximitybonus = 0;
    	boolean endgame = false;
    	boolean seenself = false;
    	int opponentcount = 0;
    	int owncount = 0;
    	int blockinrow = 0;
    	int anchorcount = 0;
    	int opponentanchorcount = 0;
    	int opponentblots = 0;
    	int exiting = 0;
    	int anchorblot = 0;
    	int opanchorblot = 0;
    	boolean countopblots = false;
    	if(player == 1)
    	{
	    	for(int i = 0; i < b.NUM_POINTS; i++)
	    	{
		    		if(b.board[i] > 0)
		    		{
		    			seenself = true;
		    			if(owncount == 0)
		    				countopblots = true;
		    			owncount+=b.board[i];
		    			if(blockinrow < 0)
		    				blockinrow = 0;
		    			if(i < 12)
		    			{
		    				sum-=GETGOINGBONUS*b.board[i]*(11-i);
		    			}
		    			else
		    			{
		    				sum+=GETGOINGBONUS*b.board[i]*(i-11);
		    			}
		    			if(!endgame)
		    			{
			    			if(b.board[i] > 1)
			    			{
			    				blockinrow++;
			    				sum+=blockinrow*BLOCKBONUS;
			    				if(i > 17)
			    					anchorcount++;
			    			}
			    			else
			    			{
			    				blockinrow = 0;
			    				sum+= (opponentcount-15 - i)*BLOTBONUS;
			    				if(i > 17)
			    					anchorblot++;
			    			}
		    			}
		    		}
		    		else if(b.board[i] < 0)
		    		{
		    			opponentcount-=b.board[i];
		    			if(blockinrow > 0)
		    				blockinrow = 0;
		    			if(!endgame)
		    			{
			    			if(b.board[i] < -1)
			    			{
			    				blockinrow--;
			    				sum+=blockinrow*BLOCKBONUS;
			    				if(i < 6)
			    					opponentanchorcount++;
			    			}
			    			else
			    			{
			    				if(countopblots)
			    					opponentblots++;
			    				blockinrow = 0;
			    				if(owncount + b.on_bar[1]> 0)
			    				{
			    					sum+= ((owncount + (23-i))*BLOTBONUS);
			    				}
			    			}
		    			}
		    		}
		    		else
		    		{
		    			blockinrow = 0;
		    		}
	    	}
	    	float asdf = ((anchorcount-anchorblot)*(b.on_bar[0]+ opponentblots))*ANCHORBONUS;
        	sum+= asdf;
    	}
    	else if(player == 0)
    	{
    		countopblots=true;
	    	for(int i = 0; i < b.NUM_POINTS; i++)
	    	{
		    		if(b.board[i] < 0)
		    		{
		    			seenself = true;
		    			if(owncount == 15)
		    				countopblots = false;
		    			owncount-=b.board[i];
		    			if(blockinrow < 0)
		    				blockinrow = 0;
		    			if(i > 11)
		    			{
		    				sum+=GETGOINGBONUS*b.board[i]*(i-11);
		    			}
		    			else
		    			{
		    				sum-=GETGOINGBONUS*b.board[i]*(11-i);
		    			}
		    			if(!endgame)
		    			{
			    			if(b.board[i] < -1)
			    			{
			    				blockinrow++;
			    				sum+=blockinrow*BLOCKBONUS;
			    				if(i < 6)
			    					anchorcount++;
			    			}
			    			else
			    			{
			    				blockinrow = 0;
			    				sum+= (opponentcount - (23-i))*BLOTBONUS;
			    				if(i < 6)
			    					anchorblot++;
			    			}
		    			}
		    		}
		    		else if(b.board[i] > 0)
		    		{
		    			opponentcount+=b.board[i];
		    			if(blockinrow > 0)
		    				blockinrow = 0;
		    			if(!endgame)
		    			{
			    			if(b.board[i] > 1)
			    			{
			    				blockinrow--;
			    				sum+=blockinrow*BLOCKBONUS;
			    				if(i > 17)
			    					opponentanchorcount++;
			    			}
			    			else
			    			{
			    				if(countopblots)
			    					opponentblots++;
			    				blockinrow = 0;
			    				if(owncount < 15)
			    				{
			    					sum+= ((15 - owncount + i)*BLOTBONUS);
			    				}
			    			}
		    			}
		    		}
		    		else
		    		{
		    			blockinrow = 0;
		    		}
	    	}
	    	float asdf = ((anchorcount-anchorblot)*(b.on_bar[1]+ opponentblots))*ANCHORBONUS;
        	sum+= asdf;
    	}
	return (float) sum;
    }
}