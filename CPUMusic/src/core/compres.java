package core;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Compressor;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.SamplePlayer;

public class compres {

	public static void main(String[] args) {
		AudioContext ac = new AudioContext();
		String sourceFile = "audio/teste.mp3";
		SamplePlayer sp = null;
		try { 
			sp = new SamplePlayer(ac, SampleManager.sample(sourceFile));
		}
		catch(Exception e)
		{
			System.out.println("Exception while attempting to load sample!");
			e.printStackTrace();
		}
		sp.setKillOnEnd(false);
		// Create a new compressor with a single output channel.
		Compressor c = new Compressor(ac, 1);

		// The attack is how long it takes for compression to ramp 
		// up, once the threshold is crossed.
		c.setAttack(100);
		// The decay is how long it takes for compression to trail
		// off, once the threshold is crossed in the opposite 
		// direction.
		c.setDecay(200);

		// The ratio and the threshold work together to determine 
		// how much a signal is squashed. The ratio is the 
		// NUMERATOR of the compression amount 2.0 = 2:1 = for 
		// every two decibels above the threshold, a single decibel
		// will be output. The threshold is the loudness at which 
		// compression can begin
		c.setRatio(4.0f);
		c.setThreshold(0.6f);

		// The knee is an advanced setting that you should leave 
		// alone unless you know what you are doing.
		//c.setKnee(0.5);

		// connect the SamplePlayer to the compressor
		c.addInput(sp);
		Glide gainValue = new Glide(ac, 0.0f);
		Gain g = new Gain(ac, 1, gainValue);
		// connect the Compressor to the gain
		g.addInput(c); 
		// connect the Compressor to the AudioContext
		ac.out.addInput(c);
		ac.start();
	}
}