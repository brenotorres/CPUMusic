package core;
import org.hyperic.sigar.SigarException;

import dados.Sensors;
public class core {

	public static void main(String[] args) throws Exception{
		final Sensors sensor = new Sensors();



		Thread MEM = new Thread(){
			public void run(){
				while(true){
					try {
						Thread.sleep(1000);
						sensor.getInformationsAboutMemory();
					} catch (InterruptedException e) {

						e.printStackTrace();
					}	

				}

			}
		};
		MEM.start();


		Thread CPU = new Thread(){
			public void run(){
				while(true){
					try {
						Thread.sleep(1000);
						//ta dando diferente do do windows, conferi se meu calculo da porcentagem é coerente
						sensor.getInformationsAboutCPU();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		};
		CPU.start();

		Thread Disk = new Thread(){
			public void run(){
				while(true){
					try {
						//Thread.sleep(1000);
						//ta dando diferente do do windows, conferi se meu calculo da porcentagem é coerente
						sensor.getInformationsAboutDISK();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (SigarException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		};
		Disk.start();



		Disk.join();
		MEM.join();	
		CPU.join();




	}
}
