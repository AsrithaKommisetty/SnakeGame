import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class GamePanel extends JPanel implements ActionListener{
	static final int SCREEN_HEIGHT=600;
	static final int SCREEN_WIDTH=600;
	static final int SCREEN_UNIT=25;//GRID SIZE
	int appleX;//APPLE X COORDINATE
	int appleY;//APPLE Y COORDINATE
	int[]x=new int[(int)Math.pow(SCREEN_HEIGHT/SCREEN_UNIT,2)];
	int[]y=new int[(int)Math.pow(SCREEN_WIDTH/SCREEN_UNIT,2)];
	int bodyParts=2;//initial size of the snake
	char direction='R';//default direction
	Timer timer;
	int DELAY=200;
	boolean running=true;
	int score=0;
	
	GamePanel(){
		this.setPreferredSize(new Dimension(600,600));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		spawnApple();
		timer=new Timer(DELAY,this);
		timer.start();
		this.requestFocusInWindow();
		this.addKeyListener(new myKeyAdapter());
	}
	
	
	//all the painting components lie here
	@Override
	public void paintComponent(Graphics g) {//the snake
		super.paintComponent(g);
		g.setColor(Color.green);//snake colour
		for(int i=0;i<bodyParts;i++) {
			if(i==0) {
				g.setColor(Color.green);
			}
			g.fillRect(x[i], y[i], SCREEN_UNIT, SCREEN_UNIT);
		}
		g.setColor(Color.BLACK);//grid colour
		drawGrid(g);
		g.setColor(Color.red);
		g.fillOval(appleX,appleY,SCREEN_UNIT,SCREEN_UNIT);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " + score, 
		    (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2, 
		    g.getFont().getSize() + 10);

		
		if(!running) {
			g.setColor(Color.red);
			g.setFont(new Font("Arial", Font.BOLD, 48));
			FontMetrics metric = getFontMetrics(g.getFont());
			g.drawString("Game Over", 
			        (SCREEN_WIDTH - metric.stringWidth("Game Over")) / 2, 
			        SCREEN_HEIGHT / 2);
			
		}
	}
	
	
	//making the grid
	public static void drawGrid(Graphics g) {
		for(int i=0;i<SCREEN_HEIGHT/SCREEN_UNIT;i++) {
			int pos=i*SCREEN_UNIT;
			g.drawLine(pos,0,pos,SCREEN_HEIGHT);//VERTICAL
			g.drawLine(0, pos, SCREEN_WIDTH, pos);//HORIZONTAL
		}
	}
	//APPLE CLASS
	
	public void spawnApple() {
		Random random=new Random();
		appleX=random.nextInt(SCREEN_WIDTH/SCREEN_UNIT)*SCREEN_UNIT;
		appleY=random.nextInt(SCREEN_HEIGHT/SCREEN_UNIT)*SCREEN_UNIT;
	}

	//MOVE LOGIC
	public void move() {
		for(int i=bodyParts;i>0;i--) {
			x[i]=x[i-1];//these two lines makes the previous block(tail) follow the head in its path
			y[i]=y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0]=y[0]-SCREEN_UNIT;
			break;
		
		case 'D':
			y[0]=y[0]+SCREEN_UNIT;
			break;
		case 'L':
			x[0]=x[0]-SCREEN_UNIT;
			break;
		case 'R':
			x[0]=x[0]+SCREEN_UNIT;
			break;
		}	
	}
	
	//timer setup
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			wallCollision();
			selfCollision();
		}
		repaint();
		
		
	}
	
	//checking if the apple is consumed or not
	public void checkApple() {
		if(x[0]==appleX && y[0]==appleY) {
			bodyParts++;
			score++;
			if(DELAY>50) {
				DELAY=DELAY-5;
				timer.setDelay(DELAY);
			}
			spawnApple();
		}
	} 
	
	
	//wall collision
	public void wallCollision() {
		if(x[0]<0 || x[0]>= SCREEN_WIDTH || y[0]<0 || y[0]>=SCREEN_HEIGHT) {
			running=false;
			timer.stop();
		}
	}
	
	//self collision
	public void selfCollision() {
		for(int i=bodyParts;i>0;i--) {
			if(x[i]==x[0] && y[i]==y[0]) {
				running=false;
				timer.stop();
				break;
			}
		}
	}
	
	public class myKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction!='R') direction='L';
				break;
			case KeyEvent.VK_RIGHT:
				if(direction!='L') direction='R';
				break;
			case KeyEvent.VK_UP:
				if(direction!='D') direction='U';
				break;
			case KeyEvent.VK_DOWN:
				if(direction!='U') direction='D';
				break;
			}
		}
	}
}
