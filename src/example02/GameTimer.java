package example02;

import java.io.File;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameTimer extends AnimationTimer{
	
	private GraphicsContext gc;
	private Scene theScene;
	private Ship myShip;
	private Ship Keine;
	private Ship Koa;
	private LinkedList<Bullet> bulletsremove;
	private long startSpawn;
	private Random r;
	private AudioClip deathsound;
	private AudioClip attack;
	private final static int MAX_BULLETS=9;
	private final static int MAX_BULLETS2=4;
	private final static int MAX_BOSS_HEALTH=700;
	private final static int MAX_PLAYER_HEALTH=100;
	
	
	
	GameTimer(GraphicsContext gc, Scene theScene){
		this.gc = gc;
		this.theScene = theScene;
		this.r = new Random();
		this.Koa = new Ship("Koa",600,400,1, GameTimer.MAX_BOSS_HEALTH-200);
		this.myShip = new Ship("Dai",100,250,0,GameTimer.MAX_PLAYER_HEALTH );
		this.Keine = new Ship("Keine",700,300,2, GameTimer.MAX_BOSS_HEALTH);
		this.deathsound =  new AudioClip(new File("SFX/DEAD.wav").toURI().toString() );	
		this.attack = new AudioClip(new File("SFX/ATTACK5.wav").toURI().toString() );

		//call method to handle mouse click event
		this.handleKeyPressEvent();
		this.bulletsremove = new LinkedList<Bullet>();
		
	}

	@Override
	public void handle(long currentNanoTime) {
		
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);

		
		//add bullets
		if(this.Keine.getBullets().size()<GameTimer.MAX_BULLETS) this.Keine.getBullets().add(new Bullet(Keine.getX()-20,Keine.getY()+5, 0));
		if(this.Koa.getBullets().size()<GameTimer.MAX_BULLETS2) this.Koa.getBullets().add(new Bullet(Koa.getX()-20,Koa.getY()+5, 2));
			
		if(Koa.getY()<myShip.getY()) {//other character follows player
			Koa.setDY(r.nextInt(3));
		}else {
			Koa.setDY(r.nextInt(3)*-1);
		}
		
	
			
		Keine.randomMove(Keine.getMove());//boss moves up and down
		//call the methods to move the ship
		this.myShip.move();
		this.Keine.move();
		this.Koa.move();
		
		if(myShip.collidesWith(Keine) || myShip.collidesWith(Koa)) {//player dies if touches other chars
			myShip.die();
		} 

		
		if(Koa.isAlive()) {//if boss assistant is alive
			Koa.render(gc);
			gc.drawImage(new Image("images/health4.png", GameTimer.MAX_BOSS_HEALTH-200, 10, false, false), 150, 470);//draw health
			gc.drawImage(new Image("images/health3.png", Koa.getStrength(), 10, false, false), 150, 470);
		} else {
			
			this.Koa.getBullets().clear();
		}
		
		if(!myShip.isAlive()) {//if player is dead
			this.Koa.getBullets().clear();
			this.Keine.getBullets().clear();//remove bullets so you can see the lose message
			this.myShip.getBullets().clear();
			this.deathsound.play(1);
			this.stop();
			Font theFont = Font.font("Helvetica",FontWeight.BOLD,55);
			this.gc.setFont(theFont);
			this.gc.setFill(Color.GOLD);
			this.gc.fillText("You lose", 285,250);
			
		}else {
			gc.drawImage(new Image("images/player.png", GameTimer.MAX_PLAYER_HEALTH*5, 10, false, false), 150, 50);
			gc.drawImage(new Image("images/player1.png", myShip.getStrength()*5, 10, false, false), 150, 50);
			this.myShip.render(this.gc);
		}
		
		if(Keine.isAlive()) {//check if boss is alive
			
			gc.drawImage(new Image("images/health.png", GameTimer.MAX_BOSS_HEALTH*.8, 10, false, false), 120, 450);
			gc.drawImage(new Image("images/health1.png", Keine.getStrength()*.8, 10, false, false), 120, 450);
			this.Keine.render(gc);
		}
		else{
			this.deathsound.play(1);
			Keine.setStrength1(0);
			Font theFont = Font.font("Helvetica",FontWeight.BOLD,55);
			this.gc.setFont(theFont);
			this.gc.setFill(Color.GOLD);
			this.gc.fillText("Winner", 300,250);
			this.stop();
		}
		//render
	

		for(Bullet bullet1: Koa.getBullets()) {//other enemy bullets
			bullet1.render(gc);  
			if(bullet1.getX()>10) {       
				if(bullet1.collidesWith(myShip)) {
					myShip.setStrength(bullet1.getStrength());//reduce strength 
					bullet1.setStrength(0);//set strength to zero since bullet collides more than once with target
					System.out.print("Health "+ myShip.getStrength());
				}                      
				bullet1.setDX(-2);
				bullet1.setDY(r.nextInt(6+6)-6);//random y creates bullet spread
				
			}else {
				bullet1.setVisible(false);
				this.bulletsremove.add(bullet1);//add fired bullets to arraylist for removal
			}
			bullet1.move();
		}
		
		for(Bullet bullet1: Keine.getBullets()) {//ENEMY BOSS BULLETS
			
			bullet1.render(gc);  
			if(bullet1.getX()>10) {       
				if(bullet1.collidesWith(myShip)) {//check for collisions
					myShip.setStrength(bullet1.getStrength());
					bullet1.setStrength(0);
					System.out.print("Health "+ myShip.getStrength());
				}                      
				bullet1.setDX(-4);
				bullet1.setDY(r.nextInt(8+6)-6);
				
			}else {
				bullet1.setVisible(false);
				this.bulletsremove.add(bullet1);
			}
			bullet1.move();
		}

		for(Bullet bullet2: myShip.getBullets()) {//PLAYER BULLETS
			bullet2.render(this.gc);
			if(bullet2.getX()<770) {
				if(bullet2.collidesWith(Keine)) {//check for collisions
					Keine.setStrength(bullet2.getStrength());
					bullet2.setVisible(false);
					this.bulletsremove.add(bullet2);
				}
				if(Koa.isAlive() && bullet2.collidesWith(Koa)) {
					Koa.setStrength(bullet2.getStrength());
					bullet2.setVisible(false);
					this.bulletsremove.add(bullet2);
				}
				bullet2.setDY(0);
				bullet2.setDX(25);
			}else {
				bullet2.setVisible(false);
				this.bulletsremove.add(bullet2);
			}
			bullet2.move();
		}
		
		this.Koa.getBullets().removeAll(this.bulletsremove);//clear fired bullets of sprite
		this.myShip.getBullets().removeAll(this.bulletsremove);
		this.Keine.getBullets().removeAll(this.bulletsremove);
		
		this.bulletsremove.clear();//remove list of fired bullets
		
	}
	
//	
	
	//method that will listen and handle the key press events
	private void handleKeyPressEvent() {
		theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                moveMyShip(code);
			}
			
		});
		
		theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		            public void handle(KeyEvent e){
		            	KeyCode code = e.getCode();
		                stopMyShip(code);
		            }
		        });
    }
	
	//method that will move the ship depending on the key pressed
	private void moveMyShip(KeyCode ke) {
		if(ke==KeyCode.UP) { 
			if(this.myShip.getY()>10) this.myShip.setDY(-5);  
			else this.myShip.setDY(0);
		}

		if(ke==KeyCode.LEFT) this.myShip.setDX(-5);

		if(ke==KeyCode.DOWN) this.myShip.setDY(5);
		
		if(ke==KeyCode.RIGHT) this.myShip.setDX(5);
		if(ke==KeyCode.SPACE) {

			this.attack.setCycleCount(1);
			this.attack.setRate(1);
			this.attack.play(.75);
			
			if(myShip.getBullets().size()<3) myShip.getBullets().add(new Bullet(myShip.x+40,(myShip.y+10),1));

			
		}
		
		
		System.out.println(ke+" key pressed.");
   	}
	
	//method that will stop the ship's movement; set the ship's DX and DY to 0
	private void stopMyShip(KeyCode ke){
		this.myShip.setDX(0);
		this.myShip.setDY(0);
	}
	public GraphicsContext returngc() {
		return this.gc;
	}

	
}
