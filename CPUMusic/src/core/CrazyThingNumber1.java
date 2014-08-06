package core;

import dados.Sensors;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.Noise;
import net.beadsproject.beads.ugens.OnePoleFilter;
import net.beadsproject.beads.ugens.Reverb;
import net.beadsproject.beads.ugens.SamplePlayer;

public class CrazyThingNumber1 {
	
	public static void main(String[] args) throws InterruptedException {
		int FILTER = 2;
		double POSITION = 0;
		int ENVELOPE = 1;

		AudioContext ac;

		ac = new AudioContext();
		
		String audioFile = "audio/teste2.mp3";
		GranularSamplePlayer player = new GranularSamplePlayer(ac, new Sample(0));
		player.setSample(SampleManager.sample(audioFile));
		//OnePoleFilter filter1 = new OnePoleFilter(ac, 100);
		//filter1.addInput(player);
		BiquadFilter filter1 = new BiquadFilter(ac, BiquadFilter.BP_SKIRT, 400, 0.5f);
		filter1.addInput(player);
		
		//BiquadFilter filter2 = new BiquadFilter(ac, 500, BiquadFilter.BESSEL_HP);
		//filter2.addInput(player);
		//player.setToEnd();
		//Glide rateValue = new Glide(ac, 1, -1);
		//Envelope rateEnvelope1 = new Envelope(ac, -1);
		//Envelope rateEnvelope2 = new Envelope(ac, 1);
		
		//player.setRate(rateEnvelope1);
		
		Gain g = new Gain(ac, 2, 0.2f);
		g.addInput(filter1);
		g.addInput(player);
		
		
		//Reverb r = new Reverb(ac, 2);
		//r.setSize(0.9f);
		//r.setDamping(0.7f);
		//r.addInput(g);
		
		
		//ac.out.addInput(r);
		Sensors sensor = new Sensors();
		

		ac.start();
		ac.start();
		while(true){
			//simulando a thread
			Thread.sleep(2000);
			g.removeAllConnections(filter1);
			filter1 = new BiquadFilter(ac, BiquadFilter.BP_SKIRT, (float) (sensor.getInformationsAboutMemory()*10) , 3f);
			filter1.addInput(player);
			g.addInput(filter1);
			
			
			
//			player.setPosition(POSITION);
//			POSITION = POSITION + 50000;
//			if(ENVELOPE == 1){
//				player.setRate(rateEnvelope2);
//				ENVELOPE = 2;
//			}else{
//				player.setRate(rateEnvelope1);
//				ENVELOPE = 1;
//			}	
			
//			if(FILTER == 1){
//				g.removeAllConnections(filter1);	
//				g.addInput(filter2);
//				FILTER = 2;
//			}else{
//				g.removeAllConnections(filter2);
//				g.addInput(filter1);
//				FILTER = 1;
//			}
		
		
			ac.out.addInput(g);
		}
		
	}

}
