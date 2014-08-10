package gui;

import java.io.File;
import java.util.Vector;

import org.hyperic.sigar.SigarException;

import sun.rmi.runtime.NewThreadAction;

import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.ugens.Envelope;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.media.Media;
import javafx.application.Application;
import javafx.beans.property.LongPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import core.Mp3;
import core.PlayerBeads;
import core.PlayerJavaFX;
import core.PlayerMusic;
import core.interfacePlayer;
import core.RepositorioMusica;


public class Gui extends Application {

	PlayerBeads player;
	private int lineSelect;
	//	private int lineSelectp;
	private boolean repeat = false;
	private Label playTime;
	private TextField musicTab = new TextField();
	//	private Label playTimep;


	//Slider volumep = new Slider();
	//Slider reproducaop = new Slider(); // 9
	final Button botPlay = new Button(); 	
	final Button botPause = new Button();
	final Button botStop = new Button();
	final Button botMute = new Button();
	final Button botAnt = new Button();
	final Button botPro = new Button();

	final Image iplay = new Image(getClass().getResourceAsStream("play.png"));
	final Image ipause = new Image(getClass().getResourceAsStream("pause.png"));
	final Image ifone = new Image(getClass().getResourceAsStream("fone.png"));
	final Image istop = new Image(getClass().getResourceAsStream("stop.png"));
	final Image imute = new Image(getClass().getResourceAsStream("mute.png"));
	final Image iantes = new Image(getClass().getResourceAsStream("anterior.png"));
	final Image iproximo = new Image(getClass().getResourceAsStream("proximo.png"));


	Label UsoCPU;
	Label UsoMem;
	Label UsoDisco;


	public double tempo;
	public  long seek=0;
	public static boolean tre = false;
	public static interfacePlayer i = new PlayerMusic();
	private Scene cena ;
	private int state = UNKNOWN;	
	private static final int UNKNOWN = -1;
	private static final int PLAYING = 0;
	private static final int PAUSED = 1;
	private static final int STOPPED = 2;
	private static final int SEEKING = 3;
	private boolean mute = false;
	private String diretorio = "";
	private Vector<Mp3> vectorMp3 = new Vector<Mp3>();
	static TableView<Mp3> table = new TableView();
	//private TableView playlist = new TableView();
	private TableView tabledown = new TableView();
	private Slider volume = new Slider();
	final Slider reproducao = new Slider(); // 9
	private File f;
	final ObservableList<Mp3> data = FXCollections.observableArrayList();
	public static LongPropertyBase a;
	public boolean thread = false;
	final ObservableList<Mp3> datagenero = FXCollections.observableArrayList();
	final ObservableList<Mp3> dataArtista = FXCollections.observableArrayList();
	private Menu menuGen = new Menu("Gênero");
	private Menu menuArtist = new Menu("Artista");
	private Menu menuCustons = new Menu("Customizadas");
	private Menu menuAll = new Menu("Outros");


	public static void main(String[] args) {
		launch(); 
	}


	@Override
	public void start(final Stage palco) throws Exception {

		palco.getIcons().add(new Image(getClass().getResourceAsStream("caja.png")));
		final VBox raiz = new VBox(10); 
		final VBox configini = new VBox(10);
		cena = new Scene(configini, 300, 200); 
		//Color c = Color.web("0x01022FF",1.0);// blue as a hex web value, explicit alpha
		cena.setFill(Color.GRAY);//escolher a cor....

		Label d = new Label("Diretorio");
		final TextField dir = new TextField("");
		dir.setTooltip(new Tooltip("Digite o diretorio das musicas que deseja tocar com o Cajá Player"));
		Button config = new Button("Configurar");

		configini.getChildren().addAll(d, dir, config);

		config.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento){
				//cena = new Scene(raiz, 800, 600);
				//Player player = new Player(mp);
				//Media m = new Media(null);
				//mp = new MediaPlayer(m);

				player = new PlayerBeads();
				cena.setRoot(raiz);

				palco.setWidth(1024);
				palco.setHeight(720);

				palco.setX(200);
				palco.setY(5);

				diretorio = dir.getText();

				//playlist.prefWidthProperty().bind(cena.widthProperty());
				//playlist.prefHeightProperty().bind(cena.heightProperty());
				table.prefWidthProperty().bind(cena.widthProperty());
				table.prefHeightProperty().bind(cena.heightProperty());

				System.out.println(table.heightProperty());
				table.prefHeight(500);
				palco.show();
				RepositorioMusica a = new RepositorioMusica(diretorio);

				vectorMp3 = a.gerarLista();

				datagenero();
				dataArtista();

				if(!vectorMp3.isEmpty()){
					int i = 0;
					while(i<vectorMp3.size()){
						data.add(vectorMp3.get(i));
						i++;
					}
				}

				TabPane tabPane = new TabPane();

				Tab tab = new Tab();
				tab.setText("Player");
				tab.setContent(Player1());
				tab.setClosable(false);

				Tab tab2 = new Tab();
				tab2.setText("Efeitos");
				tab2.setContent(Efeitos());
				tab2.setClosable(false);

				Tab tab3 = new Tab();
				tab3.setText("Configurações");
				tab3.setContent(Config());
				tab3.setClosable(false);

				//				Tab tab4 = new Tab();
				//				tab4.setText("Playlists");
				//				tab4.setContent(Playlists());
				//				tab4.setClosable(false);

				tabPane.getTabs().addAll(tab, tab2, tab3);
				raiz.getChildren().add(tabPane);




				new Thread(new Runnable() {
					@Override
					public void run() {

						while(true){
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Platform.runLater(new Runnable() {
							    @Override
							    public void run() {
									double mem = player.sensor.getInformationsAboutMemory();
//									UsoMem.setText("\n\n\nUso da Memória: " + String.format("%.2f", mem) + "%");
									double cpu = player.sensor.getInformationsAboutCPU();
									UsoCPU.setText("\n\n\nUso da CPU: "+String.format("%.2f", cpu) + "%                           "+
											"Uso da Memória: " + String.format("%.2f", mem) + "%");
//									try {
//										String disco = player.sensor.getInformationsAboutDISK();
//										UsoDisco.setText("\n\n\nUso do disco: "+disco);
//									} catch (SigarException e) {
//										e.printStackTrace();
//									} catch (InterruptedException e) {
//										e.printStackTrace();
//									}
							    }
							});
						}
					}
				}).start();

		}
	});


		palco.setTitle("Cajá player");
		palco.setScene(cena);

		palco.show(); // 1
}




private Pane Player1() {

	//HBox linharepro = new HBox();
	VBox vbox = new VBox();

	table.setEditable(true);

	playTime = new Label();
	playTime.setPrefWidth(130);
	playTime.setMinWidth(50);	


	TableColumn musicCol = new TableColumn("Música");

	musicCol.prefWidthProperty().bind(table.widthProperty().divide(4));
	musicCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("titulo"));

	TableColumn tempoCol = new TableColumn("Duração");

	tempoCol.prefWidthProperty().bind(table.widthProperty().divide(10));
	tempoCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("tempo"));

	TableColumn artistaCol = new TableColumn("Artista");
	artistaCol.prefWidthProperty().bind(table.widthProperty().divide(4));
	artistaCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("autor"));

	TableColumn albumCol = new TableColumn("Álbum");
	albumCol.prefWidthProperty().bind(table.widthProperty().divide(5));
	albumCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("album"));

	TableColumn generoCol = new TableColumn("Gênero");
	generoCol.prefWidthProperty().bind(table.widthProperty().divide(5));
	generoCol.setCellValueFactory(new PropertyValueFactory<Mp3, String>("genero"));

	table.getColumns().addAll(musicCol, tempoCol, artistaCol, albumCol, generoCol);
	table.setItems(data);

	table.setOnMouseClicked(new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {
			if (event.getClickCount()>1) {
				player.stop();
				state = STOPPED;
				botPlay.fire();
			}
		}
	});

	reproducao.setMin(0);
	reproducao.setMaxWidth(Double.MAX_VALUE);
	reproducao.prefWidthProperty().bind(cena.widthProperty());
	reproducao.setShowTickLabels(false); // 10
	reproducao.setShowTickMarks(false); // 11

	reproducao.valueProperty().addListener(new InvalidationListener() {
		public void invalidated(Observable ov) {
			if (reproducao.isValueChanging()) {
				System.out.println(Math.floor(reproducao.getValue())+"BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBbb");
				player.seek(Math.floor(reproducao.getValue()));
				//player.seek(10);
				updateValues();
			}
		}
	});

	a = new LongPropertyBase() {

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getBean() {
			// TODO Auto-generated method stub
			return null;
		}
	};

	a.addListener(new ChangeListener<Number>() {
		public void changed(ObservableValue<? extends Number> ov, Number old_v, Number new_v){
			reproducao.setValue( new_v.longValue() + seek);
		}
	});

	//		menuAll.setOnAction(new EventHandler<MouseEvent>() {
	//			@Override
	//			public void handle(MouseEvent evento){
	//				table.setItems(data);
	//			}
	//		});

	MenuItem todos = new MenuItem("Todas");
	MenuItem nenhuma = new MenuItem("Nenhuma");

	todos.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evento){
			table.setItems(data);
		}
	});

	nenhuma.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evento){
			ObservableList<Mp3> n = FXCollections.observableArrayList();
			table.setItems(n);
		}
	});

	menuAll.getItems().addAll(todos, nenhuma);

	MenuItem mescu = new MenuItem("Menos Tocadas");
	MenuItem Top = new MenuItem("Top 10");

	Top.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evento){
			Gerarplaytop10();			
		}
	});

	mescu.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evento){
			Gerarplaymenosouvidas();
		}
	});

	menuCustons.getItems().addAll(Top, mescu);

	MenuBar menuBar = new MenuBar();
	menuBar.getMenus().addAll(menuArtist, menuGen, menuCustons, menuAll);


	HBox rep= new HBox();
	playTime.setText("--:--/--:--");


	rep.getChildren().addAll(reproducao, playTime);
	vbox.getChildren().addAll(menuBar, Menuplayer(), new Separator(), rep, new Separator(), table);

	return vbox;
}

Node Menuplayer(){
	HBox hbox = new HBox();

	volume.setMaxWidth(100); 
	volume.setShowTickLabels(false); // 10
	volume.setShowTickMarks(false); // 11
	volume.setMin(0);
	volume.setMax(1);
	volume.setValue(0.7);

	botPlay.setGraphic(new ImageView(iplay));
	botPlay.setContentDisplay(ContentDisplay.CENTER);
	botPause.setGraphic(new ImageView(ipause));
	botStop.setGraphic(new ImageView(istop));
	botMute.setGraphic(new ImageView(ifone));
	botAnt.setGraphic(new ImageView(iantes));
	botPro.setGraphic(new ImageView(iproximo));

	botPlay.setStyle("-fx-base: transparent; ");//deixar transparente
	botPlay.setFocusTraversable(false);//tirar borda
	botStop.setStyle("-fx-base: transparent;");
	botStop.setFocusTraversable(false);
	botMute.setStyle("-fx-base: transparent;");
	botMute.setFocusTraversable(false);
	botPro.setStyle("-fx-base: transparent;");
	botPro.setFocusTraversable(false);
	botAnt.setStyle("-fx-base: transparent;");
	botAnt.setFocusTraversable(false);


	volume.valueProperty().addListener(new ChangeListener<Number>() {
		public void changed(ObservableValue<? extends Number> ov, Number old_v, Number new_v){
			player.set_volume((float)new_v.doubleValue());
		}
	});


	botPlay.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evento) {
			if (player != null){
				if(state != PLAYING){
					state = PLAYING;
					lineSelect = table.getSelectionModel().getSelectedIndex();
					Mp3 o = ((Mp3)table.getSelectionModel().getSelectedItem());
					//					f = new File(o.getPath());
					//
					//					Media m = new Media(f.toURI().toString());
					//					player.play(m);
					player.play(o.getPath());
					vectorMp3.get(vectorMp3.indexOf(o)).incremeta();
					reproducao.setValue(0.0);

					//					player.mp.currentTimeProperty().addListener(new InvalidationListener() {
					//						public void invalidated(Observable ov) {
					//							updateValues();
					//						}
					//					});

					player.clock.addMessageListener(new Bead() {
						public void messageReceived(Bead message) {
							updateValues();
						}
					}
							);


					musicTab.setText(o.getTitulo());
					musicTab.setAlignment(Pos.CENTER);
					musicTab.setVisible(true);
					botPlay.setGraphic(new ImageView(ipause));
				}else{
					state = PAUSED;
					player.pause();
					botPlay.setGraphic(new ImageView(iplay));
				}

			}
		}
	});

	botStop.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evento){
			player.stop();
			reproducao.setValue(0);
			if(state == PLAYING){
				musicTab.setText("");
				musicTab.setVisible(false);
				botPlay.setGraphic(new ImageView(iplay));
				state = STOPPED;
			}
		}
	});

	botMute.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle (ActionEvent evento){
			if(state == PLAYING||state == PAUSED){	
				if(mute){
					mute = false;
					player.mute(mute);
					volume.setDisable(mute);
					botMute.setGraphic( new ImageView(ifone) );
				}else{
					mute = true;
					player.mute(mute);
					volume.setDisable(mute);
					botMute.setGraphic( new ImageView(imute) );
				}
			}	
		}
	});

	botPro.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evento){
			if (player.player != null){
				System.out.println("cu");
				table.getSelectionModel().selectLast();
				int ultimo = table.getSelectionModel().getSelectedIndex();
				if (lineSelect < ultimo){
					table.getSelectionModel().select(lineSelect+1);
					botStop.fire();
					botPlay.fire();
				}else{
					if (repeat){
						table.getSelectionModel().select(0);
						botStop.fire();
						botPlay.fire();
					}else{
						botStop.fire();
					}
				}
			}
		}
	});

	botAnt.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evento){
			table.getSelectionModel().clearAndSelect(lineSelect-1);
			botStop.fire();
			botPlay.fire();
		}
	});

	musicTab.setPrefHeight(25);
	HBox.setHgrow(musicTab, Priority.ALWAYS);
	musicTab.setEditable(false);
	musicTab.setVisible(false);

	hbox.setSpacing(11);
	hbox.getChildren().addAll(botAnt, botPlay, botPro, botStop, musicTab, botMute, volume);
	hbox.setAlignment(Pos.CENTER);
	return hbox;
}

public void updateValues() {
	if (playTime != null && reproducao != null && volume != null) {
		Platform.runLater(new Runnable() {
			public void run() {
				if (state == PLAYING){
					double duration = player.player.getSample().getLength();
					if(duration!=0){
						double currentTime = player.get_Tempo();
						playTime.setText(formatTime(currentTime, duration));
						if (!reproducao.isDisabled()&& duration > 0 && !reproducao.isValueChanging()) {
							reproducao.setValue((currentTime/duration)*100);
						}
						if (currentTime >= duration){
							botPro.fire();
						}
					}
				}
			}
		});
	}
}

private static String formatTime(double elapsed, double duration) {
	int intElapsed = (int) Math.floor(elapsed/1000);
	int elapsedHours = intElapsed / (60 * 60);
	if (elapsedHours > 0) {
		intElapsed -= elapsedHours * 60 * 60;
	}
	int elapsedMinutes = intElapsed / 60;
	int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
			- elapsedMinutes * 60;
	if (duration > 0) {
		int intDuration = (int) Math.floor(duration/1000);
		int durationHours = intDuration / (60 * 60);
		if (durationHours > 0) {
			intDuration -= durationHours * 60 * 60;
		}
		int durationMinutes = intDuration / 60;
		int durationSeconds = intDuration - durationHours * 60 * 60
				- durationMinutes * 60;
		if (durationHours > 0) {
			return String.format("%d:%02d:%02d/%d:%02d:%02d", elapsedHours,
					elapsedMinutes, elapsedSeconds, durationHours,
					durationMinutes, durationSeconds);
		} else {
			return String.format("%02d:%02d/%02d:%02d", elapsedMinutes,
					elapsedSeconds, durationMinutes, durationSeconds);
		}
	} else {
		if (elapsedHours > 0) {
			return String.format("%d:%02d:%02d", elapsedHours,
					elapsedMinutes, elapsedSeconds);
		} else {
			return String.format("%02d:%02d", elapsedMinutes,
					elapsedSeconds);
		}
	}
}

private Node Efeitos(){
	HBox hbox = new HBox();

	hbox.setAlignment(Pos.TOP_LEFT);
	hbox.setPadding(new Insets(15,0,15,0));
	hbox.setSpacing(10);
	hbox.autosize();

	VBox vbox = new VBox();
	vbox.setPadding(new Insets(15,15,15,15));


	Label CPU = new Label("CPU:\n\n\n");
	CPU.setAlignment(Pos.CENTER);

	UsoCPU = new Label("\n\n\nUso do CPU: ");
	UsoCPU.setAlignment(Pos.CENTER);

	final ImageView icon = new ImageView();


	final ToggleGroup group = new ToggleGroup();

	RadioButton rb0 = new RadioButton("Sem efeito");
	rb0.setToggleGroup(group);
	rb0.setSelected(true);

	RadioButton rb1 = new RadioButton("Reverb");
	rb1.setToggleGroup(group);

	RadioButton rb2 = new RadioButton("Filtro");
	rb2.setToggleGroup(group);

	RadioButton rb3 = new RadioButton("Pitch shift");
	rb3.setToggleGroup(group);

	RadioButton rb4 = new RadioButton("Time stretch");
	rb4.setToggleGroup(group);

	RadioButton rb5 = new RadioButton("Flanging");
	rb5.setToggleGroup(group);


	icon.setImage(new Image(
			getClass().getResourceAsStream("caja.png"
					//                    group.getSelectedToggle().getUserData().toString() + 
					//                        ".jpg"
					)
			));

	group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		public void changed(ObservableValue<? extends Toggle> ov,
				Toggle old_toggle, Toggle new_toggle) {
			if (group.getSelectedToggle() != null) {
				final Image image = new Image(
						getClass().getResourceAsStream("caja.png"
								//                                group.getSelectedToggle().getUserData().toString() + 
								//                                    ".jpg"
								)
						);
				icon.setImage(image);
				RadioButton chk = (RadioButton)group.getSelectedToggle();
				player.fx = chk.getText();
				System.out.println("group");
			}                
		}
	});

	vbox.getChildren().addAll(/*CPU, */rb0, rb1, rb2, rb3, rb4, rb5/*, icon*/, UsoCPU);



//	VBox vbox2 = new VBox();
//	vbox2.setPadding(new Insets(15,15,15,15));
//
//
//	Label mem = new Label("Memória:\n\n\n");
//	mem.setAlignment(Pos.CENTER);
//
//	UsoMem = new Label("\n\n\nUso da Memória: ");
//	UsoMem.setAlignment(Pos.CENTER);
//
//	final ImageView icon2 = new ImageView();
//
//
//	final ToggleGroup group2 = new ToggleGroup();
//
//	RadioButton rb02 = new RadioButton("Sem efeito");
//	rb02.setToggleGroup(group2);
//	rb02.setSelected(true);
//
//	RadioButton rb12 = new RadioButton("Reverb");
//	rb12.setToggleGroup(group2);
//
//	RadioButton rb22 = new RadioButton("Filtro");
//	rb22.setToggleGroup(group2);
//
//	RadioButton rb32 = new RadioButton("Pitch shift");
//	rb32.setToggleGroup(group2);
//
//	RadioButton rb42 = new RadioButton("Time stretch");
//	rb42.setToggleGroup(group2);
//
//	RadioButton rb52 = new RadioButton("Flanger");
//	rb52.setToggleGroup(group2);
//
//
//	icon2.setImage(new Image(
//			getClass().getResourceAsStream("caja.png"
//					//                    group.getSelectedToggle().getUserData().toString() + 
//					//                        ".jpg"
//					)
//			));
//
//	group2.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
//		public void changed(ObservableValue<? extends Toggle> ov,
//				Toggle old_toggle, Toggle new_toggle) {
//			if (group.getSelectedToggle() != null) {
//				final Image image = new Image(
//						getClass().getResourceAsStream("caja.png"
//								//                                group.getSelectedToggle().getUserData().toString() + 
//								//                                    ".jpg"
//								)
//						);
//				icon2.setImage(image);
//				RadioButton chk = (RadioButton)group.getSelectedToggle();
//				player.fx = chk.getText();
//				System.out.println("group2");
//			}                
//		}
//	});
//
//	vbox2.getChildren().addAll(mem, rb02, rb12, rb22, rb32, rb42, rb52/*, icon2*/, UsoMem);
//
//
//
//	VBox vbox3 = new VBox();
//	vbox3.setPadding(new Insets(15,15,15,15));
//
//
//	Label disco = new Label("Disco:\n\n\n");
//	disco.setAlignment(Pos.CENTER);
//
//	UsoDisco = new Label("\n\n\nUso do Disco: ");
//	UsoDisco.setAlignment(Pos.CENTER);
//
//	final ImageView icon3 = new ImageView();
//
//
//	final ToggleGroup group3 = new ToggleGroup();
//
//	RadioButton rb03 = new RadioButton("Sem efeito");
//	rb03.setToggleGroup(group3);
//	rb03.setSelected(true);
//
//	RadioButton rb13 = new RadioButton("Reverb");
//	rb13.setToggleGroup(group3);
//
//	RadioButton rb23 = new RadioButton("Filtro");
//	rb23.setToggleGroup(group3);
//
//	RadioButton rb33 = new RadioButton("Sintese Granular");
//	rb33.setToggleGroup(group3);
//
//	RadioButton rb43 = new RadioButton("Time Shifter");
//	rb43.setToggleGroup(group3);
//
//	RadioButton rb53 = new RadioButton("Flanger");
//	rb53.setToggleGroup(group3);
//
//
//	icon3.setImage(new Image(
//			getClass().getResourceAsStream("caja.png"
//					//                    group.getSelectedToggle().getUserData().toString() + 
//					//                        ".jpg"
//					)
//			));
//
//	group3.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
//		public void changed(ObservableValue<? extends Toggle> ov,
//				Toggle old_toggle, Toggle new_toggle) {
//			if (group.getSelectedToggle() != null) {
//				final Image image = new Image(
//						getClass().getResourceAsStream("caja.png"
//								//                                group.getSelectedToggle().getUserData().toString() + 
//								//                                    ".jpg"
//								)
//						);
//				icon3.setImage(image);
//				RadioButton chk = (RadioButton)group.getSelectedToggle();
//				player.fx = chk.getText();
//				System.out.println("group3");
//			}
//		}
//	});
//
//	vbox3.getChildren().addAll(disco, rb03, rb13, rb23, rb33, rb43, rb53/*, icon3*/, UsoDisco);



	hbox.getChildren().addAll(vbox/*, vbox2, vbox3*/);

	return hbox;
}

private Node Config(){
	VBox vbox = new VBox();

	HBox caixaDir = new HBox();
	caixaDir.setAlignment(Pos.TOP_LEFT);
	caixaDir.setPadding(new Insets(15,0,15,0));
	caixaDir.setSpacing(10);

	Label Diretorio = new Label("Diretorio de musicas");
	final TextField txtDiretorio = new TextField(diretorio);
	txtDiretorio.setTooltip(new Tooltip("Diretorio de onde as músicas serão tocadas"));
	final Label configurado = new Label("\nConfigurado com sucesso!");
	configurado.setTextFill(Color.web("#0076a3"));
	configurado.setVisible(false);

	caixaDir.getChildren().addAll(Diretorio, txtDiretorio);


	Button config = new Button("Configurar");
	config.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evento){
			configurado.setVisible(false);

			System.out.println(txtDiretorio.getText());

			diretorio = txtDiretorio.getText();
			RepositorioMusica a = new RepositorioMusica(diretorio);

			vectorMp3 = a.gerarLista();

			datagenero();
			dataArtista();

			if(!vectorMp3.isEmpty()){
				data.clear();
				int i = 0;
				while(i<vectorMp3.size()){
					data.add(vectorMp3.get(i));							
					i++;
				}
			}

			configurado.setVisible(true);
		}
	});

	vbox.getChildren().addAll(caixaDir, config, configurado);

	return vbox;
}

public void datagenero(){
	String[] g = new String[193];
	boolean achou = false;
	int index = 0;
	for (int i = 0; i < vectorMp3.size(); i++) {
		for (int j = 0; j < index && !achou; j++ ) {
			if(g[j]!=null){
				if(g[j].equals(vectorMp3.get(i).getGenero())){
					achou = true;
				}
			}
		}
		if(!achou && vectorMp3.get(i).getGenero() != null){
			g[index] = vectorMp3.get(i).getGenero();
			index++;
			System.out.println(vectorMp3.get(i).getGenero());
			System.out.println("ADD");
			datagenero.add( vectorMp3.get(i) );
		}
		achou = false;
	}

	for(Mp3 m : datagenero){
		final String gen = m.getGenero();
		final MenuItem ab = new MenuItem(gen);
		ab.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento){
				Gerarplaygenero(gen);
			}
		});
		menuGen.getItems().add(ab);
	}
}



public void dataArtista(){
	String[] g = new String[10000];
	boolean achou = false;
	int index = 0;
	for (int i = 0; i < vectorMp3.size(); i++) {
		for (int j = 0; j < index && !achou; j++ ) {
			if(g[j]!=null){
				if( g[j].equals(vectorMp3.get(i).getAutor())){
					achou = true;
				}
			}
		}
		if(!achou && vectorMp3.get(i).getAutor() != null && !vectorMp3.get(i).getAutor().equals("")){
			g[index] = vectorMp3.get(i).getAutor();
			index++;
			dataArtista.add( vectorMp3.get(i) );
		}
		achou = false;
	}

	for(Mp3 m : dataArtista){
		final String artista = m.getAutor();
		final MenuItem ab = new MenuItem(artista);
		ab.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evento){
				Gerarplayartista(artista);
			}
		});
		menuArtist.getItems().add(ab);
	}

}

public void Gerarplaygenero(String genero){
	ObservableList<Mp3> dataplay = FXCollections.observableArrayList();
	for (int i = 0; i < vectorMp3.size(); i++) {
		if( genero.equals(vectorMp3.get(i).getGenero())){
			dataplay.add(vectorMp3.get(i));
		}								
	}

	table.setItems(dataplay);
}

public void Gerarplayartista(String artista){
	ObservableList<Mp3> dataplay = FXCollections.observableArrayList();

	for (int i = 0; i < vectorMp3.size(); i++) {
		if( artista.equals(vectorMp3.get(i).getAutor())){
			dataplay.add(vectorMp3.get(i));
		}								
	}

	table.setItems(dataplay);
}

public void Gerarplaytop10(){
	ObservableList<Mp3> datatop = FXCollections.observableArrayList();
	Mp3 tabcont[] = new Mp3[10];
	int menor = 0, indice = 0, edez=0; 

	if(vectorMp3.size()>9){
		for (int i = 0; i < vectorMp3.size(); i++) {				
			if(i>9){
				if( vectorMp3.get(i).getContador() > menor){
					tabcont[indice] = vectorMp3.get(i);
					menor = Integer.MAX_VALUE;
					for (int j = 0; j < tabcont.length; j++) {
						if(tabcont[j].getContador() < menor){
							menor = tabcont[j].getContador();
							indice = j;
						}
					}
				}
			}else{
				if(edez == 0){
					menor=vectorMp3.get(0).getContador();
					indice = 0;
				}else if(vectorMp3.get(i).getContador() < menor){
					menor = vectorMp3.get(i).getContador();
					indice = i;
				}
				tabcont[edez] = vectorMp3.get(i);				
				edez++;
			}
		}
	}


	for (int i = 0; i < tabcont.length; i++) {
		if(tabcont[i]!= null){
			System.out.println("add" + tabcont[i].getContador());
			datatop.add(tabcont[i]);
		}
	}

	table.setItems(datatop);
}

public void Gerarplaymenosouvidas(){
	ObservableList<Mp3> datatop = FXCollections.observableArrayList();
	Mp3 tabcont[] = new Mp3[10];
	int maior = 0, indice = 0, edez=0; 

	if(vectorMp3.size()>9){
		for (int i = 0; i < vectorMp3.size(); i++) {				
			if(i>9){
				if( vectorMp3.get(i).getContador() < maior){
					tabcont[indice] = vectorMp3.get(i);
					maior = 0;
					for (int j = 0; j < tabcont.length; j++) {
						if(tabcont[j].getContador() > maior){
							maior = tabcont[j].getContador();
							indice = j;
						}
					}
				}
			}else{
				if(edez == 0){
					maior = vectorMp3.get(0).getContador();
					indice = 0;
				}else if(vectorMp3.get(i).getContador() > maior){
					maior = vectorMp3.get(i).getContador();
					indice = i;
				}
				tabcont[edez] = vectorMp3.get(i);				
				edez++;
			}
		}
	}


	for (int i = 0; i < tabcont.length; i++) {
		if(tabcont[i]!= null){
			System.out.println("add" + tabcont[i].getContador());
			datatop.add(tabcont[i]);
		}
	}

	table.setItems(datatop);
}


//	private Node Playlists() {
//
//		VBox vBox = new VBox(5);
//		TableColumn playl = new TableColumn("Playlist");
//		playl.setPrefWidth(300);
//		playl.setCellValueFactory(new PropertyValueFactory<Mp3, String>("nome"));
//		playlist.getColumns().add(playl);
//
//		playlist.prefHeightProperty().bind(cena.heightProperty());
//		playlist.prefWidthProperty().bind(cena.widthProperty());
//
//		final Image iplay = new Image(getClass().getResourceAsStream("play.png"));
//		final Image ipause = new Image(getClass().getResourceAsStream("pause.png"));
//		final Image ifone = new Image(getClass().getResourceAsStream("fone.png"));
//		final Image istop = new Image(getClass().getResourceAsStream("stop.png"));
//		final Image imute = new Image(getClass().getResourceAsStream("mute.png"));
//		final Image iantes = new Image(getClass().getResourceAsStream("anterior.png"));
//		final Image iproximo = new Image(getClass().getResourceAsStream("proximo.png"));
//
//
//		final Button botPlay = new Button(); 	
//		final Button botPause = new Button();
//		final Button botStop = new Button();
//		final Button botMute = new Button();
//		final Button botAnt = new Button();
//		final Button botPro = new Button();
//
//		botPlay.setGraphic(new ImageView(iplay));
//		botPause.setGraphic(new ImageView(ipause));
//		botStop.setGraphic(new ImageView(istop));
//		botMute.setGraphic(new ImageView(ifone));
//		botAnt.setGraphic(new ImageView(iantes));
//		botPro.setGraphic(new ImageView(iproximo));
//
//
//		botPlay.setStyle("-fx-base: transparent; ");//deixar transparente
//		botPlay.setFocusTraversable(false);//tirar borda
//		botStop.setStyle("-fx-base: transparent;");
//		botStop.setFocusTraversable(false);
//		botMute.setStyle("-fx-base: transparent;");
//		botMute.setFocusTraversable(false);
//		botPro.setStyle("-fx-base: transparent;");
//		botPro.setFocusTraversable(false);
//		botAnt.setStyle("-fx-base: transparent;");
//		botAnt.setFocusTraversable(false);
//
//		botPlay.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent evento) {
//				if(state != PLAYING){
//					state = PLAYING;
//					lineSelectp = playlist.getSelectionModel().getSelectedIndex();
//					Mp3 o = ((Mp3)playlist.getSelectionModel().getSelectedItem());
//					f = new File(o.getPath());
//
//					Media m = new Media(f.toURI().toString());
//					player.play(m);
//					vectorMp3.get(vectorMp3.indexOf(o)).incremeta();
//					reproducaop.setValue(0.0);
//
//					player.mp.currentTimeProperty().addListener(new InvalidationListener() {
//						public void invalidated(Observable ov) {
//							updateValuesp();
//						}
//					});
//
//					player.mp.setOnEndOfMedia(new Runnable() {
//						public void run() {
//							botPro.fire();
//						}
//					});
//					botPlay.setGraphic(new ImageView(ipause));
//				}else{
//					state = PAUSED;
//					player.pause();
//					botPlay.setGraphic(new ImageView(iplay));
//				}
//			}
//		});
//
//		botStop.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent evento){
//				player.stop();
//				reproducao.setValue(0);
//				if(state == PLAYING){
//					botPlay.setGraphic(new ImageView(iplay));
//					state = STOPPED;
//				}
//			}
//		});
//
//		botMute.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle (ActionEvent evento){
//				if(state == PLAYING||state == PAUSED){	
//					if(mute){
//						mute = false;
//						volumep.setValue(player.get_volumeAtual());
//						player.mute(mute);
//						botMute.setGraphic( new ImageView(ifone) );
//					}else{
//						mute = true;
//						player.mute(mute);
//						//volumep.setValue(player.get_minimo());
//						botMute.setGraphic( new ImageView(imute) );
//					}
//				}	
//			}
//		});
//
//		botPro.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent evento){
//				playlist.getSelectionModel().selectLast();
//				int ultimo = playlist.getSelectionModel().getSelectedIndex();
//				if (lineSelectp < ultimo){
//					playlist.getSelectionModel().select(lineSelectp+1);
//					botStop.fire();
//					botPlay.fire();
//				}else{
//					if (repeatp){
//						playlist.getSelectionModel().select(0);
//						botStop.fire();
//						botPlay.fire();
//					}
//				}
//			}
//		});
//
//		botAnt.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent evento){
//				playlist.getSelectionModel().clearAndSelect(lineSelectp-1);
//				botStop.fire();
//				botPlay.fire();
//			}
//		});
//
//		MenuItem mescu = new MenuItem("Menos Tocadas");
//		MenuItem Top = new MenuItem("Top 10");
//
//		Top.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent evento){
//				Gerarplaytop10();			
//			}
//		});
//
//		mescu.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent evento){
//				Gerarplaymenosouvidas();
//			}
//		});
//
//		menuCustons.getItems().addAll( Top, mescu );
//		MenuBar menuBar = new MenuBar();
//		menuBar.getMenus().addAll(menuArtist, menuGen, menuCustons);
//
//		playTimep = new Label();
//		playTimep.setPrefWidth(150);
//		playTimep.setText("--:--/--:--");
//
//		reproducaop.setMin(0);
//		reproducaop.setMaxWidth(Double.MAX_VALUE);
//		reproducaop.prefWidthProperty().bind(cena.widthProperty());
//		reproducaop.setShowTickLabels(false); // 10
//		reproducaop.setShowTickMarks(false); // 11
//		reproducaop.valueProperty().addListener(new InvalidationListener() {
//			public void invalidated(Observable ov) {
//				if (reproducaop.isValueChanging()) {
//					player.seek(Math.floor(reproducaop.getValue()));
//					updateValues();
//				}
//			}
//		});
//
//		volumep.setMaxWidth(100); 
//		volumep.setShowTickLabels(false); // 10
//		volumep.setShowTickMarks(false); // 11
//		volumep.setTranslateY(volume.getTranslateY()+4);
//		volumep.setMin(0);
//		volumep.setMax(1);
//		volumep.setValue(0.7);
//
//		volumep.valueProperty().addListener(new ChangeListener<Number>() {
//			public void changed(ObservableValue<? extends Number> ov, Number old_v, Number new_v){
//				player.set_volume(new_v.doubleValue());
//			}
//		});
//
//
//
//		HBox hboxl1 = new HBox();
//		hboxl1.getChildren().addAll(botAnt, botPlay, botPro, botStop, botMute, volumep);
//		HBox hboxl2 = new HBox();
//		hboxl2.getChildren().addAll(reproducaop, playTimep);
//
//		vBox.getChildren().addAll(menuBar, hboxl1, hboxl2, playlist);
//
//		return vBox;
//
//	}
//
//	private void updateValuesp() {
//		if (playTimep != null && reproducaop != null && volumep != null) {
//			Platform.runLater(new Runnable() {
//				@SuppressWarnings("deprecation")
//				public void run() {
//					if(player.mp!=null){
//						Duration currentTime = player.mp.getCurrentTime();
//						playTimep.setText(formatTime(currentTime, player.duration));
//						//reproducao.setMax(player.mp.getTotalDuration().toSeconds());						
//						if (!reproducaop.isDisabled()&& player.duration.greaterThan(Duration.ZERO) && !reproducaop.isValueChanging()) {
//							reproducaop.setValue(currentTime.divide(player.duration).toMillis() * 100.0);
//						}
//					}
//				}
//			});
//		}
//	}


}
