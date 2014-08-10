package core;

import javafx.scene.control.RadioButton;
import dados.Sensors;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.Reverb;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.TapIn;
import net.beadsproject.beads.ugens.TapOut;

public class PlayerBeads implements interfacePlayer{

	private static final String NOFX = "Sem efeito";
	private static final String GRANULAR = "Pitch shift";
	private static final String TIME = "Time stretch";
	private static final String FILTRO = "Filtro";
	private static final String REVERB = "Reverb";
	private static final String FLANGER = "Flanging";

	public Sensors sensor;
	float TamanhoMusica;
	Thread thread;
	public String fx;
	BiquadFilter filter1;
	TapIn delayIn;
	Gain delayGain;

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
		filter1 = null;
		delayIn = null;
		delayGain = null;
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
				if (filter1 != null){
					ac.out.removeAllConnections(g);
					g.removeAllConnections(filter1);
					g = new Gain(ac, 2, 0.7f);
					g.addInput(player);
					ac.out.addInput(g);
					filter1 = null;
				}
				if (delayIn != null && delayGain != null){
					delayIn.removeAllConnections(g);
					ac.out.removeAllConnections(delayGain);
					delayGain = null;
					delayIn = null;
				}
			}
			break;
		case GRANULAR:
			if (thread != null){
				thread.stop();
				if (filter1 != null){
					ac.out.removeAllConnections(g);
					g.removeAllConnections(filter1);
					g = new Gain(ac, 2, 0.7f);
					g.addInput(player);
					ac.out.addInput(g);
					filter1 = null;
				}
				if (delayIn != null && delayGain != null){
					delayIn.removeAllConnections(g);
					ac.out.removeAllConnections(delayGain);
					delayGain = null;
					delayIn = null;
				}
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
						player.setRandomness (new Glide(ac, (float)(sensor.getInformationsAboutMemory()/100), TamanhoMusica));
						player.setPitch(new Glide(ac, (float)((sensor.getInformationsAboutCPU()*0.01)+1), TamanhoMusica));
						//valorPitch = valorPitch + 0.005f;
						//valor = valor + 0.05f;
						System.out.println("Pitch");
					}
				}
			});
			thread.start();
			break;
		case TIME:
			if (thread != null){
				thread.stop();
				if (filter1 != null){
					ac.out.removeAllConnections(g);
					g.removeAllConnections(filter1);
					g = new Gain(ac, 2, 0.7f);
					g.addInput(player);
					ac.out.addInput(g);
					filter1 = null;
				}
				if (delayIn != null && delayGain != null){
					delayIn.removeAllConnections(g);
					ac.out.removeAllConnections(delayGain);
					delayGain = null;
					delayIn = null;
				}
			}
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						double cpu = sensor.getInformationsAboutCPU();
						float speed;
						if(cpu >8 && cpu <20){
							speed = (float) cpu/10;
						}else if (cpu >= 20){
							speed = 2;
						}else{
							speed = 0.8f;
						}
						Envelope rateEnvelope = new Envelope(ac, speed);
						player.setRate(rateEnvelope);
						System.out.println("time");
					}
				}
			});
			thread.start();
			break;
		case FILTRO:
			if (thread != null){
				thread.stop();
				if (filter1 != null){
					ac.out.removeAllConnections(g);
					g.removeAllConnections(filter1);
					g = new Gain(ac, 2, 0.7f);
					g.addInput(player);
					ac.out.addInput(g);
					filter1 = null;
				}
				if (delayIn != null && delayGain != null){
					delayIn.removeAllConnections(g);
					ac.out.removeAllConnections(delayGain);
					delayGain = null;
					delayIn = null;
				}
			}
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					filter1 = new BiquadFilter(ac, BiquadFilter.BP_SKIRT, 100, 2.5f);
					filter1.addInput(player);
					g.addInput(filter1);
					while(true){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						ac.out.removeAllConnections(g);
						g.removeAllConnections(filter1);
						g = new Gain(ac, 2, 0.7f);
						//g.addInput(filter1);
						//g.addInput(player);
						double memo = sensor.getInformationsAboutMemory();
						float freq;
						if((memo - 50) > 0){
							freq = (float) (100 + (memo-50)*15);
						}else{
							freq = (float) 100;
						}
						filter1 = new BiquadFilter(ac, BiquadFilter.BP_SKIRT, freq, 2.5f);
						filter1.addInput(player);
						g.addInput(filter1);
						ac.out.addInput(g);

						System.out.println("filtro");
					}
				}
			});
			thread.start();
			break;
		case REVERB:
			if (thread != null){
				thread.stop();
				if (filter1 != null){
					ac.out.removeAllConnections(g);
					g.removeAllConnections(filter1);
					g = new Gain(ac, 2, 0.7f);
					g.addInput(player);
					ac.out.addInput(g);
					filter1 = null;
				}
				if (delayIn != null && delayGain != null){
					delayIn.removeAllConnections(g);
					ac.out.removeAllConnections(delayGain);
					delayGain = null;
					delayIn = null;
				}
			}
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					Reverb r = new Reverb(ac, 2);
					r.setSize(0.9f);
					r.setDamping(0.5f);
					r.addInput(g);
					while(true){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						double memo = sensor.getInformationsAboutMemory();
						float freq;
						if(memo > 0){
							freq = (float) memo/100;
						}else{
							freq = (float) 0;
						}
						r.setSize(freq);

						System.out.println("reverb");
					}
				}
			});
			thread.start();
			break;
		case FLANGER:
			if (thread != null){
				thread.stop();
				if (filter1 != null){
					ac.out.removeAllConnections(g);
					g.removeAllConnections(filter1);
					g = new Gain(ac, 2, 0.7f);
					g.addInput(player);
					ac.out.addInput(g);
					filter1 = null;
				}
				if (delayIn != null && delayGain != null){
					delayIn.removeAllConnections(g);
					ac.out.removeAllConnections(delayGain);
					delayGain = null;
					delayIn = null;
				}
			}
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					delayIn = new TapIn(ac, 2000);
					delayIn.addInput(g);
					TapOut delayOut = new TapOut(ac, delayIn, 50);
					delayGain = new Gain(ac, 1, 0.7f);
					delayGain.addInput(delayOut);
					delayIn.addInput(delayGain);
					ac.out.addInput(delayGain);
					while(true){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						double cpu = sensor.getInformationsAboutMemory();
						delayOut.setDelay((float)cpu%20);
						System.out.println("flanger");
					}
				}
			});
			thread.start();
			break;
		default:
			if (thread != null){
				thread.stop();
				if (filter1 != null){
					ac.out.removeAllConnections(g);
					g.removeAllConnections(filter1);
					g = new Gain(ac, 2, 0.7f);
					g.addInput(player);
					ac.out.addInput(g);
					filter1 = null;
				}
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
