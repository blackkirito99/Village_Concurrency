/**
 * carry tour groups by providing arrive and depart functionality
 * 
 * Cable car contains terminus, valley and its moving process
 * 
 * @author yuweib@student.unimelb.edu.au
 *
 */
public class CableCar {
	// whether there is group in cable car
	protected volatile boolean occupied;
	
	// current carried group
	protected volatile Group group;
	
	// whether cable car is in valley or in terminus 
	private volatile boolean inValley;
	
	// current number of group in attraction
	private volatile int groups_count;
	
	public CableCar() {
		this.occupied = false;
		this.group = null;
		this.inValley = true;
		this.groups_count = 0;
	}
	/**
	 * carry the upcoming new group
	 * @param group upcoming group
	 */
	public synchronized void arrive(Group group) {
		// if cable car is already occupied 
		// or if cable car's location is different from group's location
		// let the thread wait 
		while ( this.occupied || this.inValley != group.isInValley() || (this.groups_count  > Params.VILLAGES) && this.inValley) {
			try {
				if(group!= null) {
					//System.out.println(this.group + "is waiting a " + group.tourFinished() + " " + inValley);
				} 
				wait ();
				
			}
			catch ( InterruptedException e) {}
		}
		// mark cable car as occupied to prevent other thread to 
		// enter group into cable car at the same time
		this.occupied = true;
		// save new carried group
		this.group = group;
		// print out trace information
		System.out.println(this.group.toString() +
				" enters cable car to go " + (inValley ? "up" : "down") );
		// mark the group as not in valley 
		
		// if new  group arrive, increase count, and mark it as not in valley any more
		if(this.inValley) {
			this.group.leaveValley();
			this.groups_count  += 1;
		}
		
		notifyAll();
		
	}
	/**
	 * depart current carried group
	 * because cable car have two kinds of depart
	 * 1. consumer calls
	 * 2. first train calls
	 * I add a parameter to distinct them
	 * @param 	startTour	Whether 
	 * @return 	leaving group
	 */
	public synchronized Group depart(boolean startTour) {
		// 
		if(startTour) {
			// when group starts a tour from valley
			// if cable car is not occupied 
			// or if carried group has already finished tour
			// or if cable car is not from valley
			// let the thread wait 
			while (!this.occupied || this.group.tourFinished() || !this.inValley) {
				try {
					wait ();
				}
				catch ( InterruptedException e) {}
			}
			// mark group as tour finished, 
			//because when next time group get this process, it finished tour already
			this.group.finishTour();
			
			// update cable car location
			this.inValley = false;
		}else {
			// when group starts a tour from valley
			// if cable car is not occupied 
			// or if carried group has not yet finished tour
			// or if cable car is from valley
			// let the thread wait 
			while (!this.occupied || !this.group.tourFinished() || this.inValley) {
				try {
					wait ();
					
				}
				catch ( InterruptedException e) {}
			}
			// update cable car location
			this.inValley = true;
			
			// reduce count because group is leaving the attraction
			this.groups_count  -= 1;
		}
		// mark cable car as unoccupied to prevent other thread to 
		// depart group from cable car at the same time
		this.occupied = false;
		// print out trace information
		System.out.println(this.group.toString() +" leaves the cable car");
		
		notifyAll();
		// return leaving group
		return this.group;
	}

	/**
	 * operate cable car up and down when no group is using it
	 */
	public synchronized void operate() {
		// if cable car is occupied 
		// let the thread wait 
		while ( this.occupied ) {
			try {
				wait ();
			}
			catch ( InterruptedException e) {}
		}
		// print out trace information
		System.out.println("cable car " + (inValley ? "ascends" : "descends") );
		
		// update cable car location
		this.inValley = !this.inValley;
		
		notifyAll();
	}
	/**
	 * debug method
	 * @return text format of the cable car
	 */
	@Override
	public String toString() {
		return "CC";
	}

}
