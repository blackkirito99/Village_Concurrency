/**
 * operate cable car occationally up and down when it is not occupied
 * 
 *@author yuweib@student.unimelb.edu.au
 *
 */
public class Operator extends Thread{
	// the cable car to be operated
	private CableCar cableCar;
	
	public Operator(CableCar cableCar) {
		this.cableCar = cableCar;
	}

	public void run() {
		// sleep this tread at first to prevent influencing first group
		try {
			sleep(200);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(!isInterrupted()) {
			try {
				// operate the cable car
				cableCar.operate();
				
				// wait for the cable car to operate
				sleep(Params.OPERATE_TIME);
				
				// let some time pass before the next operation
				sleep(Params.operateLapse());
			}
			catch (InterruptedException e) {
				this.interrupt();
			}
		}
	}
}
