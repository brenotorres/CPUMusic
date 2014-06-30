package core;
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
		
		
		
		
		MEM.join();	
		CPU.join();
		



}
}
