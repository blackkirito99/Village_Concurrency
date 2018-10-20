/**
 * A group of tourists, each with its unique id, who travel 
 * between the villages by train.
 * 
 * @author yuweib@student.unimelb.edu.au
 *
 */

public class Group {
  
	// a unique identifier for this tour group
	protected int id;
	
	//the next ID to be allocated
	protected static int nextId = 1;
	
	// whether group is still in valley
	private volatile boolean inValley;
	
	// whether group has finished the tour
	private volatile boolean finishTour;

	//create a new vessel with a given Id
	protected Group(int id) {
		this.id = id;
		this.inValley = true;
		this.finishTour = false;
	}

	//get a new Group instance with a unique Id
	public static Group getNewGroup() {
		return new Group(nextId++);
	}

	//produce the Id of this group
	public int getId() {
		return this.id;
	}
	/**
	 * @return  whether group is still in valley (when first arrive)
	 */
	public synchronized  boolean isInValley() {
		return this.inValley;
	}
	/**
	 * set group as not in valley any more
	 */
	public synchronized  void leaveValley() {
		inValley = !inValley;
	}
	/**
	 * @return  whether group has finished tour
	 */
	public  synchronized  boolean tourFinished() {
		return this.finishTour;
	}
	/**
	 * mark group as finished tour
	 */
	public synchronized void finishTour() {
		this.finishTour = true;
	}
	//produce an identifying string for the group
	public   String toString() {
		return "group [" + id + "]";
	}
}
