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
import net.beadsproject.beads.ugens.SamplePlayer;

public class beads {

	public static void main(String[] args) throws InterruptedException {
		
		// Sintese Granular motherfuckermente louca.
		
		AudioContext ac;
		float valor = 0.1f;
		float valorPitch = 1;
		ac = new AudioContext();

		String audioFile = "audio/teste.mp3";
		GranularSamplePlayer player = new GranularSamplePlayer(ac, new Sample(0));
		player.setSample(SampleManager.sample(audioFile));
		float TamanhoMusica = (float)SampleManager.sample(audioFile).getLength();
		
		player.setRandomness (new Glide(ac, 0.1f, TamanhoMusica)); // mudar só esse parametro, de 0 a 5 , onde 5 é bem locão e 0 é normals, valor ideal para ficar variando é entre 0 e 0.9f
		player.setGrainInterval(new Glide(ac, 10, TamanhoMusica));
		player.setGrainSize(new Glide(ac, 40, TamanhoMusica));
		player.setPitch(new Glide(ac, 1, TamanhoMusica));
		player.setInterpolationType(SamplePlayer.InterpolationType.ADAPTIVE); // sei nem o que é direito, botei aqui pq vi na api, mas ficou interessante.

		Gain g = new Gain(ac, 2, 0.4f);
		g.addInput(player);
		ac.out.addInput(g);
		
		
		Clock clock = new net.beadsproject.beads.ugens.Clock(ac, player.getRateUGen());


		ac.start();
		ac.start();
		while(true){
			//simulando a thread
			Thread.sleep(1000);
			
			player.setRandomness (new Glide(ac, valor, TamanhoMusica));
			player.setPitch(new Glide(ac, valorPitch, TamanhoMusica));
			valorPitch = valorPitch + 0.005f;
			valor = valor + 0.05f;
			System.out.println("É, talvez seja possivel fazer em thread.");
		}
	}

}