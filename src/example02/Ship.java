package example02;

import java.util.LinkedList;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Ship extends Sprite{
	private String name;
	private int strength;
	private boolean alive;
	Random r;
	private boolean move;
	public final static Image DAI = new Image("images/daiyousei.png",Ship.SHIP_WIDTH,Ship.SHIP_WIDTH,false,false);
	public final static Image KOA = new Image("images/koa.png",Ship.SHIP_WIDTH,Ship.SHIP_WIDTH,false,false);
	public final static Image KEINE = new Image("images/keine.png",Ship.SHIP_WIDTH,Ship.SHIP_WIDTH,false,false);
	private LinkedList<Bullet> bulletlist;
	
	private final static int SHIP_WIDTH = 50;
	int height = GameStage.WINDOW_HEIGHT;
	public Ship(String name, int x, int y, int choice, int strength){
		super(x,y);
		this.name = name;
		r = new Random();
		this.move = true;
		this.strength = strength;
		this.alive = true;
		this.bulletlist=new LinkedList<Bullet>();
		switch(choice) {
		case 0:
			this.loadImage(Ship.DAI);
			break;
		case 1:
			this.loadImage(Ship.KOA);
			break;
		case 2:
			this.loadImage(Ship.KEINE);
			break;
		}
	}
	//getters

	public boolean isAlive(){
		if(this.alive) return true;
		return false;
	} 
	public String getName(){
		return this.name;
	}

	public void die(){
    	this.alive = false;
    }

	public void move() {
    	this.x += this.dx;
    	this.y += this.dy;
	}
	public void setStrength(int dmg) {
		this.strength-=dmg;
		if(this.strength<=0) {
			this.alive=false;
			this.setVisible(false);
		}
	}
	public void setStrength1(int dmg) {
		this.strength=dmg;
	}
	
	
	public LinkedList<Bullet> getBullets() {//Bullet list
		return this.bulletlist;
	}
	public void addBullets(Bullet bullet1) {
		this.bulletlist.add(bullet1);
	}
	
	public void shootBullets(GraphicsContext gc, int border, int speed, int dx, int dy, Ship enemy, boolean player, LinkedList<Bullet> bulletsremove) {
		if(!player) {
			for(Bullet bullet1: this.getBullets()) {//ENEMY BULLETS
				bullet1.render(gc);  
				if(bullet1.getX()>dx) {       
					if(bullet1.collidesWith(enemy)) {
						enemy.setStrength(bullet1.getStrength());//reduce strength
						bullet1.setStrength(0);//set bullet strength to zero since it collides more than once
					}                      
					bullet1.setDX(dx);
					bullet1.setDY(dy);
					
				}else {
					bullet1.setVisible(false);
					bulletsremove.add(bullet1);
				}
				bullet1.move();
			}

		}else {
			for(Bullet bullet2: this.getBullets()) {//PLAYER BULLETS
				bullet2.render(gc);
				if(bullet2.getX()<dx) {
					if(bullet2.collidesWith(enemy)) {
						enemy.setStrength(bullet2.getStrength());
						bullet2.setVisible(false);
						bulletsremove.add(bullet2);
					}
					bullet2.setDY(0);
					bullet2.setDX(20);
				}else {
					bullet2.setVisible(false);
					bulletsremove.add(bullet2);
				}
				bullet2.move();
			}
		}
	}
	
	public void randomMove(boolean up) {
		
		if(up){//move up
			if(this.getY()>10) {
				this.setDY(r.nextInt(3)-5);
			} else {
				this.setDX(0);
				this.setDY(0);
				this.move = false;
			}
		}else {//move down
			if(this.getY()<460) {
				this.setDY(r.nextInt(3)+1);
			} else {
				this.setDX(0);
				this.setDY(0);
				this.move = true;
			}
		}
	}
	public int getStrength() {
		return this.strength;
	}
	public boolean getMove() {
		return this.move;
	}

}
