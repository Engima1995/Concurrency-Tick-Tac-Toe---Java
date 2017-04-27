import java.util.concurrent.BlockingQueue;

/**
 * @author Sukhwinder Singh - P14184295
 * 
 * Player Class implements the Runnable Interface so that it can run as a Thread alongside with the 
 * Referee Class to provide a simulation of the traditional Rock, Paper, Scissors game.
 * 
 * The Player Class introduces the concept of having 2 or more Players running simultaneously, allowing
 * them to compete against each other in the game of Rock, Paper, Scissors. Each Player will wait their
 * turns once notifying the Referee Class. The Player Thread will carry on running until the Player turns
 * have completed and the Players have stopped playing.
 * */

public class Player implements Runnable{

	private int id;								//Initialises the id, used to keep track of each player. Giving them unique id
	
	private MVar<Integer> result;				//MVar<Integer> is used to store the result from the Referee to the Player. Providing a 
												//Synchronised communication.
	
	private BlockingQueue<Player> queue;		//BlockingQueue<Player> is a global declaration used by both the Player and Referee to
	                                            //wait and notify one another, for adding/removing from queue.
	
	private Shapes rps;						    //Shapes class "rps" will retrieve a random Shape from the Class and the "holder" will
	private Shapes holder;						//will store the value to keep track of the Shape.
	
	private int win, lose, draw;				//win, lose and draw represents the internal state of the Player and what their results are
												//after playing their turn.
	
	private int turns;							//turns is used to keep track of how many turns each Player have taken. It is used for debugging
	
	private int NTURNS;							//NTURNS is the global variable used to hold the number of turns passed from the main program.
	
	private int tempResult;						//tempResult refers to the temporary result passed by the MVar to update the internal state of the Player.

	
	/**
	 * The Default Constructor for the Player Class which requires an input of id, to establish the difference between 1 Player and another.
	 * NTURNS defines the number of turns each Player will take. Lastly, BlockingQueue<Player> will be the queue passed from the main
	 * application, where the Player and Referee are both executed and they both share the same queue to keep track of the communication.
	 * 
	 * @param id - uniquely identifies each Player
	 * @param NTURNS - number of turns each Player takes
	 * @param queue - represents the queue that both the Player and Referee will share, for synchronous communication
	 * */
	public Player(int id, int NTURNS, BlockingQueue<Player> queue){		//Sets Internal state for the Player Class
		this.id = id;
		this.NTURNS = NTURNS;			
		this.queue = queue;
		result = new MVar<Integer>();
		win = lose = draw = turns = 0;
	}

	/**
	 * The run() method is implemented through the Runnable Interface. The Player Class has overridden the
	 * previous state of the run() method to provide it's own state. In this case, the Player will pick
	 * their turns and a random shape and notifies the Referee by putting themselves into the waiting queue.
	 * 
	 * The Player then stops waiting after being notified by the Referee and the Player will then update their
	 * internal states by grabbing the results and display a meaningful text message, displaying the number of
	 * wins, losses and draws as well as turns to show all Players had their turns (useful for debugging). 
	 * 
	 * If all that fails, an exception is thrown.
	 * */
	@Override
	public void run() {
		if(NTURNS > 0){									//If NTURNS is greater than 0 then it is safe to let Players have a go each.
			try {
				
				for(int i=1; i<=NTURNS; i++){			//For each turn, the player will update their turns and get a random Shape.
					turns++;							//They then put themselves into a queue and wait for the response provided
					holder = rps.randomShape();			//by the Referee.
					queue.put(this);
					tempResult = result.takeMVar();
					if(tempResult == 1){
						win++;
					}else if(tempResult == 2){
						lose++;
					}else if(tempResult == 3){
						draw++;
					}
				}
														//Output is then displayed
				System.out.print("\n\n\tPlayer " + id + " ----> Wins: " + win + " Draws: " + draw + " Losses: " + lose + " Turns: " + turns + "\n");
				
			} catch (InterruptedException e) {			//else Exception is thrown
				e.printStackTrace();
			}
		}
	}

	/**
	 * getPlayerId() will return the value of the Player id. It can be useful for selecting specific Players.
	 * 
	 * @return returns Player id
	 * */
	public int getPlayerId(){
		return id;
	}

	/**
	 * getShape() will return the value of the Shape, which will be a random selection.
	 * 
	 * @return returns random Shape
	 * */
	public Shapes getShape(){
		return holder;
	}

	/**
	 * getResult() will return the MVar<Integer> for updating internal state of the Player, to keep track of tally of wins, losses and draws.
	 * 
	 * @return returns result for updating win, loss, draw internal state
	 * */
	public MVar<Integer> getResult(){
		return result;
	}

}
