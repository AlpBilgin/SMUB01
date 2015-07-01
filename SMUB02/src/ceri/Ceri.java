package ceri;


import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

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
	public Pencere�dareci(){
		super();
	}
	public void windowClosing(WindowEvent e){
		System.exit(0);
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





class Oyunalan� extends JPanel implements MouseMotionListener , MouseListener, ActionListener  {
	
	
	private static final long serialVersionUID = 1L;
	private int state; // 0=menu, 1=gameplay, 2=endmenu 3=pausemenu
	private Karakter karakter;
	private JButton startButton;
	private JButton exitButton;
	private Anapencere owner;
	private Font font;
	private Vector<Mob> shotVekt�r�;	
	private Vector<Mob> dusmanVekt�r�;
	private Vector<Mob> dusmanShotVekt�r�;	
	private Image image;
	private Image image1;
	private ImageIcon icon;
	int targetX;
	int targetY;

	public Oyunalan�(int x, int y, Anapencere owner){
		
		dusmanVekt�r�=new Vector<Mob>();
		dusmanShotVekt�r�=new Vector<Mob>();
		shotVekt�r�=new Vector<Mob>();
		state=0;
		this.owner=owner;
		
		startButton=new JButton("START");
		exitButton=new JButton("EXIT");
		setLayout(null);
		startButton.setBounds(owner.getWidth()/2-100, owner.getHeight()/2-15, 200, 30);
		exitButton.setBounds(owner.getWidth()/2-100, owner.getHeight()/2-15+30+10, 200, 30); //15 is centering, 30 is button offset, 10 is spacing
	
		add(startButton);
		add(exitButton);
		startButton.addActionListener(this);
		exitButton.addActionListener(this);
		font = new Font("SansSerif", Font.BOLD, 20);	
		
		//try{
			image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/char.jpg"));
			image1 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/shot.jpg"));
			icon = new ImageIcon(image);
			karakter= new Karakter(32,32,icon, this); //create label of size 32x32 will follow mouse
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
	
	public void lose(){
		state=2;
		karakter.setVisible(false);
		startButton.setVisible(true);
		exitButton.setVisible(true);
		
		//TODO +++ bunlara g�venli data bo�altma ekle
		//for(int i=0; i<dusmanVekt�r�.size() ;i++) dusmanVekt�r�.remove(i);
		//for(int i=0; i<dusmanShotVekt�r�.size() ;i++) dusmanShotVekt�r�.remove(i);
		//for(int i=0; i<shotVekt�r�.size() ;i++) shotVekt�r�.remove(i);		
		
	}
	
	public void suspend(){ //pause menu
		state=3; //pause menu
		karakter.setVisible(false);
		startButton.setVisible(true);
		exitButton.setVisible(true);
		
	}
	
	public Anapencere getOwner(){		
		return owner;
	}
	
	public void setState(int s) { state=s; }
	public int getstate() {return state;}
	
	private void drawMenu(Graphics g){		
		    
	    FontMetrics fontMetrics = g.getFontMetrics(font);	    
	    g.setFont(font);    
	    
	    int cx=(owner.getWidth()/2)-(fontMetrics.stringWidth("TEKMETOKAT THE GAME")/2); //get window center with width, subtract half of string length to set x anchor
	    int cy=(owner.getHeight()/2)-200; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString("TEKMETOKAT THE GAME",cx , cy);	   
		
	}
	private void drawGameplay(Graphics g){
		for(int i=0; i<dusmanVekt�r�.size(); i++){
	    	g.drawImage(image,dusmanVekt�r�.elementAt(i).getX(),dusmanVekt�r�.elementAt(i).getY(),null);
		}
	    for(int i=0; i<dusmanShotVekt�r�.size(); i++){
	    	g.drawImage(image1,dusmanShotVekt�r�.elementAt(i).getX(),dusmanShotVekt�r�.elementAt(i).getY(),null);
	    }
	    for(int i=0; i<shotVekt�r�.size(); i++){
	    	g.drawImage(image1,shotVekt�r�.elementAt(i).getX(),shotVekt�r�.elementAt(i).getY(),null);
	    }
		
	}
	private void drawEndMenu(Graphics g){
		
		FontMetrics fontMetrics = g.getFontMetrics(font);	    
	    g.setFont(font);    
	    
	    int cx=(owner.getWidth()/2)-(fontMetrics.stringWidth("///o\\")/2); //get window center with width, subtract half of string length to set x anchor
	    int cy=(owner.getHeight()/2)-200; //get window center with height, subtract arbitrary offset to set y anchor
	    g.drawString("///o\\",cx , cy);
		
	}
	
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);	
		
		if(state ==0){
			drawMenu(g);
		}
		else if(state==1){
			drawGameplay(g);
		}
		else if(state==2){
			drawEndMenu(g);
		}	    
	}

	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
				

		if(state ==0){//drawMenu is called by paint TODO
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
		}
		else if(state==1){//drawGameplay is called by paint
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
			
			if (targetX > karakter.getWidth()/2 + owner.getX() + owner.getInsets().left && targetX < (owner.getWidth()-karakter.getWidth()/2+ owner.getX()-owner.getInsets().right) ) {
				karakter.setX(targetX - owner.getX() - owner.getInsets().left-(karakter.getWidth()/2)); 	
			}
			if (targetY > karakter.getHeight()/2 + owner.getY() + owner.getInsets().top && targetY < (owner.getHeight()-karakter.getHeight()/2+ owner.getY()-owner.getInsets().bottom)){
				karakter.setY(targetY - owner.getY() - owner.getInsets().top-(karakter.getHeight()/2));
			}
		}
		else if(state==2){//drawEndMenu is  TODO called
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(state ==0){//drawMenu is called by paint TODO
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;	
			
			    	
			    
		}
		else if(state==1){//drawGameplay is called by paint
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
			if(e.getButton()== MouseEvent.BUTTON1) shotVekt�r�.add(new Mob(targetX-7,targetY-9)); // bunu parametrik yap
		}
		else if(state==2){//drawEndmenu is called TODO
			targetX=e.getLocationOnScreen().x;
			targetY=e.getLocationOnScreen().y;
		}
		
			
			
		
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		//  Auto-generated method stub
		
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
	    	state=1;
	    	startButton.setVisible(false);
	    	exitButton.setVisible(false);	    	
	    	karakter.setVisible(true); //make char visible			
		}
		else if(source==exitButton){
			System.exit(0);
		}
		
	}
}

class Anapencere extends JFrame{
	
	
	private static final long serialVersionUID = 1L;
	private Oyunalan� oyunalan�;
	PipedInputStream pipeHead;

	public Anapencere(int x, int y, String isim, PipedInputStream pipeHead){
		setSize(x, y);
		setTitle(isim);
		setResizable(false);
		addWindowListener(new Pencere�dareci());
		
		oyunalan� = new Oyunalan�(50,50,this);
		oyunalan�.setFocusable(true);
		oyunalan�.requestFocusInWindow();
		
		this.pipeHead=pipeHead;
		
		Container contentPane = getContentPane();
		contentPane.add(oyunalan�);
		addMouseMotionListener(oyunalan�);
		addMouseListener(oyunalan�);
	}
	
	public PipedInputStream getPipeInput(){
		return pipeHead;
	}
	
	public Oyunalan� getPanel(){
		return oyunalan�;		
	}
	
}


class BackThread implements Runnable {
	private Anapencere anapencere;
	private boolean d�sman�ret;
	private int d�smansayisi;
	private int counter;
	private Random random;
	private Vector<Mob> d�smanVekt�r�;
	private Vector<Mob> d�smanShotVekt�r�;
	private Vector<Mob> shotVekt�r�;
	private Karakter karakter;
	
	public BackThread(Anapencere anapencere, int d�smansayisi, PipedOutputStream pipeTail){
		this.anapencere = anapencere;
		random=new Random();
		counter=0;
		d�sman�ret=true;
		this.d�smansayisi=d�smansayisi;
		d�smanVekt�r�=anapencere.getPanel().getDusmanVekt�r�();
		d�smanShotVekt�r�=anapencere.getPanel().getDusmanShotVekt�r�();
		shotVekt�r�=anapencere.getPanel().getShotVekt�r�();
		this.karakter=anapencere.getPanel().getKarakter();
	}
	
	public void addShot(){}

	
	public void increment(){
		
		for(int i=0; i<d�smanVekt�r�.size(); i++){
			if(counter%2==0)d�smanVekt�r�.elementAt(i).setY(d�smanVekt�r�.elementAt(i).getY()+3);// bunu parametrik yap
			else d�smanVekt�r�.elementAt(i).setY(d�smanVekt�r�.elementAt(i).getY()+2);// bunu parametrik yap
			if(counter%75==0) d�smanShotVekt�r�.add(new Mob(d�smanVekt�r�.elementAt(i).getX()+12,d�smanVekt�r�.elementAt(i).getY()+12));// bunu parametrik yap
			
			if(d�smanVekt�r�.elementAt(i).getY() > (anapencere.getY()+anapencere.getHeight())){
				d�smanVekt�r�.elementAt(i).setY(-10);// bunu parametrik yap
				d�smanVekt�r�.elementAt(i).setX(random.nextInt(500));// bunu parametrik yap
			}		
		}
		
		for(int k=0; k<this.d�smanShotVekt�r�.size(); k++){
			d�smanShotVekt�r�.elementAt(k).setY(d�smanShotVekt�r�.elementAt(k).getY()+4);// bunu parametrik yap
			if(karakter.getX()-d�smanShotVekt�r�.elementAt(k).getX() < 6   
					&& karakter.getY()-d�smanShotVekt�r�.elementAt(k).getY() < 6 
					&& d�smanShotVekt�r�.elementAt(k).getX()-karakter.getX() < 31 
					&& d�smanShotVekt�r�.elementAt(k).getY()-karakter.getY() < 31  ){				  
				anapencere.getPanel().lose();//loss condition
				
				
			}
				
			if(d�smanShotVekt�r�.elementAt(k).getY() > (anapencere.getY()+anapencere.getHeight())){
				d�smanShotVekt�r�.remove(k);
			}
			
		}
		
		for(int j=0; j<shotVekt�r�.size(); j++){
			shotVekt�r�.elementAt(j).setY(shotVekt�r�.elementAt(j).getY()-4); // bunu parametrik yap
			
			boolean kill=false;
			for(int i=0; i<d�smanVekt�r�.size(); i++){				
				if(d�smanVekt�r�.elementAt(i).getX()-shotVekt�r�.elementAt(j).getX() < 6   
						&& d�smanVekt�r�.elementAt(i).getY()-shotVekt�r�.elementAt(j).getY() < 6 
						&& shotVekt�r�.elementAt(j).getX()-d�smanVekt�r�.elementAt(i).getX() < 31 
						&& shotVekt�r�.elementAt(j).getY()-d�smanVekt�r�.elementAt(i).getY() < 31  ){ // bunu parametrik yap
					d�smanVekt�r�.elementAt(i).setY((-1*random.nextInt(50)));// bunu parametrik yap
					d�smanVekt�r�.elementAt(i).setX(random.nextInt(500));// bunu parametrik yap
					kill = true;
					break;
				}				
			}
			if(kill)shotVekt�r�.remove(j);
		}
		
		for(int j=0; j<shotVekt�r�.size(); j++){
			if(shotVekt�r�.size()>=(j+1) && shotVekt�r�.elementAt(j).getY() < 1 ) shotVekt�r�.remove(j) ;// bunu parametrik yap
		}
		
		System.out.print("\n");
	}
	
	public void run(){
		while(true){
			
			if((counter%40) == 0 && d�sman�ret){				
				d�smanVekt�r�.add(new Mob(random.nextInt(500),-10));// bunu parametrik yap
			}
			if(d�sman�ret && d�smanVekt�r�.size()>=d�smansayisi){
				d�sman�ret=false;
			}
			
			
			increment();
			
		
			counter++;
			
			try{
			Thread.sleep(20);
			}
			catch(InterruptedException e){
				continue;				
			}
			
			anapencere.getPanel().repaint();
			
		}
		
	}
} 


public class Ceri {
	static final int D�SMANSAYISI =20;

	public static void main(String[] args) throws IOException{
		
		PipedOutputStream pipeTail = new PipedOutputStream();
		PipedInputStream pipeHead = new PipedInputStream(pipeTail);
		
		
		Anapencere anapencere = new Anapencere(500,700,"SMUB TEKMETOKAT!", pipeHead);
		anapencere.setVisible(true);		
		
		new Thread(new BackThread(anapencere,D�SMANSAYISI,pipeTail)).start();		
		
		return;

	}

}
