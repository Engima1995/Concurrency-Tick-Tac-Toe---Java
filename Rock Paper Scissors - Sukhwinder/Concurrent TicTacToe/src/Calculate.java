/**
 * @author Sukhwinder Singh - P14184295
 * 
 * The Calculate Class takes 2 Player Threads and compares their results and it then returns a value of:
 * 1. Representing Player 1 have won
 * 2. Representing Player 2 have won
 * 3. Representing both Players have drawn 
 * */
public class Calculate {

	private Shapes s;		//Shapes is used for comparing what Shapes the Players have picked and compares it with their opponents
	private Player p1;		//p1 and p2 represents 2 Player Classes/Threads, playing against one another
	private Player p2;

	/**
	 *  The Default Constructor for the Calculate Class is having 2 Player inputs, representing Player 1 and Player 2
	 *  and initialising their states.
	 *  
	 *  @param p1 - represents Player 1
	 *  @param p2 - represents Player 2
	 * */
	public Calculate(Player p1, Player p2){
		this.p1 = p1;
		this.p2 = p2;
	}

	/**
	 * result() returns the value of
	 * 
	 *  1. Representing Player 1 have won
	 *  2. Representing Player 2 have won
	 *  3. Representing both Players have drawn 
	 *  
	 *  after comparing their Shapes.
	 *  
	 *  @return returns value of who has won, lost, drawn
	 * 
	 * */
	public int result() throws InterruptedException{

		//Compares Shapes and returns value
		
		if(p1.getShape() == s.PAPER && p2.getShape() == s.PAPER){
			return 3;
		}

		if(p1.getShape() == s.ROCK && p2.getShape() == s.ROCK){
			return 3;
		}

		if(p1.getShape() == s.SCISSORS && p2.getShape() == s.SCISSORS){
			return 3;
		}

		if(p1.getShape() == s.ROCK && p2.getShape() == s.PAPER){
			return 2;
		}

		if(p1.getShape() == s.PAPER && p2.getShape() == s.ROCK){
			return 1;
		}

		if(p1.getShape() == s.ROCK && p2.getShape() == s.SCISSORS){
			return 1;
		}

		if(p1.getShape() == s.SCISSORS && p2.getShape() == s.ROCK){
			return 2;
		}

		if(p1.getShape() == s.SCISSORS && p2.getShape() == s.PAPER){
			return 1;
		}

		if(p1.getShape() == s.PAPER && p2.getShape() == s.SCISSORS){
			return 2;
		}
		
		//If none match, then 0 is returned to represent an Error !
		return 0;
	}

}
