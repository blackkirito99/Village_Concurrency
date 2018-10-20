/**
 * Train transfer tour groups between cable car and villages
 * 
 * We assume each train starts from its source
 * and after deliver a group, it will go back to its source immediately
 * 
 * There are three train types:
 * 1) first train: 	transfer group from up-going cable car to first village
 * 2) middle train: 	transfer group between villages
 * 3) last train: 	transfer group from  last village to down-going cable car 
 * 
 * @author yuweib@student.unimelb.edu.au
 *
 */

public class Train extends Thread{
	private CableCar cableCar;
	private Village startingVillage;
	private Village endingVillage;
	// all train type
	public enum TrainType {
	    FIRSTTRAIN, MIDDLETRAIN, LASTTRAIN
	}
	// field to save the train type
	private TrainType currentType;
	
	// construct middle train
	public Train(Village village1, Village village2) {
		this.startingVillage = village1;
		this.endingVillage = village2;
		// save the train type
		this.currentType = TrainType.MIDDLETRAIN;
	}
	
	// construct first train
	public Train(CableCar cableCar, Village village2) {
		this.cableCar = cableCar;
		this.endingVillage = village2;
		// save the train type
		this.currentType = TrainType.FIRSTTRAIN;
	}
	
	// construct last train
	public Train(Village village1, CableCar cableCar) {
		this.startingVillage = village1;
		this.cableCar = cableCar;
		// save the train type
		this.currentType = TrainType.LASTTRAIN;
	}

	public void run() {
		while(!isInterrupted()) {
			try {
				Group group;
				switch(this.currentType) {
				case MIDDLETRAIN: 
					// get group from previous village
					group = this.startingVillage.depart();
					
					// wait train to come to its destination village
					sleep(Params.JOURNEY_TIME);	
					this.endingVillage.arrive(group);
					
					// wait train to come back (source position)
					sleep(Params.JOURNEY_TIME);
					break;
					
				case FIRSTTRAIN:
					// get group from up-going cable car
					group = this.cableCar.depart(true);
					
					// wait train to come to its destination first village
					sleep(Params.JOURNEY_TIME);	
					this.endingVillage.arrive(group);
					
					// wait train to come back (source position)
					sleep(Params.JOURNEY_TIME);
					break;
				case LASTTRAIN:
					// get group from last village
					group = this.startingVillage.depart();
					
					// wait train to come to its destination down-going cable car
					sleep(Params.JOURNEY_TIME);	
					this.cableCar.arrive(group);
					
					// wait train to come back (source position)
					sleep(Params.JOURNEY_TIME);
					break;
				default:
					break;
				}			
			}
			catch (InterruptedException e) {
				this.interrupt();
			}
		}
	}


}
