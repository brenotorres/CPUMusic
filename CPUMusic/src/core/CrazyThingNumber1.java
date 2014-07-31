package core;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.Noise;
import net.beadsproject.beads.ugens.OnePoleFilter;
import net.beadsproject.beads.ugens.SamplePlayer;

public class CrazyThingNumber1 {
	
	public static void main(String[] args) {

		AudioContext ac;

		ac = new AudioContext();
		/*
		 * In lesson 4 we played back samples. This example is almost the same
		 * but uses GranularSamplePlayer instead of SamplePlayer. See some of
		 * the controls below.
		 */
		String audioFile = "D:/Music/iTunes/iTunes Media/Music/Disclosure _ Settle (Deluxe Edition)/Settle (Deluxe Version)/03 Latch (Album Version).mp3";
		GranularSamplePlayer player = new GranularSamplePlayer(ac, new Sample(0));
		player.setSample(SampleManager.sample(audioFile));
		OnePoleFilter filter1 = new OnePoleFilter(ac, 200);
		filter1.addInput(player);
		
		
		
		
		Gain g = new Gain(ac, 2, 0.2f);
		//g.addInput(player);
		
		g.addInput(filter1);
		
		ac.out.addInput(g);

		ac.start();
		ac.start();
	}

}
