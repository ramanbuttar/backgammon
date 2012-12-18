/*
 * Driver program for backgammon program.
 */

public class Bg {
    
    public static void main(String[] args) {
	/* Start up a backgammon game using these two agents. */
	
    BackgammonAgent agent0 = null;
    BackgammonAgent agent1 = null;
    
    //agent0 = new UserBackgammonAgent(0);
    	
    //agent0 = new ExpectiminimaxBackgammonAgent(0);
    //agent0 = new ExpectiminimaxBackgammonAgent2(0);
    //agent0 = new ExpectiminimaxBackgammonAgent3(0);
    agent0 = new SimpleBackgammonAgent(0);
    //agent0 = new ExpectiminimaxBackgammonAgent(0, new BackgammonEvaluationMM());
    
    //agent1 = new ExpectiminimaxBackgammonAgent(1);
    //agent1 = new ExpectiminimaxBackgammonAgent2(1);
    //agent1 = new ExpectiminimaxBackgammonAgent3(1);
    //agent1 = new SimpleBackgammonAgent(1);
    agent1 = new ExpectiminimaxBackgammonAgent(1, new BackgammonEvaluation());

    //test(agent0, agent1, 20, false);
    //test(agent0, agent1, 1, true);
    testBunch(5);
	
    }
    
    private static void test(BackgammonAgent agent0, BackgammonAgent agent1, int num, boolean verbose){
    	int player0 = 0;
    	int player1 = 0;
    	int result = 0;
    	BackgammonGame bg = null;
    	
    	while (player0 + player1 != num) {
    		System.out.println("Try: " + (player0 + player1));
    		bg = new BackgammonGame(agent0, agent1, System.out);
    		bg.board.verbose = verbose;
    		bg.board.use_doubles = true;
    		result = bg.playGame();
    		if (result == 0){
    			player0++;			
    		} else if (result == 1){
    			player1++;
    		}
    		System.out.println(player0 + " - " + player1);
    		System.out.println();		
    	}
        System.out.println("Agent0 = " + player0);
        System.out.println("Agent1 = " + player1);
        //System.out.println();
    }
    
    private static void testBunch(int num){
    	BackgammonAgent agent0 = null;
        BackgammonAgent agent1 = null;
        
        System.out.println("Simple vs Me");
        agent0 = new SimpleBackgammonAgent(0);
        agent1 = new ExpectiminimaxBackgammonAgent(1);
        test(agent0, agent1, num, false);
        
        agent0 = new ExpectiminimaxBackgammonAgent(0);
        agent1 = new SimpleBackgammonAgent(1);
        System.out.println("Me vs Simple");
        test(agent0, agent1, num, false);
        
        agent0 = new ExpectiminimaxBackgammonAgent(0);
        agent1 = new ExpectiminimaxBackgammonAgent(1);
        System.out.println("Me vs Me");
        test(agent0, agent1, num, false);
        
        agent0 = new ExpectiminimaxBackgammonAgent(0, new BackgammonEvaluation2());
        agent1 = new ExpectiminimaxBackgammonAgent(1, new BackgammonEvaluation());
        System.out.println("Old vs New");
        test(agent0, agent1, num, false);
        
        agent0 = new ExpectiminimaxBackgammonAgent(0, new BackgammonEvaluation());
        agent1 = new ExpectiminimaxBackgammonAgent(1, new BackgammonEvaluation2());
        System.out.println("New vs Old");
        test(agent0, agent1, num, false);
        
        agent0 = new ExpectiminimaxBackgammonAgent(0, new BackgammonEvaluationMM());
        agent1 = new ExpectiminimaxBackgammonAgent(1, new BackgammonEvaluation());
        System.out.println("Mike vs Me");
        test(agent0, agent1, num, false);
        
        agent0 = new ExpectiminimaxBackgammonAgent(0, new BackgammonEvaluation());
        agent1 = new ExpectiminimaxBackgammonAgent(1, new BackgammonEvaluationMM());
        System.out.println("Me vs Mike");
        test(agent0, agent1, num, false);
    }
    
}

