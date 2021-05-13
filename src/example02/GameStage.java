package example02;

import java.io.File;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameStage {
	public static final int WINDOW_HEIGHT = 500;
	public static final int WINDOW_WIDTH = 800;
	private Scene scene;
	private Stage stage;
	private Pane root;
	private Canvas canvas;
	private GraphicsContext gc;
	private GameTimer gametimer;
	
	
	//the class constructor
	public GameStage() {
		this.root = new Pane();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,Color.CADETBLUE);	
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);	
		this.gc = canvas.getGraphicsContext2D();
		//instantiate an animation timer
		this.gametimer = new GameTimer(this.gc,this.scene);
	}

	//method to add the stage elements
	public void setStage(Stage stage) {
		this.stage = stage;
		
		//set stage elements here	     
		this.root.getChildren().add(canvas);
		 
		this.stage.setTitle("Bootleg Touhou");
		this.stage.setScene(this.scene);
		Image bg = new Image("images/reninger.jpg", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, false, false);//add background image to root
		BackgroundImage bgi = new BackgroundImage(bg,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		
		Media sound1 = new Media( new File("bambooforest.mp3").toURI().toString() );
		MediaPlayer player1 = new MediaPlayer(sound1);
		MediaView mv = new MediaView(player1);
		player1.play();
		player1.setVolume(2);
		this.root.getChildren().add(mv);
				
		this.root.setBackground(new Background(bgi));
		//invoke the start method of the animation timer
		this.gametimer.start();
		
		this.stage.show();
	}
	public Pane getPane() {
		return(this.root);
	}
	
	
	
}

