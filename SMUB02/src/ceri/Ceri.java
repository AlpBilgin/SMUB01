package ceri;


import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
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




class PencereÝdareci extends WindowAdapter{	
	private Oyunalaný oyunalaný;
	public PencereÝdareci(Oyunalaný oyunalaný){
		super();		
		this.oyunalaný=oyunalaný;
	}
	public void windowClosing(WindowEvent e){
		System.exit(0);
	}	
	public void windowLostFocus(WindowEvent e){
		oyunalaný.setMode(3);
		oyunalaný.setDefaultCursor();
	}
	public void windowDeactivated(WindowEvent e){
		oyunalaný.setMode(3);
		oyunalaný.setDefaultCursor();
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
	//private Oyunalaný owner;
	
	// override inherited x and y for convenience
	private int x;
	private int y;
	private int width;
	private int height;
	
	//constructor
	public Karakter(int widht, int height, Icon resim ,Oyunalaný aidiyet){
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





class Oyunalaný extends JPanel implements MouseMotionListener , MouseListener, ActionListener, KeyListener  {
	
	
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
	private Vector<Mob> shotVektörü;	
	private Vector<Mob> dusmanVektörü;
	private Vector<Mob> dusmanShotVektörü;
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
	

	public Oyunalaný(Anapencere owner){
		
		dusmanVektörü=new Vector<Mob>();
		dusmanShotVektörü=new Vector<Mob>();
		shotVektörü=new Vector<Mob>();
		mode=0;
		this.owner=owner;
		setHealth(100);	
		setEnergy(300);
		backgroundCounter=0;
		setShield(false);
		
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
	public Vector<Mob> getDusmanVektörü(){
		return dusmanVektörü;
	}
	public Vector<Mob> getDusmanShotVektörü(){
		return dusmanShotVektörü;
	}
	public Vector<Mob> getShotVektörü(){
		return shotVektörü;
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
	    String footer = "Coding by Alp Bilgin, Graphics by Ýbrahim Muhammet Çelik";
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
		
		for(int i=0; i<dusmanVektörü.size(); i++){
	    	g.drawImage(image2,dusmanVektörü.elementAt(i).getX(),dusmanVektörü.elementAt(i).getY(),null);
		}
	    for(int i=0; i<dusmanShotVektörü.size(); i++){
	    	g.drawImage(image3,dusmanShotVektörü.elementAt(i).getX(),dusmanShotVektörü.elementAt(i).getY(),null);
	    }
	    for(int i=0; i<shotVektörü.size(); i++){
	    	g.drawImage(image1,shotVektörü.elementAt(i).getX()-owner.getInsets().left,shotVektörü.elementAt(i).getY()-owner.getInsets().top,null);
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
			g.fillRect(100, 617, getEnergy(), 15);			
		}
	    if(!getShield() && getEnergy()<300)
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
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {		
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		
		if(e.getButton()==MouseEvent.BUTTON3){
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
				shotVektörü.add(new Mob(targetX-owner.getX()-(SHOTWIDTH/2) ,targetY-owner.getY()-SHOTHEIGHT));
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
		}
		/**
		 * 
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
	private Oyunalaný oyunalaný;
	private DoubleLockPipe pipe;
	public boolean moveshots;
	public boolean paused;
	

	public Anapencere(int x, int y, String isim,DoubleLockPipe pipe){
		setSize(x, y);
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    
	    this.setLocation((int) ((dimension.getWidth() - this.getWidth()) / 2), (int) ((dimension.getHeight() - this.getHeight()) / 2));
		
		setTitle(isim);
		setResizable(false);		
		oyunalaný = new Oyunalaný(this);
		oyunalaný.setFocusable(true);
		oyunalaný.requestFocusInWindow();
		
		addWindowListener(new PencereÝdareci(oyunalaný));
		
		this.pipe=pipe;
		moveshots=true;
		
		Container contentPane = getContentPane();
		contentPane.add(oyunalaný);
		addMouseMotionListener(oyunalaný);
		addMouseListener(oyunalaný);
		
	}
	
	public DoubleLockPipe getPipe(){
		return pipe;
	}
	
	public Oyunalaný getPanel(){
		return oyunalaný;		
	}
	
	
}


class BackThread implements Runnable {
	private Anapencere anapencere;
	private boolean düsmanüret;	
	private int counter;
	private Random random;
	private Vector<Mob> düsmanVektörü;
	private Vector<Mob> düsmanShotVektörü;
	private Vector<Mob> shotVektörü;
	private Karakter karakter;
	private DoubleLockPipe pipe;
	private long kill;	
	
	static final int DÜSMANWIDTH =26;
	static final int DÜSMANHEIGHT =32;
	static final int SHOTWIDTH =16;
	static final int SHOTHEIGHT=32;
	
	public BackThread(Anapencere anapencere, int düsmansayisi, DoubleLockPipe pipe){
		this.anapencere = anapencere;
		random=new Random();
		counter=0;
		düsmanüret=false;
		
		düsmanVektörü=anapencere.getPanel().getDusmanVektörü();
		düsmanShotVektörü=anapencere.getPanel().getDusmanShotVektörü();
		shotVektörü=anapencere.getPanel().getShotVektörü();
		this.karakter=anapencere.getPanel().getKarakter();
		this.pipe=pipe;
		kill=0;
		for(int i = 0; i<düsmansayisi;i++) düsmanVektörü.add(new Mob(random.nextInt(this.anapencere.getWidth()-(2*DÜSMANWIDTH))+(DÜSMANWIDTH/2),-random.nextInt(this.anapencere.getHeight())-(DÜSMANHEIGHT/2))); // populate and initialise enemy vector
	}
	
	public void reInitialiseEnemies(){
		
		for(int i=0; i<düsmanVektörü.size(); i++){
			düsmanVektörü.elementAt(i).setY(-random.nextInt(500));
		}
		
	}
	
	public void clearTable(){
		
		düsmanShotVektörü.clear();
		shotVektörü.clear();
		anapencere.getPanel().setHealth(100);
		
	}

	
	public void increment(){
		
		
		// move enemies , generate shots when enemies are in window AND loop enemies when they leave window
		for(int i=0; i<düsmanVektörü.size(); i++){
			if(counter%2==0)düsmanVektörü.elementAt(i).setY(düsmanVektörü.elementAt(i).getY()+3);// bunu parametrik yap
			else düsmanVektörü.elementAt(i).setY(düsmanVektörü.elementAt(i).getY()+2);// bunu parametrik yap
			if(counter%75==0 && düsmanVektörü.elementAt(i).getY()>0) düsmanShotVektörü.add(new Mob(düsmanVektörü.elementAt(i).getX()+9,düsmanVektörü.elementAt(i).getY()+12));// 9 and 12 are the offsets taht center the shot inside the mob
			
			if(düsmanVektörü.elementAt(i).getY() > (anapencere.getY()+anapencere.getHeight())){
				düsmanVektörü.elementAt(i).setY(-random.nextInt(this.anapencere.getHeight())-(DÜSMANHEIGHT/2));// bunu parametrik yap
				düsmanVektörü.elementAt(i).setX(random.nextInt(this.anapencere.getWidth()-(2*DÜSMANWIDTH))+(DÜSMANWIDTH/2));// bunu parametrik yap
			}		
		}
		
		//move enemy shots and collision detection between enemy shots and player
		for(int k=0; k<this.düsmanShotVektörü.size(); k++){
			if(anapencere.moveshots)düsmanShotVektörü.elementAt(k).setY(düsmanShotVektörü.elementAt(k).getY()+4);// bunu parametrik yap
			if(karakter.getX()-düsmanShotVektörü.elementAt(k).getX() < 8
					&& düsmanShotVektörü.elementAt(k).getX()-karakter.getX() < 14 
					&& karakter.getY()-düsmanShotVektörü.elementAt(k).getY() < 8					
					&& düsmanShotVektörü.elementAt(k).getY()-karakter.getY() <  32 ){
				
						if(!anapencere.getPanel().getShield())	anapencere.getPanel().setHealth(anapencere.getPanel().getHealth()-10);
						
						düsmanShotVektörü.remove(k);
						break;
						
			}			
		}
		
		//remove enemy shots when they leave window
		for(int k=0; k<this.düsmanShotVektörü.size(); k++){
			if(düsmanShotVektörü.elementAt(k).getY() > (anapencere.getY()+anapencere.getHeight())){
				düsmanShotVektörü.remove(k);
			}
		}
		
		// move player shots, detect collision between enemies and player shots(loop dead enemies and remove shot), remove player shots when they leave window
		for(int j=0; j<shotVektörü.size(); j++){
			shotVektörü.elementAt(j).setY(shotVektörü.elementAt(j).getY()-4); // bunu parametrik yap
			
			boolean collision =false;
			for(int i=0; i<düsmanVektörü.size(); i++){				
				if(düsmanVektörü.elementAt(i).getX()-shotVektörü.elementAt(j).getX() < 16   
						&& düsmanVektörü.elementAt(i).getY()-shotVektörü.elementAt(j).getY() < 32 
						&& shotVektörü.elementAt(j).getX()-düsmanVektörü.elementAt(i).getX() < 26 
						&& shotVektörü.elementAt(j).getY()-düsmanVektörü.elementAt(i).getY() < 32  ){ // bunu parametrik yap
					düsmanVektörü.elementAt(i).setY(-random.nextInt(this.anapencere.getHeight())-(DÜSMANHEIGHT/2));// bunu parametrik yap
					düsmanVektörü.elementAt(i).setX(random.nextInt(this.anapencere.getWidth()-(2*DÜSMANWIDTH))+(DÜSMANWIDTH/2));// bunu parametrik yap
					kill++;
					collision=true;
					break;
				}
				
			}
			if(collision) shotVektörü.remove(j);
		}
		
		for(int i=0; i<düsmanVektörü.size(); i++){
			
			if(düsmanVektörü.elementAt(i).getX()-karakter.getX() < 14   
					&& düsmanVektörü.elementAt(i).getY()-karakter.getY() < 32 
					&& karakter.getX()-düsmanVektörü.elementAt(i).getX() < 26 
					&& karakter.getY()-düsmanVektörü.elementAt(i).getY() < 32  ){
				düsmanVektörü.elementAt(i).setY(-random.nextInt(this.anapencere.getHeight())-(DÜSMANHEIGHT/2));// bunu parametrik yap
				düsmanVektörü.elementAt(i).setX(random.nextInt(this.anapencere.getWidth()-(2*DÜSMANWIDTH))+(DÜSMANWIDTH/2));
				if(!anapencere.getPanel().getShield()) anapencere.getPanel().setHealth(anapencere.getPanel().getHealth()-10);
				kill++;
				
			}
			
		}
		
		for(int j=0; j<shotVektörü.size(); j++){
			if(shotVektörü.size()>=(j+1) && shotVektörü.elementAt(j).getY() < 1 ) shotVektörü.remove(j) ;// bunu parametrik yap
		}
		
		if(anapencere.getPanel().getHealth()<=0){
			anapencere.getPanel().lose(kill);//loss condition
			düsmanüret=true;			
		}
		
		if(anapencere.getPanel().getShield()){
			anapencere.getPanel().setEnergy(anapencere.getPanel().getEnergy()-1);
			if(anapencere.getPanel().getEnergy()<=0){
				anapencere.getPanel().setShield(false);
			}
		}
		
		
		this.anapencere.getPanel().updateScore(kill);
		
		
	}
	
	public void run(){
		while(true){
			
			if(anapencere.paused && anapencere.getPanel().getMode() == 1){
				düsmanüret=true;
				anapencere.paused=false;
				
			}
			
			if(düsmanüret){
				clearTable();
				reInitialiseEnemies();
				düsmanüret=false;
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
	static final int DÜSMANSAYISI =20;	

	public static void main(String[] args){
		
		DoubleLockPipe pipe = new DoubleLockPipe();
		
		Anapencere anapencere = new Anapencere(500,700,"SMUB",pipe);
		
			
		new Thread(new FrontThread(anapencere)).start();
		new Thread(new BackThread(anapencere,DÜSMANSAYISI,pipe)).start();		
		
		return;

	}

}
