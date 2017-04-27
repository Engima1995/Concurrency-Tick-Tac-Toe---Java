import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Sukhwinder Singh - P14184295
 * 
 * Game Class enables all the sub-classes - Player, Referee, Calculate, Shapes and MVar to run in sequence to
 * provide a running Rock, Paper, Scissors game. 
 * 
 * Game Class introduces the concept of concurrency through the traditional game. 
 * Through synchronise communication, the two Classes: Player and Referee, will communicate with one another
 * to simulate the game. 
 * */

public class Game {

	public static void main(String[] args) throws InterruptedException, ExecutionException{

		int NTURNS = 3;			    //Initialises the number of turns the players will take to play the game
		int NPLAYERS = 4;				//Initialises the number of players allowed to play the game
		int pcount = 0;					//Initialises pcount (player count) variable which allows the tracking of players when scheduled for running
		
		boolean evenPlayers;			//Initialises the boolean variable: evenPlayers, to keep track of players.
										//In logical terms, only players that are even should be able to play. 
										//If players are odd then there could be the risk of deadlocks or even starvation, as well as
										//traditionally, it does not make sense for odd number of players to play rock paper scissors simultaneously
		
		
		/*
		 * if the player is less than two, then we know that first of all the player number is odd and secondly, a single person on their
		 * own cannot play the game. 
		 * 
		 * if the turns are also less than 1, then it should not be possible for the player to play the game as there wont be any turns.
		 * 
		 * 1. if both variables are true then the first statement executes
		 * 2. else if players alone is less than 2, then second statement executes
		 * 3. else if turns alone is less than 1, then third statement executes
		 * 4. else nothing happens
		 * */
		
		if(NPLAYERS < 2 && NTURNS < 1){
			System.out.println("Player value is less than 2 and Turn value is less than 1.");
			return;
		}else if(NPLAYERS < 2){
			System.out.println("Player value is less than 2.");
			return;
		}else if(NTURNS < 1){
			System.out.println("Turn value is less than 1.");
			return; 
		}else{}

		/*
		 * 1. BlockingQueue<Player> queue 
		 * 
		 * 	  BlockingQueue essentially supports operations that wait for the queue to become non-empty when 
		 * 	  Retrieving an element, and wait for space to become available in the queue when storing an element.
		 * 	  In this case ArrayBlockingQueue was chosen for FIFO operation, which is also a BlockingQueue that behaves
		 * 	  like a stack. This style of Queue is chosen due to the nature of Players notify and waiting for the Referee
		 * 	  to add them to the queue and let the Players play. It does not matter which Player goes first, so long as they
		 * 	  all get to play.
		 * 
		 * 2. ScheduledExecutorService player
		 * 
		 * 	  An Executor that provides methods to manage termination and methods that can produce a future for 
		 *    tracking progress of one or more asynchronous tasks. ScheduledExecutorService is an ExecutorService
		 *    that can schedule commands to run after a given delay or to execute periodically. In this case NPLAYERS
		 *    represents the size of the ThreadPool and how many Players can be executed of that size.
		 *    
		 * 3. ExecutorService referee
		 * 
		 *    ExecutorService as mentioned above, but this time it only executes a single thread which will be the Referee thread.
		 * */	
		
		BlockingQueue<Player> queue = new ArrayBlockingQueue<Player>(NPLAYERS);			
		ScheduledExecutorService player = Executors.newScheduledThreadPool(NPLAYERS);
		ExecutorService referee = Executors.newSingleThreadExecutor();

		/*
		 * 1. Future<?> fr
		 * 
		 * 	  A Future represents the result of an asynchronous computation. Methods are provided to check if the computation is complete, 
		 *    to wait for its completion, and to retrieve the result of the computation. In this case the Future will be the Referee Thread,
		 *    that will allow Players to play against one another until all Players have played their turns. 
		 *    
		 *    The Referee Thread will be alive, so the Future will help terminate the Thread gracefully once all Players have stopped playing.
		 * */
		
		Future<?> fr = referee.submit(new Referee(queue));	
		
		/*
		 * 1. evenPlayers holds true or false, after calculating the size of NPLAYERS modulus by 2. It checks if the value returned is
		 *    even or odd, which represents the Players and whether or not they are even or odd.
		 * */

		evenPlayers = (NPLAYERS % 2 == 0);
		
		/*
		 * 1. if Players are not even then a message gets printed and the program terminates until the user changes the 
		 *    settings of the NPLAYERS variable.
		 *    
		 * 2. else Players are even, a for loop will initialise the Players to be scheduled within the Thread Pool after 100 milliseconds
		 *    a new player gets executed. pcount is incremented for each iteration, representing the number of players. It is used to keep 
		 *    track of Players when being scheduled.
		 * */

		if(!evenPlayers){
			System.out.println("Players need to be even.");
			return;
		}else{
			for(int i = 0; i<NPLAYERS; i++){
				player.schedule(new Player(i+1, NTURNS, queue), 50, TimeUnit.MILLISECONDS);
				pcount++;

			}
		}
		
		/*
		 * 1. player.shutdown() will initiate an orderly shutdown in which previously submitted tasks are executed, 
		 *    but no new tasks will be accepted. 
		 *    
		 * 2. if player.awaitTermination() will block any other tasks for 2 Minutes after the call of player.shutdown(), 
		 *    awaiting orderly termination of all tasks before it performs any other task. This is a good way of confirming
		 *    that Players have officially stopped. It is also good for the Referee Thread to know for certain when to shutdown.
		 * */
		
		player.shutdown();
		if(player.awaitTermination(100, TimeUnit.MINUTES)){
			System.out.println("\nTotal players that played were " + pcount + ". The ExecutorService has now shutdown.");
		}
		
		/*
		 * 1. if player.isShutdown(), then the Future will gracefully terminate the Referee Thread by setting the boolean to true
		 *    which will Interrupt the Referee Threads internal state and cause they Referee to gracefully to close down.
		 * */

		if(player.isShutdown()){
			fr.cancel(true);
		}

	}

}
