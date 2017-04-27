/**
 * @author Sukhwinder Singh - P14184295
 * 
 * The MVar<E> class is a useful class for the communication between 2 separate Threads. 
 * The E represents any Element/Class can be set and MVar<E> is of type E. 
 * 
 * The MVar<E> provides synchronous communication between 2 Threads. It is of best use
 * when there is a producer and a consumer. They can both put and take but each time they do that,
 * one Thread would have to wait for the other Thread to finish and notify. It is a higher abstraction
 * of traditional Locks.
 * */
class MVar<E>
{
    private E state;								//E represents any type, the state is what it dropped and taken 
    private boolean isSet = false;					//isSet refers to the state of the put and take. It helps the two Threads
    												//to wait and notify each other
    
    /**
     * putMVar() is used by one Thread to put a state of type E and sets the internal state of the MVar to of type E.
     * It also makes other Threads wait until the current Thread is done with this and it notifies.
     * */
    public synchronized void putMVar(E s) {			
        while (isSet) {								//while isSet = true then the Thread trying to put a state of type E has to wait
            try {
                wait();
            } catch (InterruptedException e) {		//Exception is thrown if interrupted
                // handle exception
            }
        }
        isSet = true;								//a Thread that has put a state of type E will set the isSet to true and 
        state = s;									//stores the state into MVar and notify all Threads using MVar to stop waiting.
        notifyAll();
    }

    /**
     * takeMVar() is used by one Thread to take a state of type E and returns the current state of MVar.
     * It also makes other Threads wait until the current Thread is done with this and it notifies.
     * */
    public synchronized E takeMVar() {
        while (!isSet) {							//while !isSet = false = true then the Thread trying to take a state of type E has to wait
            try {
                wait();
            } catch (InterruptedException e) {		//Exception is thrown if interrupted
                // handle exception
            }
        }
        isSet = false;								//a Thread that has taken a state of type E will set the isSet to false and 
        notifyAll();								//notify all Threads using MVar to stop waiting and returns the state.
        return state;
    }
}