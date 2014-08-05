package core;

import javafx.scene.control.RadioButton;
import dados.Sensors;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;

public class PlayerBeads implements interfacePlayer{

	private static final String NOFX = "Sem efeito";
	private static final String GRANULAR = "Sintese Granular";

	Sensors sensor;
	float TamanhoMusica;
	Thread thread;
	public String fx;

	AudioContext ac;
	Gain g;
	public GranularSamplePlayer player;
	public Clock clock;

	String file;
	float volume = 0;

	public PlayerBeads(){
		fx = NOFX;
		sensor = new Sensors();
		ac = new AudioContext();
		player = new GranularSamplePlayer(ac, new Sample(0));
	}

	public void setup(String file){
		this.file = file;
		ac.stop();
		g = new Gain(ac, 2, 0.7f);
		player = new GranularSamplePlayer(ac, SampleManager.sample(this.file));
		TamanhoMusica = (float)SampleManager.sample(file).getLength();
		conf();
		g.addInput(player);
		clock = new Clock(ac, player.getRateUGen());
		ac.out.addDependent(clock);
		g.removeAllConnections(player);
		g.addInput(player);
		ac.out.addInput(g);
	}

	public void conf(){		
		switch (fx) {
		case NOFX:
			if (thread != null){
				thread.stop();
			}
			break;
		case GRANULAR:
			if (thread != null){
				thread.stop();
			}
			player.setRandomness (new Glide(ac, 0.1f, TamanhoMusica)); // mudar só esse parametro, de 0 a 5 , onde 5 é bem locão e 0 é normals, valor ideal para ficar variando é entre 0 e 0.9f
			player.setGrainInterval(new Glide(ac, 10, TamanhoMusica));
			player.setGrainSize(new Glide(ac, 40, TamanhoMusica));
			player.setPitch(new Glide(ac, 1, TamanhoMusica));
			player.setInterpolationType(SamplePlayer.InterpolationType.ADAPTIVE); // sei nem o que é direito, botei aqui pq vi na api, mas ficou interessante.
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//System.out.println( "vije " +(float) (sensor.getInformationsAboutCPU()+1));
						player.setRandomness (new Glide(ac, (float)(sensor.getInformationsAboutMemory()/50), TamanhoMusica));
						player.setPitch(new Glide(ac, (float)((sensor.getInformationsAboutCPU()*0.01)+1), TamanhoMusica));
						//valorPitch = valorPitch + 0.005f;
						//valor = valor + 0.05f;
						System.out.println("É, é possivel fazer em thread.");
					}
				}
			});
			thread.start();
			break;
		default:
			if (thread != null){
				thread.stop();
			}
			break;
		}
	}

	public void play(String file) {
		if (player != null && player.isPaused()){
			player.pause(false);
		}else{
			System.out.println(file);
			setup(file);
			ac.start();
			//player.start();
		}
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
