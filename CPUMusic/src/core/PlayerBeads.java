package core;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.GranularSamplePlayer;

public class PlayerBeads implements interfacePlayer{

	AudioContext ac;
	Gain g;
	public GranularSamplePlayer player;
	public Clock clock;

	String file;
	float volume = 0;

		public PlayerBeads(){
			ac = new AudioContext();
			player = new GranularSamplePlayer(ac, new Sample(0));
		}

	public void setup(String file){
		this.file = file;
		ac.stop();
		g = new Gain(ac, 2, 0.7f);
//		player.reTrigger();
		player = new GranularSamplePlayer(ac, SampleManager.sample(this.file));
		g.addInput(player);
//		Envelope rateEnvelope = new Envelope(ac, SampleManager.sample(this.file).getSampleRate());
//		player.setRate(rateEnvelope);
//		player.reset();
//		player.setSample(SampleManager.sample(this.file));
		clock = new Clock(ac, player.getRateUGen());
		ac.out.addDependent(clock);
		g.removeAllConnections(player);
		g.addInput(player);
		ac.out.addInput(g);
	}

	public void play(String file) {
			System.out.println(file);
			setup(file);
			ac.start();
			//player.start(); 
	}

	public void pause() {
		if (player != null && !player.isPaused()){
			player.pause(true);
		}
	}

	public void stop() {
		if (player != null && file != null){
			ac.stop();
			player.removeAllConnections(player.getRateUGen());
			player.kill();
			ac.out.removeDependent(clock);
			clock = null;
			ac.out.removeAllConnections(g);
			g = null;
			SampleManager.removeSample(this.file);
			file = null;
		}
	}

	public void seek(double sec) {
		player.setPosition(player.getSample().getLength()*(sec / 100.0));
	}

	public void mute(boolean mute) {
		if (mute){
			volume = g.getGain();
			g.setGain(0);
		}else{
			g.setGain(volume);
		}
	}

	public void set_volume(float f) {
		g.setGain(f);
	}

	public double get_volumeAtual() {
		float f = Math.max(volume, g.getGain());
		volume = 0;
		return f;
	}

	public double get_Tempo() {
		return player.getPosition();
	}

	public void reproducao() {
		// TODO Auto-generated method stub

	}

	public long Microseconds() {
		// TODO Auto-generated method stub
		return 0;
	}

	public float get_maximo() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double get_minimo() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String get_album() {
		// TODO Auto-generated method stub
		return null;
	}

	public String get_title() {
		// TODO Auto-generated method stub
		return null;
	}

	public String get_author() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		PlayerBeads player = new PlayerBeads();
		player.play("audio/teste.mp3");
		try {
			Thread.sleep((long)player.player.getSample().getLength()+10000);
			player.play("audio/teste.mp3");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
