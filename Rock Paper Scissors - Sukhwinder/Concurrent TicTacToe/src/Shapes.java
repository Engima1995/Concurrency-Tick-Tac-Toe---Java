import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Sukhwinder Singh - P14184295
 * 
 * The Shapes enum is useful for returning random Shapes - Rock, Paper and Scissors.
 * The enum only stores 3 values and will randomise an output, which can be returned using the randomShape() function.
 * */
public enum Shapes {
	
	ROCK, PAPER, SCISSORS;
	
	private static final List<Shapes> VALUES = Collections.unmodifiableList(Arrays.asList(values()));	//Turns the enum into a collection of values
	private static final int SIZE = VALUES.size();														//The size of the collection is then calculated which is 3
	private static final Random RANDOM = new Random();													//A new RANDOM variable is instantiated 

	/**
	 * randomShape() will return a random shape. The choice is between Rock, Paper and Scissors
	 * 
	 * @return returns random Shape
	 * */
	public static Shapes randomShape(){
		return VALUES.get(RANDOM.nextInt(SIZE));														//returns the next random Shape value
	}
	
}
