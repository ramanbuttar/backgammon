import java.util.*;

public class Dice {
    // Maximum value for die rolls.  Die rolls in {1,2,...,DIE_MAX}.
    public static int DIE_MAX = 6;

    Random r;
    // Current values of the dice.
    int dc1, dc2;
    
	
    public Dice()
    {
	r = new Random();
    }

    // Set the dc1 and dc2 variables to new random values in {1,2,...,DIE_MAX}.
    public void toss()
    {

	dc1 = r.nextInt(DIE_MAX) + 1;
	dc2 = r.nextInt(DIE_MAX) + 1;
    }
}
