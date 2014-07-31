package core;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.Noise;
import net.beadsproject.beads.ugens.SamplePlayer;

public class beads {

	public static void main(String[] args) {

		AudioContext ac;

		ac = new AudioContext();
		/*
		 * In lesson 4 we played back samples. This example is almost the same
		 * but uses GranularSamplePlayer instead of SamplePlayer. See some of
		 * the controls below.
		 */
		String audioFile = "audio/teste.mp3";
		GranularSamplePlayer player = new GranularSamplePlayer(ac, new Sample(0));
		player.setSample(SampleManager.sample(audioFile));
		/*
		 * Have some fun with the controls.
		 */
		// loop the sample at its end points
//		player.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
//		player.getLoopStartUGen().setValue(0);
//		player.getLoopEndUGen().setValue(
//				(float)SampleManager.sample(audioFile).getLength());
//		// control the rate of grain firing
//		Envelope grainIntervalEnvelope = new Envelope(ac, 100);
//		grainIntervalEnvelope.addSegment(20, 10000);
//		player.setGrainInterval(grainIntervalEnvelope);
//		// control the playback rate
//		Envelope rateEnvelope = new Envelope(ac, 1);
//		rateEnvelope.addSegment(1, 5000);
//		rateEnvelope.addSegment(0, 5000);
//		rateEnvelope.addSegment(0, 2000);
//		rateEnvelope.addSegment(-0.1f, 2000);
//		player.setRate(rateEnvelope);
//		// a bit of noise can be nice
//		player.getRandomnessUGen().setValue(0.01f);
		/*
		 * And as before...
		 */
		Gain g = new Gain(ac, 2, 0.2f);
		g.addInput(player);
		ac.out.addInput(g);

		Clock clock = new net.beadsproject.beads.ugens.Clock(ac, player.getRateUGen());
		/*
		 * Tell the clock to tick (you probably don't want
		 * to do this except for debugging.
		 */

//		clock.addMessageListener(new Bead() {
//			public void messageReceived(Bead message) {
//				System.out.println("cu");
//			}
//		}
//				);
//		ac.out.addDependent(clock);


		/*
		 * Now this is new, because the clock doesn't have
		 * any outputs, we can't add it to the AudioContext
		 * and that means it won't run. So we use the method
		 * addDependent() instead.
		 */

		ac.start();
		ac.start();
	}

}