package example02;
import java.util.Random;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
public class Bullet extends Sprite{
	private int strength;
	private boolean alive;
	private final static int BULLET_WIDTH = 20;
	
	public final static Image BULLET_IMAGE = new Image("images/danmaku1.png",BULLET_WIDTH,BULLET_WIDTH,false,false);
	public final static Image BULLET2_IMAGE = new Image("images/danmaku2.png",BULLET_WIDTH,BULLET_WIDTH,false,false);
	public final static Image BULLET3_IMAGE = new Image("images/danmaku3.png",40,40,false,false);
	public Bullet(int x, int y, int bullet){
		super(x,y);

	
		this.strength = 20;
		this.alive = true;
		switch(bullet) {
		case 1:
			this.loadImage(Bullet.BULLET_IMAGE);
			break;
		case 0:
			this.loadImage(Bullet.BULLET2_IMAGE);
			break;
		default: 
			this.loadImage(Bullet.BULLET3_IMAGE);
			break;
			
		}

	
	}	
	public void shoot(GraphicsContext gc) {
		this.render(gc);
		while(this.getX()<200) {
			this.move();
			this.setDX(20);
		}
		
	}	
	public boolean isAlive(){
		if(this.alive) return true;
		return false;
	} 


	public void die(){
    	this.alive = false;
    }
	public void setStrength(int strength) {
		this.strength=strength;

	}
	public void move() {
    	this.x += this.dx;
    	this.y += this.dy;
	}
	public int getStrength() {
		return this.strength;
	}

}