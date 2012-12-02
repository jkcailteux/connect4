import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Font;
import java.awt.geom.*;
import javax.swing.JApplet;
import javax.swing.event.MouseInputAdapter;


public class Connect4 extends JApplet 
	implements Runnable,MouseListener{
	private Image bufferImage;		// This is the buffered Image
	private Graphics bufferGraphic;	// This is the Graphic for the bufferImage

	private Image backingImage;		// This is the backing Image
	private Graphics backingGraphic;// This is the Graphic for the backingImage
	
	//player 1 starts
	private int playerturn=1;
	//0 means no winner, 1=player 1, 2=player2
	private int winner=0;
	private int[][] grid= new int[7][6];
	private int[] top=new int[7];
	//threading variables
	boolean please_stop = true;
	Thread animator;
	
	public  void init(){
		//setup grid to all zeroes
		for (int x=0;x<7;x++){
			top[x]=0;
			for(int y=0;y<6;y++){
				grid[x][y]=0;
			}
		}
		bufferImage= createImage(350,500);
		bufferGraphic= bufferImage.getGraphics();
		Graphics g = bufferGraphic;
		paint(g);
		
		addMouseListener(new MouseInputAdapter() { 
			public void mouseClicked(MouseEvent e) {			
				Graphics g = bufferGraphic;
				int x=e.getX();
				int y=e.getY();
				
				//check for placement, check for winner
				if( y<=400 && y>=100){
						markspot(x);
				}
			}
			
		});
			
	}
	public void paint(Graphics g){
	
		if (winner==0){	
			Graphics2D g2= (Graphics2D) g;
			//fonts allow for bigger Xs and Os
			g.drawString("Connect4",100, 50);
			//g.drawLine(0, 50, 300, 50);
			for(int x=100;x!=450;x=x+50){
				g.drawLine(0,x,350,x);
			}
			for(int x=0;x!=400;x=x+50){
				g.drawLine(x,100,x,400);
			}
			g.drawString("Player 1 is Red,  Player 2 is Black",75, 425);
			//reset text to show turn
			g.clearRect(75, 440, 350, 50) ;
			if(playerturn==1){
				g.drawString("It is player 1's Turn",100, 450);
			} else{
				g.drawString("It is player 2's Turn",100, 450);
			}
			
			//Draw Grid here
			for (int x=0;x<7;x++){
				for(int y=0;y<6;y++){
					if(grid[x][y]==1){
						g2.setColor(Color.RED);
						g2.fill(new Ellipse2D.Double((x*50),(350-y*50), 50,50));
					}
					if(grid[x][y]==2){
						g2.setColor(Color.BLACK);
						g2.fill(new Ellipse2D.Double((x*50),(350-y*50), 50,50));
					}
					
				}
			}
			
		} else if(winner==1){
			System.out.println("Player 1 wins!!!");
			g.clearRect(0, 0, 350, 500) ;
			g.drawString("Player 1 Wins!!!!!",40, 40);
			please_stop = true;
		}else if(winner==2){
			System.out.println("Player 2 wins!!!");
			g.clearRect(0, 0, 350, 500) ;
			g.drawString("Player 2 Wins!!!!!",40, 40);
			please_stop = true;
		}
		
	}
	public void update(Graphics g) {
		paint(g);
	}
	@Override
	public void run() {
		while(!please_stop) {
			repaint();
			try {Thread.sleep(100); } catch (InterruptedException e) { }		
		}
	}
	public void start(){
		if (animator == null) {
			please_stop = false;
			animator = new Thread(this);
			animator.start();
		}
		animator = null;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void changeturn(){
		if(playerturn==1){
			playerturn=2;
		}else if(playerturn==2){
			playerturn=1;
		}
	}
	public void markspot(int x){
		int column=0;
		
		//get column value
		for(int y=0;y!=7;y++){
			if(x>=50*y && x<=(50+50*y)){
				column=y;
			}
		}
		//if column filled, just end
		if(top[column]==6){
			return;
		}
		
		if(grid[column][top[column]]==0){
			grid[column][top[column]]=playerturn;
			top[column]++;
			changeturn();
			printgrid();
			winner=checkwinner();
		}
	}
	
	public int checkwinner(){
		int counter1=0,counter2=0;
		
		//check horizontal win
		for (int y=0;y!=6;y++){
			counter1=0;counter2=0;
			for(int x=0;x!=7;x++){
				if(grid[x][y]==0){
					counter1=0;
					counter2=0;
				}
				else if(grid[x][y]==1){
					counter2=0;
					counter1++;
				}
				else if(grid[x][y]==2){
					counter1=0;
					counter2++;
				}
				if(counter1==4){
					return 1;
				}
				else if(counter2==4){
					return 2;
				}
			}
		}
		//check for vertical win
		counter1=0;counter2=0;
		for(int x=0;x!=7;x++){
			counter1=0;counter2=0;
			for(int y=0;y!=6;y++){
				if(grid[x][y]==0){
					counter1=0;
					counter2=0;
				}
				else if(grid[x][y]==1){
					counter2=0;
					counter1++;
				}
				else if(grid[x][y]==2){
					counter1=0;
					counter2++;
				}
				if(counter1==4){
					return 1;
				}
				else if(counter2==4){
					return 2;
				}	
			}
		
		}
		//check for diagonal, L to R win
		counter1=0;counter2=0;
		for(int y=0;y!=2;y++){
			for(int x=0;x!=4;x++){
				if(grid[x][y]==1 && grid[x+1][y+1]==1 && grid[x+2][y+2]==1 && grid[x+3][y+3]==1)
					return 1;
				else if(grid[x][y]==2 && grid[x+1][y+1]==2 && grid[x+2][y+2]==2 && grid[x+3][y+3]==2)
					return 2;
			}
		}
		//check for diagonal, R to L win
		counter1=0;counter2=0;
		for(int y=0;y!=2;y++){
			for(int x=3;x!=7;x++){
				if(grid[x][y]==1 && grid[x-1][y+1]==1 && grid[x-2][y+2]==1 && grid[x-3][y+3]==1)
					return 1;
				else if(grid[x][y]==2 && grid[x-1][y+1]==2 && grid[x-2][y+2]==2 && grid[x-3][y+3]==2)
					return 2;
			}
		}
	

		return 0;
	}
	public void printgrid(){
		for(int y=0;y!=6;y++){
			for(int x=0;x!=7;x++){
				System.out.print(grid[x][5-y]+" ");
			}
			System.out.println("");
		}
		System.out.println("");
	}
}

