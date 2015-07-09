package ceri;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Vector;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


class Pencere�dareci extends WindowAdapter{	
	private Oyunalan� oyunalan�;
	public Pencere�dareci(Oyunalan� oyunalan�){
		super();		
		this.oyunalan�=oyunalan�;
	}
	public void windowClosing(WindowEvent e){
		System.exit(0);
	}	
	public void windowLostFocus(WindowEvent e){
		oyunalan�.setMode(3);
		oyunalan�.setDefaultCursor();
	}
	public void windowDeactivated(WindowEvent e){
		oyunalan�.setMode(3);
		oyunalan�.setDefaultCursor();
	}
}

class DoubleLockPipe {
	public Vector<Mob> buffer;
	public int lockA;
	public int lockB;
	DoubleLockPipe(){
		buffer = new Vector<Mob>();
		lockA=0;
		lockB=0;
	}
	
}


class Karakter extends JLabel{
	
	private static final long serialVersionUID = 1L;
	//private Oyunalan� owner;
	
	// override inherited x and y for convenience
	private int x;
	private int y;
	private int width;
	private int height;
	
	//constructor
	public Karakter(int widht, int height, Icon resim ,Oyunalan� aidiyet){
		super(resim, SwingConstants.CENTER);
		setSize(widht, height);
		this.height=height;
		this.width=widht;
		//this.owner=aidiyet;		
	}
	
	public int getX(){return x;}
	public void setX(int x){this.x=x;}
	public int getY(){return y;}
	public void setY(int y){this.y=y;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}	
}

class Mob{
	int x;
	int y;
	Mob(int x, int y){
		this.x=x;
		this.y=y;
	}
	public int getX(){return x;}
	public void setX(int x){this.x=x;}
	public int getY(){return y;}
	public void setY(int y){this.y=y;}
}





class Oyunalan� extends JPanel implements MouseMotionListener , MouseListener, ActionListener, KeyListener  {
	
	
	private static final long serialVersionUID = 1L;
	static final int BUTTONWIDHT =200;
	static final int BUTTONHEIGHT =30;
	static final int BUTTONSPACING =10;
	static final int SHOTWIDTH =16;
	static final int SHOTHEIGHT=32;
	private int mode; 
	private Karakter karakter;
	private JButton startButton;
	private JButton exitButton;
	private Anapencere owner;
	private Font headerFont;
	private Font textFont;
	private Font footerFont;
	private Vector<Mob> shotVekt�r�;	
	private Vector<Mob> dusmanVekt�r�;
	private Vector<Mob> dusmanShotVekt�r�;
	private int health;
	private int energy;
	private Image imageBG;
	private Image image;
	private Image image1;
	private Image image2;
	private Image image3;
	private Image image4;
	private ImageIcon icon;
	private long score;
	private int targetX;
	private int targetY;
	private Cursor blankCursor;
	private int backgroundCounter;
	private boolean shield;
	private Robot robot;
	

	public Oyunalan�(Anapencere owner){
		
		dusmanVekt�r�=new Vector<Mob>();
		dusmanShotVekt�r�=new Vector<Mob>();
		shotVekt�r�=new Vector<Mob>();
		mode=0;
		this.owner=owner;
		setHealth(100);	
		setEnergy(150);
		backgroundCounter=0;
		setShield(false);
		try {
			robot=new Robot();
		} catch (AWTException e) {
			//  Auto-generated catch block
			e.printStackTrace();
			robot=null;
		}
		
		blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");
		
		startButton=new JButton("NEW GAME");
		exitButton=new JButton("EXIT");
		setLayout(null);
		startButton.setBounds(owner.getWidth()/2-BUTTONWIDHT/2, owner.getHeight()/2-BUTTONHEIGHT/2, BUTTONWIDHT, BUTTONHEIGHT);
		exitButton.setBounds(owner.getWidth()/2-BUTTONWIDHT/2, owner.getHeight()/2-BUTTONHEIGHT/2+BUTTONHEIGHT+BUTTONSPACING, BUTTONWIDHT, BUTTONHEIGHT); //15 is centering, 30 is button offset, 10 is spacing
	
		add(startButton);
		add(exitButton);
		startButton.addActionListener(this);
		exitButton.addActionListener(this);
		addKeyListener(this);
		headerFont = new Font("SansSerif", Font.BOLD, 48);
		textFont = new Font("SansSerif", Font.BOLD, 20);
		footerFont = new Font("SansSerif", Font.PLAIN, 14);
		
		
		//try{
			imageBG = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/bg.png"));
			image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/char.png"));
			image1 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/shot.png"));
			image2 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/char1.png"));
			image3 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/shot1.png"));
			image4 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/shield.png"));
			icon = new ImageIcon(image);
			karakter= new Karakter(image.getWidth(null),image.getHeight(null),icon, this); //create label that will follow mouse
			karakter.setVisible(false);			
			add(karakter);					
		//}
		//catch(IOException e){
		//	System.out.print("dosya yok!");
		//	System.exit(ABORT);
		//}		
	}
	public Vector<Mob> getDusmanVekt�r�(){
		return dusmanVekt�r�;
	}
	public Vector<Mob> getDusmanShotVekt�r�(){
		return dusmanShotVekt�r�;
	}
	public Vector<Mob> getShotVekt�r�(){
		return shotVekt�r�;
	}
	public Karakter getKarakter(){
		return karakter;
	}
	
	
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public boolean getShield() {
		return shield;
	}
	public void setShield(boolean shield) {
		this.shield = shield;
	}
	public int getEnergy() {
		return energy;
	}
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	public void setDefaultCursor(){
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	public void setTransparentCursor(){
		setCursor(blankCursor);
	}
	public void lose(long kill){
		mode=2;
		karakter.setVisible(false);
		startButton.setVisible(true);
		exitButton.setVisible(true);
		score=kill;
		setDefaultCursor();
		
	}
	public void updateScore(long kill){		
		score=kill;		
		
	}
	
	public void suspend(){ //pause menu
		mode=3; //pause menu
		karakter.setVisible(false);
		startButton.setVisible(true);
		exitButton.setVisible(true);
		
	}
	
	public Anapencere getOwner(){		
		return owner;
	}
	
	/**
	 * // 0=menu, 1=gameplay, 2=endmenu 3=pausemenu
	 * @param s
	 */
	public void setMode(int s) {
		mode=s; 
	}
	
	/**
	 * // 0=menu, 1=gameplay, 2=endmenu 3=pausemenu
	 * @param s
	 */
	public int getMode() {
		return mode;
	}
	
	private void drawMenu(Graphics g){
		
		startButton.setVisible(true);
    	exitButton.setVisible(true);	    	
    	karakter.setVisible(false); //make char visible
		
		    
	    FontMetrics textMetrics = g.getFontMetrics(textFont);
	    FontMetrics headerMetrics = g.getFontMetrics(headerFont);
	    FontMetrics footerMetrics = g.getFontMetrics(footerFont);
	    
	    g.setFont(headerFont);    
	    
	    int cx=(owner.getWidth()/2)-(headerMetrics.stringWidth("SMUB THE GAME")/2); //get window center with width, subtract half of string length to set x anchor
	    int cy=(owner.getHeight()/2)-200; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString("SMUB THE GAME",cx , cy);
	    
	    g.setFont(textFont); 
	    
	    String instructions = "Control the character with mouse.";
	    int x1=(owner.getWidth()/2)-(textMetrics.stringWidth(instructions)/2); //get window center with width, subtract half of string length to set x anchor
	    int y1=(owner.getHeight()/2)+100; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(instructions, x1 , y1);
	    
	    instructions = "Left click to shoot.";
	    x1=(owner.getWidth()/2)-(textMetrics.stringWidth(instructions)/2); //get window center with width, subtract half of string length to set x anchor
	    y1+=40; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(instructions, x1 , y1);
	    instructions = "Right click to Shield.";
	    x1=(owner.getWidth()/2)-(textMetrics.stringWidth(instructions)/2); //get window center with width, subtract half of string length to set x anchor
	    y1+=40; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(instructions, x1 , y1);
	    instructions = "Shield energy recharges when shield is off.";
	    x1=(owner.getWidth()/2)-(textMetrics.stringWidth(instructions)/2); //get window center with width, subtract half of string length to set x anchor
	    y1+=40; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(instructions, x1 , y1);
	    instructions = "Esc to pause.";
	    x1=(owner.getWidth()/2)-(textMetrics.stringWidth(instructions)/2); //get window center with width, subtract half of string length to set x anchor
	    y1+=40; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(instructions, x1 , y1);
	    
	    g.setFont(footerFont);
	    String footer = "Coding by Alp Bilgin, Graphics by �brahim Muhammet �elik";
	    x1=(owner.getWidth()/2)-(footerMetrics.stringWidth(footer)/2); //get window center with width, subtract half of string length to set x anchor
	    y1=owner.getHeight()-owner.getInsets().bottom-30; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(footer, x1 , y1);
	}
	private void drawGameplay(Graphics g){
		startButton.setVisible(false);
    	exitButton.setVisible(false);	    	
    	karakter.setVisible(true); //make char visible
    	
    	//following is background
    	
    	g.drawImage(imageBG,0,backgroundCounter,null);
    	g.drawImage(imageBG,0,backgroundCounter-700,null);
    	backgroundCounter++;
    	backgroundCounter%=700;
    	
    	//following are mobs
		
		for(int i=0; i<dusmanVekt�r�.size(); i++){
	    	g.drawImage(image2,dusmanVekt�r�.elementAt(i).getX(),dusmanVekt�r�.elementAt(i).getY(),null);
		}
	    for(int i=0; i<dusmanShotVekt�r�.size(); i++){
	    	g.drawImage(image3,dusmanShotVekt�r�.elementAt(i).getX(),dusmanShotVekt�r�.elementAt(i).getY(),null);
	    }
	    for(int i=0; i<shotVekt�r�.size(); i++){
	    	g.drawImage(image1,shotVekt�r�.elementAt(i).getX()-owner.getInsets().left,shotVekt�r�.elementAt(i).getY()-owner.getInsets().top,null);
	    }
	    
	    if(getShield()){
	    	g.drawImage(image4,targetX-owner.getX()-image4.getWidth(null)/2-owner.getInsets().left,targetY-owner.getY()-image4.getHeight(null)/2-owner.getInsets().top,null);
	    	
	    }
	    
	    // following is health bar
	    g.setFont(footerFont);
	    FontMetrics footerMetrics  = g.getFontMetrics(footerFont);
	    
	    g.fillRect(90, 590, 320, 52);
	    
	    if(getHealth()>=0){
			g.setColor(Color.RED);
			g.fillRect(100, 600, getHealth()*3, 15);
			
		}
	    if(getEnergy()>=0){
			g.setColor(Color.YELLOW);
			g.fillRect(100, 617, getEnergy()*2, 15);			
		}
	    if(!getShield() && getEnergy()<150)
	    	setEnergy(getEnergy()+1);
	    
	    g.setColor(Color.BLACK);
	    String label = "Health:";
	    int x1=100; 
	    int y1=593+footerMetrics.getHeight();
	    g.drawString(label, x1 , y1);
	    label = "Energy:";
	    x1=100; 
	    y1=610+footerMetrics.getHeight();
	    g.drawString(label, x1 , y1);
	    
	   
	    
	    
		
		
	}
	private void drawEndMenu(Graphics g){
		startButton.setVisible(true);
    	exitButton.setVisible(true);	    	
    	karakter.setVisible(false); //make char visible
		
		
		FontMetrics fontMetrics = g.getFontMetrics(headerFont);	    
	    g.setFont(headerFont);    
	    
	    int cx=(owner.getWidth()/2)-(fontMetrics.stringWidth("YOU LOST")/2); //get window center with width, subtract half of string length to set x anchor
	    int cy=(owner.getHeight()/2)-200; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString("YOU LOST",cx , cy);
	    
	    int sx=(owner.getWidth()/2)-(fontMetrics.stringWidth("SCORE: "+Objects.toString(score))/2); //get window center with width, subtract half of string length to set x anchor
	    int sy=(owner.getHeight()/2)-150; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString("SCORE: "+Objects.toString(score),sx , sy);
	    
		
	}
	/**
	 * Call this function form an API function that is called by OS with Graphics g as parameter
	 * @param g
	 */
	private void drawPauseMenu(Graphics g){
		
		startButton.setVisible(true);
		exitButton.setVisible(true);		    	
    	karakter.setVisible(false); //make char visible
		
		FontMetrics fontMetrics = g.getFontMetrics(headerFont);
		FontMetrics textMetrics = g.getFontMetrics(textFont);
		FontMetrics footerMetrics = g.getFontMetrics(footerFont);
	    g.setFont(headerFont);    
	    
	    int cx=(owner.getWidth()/2)-(fontMetrics.stringWidth("PAUSED")/2); //get window center with width, subtract half of string length to set x anchor
	    int cy=(owner.getHeight()/2)-200; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString("PAUSED",cx , cy);
	    
	    int sx=(owner.getWidth()/2)-(fontMetrics.stringWidth("SCORE: "+Objects.toString(score))/2); //get window center with width, subtract half of string length to set x anchor
	    int sy=(owner.getHeight()/2)-150; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString("SCORE: "+Objects.toString(score),sx , sy);
	    
	    fontMetrics = g.getFontMetrics(textFont);	
	    g.setFont(textFont);  
	    
	    int kx=(owner.getWidth()/2)-(fontMetrics.stringWidth("Press ESC to unpause.")/2); //get window center with width, subtract half of string length to set x anchor
	    int ky=(owner.getHeight()/2)-100; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString("Press ESC to unpause.",kx , ky);
	    
 g.setFont(textFont); 
	    
	    String instructions = "Control the character with mouse.";
	    int x1=(owner.getWidth()/2)-(textMetrics.stringWidth(instructions)/2); //get window center with width, subtract half of string length to set x anchor
	    int y1=(owner.getHeight()/2)+100; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(instructions, x1 , y1);
	    
	    instructions = "Left click to shoot.";
	    x1=(owner.getWidth()/2)-(textMetrics.stringWidth(instructions)/2); //get window center with width, subtract half of string length to set x anchor
	    y1+=40; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(instructions, x1 , y1);
	    instructions = "Right click to Shield.";
	    x1=(owner.getWidth()/2)-(textMetrics.stringWidth(instructions)/2); //get window center with width, subtract half of string length to set x anchor
	    y1+=40; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(instructions, x1 , y1);
	    instructions = "Shield energy recharges when shield is off.";
	    x1=(owner.getWidth()/2)-(textMetrics.stringWidth(instructions)/2); //get window center with width, subtract half of string length to set x anchor
	    y1+=40; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(instructions, x1 , y1);
	    instructions = "Esc to pause.";
	    x1=(owner.getWidth()/2)-(textMetrics.stringWidth(instructions)/2); //get window center with width, subtract half of string length to set x anchor
	    y1+=40; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(instructions, x1 , y1);
	    
	    g.setFont(footerFont);
	    String footer = "Coding by Alp Bilgin, Graphics by �brahim Muhammet �elik";
	    x1=(owner.getWidth()/2)-(footerMetrics.stringWidth(footer)/2); //get window center with width, subtract half of string length to set x anchor
	    y1=owner.getHeight()-owner.getInsets().bottom-30; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString(footer, x1 , y1);
	    
		
	}
	
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);	
		
		if(getMode() ==0){
			drawMenu(g);
		}
		else if(getMode()==1){
			drawGameplay(g);
		}
		else if(getMode()==2){
			drawEndMenu(g);
		}
		else if(getMode()==3){
			drawPauseMenu(g);
		}
	}

	
	@Override
	public void mouseDragged(MouseEvent e) {
		

		if(getMode() ==0){//drawMenu is called by paint 
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
		}
		else if(getMode()==1){//drawGameplay is called by paint
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
			
			if (targetX > karakter.getWidth()/2 + owner.getX() + owner.getInsets().left && targetX < (owner.getWidth()-karakter.getWidth()/2+ owner.getX()-owner.getInsets().right) ) {
				karakter.setX(targetX - owner.getX() - owner.getInsets().left-(karakter.getWidth()/2)); 	
			}
			if (targetY > karakter.getHeight()/2 + owner.getY() + owner.getInsets().top && targetY < (owner.getHeight()-karakter.getHeight()/2+ owner.getY()-owner.getInsets().bottom)){
				karakter.setY(targetY - owner.getY() - owner.getInsets().top-(karakter.getHeight()/2));
			}
		}
		else if(getMode()==2){//drawEndMenu is   called
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
		}
		else if(getMode()==3){//drawEndMenu is   called
			if(robot==null){
				targetX=e.getLocationOnScreen().x;
				targetY=e.getLocationOnScreen().y;
			}
		}
		
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
				

		if(getMode() ==0){//drawMenu is called by paint 
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
		}
		else if(getMode()==1){//drawGameplay is called by paint
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
			
			if (targetX > karakter.getWidth()/2 + owner.getX() + owner.getInsets().left && targetX < (owner.getWidth()-karakter.getWidth()/2+ owner.getX()-owner.getInsets().right) ) {
				karakter.setX(targetX - owner.getX() - owner.getInsets().left-(karakter.getWidth()/2)); 	
			}
			if (targetY > karakter.getHeight()/2 + owner.getY() + owner.getInsets().top && targetY < (owner.getHeight()-karakter.getHeight()/2+ owner.getY()-owner.getInsets().bottom)){
				karakter.setY(targetY - owner.getY() - owner.getInsets().top-(karakter.getHeight()/2));
			}
		}
		else if(getMode()==2){//drawEndMenu is   called
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
		}
		else if(getMode()==3){//drawEndMenu is   called
			
			if(robot==null){
				targetX=e.getLocationOnScreen().x;
				targetY=e.getLocationOnScreen().y;
				
				
				
				if (targetX > karakter.getWidth()/2 + owner.getX() + owner.getInsets().left && targetX < (owner.getWidth()-karakter.getWidth()/2+ owner.getX()-owner.getInsets().right) ) {
					karakter.setX(targetX - owner.getX() - owner.getInsets().left-(karakter.getWidth()/2)); 	
				}
				if (targetY > karakter.getHeight()/2 + owner.getY() + owner.getInsets().top && targetY < (owner.getHeight()-karakter.getHeight()/2+ owner.getY()-owner.getInsets().bottom)){
					karakter.setY(targetY - owner.getY() - owner.getInsets().top-(karakter.getHeight()/2));
				}
			}
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {		
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		
		if(e.getButton()==MouseEvent.BUTTON3 && getEnergy()>=149 && getMode()==1){
			setShield(true);
		}
		
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(getMode() ==0){//drawMenu is called by paint 
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;	
			
			    	
			    
		}
		else if(getMode()==1){//drawGameplay is called by paint
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
			if(e.getButton()== MouseEvent.BUTTON1){
				owner.getPipe().lockB=1;
				while(owner.getPipe().lockA==0){
					try {
						Thread.sleep(1);
					} catch (InterruptedException e1) {
						
						System.out.print(e1.getMessage());
					}				
				}
				shotVekt�r�.add(new Mob(targetX-owner.getX()-(SHOTWIDTH/2) ,targetY-owner.getY()-SHOTHEIGHT));
				owner.getPipe().lockA=0;
				owner.getPipe().lockB=0;
			}
			if(e.getButton()==MouseEvent.BUTTON3){
				setShield(false);
			}
		}
		else if(getMode()==2){//drawEndmenu is called 
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
		}
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		//  Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		//  Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source==startButton){
			if(getMode()==3){
				this.owner.paused=true;
				setDefaultCursor();
				
			}
			setMode(1);
			setTransparentCursor();
		}
		else if(source==exitButton){
			System.exit(0);
		}
		
	}
	@Override
	public void keyTyped(KeyEvent e) {	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		//  Auto-generated method stub
		if(getMode()==1 && e.getKeyCode()==KeyEvent.VK_ESCAPE){
			setMode(3);
			setDefaultCursor();
		}
		else if(getMode()==3 && e.getKeyCode()==KeyEvent.VK_ESCAPE){
			setMode(1);
			setCursor(blankCursor);
			if(robot!=null){
				if (targetX > karakter.getWidth()/2 + owner.getX() + owner.getInsets().left && targetX < (owner.getWidth()-karakter.getWidth()/2+ owner.getX()-owner.getInsets().right)&& targetY > karakter.getHeight()/2 + owner.getY() + owner.getInsets().top && targetY < (owner.getHeight()-karakter.getHeight()/2+ owner.getY()-owner.getInsets().bottom)  ) {
					robot.mouseMove(targetX, targetY); 	
				}
				
				
			}
		}
		/**
		 * this is purely for debugging. pressing A freezes shots so hitboxes and collision can be tested.
		 */
		/*if(e.getKeyCode()==KeyEvent.VK_A){
			owner.moveshots= (!owner.moveshots);
		}*/
		
		
	}
	@Override
	//
	public void keyReleased(KeyEvent e) {
		
	}
}

class Anapencere extends JFrame{
	
	
	private static final long serialVersionUID = 1L;
	private Oyunalan� oyunalan�;
	private DoubleLockPipe pipe;
	public boolean moveshots;
	public boolean paused;
	

	public Anapencere(int x, int y, String isim,DoubleLockPipe pipe){
		setSize(x, y);
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    
	    this.setLocation((int) ((dimension.getWidth() - this.getWidth()) / 2), (int) ((dimension.getHeight() - this.getHeight()) / 2));
		
		setTitle(isim);
		setResizable(false);		
		oyunalan� = new Oyunalan�(this);
		oyunalan�.setFocusable(true);
		oyunalan�.requestFocusInWindow();
		
		addWindowListener(new Pencere�dareci(oyunalan�));
		
		this.pipe=pipe;
		moveshots=true;
		
		Container contentPane = getContentPane();
		contentPane.add(oyunalan�);
		addMouseMotionListener(oyunalan�);
		addMouseListener(oyunalan�);
		
	}
	
	public DoubleLockPipe getPipe(){
		return pipe;
	}
	
	public Oyunalan� getPanel(){
		return oyunalan�;		
	}
	
	
}

/**
 * The worker thread for internal logic and draw requests
 * 
 * @author pc
 *
 */
class BackThread implements Runnable {
	private Anapencere anapencere;
	// this is a flag used to signal enemy coordinate initialisation
	private boolean d�sman�ret;
	// this is a counter for internal periodicity
	private int counter;
	//random generator
	private Random random;
	//vector to contain NPC data
	private Vector<Mob> d�smanVekt�r�;
	//vectors for generated shots
	private Vector<Mob> d�smanShotVekt�r�;
	private Vector<Mob> shotVekt�r�;
	//the object for PC
	private Karakter karakter;
	//mutex buffer for mouse event transmit safety	
	private DoubleLockPipe pipe;
	// kill counter (gets dumped into score attribute on the other side)
	private long kill;	
	// hitbox dimensions
	static final int D�SMANWIDTH =26;
	static final int D�SMANHEIGHT =32;
	static final int SHOTWIDTH =16;
	static final int SHOTHEIGHT=32;
	static final int KARAKTERWIDTH =14;
	static final int KARAKTERHEIGHT =32;
	static final int DUSMANSHOTSIZE=8;
	
	//constructor
	public BackThread(Anapencere anapencere, int d�smansayisi, DoubleLockPipe pipe){
		this.anapencere = anapencere;
		random=new Random();
		counter=0;
		d�sman�ret=false;
		
		d�smanVekt�r�=anapencere.getPanel().getDusmanVekt�r�();
		d�smanShotVekt�r�=anapencere.getPanel().getDusmanShotVekt�r�();
		shotVekt�r�=anapencere.getPanel().getShotVekt�r�();
		this.karakter=anapencere.getPanel().getKarakter();
		this.pipe=pipe;
		kill=0;
		// auto initialise enemies while the game is being loaded for fast internal transition.
		for(int i = 0; i<d�smansayisi;i++) d�smanVekt�r�.add(new Mob(random.nextInt(this.anapencere.getWidth()-(2*D�SMANWIDTH))+(D�SMANWIDTH/2),-random.nextInt(this.anapencere.getHeight())-(D�SMANHEIGHT/2))); // populate and initialise enemy vector
	}
	
	//It is pointless to destroy enemies in an infinite runner. So they re simply reshuffles t out of the screen.
	public void reInitialiseEnemies(){
		
		for(int i=0; i<d�smanVekt�r�.size(); i++){
			d�smanVekt�r�.elementAt(i).setY(-random.nextInt(500));
		}
		
	}
	//destroy all shots. Reset PC resources.
	public void clearTable(){
		
		d�smanShotVekt�r�.clear();
		shotVekt�r�.clear();
		anapencere.getPanel().setHealth(100);
		anapencere.getPanel().setEnergy(150);
		
	}

	//This is where the bulk of the internal logic happens
	public void increment(){
		
		
		// move enemies , generate shots when enemies are in window AND loop enemies when they leave window
		for(int i=0; i<d�smanVekt�r�.size(); i++){
			
			//move enemies, the visible result is the average of the two incrementing integers
			if(counter%2==0)d�smanVekt�r�.elementAt(i).setY(d�smanVekt�r�.elementAt(i).getY()+3);// bunu parametrik yap
			else d�smanVekt�r�.elementAt(i).setY(d�smanVekt�r�.elementAt(i).getY()+2);// bunu parametrik yap
			//every 75 iterations every enemy lower than window y coordinate generates a shot			
			if(counter%75==0 && d�smanVekt�r�.elementAt(i).getY()>anapencere.getY()) d�smanShotVekt�r�.add(new Mob(d�smanVekt�r�.elementAt(i).getX()+9,d�smanVekt�r�.elementAt(i).getY()+12));// 9 and 12 are the offsets taht center the shot inside the mob
			//enemy is recycled if it leaves window.
			if(d�smanVekt�r�.elementAt(i).getY() > (anapencere.getY()+anapencere.getHeight())){
				d�smanVekt�r�.elementAt(i).setY(-random.nextInt(this.anapencere.getHeight())-(D�SMANHEIGHT/2));
				d�smanVekt�r�.elementAt(i).setX(random.nextInt(this.anapencere.getWidth()-(2*D�SMANWIDTH))+(D�SMANWIDTH/2));
			}		
		}
		
		//move enemy shots and collision detection between enemy shots and player
		for(int k=0; k<this.d�smanShotVekt�r�.size(); k++){
			//move enemy shots
			if(anapencere.moveshots)d�smanShotVekt�r�.elementAt(k).setY(d�smanShotVekt�r�.elementAt(k).getY()+4);// bunu parametrik yap
			//collision detection
			if(karakter.getX()-d�smanShotVekt�r�.elementAt(k).getX() < DUSMANSHOTSIZE
					&& d�smanShotVekt�r�.elementAt(k).getX()-karakter.getX() < KARAKTERWIDTH 
					&& karakter.getY()-d�smanShotVekt�r�.elementAt(k).getY() < DUSMANSHOTSIZE					
					&& d�smanShotVekt�r�.elementAt(k).getY()-karakter.getY() <  KARAKTERHEIGHT ){
						// if shield is not active, reduce health
						if(!anapencere.getPanel().getShield())	anapencere.getPanel().setHealth(anapencere.getPanel().getHealth()-10);
						//remove shot regardles of damage
						d�smanShotVekt�r�.remove(k);						
			}			
		}
		
		//remove enemy shots when they leave window
		for(int k=0; k<this.d�smanShotVekt�r�.size(); k++){
			if(d�smanShotVekt�r�.elementAt(k).getY() > (anapencere.getY()+anapencere.getHeight())){
				d�smanShotVekt�r�.remove(k);
			}
		}
		
		// move player shots, detect collision between enemies and player shots(loop dead enemies and remove shot), remove player shots when they leave window
		for(int j=0; j<shotVekt�r�.size(); j++){
			//move player shots
			shotVekt�r�.elementAt(j).setY(shotVekt�r�.elementAt(j).getY()-4); // bunu parametrik yap
			
			boolean collision =false;
			//check for collision
			for(int i=0; i<d�smanVekt�r�.size(); i++){				
				if(d�smanVekt�r�.elementAt(i).getX()-shotVekt�r�.elementAt(j).getX() < SHOTWIDTH   
						&& d�smanVekt�r�.elementAt(i).getY()-shotVekt�r�.elementAt(j).getY() < SHOTHEIGHT 
						&& shotVekt�r�.elementAt(j).getX()-d�smanVekt�r�.elementAt(i).getX() < D�SMANWIDTH 
						&& shotVekt�r�.elementAt(j).getY()-d�smanVekt�r�.elementAt(i).getY() < D�SMANHEIGHT  ){ 
					//recycle shot enemy
					d�smanVekt�r�.elementAt(i).setY(-random.nextInt(this.anapencere.getHeight())-(D�SMANHEIGHT/2));
					d�smanVekt�r�.elementAt(i).setX(random.nextInt(this.anapencere.getWidth()-(2*D�SMANWIDTH))+(D�SMANWIDTH/2));
					//increment kill counter
					kill++;
					//set collision true
					collision=true;
					//break inner loop
					break;
				}
				
			}
			//if inner loop detects a collision remove shot in outer loop
			if(collision) shotVekt�r�.remove(j);
		}
		
		//if shield is not active, check for player/enemy collision
		if(!anapencere.getPanel().getShield()){
			for(int i=0; i<d�smanVekt�r�.size(); i++){
			
				if(d�smanVekt�r�.elementAt(i).getX()-karakter.getX() < KARAKTERWIDTH   
						&& d�smanVekt�r�.elementAt(i).getY()-karakter.getY() < KARAKTERHEIGHT 
						&& karakter.getX()-d�smanVekt�r�.elementAt(i).getX() < D�SMANWIDTH 
						&& karakter.getY()-d�smanVekt�r�.elementAt(i).getY() < D�SMANHEIGHT  ){
					//if collision recycle enemy
					d�smanVekt�r�.elementAt(i).setY(-random.nextInt(this.anapencere.getHeight())-(D�SMANHEIGHT/2));// bunu parametrik yap
					d�smanVekt�r�.elementAt(i).setX(random.nextInt(this.anapencere.getWidth()-(2*D�SMANWIDTH))+(D�SMANWIDTH/2));
					//reduce health
					anapencere.getPanel().setHealth(anapencere.getPanel().getHealth()-10);
					//increase kill count
					kill++;				
				}			
			}
		}
		//if shield is active, displace enemies to either left or right depending on angle of contact
		else{	
			for(int i=0; i<d�smanVekt�r�.size(); i++){
				if(karakter.getX()-d�smanVekt�r�.elementAt(i).getX() < D�SMANWIDTH   
						&& d�smanVekt�r�.elementAt(i).getY()-karakter.getY() < KARAKTERHEIGHT 
						&& karakter.getX()-d�smanVekt�r�.elementAt(i).getX() >= (D�SMANWIDTH-KARAKTERWIDTH)/2 
						&& karakter.getY()-d�smanVekt�r�.elementAt(i).getY() < D�SMANHEIGHT  ){
					d�smanVekt�r�.elementAt(i).setX(d�smanVekt�r�.elementAt(i).getX()-2);
				}
				if(d�smanVekt�r�.elementAt(i).getX()-karakter.getX() < KARAKTERWIDTH   
						&& d�smanVekt�r�.elementAt(i).getY()-karakter.getY() < KARAKTERHEIGHT 
						&& karakter.getX()-d�smanVekt�r�.elementAt(i).getX() < (D�SMANWIDTH-KARAKTERWIDTH)/2  
						&& karakter.getY()-d�smanVekt�r�.elementAt(i).getY() < D�SMANHEIGHT  ){
					d�smanVekt�r�.elementAt(i).setX(d�smanVekt�r�.elementAt(i).getX()+2);
				}
				
			}
			
		}
		//remove player shots if they move beyond window
		for(int j=0; j<shotVekt�r�.size(); j++){
			if(/*shotVekt�r�.size()>=(j+1) &&*/ shotVekt�r�.elementAt(j).getY() < anapencere.getY() )
				shotVekt�r�.remove(j) ;
		}
		
		//if health is less or equal to 0, end game
		if(anapencere.getPanel().getHealth()<=0){
			//function to change window state
			anapencere.getPanel().lose(kill);//loss condition
			//flag to change internal logic state
			d�sman�ret=true;			
		}
		
		//drain shield energy while shield is active
		if(anapencere.getPanel().getShield()){
			anapencere.getPanel().setEnergy(anapencere.getPanel().getEnergy()-1);
			//if shield energy is empty, turn off shield
			if(anapencere.getPanel().getEnergy()<=0){
				anapencere.getPanel().setShield(false);
			}
		}
		
		//periodically update score
		this.anapencere.getPanel().updateScore(kill);
		
		
	}
	
	public void run(){
		while(true){
			
			//reset internal logic if game is restarted while paused
			if(anapencere.paused && anapencere.getPanel().getMode() == 1){
				d�sman�ret=true;
				anapencere.paused=false;				
			}
			
			//if internal reset flag is set, go ahead and do it. TODO
			if(d�sman�ret){
				clearTable();
				reInitialiseEnemies();
				d�sman�ret=false;
				kill=0;
			}			
			
			if(pipe.lockB==1){
				pipe.lockA=1;
				try{
					Thread.sleep(1);
				}
				catch(InterruptedException e2){
					continue;				
				}
			}
			//Only increment is if gameplay is active
			else if (anapencere.getPanel().getMode()==1){		
				pipe.lockA=0;					
				increment();		
				counter++;			
				try{
					Thread.sleep(20);
				}
				catch(InterruptedException e){
					continue;				
				}				
			}
			else{
				try{
					Thread.sleep(20);
				}
				catch(InterruptedException e){
					continue;				
				}
				
			}
			anapencere.getPanel().repaint();
			
		}
		
	}
} 

class FrontThread implements Runnable {
	
	private Anapencere anapencere;
	

	FrontThread(Anapencere anapencere){
		this.anapencere = anapencere;
		
	}
	
	public void run() {
		anapencere.setVisible(true);
		
	}
	
}

public class Ceri {
	
	static final int D�SMANSAYISI =20;	

	public static void main(String[] args){
		
		//this is a custom mutex buffer. See class declaration
		DoubleLockPipe pipe = new DoubleLockPipe();
		
		//custom mutex buffer is an attribute of the main window thread functions will access this internal buffer of window object with protocol
		Anapencere anapencere = new Anapencere(500,700,"SMUB",pipe);
		
		//start a new thread. See FrontThread.run() to see what is actually does	
		new Thread(new FrontThread(anapencere)).start();
		
		//start a new thread. See BackThread.run() to see what is actually does
		new Thread(new BackThread(anapencere,D�SMANSAYISI,pipe)).start();		
		
		return;

	}

}
