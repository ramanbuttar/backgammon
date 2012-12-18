import java.io.BufferedWriter;
import java.io.FileWriter;

public class ExpectiminimaxBackgammonAgent extends BackgammonAgent {
    private Evaluation evaluation;
    private int[][] dierolls = 
    {
    		{1,1}, {2,2}, {3,3}, {4,4}, {5,5}, {6,6}, {1,2}, {1,3}, {1,4}, {1,5}, {1,6},
    		{2,3}, {2,4}, {2,5}, {2,6}, {3,4}, {3,5}, {3,6}, {4,5}, {4,6}, {5,6}
    };
    private int maxply = 2;
    FileWriter fstream;
    BufferedWriter out;
    public static final int MAX = 0;
    public static final int CHANCEAFTERMAX = 1;
    public static final int MIN = 2;
    public static final int CHANCEAFTERMIN = 3;
    boolean printNodes = false;
    
    public ExpectiminimaxBackgammonAgent(int my_player_num) {
		super(my_player_num);
		evaluation = new BackgammonEvaluation();
	if(printNodes)
	{
		try{
		    // Create file 
		    fstream = new FileWriter("out.txt");
		     out = new BufferedWriter(fstream);
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
	}
    }

    public ExpectiminimaxBackgammonAgent(int my_player_num, Evaluation e) {
	super(my_player_num);
	evaluation = e;
    }

    public Move chooseMove(BackgammonBoard b) 
    {
    	int otherplayer = (player_num+1)%2;
    	BackgammonBoard temp = (BackgammonBoard)b.clone();
    	MoveSet ms = b.getValidMoves(player_num);
    	int bestmove = 0;
    	double maxsofar = 0.0;
    	double tempmin = 0.0;
    	boolean calculated = false;
    	for(int i = 0; i < ms.getSize(); i++)
    	{
    		temp = (BackgammonBoard)b.clone();
    		temp.applyMove(player_num, ms.getMove(i));
    		tempmin = MinMaxRecursive(temp, otherplayer, maxply-1, CHANCEAFTERMAX, 1);
    		if(tempmin > maxsofar || calculated == false)
    		{
    			bestmove = i;
    			maxsofar = tempmin;
    			calculated = true;
    		}
    	}
    	return ms.getMove(bestmove);
    }
   
    public double MinMaxRecursive(BackgammonBoard b, int player, int depth, int state, int level)
    {
    	//General
    	BackgammonBoard temp = (BackgammonBoard)b.clone();
    	
    	int nextstate = (state+1)%4;
    	double returnvalue = 0.0;
    	if(state == CHANCEAFTERMAX || state == CHANCEAFTERMIN)
    	{
    		//Chance
    		double avg = 0.0;
        	double prob = 0.0;
        	for(int i = 0; i < 21; i++)
        	{
        		temp = (BackgammonBoard)b.clone();
        		temp.dc1 = dierolls[i][0];
        		temp.dc2 = dierolls[i][1];
        		if(i < 6)
        		{
        			prob = 1.0/36.0;
        		}
        		else
        		{
        			prob = 1.0/18.0;
        		}
        		avg+=prob*MinMaxRecursive(temp, player, depth, nextstate, level+1);
        	}
        	returnvalue =  avg/21.0;
    	}
    	else
    	{
    		int otherplayer = (player+1)%2;
	    	//Min/Max
    		depth--;
    		
	    	double bestsofar = 0.0;
	    	double tempval = 0.0;
	    	boolean calculated = false;
	    	MoveSet ms = temp.getValidMoves(player);
	    	
	    	for(int i = 0; i < ms.getSize(); i++)
	    	{
	    		temp = (BackgammonBoard)b.clone();
	    		temp.applyMove(player, ms.getMove(i));
	    		if(depth>0)
	    		{
	    			tempval = MinMaxRecursive(temp, otherplayer, depth, nextstate, level+1);
	    		}
	    		else
	    		{
	    			tempval = evaluation.boardScore(temp, player_num);
	    		}
	    		if(state == MAX)
	    		{
		    		if(tempval > bestsofar || calculated == false)
		    		{
		    			calculated = true;
		    			bestsofar = tempval;
		    		}
	    		}
	    		else if(state == MIN)
	    		{
	    			if(tempval < bestsofar || calculated == false)
	        		{
	        			calculated = true;
	        			bestsofar = tempval;
	        		}
	    		}
	    	}
	    	returnvalue =  bestsofar;
    	}
    	return returnvalue;
    }
}