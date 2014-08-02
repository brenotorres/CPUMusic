package core;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.Reverb;

public class CrazyThingNumber2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AudioContext ac;

		ac = new AudioContext();

		String audioFile = "audio/teste.mp3";
		GranularSamplePlayer player = new GranularSamplePlayer(ac, new Sample(0));
		player.setSample(SampleManager.sample(audioFile));

		Gain g = new Gain(ac, 2, 0.8f);
		g.addInput(player);
		Reverb r = new Reverb(ac, 2);
		r.setSize(0.9f);
		r.setDamping(0.5f);
		// You can also control a Reverb's early reflections and 
		// late reverb.
		// To do so, use r.setEarlyReflectionsLevel(0-1); or 
		// r.setLateReverbLevel(0-1);
		r.addInput(g);


		//ac.out.addInput(r);

		ac.out.addInput(g);

		ac.start();
		ac.start();

	}

}
