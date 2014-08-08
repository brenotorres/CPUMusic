package core;

import dados.Sensors;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.GranularSamplePlayer;

public class TimeShifter {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		AudioContext ac;

		ac = new AudioContext();
		
		String audioFile = "audio/teste2.mp3";
		GranularSamplePlayer player = new GranularSamplePlayer(ac, new Sample(0));
		player.setSample(SampleManager.sample(audioFile));
		
		Gain g = new Gain(ac, 2, 0.2f);
		g.addInput(player);
		
		Sensors sensor = new Sensors();
		

		ac.start();
		ac.start();
		
		while(true){
			Thread.sleep(2000);
			double cpu = sensor.getInformationsAboutCPU();
			float speed;
			if(cpu >40){
				speed = (float) cpu/50;
			}else{
				speed = 0.8f;
			}
			Envelope rateEnvelope = new Envelope(ac, speed);
			player.setRate(rateEnvelope);
			ac.out.addInput(g);
		}

	}

}
