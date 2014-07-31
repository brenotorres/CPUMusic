package core;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public interface interfacePlayer {
      
    void play(String path);
      
    void pause();
    
    void stop();
    
    void seek(double sec);
    
    String get_album();
    
    String get_title();
    
    String get_author();
    
    void set_volume(float f);
    	
    float get_maximo();
    
    double get_minimo();
    
	double get_volumeAtual();

	double get_Tempo();

	void mute(boolean mute);

	void reproducao();

	long Microseconds();

	//void updateValues();

}