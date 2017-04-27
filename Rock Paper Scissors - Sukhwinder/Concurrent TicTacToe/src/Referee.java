import java.util.concurrent.BlockingQueue;

/**
 * @author Sukhwinder Singh - P14184295
 * 
 * Referee Class implements the Runnable Interface so that it can run as a Thread alongside with the 
 * Player Class to provide a simulation of the traditional Rock, Paper, Scissors game.
 * 
 * The Referee Class introduces the concept of maintaining a queue, allowing 2 or more Players to play
 * against each other but only allows 2 Players at a time. Logically only 2 Players should be allowed to play
 * with one another and no more, so the Referee Class adheres to these rules and provides the Players to play
 * by using up all their turns as well as making sure each Player will get their turn. 
 * 
 * The Referee Class also updates the internal state of the Player after letting the Players play.
 * */
public class Referee implements Runnable{

	private BlockingQueue<Player> queue;			//BlockingQueue<Player> is a global declaration used by both the Player and Referee to
													//wait and notify one another, for adding/removing from queue.
	
	private Calculate calc;							//Calculate Class provides the Referee to examine which Player has won, lost or drawn. It is
													//a useful Class as it does all the calculations for the Referee and the Referee only has to
													//notify the Player for their internal states.
	
	private int result = 0;							//results variable is used to keep track of who has won. 1 states Player 1 has won, 2 states
													//Player 2 has won, 3 states that both Players have drawn.
	
	
	/**
	 * The Default Constructor for the Referee Class which requires a BlockingQueue<Player> that will be the queue passed from the main
	 * application, where the Player and Referee are both executed and they both share the same queue to keep track of the communication.
	 *
	 * @param queue - represents the queue that both the Player and Referee will share, for synchronous communication
	 * */
	public Referee(BlockingQueue<Player> queue){	//Sets Internal state for the Referee Class

		this.queue = queue;

	}

	/**
	 * The run() method is implemented through the Runnable Interface. The Referee Class has overridden the
	 * previous state of the run() method to provide it's own state. In this case, the Referee Thread will sleep
	 * for a certain duration of time and eventually it will wake up and take 2 Players at a time from the queue
	 * shared by both the Player and Referee and calculate their results and update their internal states after
	 * displaying who has won the previous game. 
	 * 
	 * If all that fails, an exception is thrown.
	 * */
	@Override
	public void run() {
		
			/*
			 * The Thread sleeps for a certain duration. After waking up it checks for an interruptions and if there were no 
			 * interruptions with this thread then Player arrays are allocated of size 2, representing 2 Players at a time.
			 * 
			 * The Referee will take anything from the queue if there is anything, in this case Players waiting and stores them
			 * in the Player array in the appropriate indexes.
			 * 
			 * The Thread sleeps for a bit then checks for the length of the Player array, if 2 then the Referee calculates
			 * the results of the Players and updates their internal results depending on what the result was.
			 * 
			 * If all fails or Thread is interrupted, then Exception is thrown. In this case it is used for gracefully halting
			 * the Referee Thread and a message is output, saying "Referee ended the game."
			 * */

			try {

				Thread.sleep(500);
				while(!Thread.currentThread().isInterrupted()){
					
					Player[] players = new Player[2];
					
					for(int i=0; i<2; i++){
						players[i] = queue.take();
					}
					
					Thread.sleep(100);
					
					if(players.length == 2){
						calc = new Calculate(players[0], players[1]);
						result = calc.result();
						System.out.print("\nPlayer " + players[0].getPlayerId() + " (" + players[0].getShape() + ") <-> " + "Player " + players[1].getPlayerId() + " (" + players[1].getShape() + ") ===> ");
						if(result == 1){
							System.out.print(" Player " + players[0].getPlayerId() + " Wins !");
							players[0].getResult().putMVar(1);
							players[1].getResult().putMVar(2);
						}else if(result == 2){
							System.out.print(" Player " + players[1].getPlayerId() + " Wins !");
							players[0].getResult().putMVar(2);
							players[1].getResult().putMVar(1);
						}else if(result == 3){
							System.out.print(" It's a draw !");
							players[0].getResult().putMVar(3);
							players[1].getResult().putMVar(3);
						}else{
							System.out.println(" Failed.\n");
						}
					}

				}

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			System.out.print("\nReferee ended the game.");

	}

}
