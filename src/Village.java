/**
 * host tour groups by providing arrive and depart functionality
 * 
 * @author yuweib@student.unimelb.edu.au
 *
 */
public class Village{
	// whether there is group in village
	protected volatile boolean occupied;
	// current hosted group
	protected volatile Group group;
	
	private int village_ID;
	
	public Village(int id) {
		this.village_ID = id;
		this.occupied = false;
		this.group = null;
	}
	
	/**
	 * host the upcoming new group
	 * @param group upcoming group
	 */
	public synchronized void arrive(Group group) {
		// let the thread wait if village is already occupied
		while ( occupied ) {
			try {
				wait ();
			}
			catch ( InterruptedException e) {}
		}
		// mark village as occupied to prevent other thread to 
		// arrive group at the same time
		this.occupied = true;
		// save new hosted group
		this.group = group;
		// print out trace information
		System.out.println(this.group.toString() + 
				" enters  village " + this.village_ID);
		
		notifyAll();
		
	}
	
	/**
	 * depart current hosting group
	 * @return leaving group
	 */
	public synchronized Group depart() {
		// let the thread wait if village is empty now
		while ( !occupied ) {
			try {
				wait ();
			}
			catch ( InterruptedException e) {}
		}
		// mark village as unoccupied to prevent other thread to 
		// depart group at the same time
		this.occupied = false;
		// print out trace information
		System.out.println(this.group.toString() +" leaves village " + this.village_ID);
		notifyAll();
		// return departing group
		return this.group;
	}
	
	/**
	 * debug method
	 * @return text format of the village
	 */
	@Override
	public String toString() {
		return "V"+this.village_ID;
	}

}
