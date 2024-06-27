import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener,KeyListener{
    int boardwidth=360;
    int boardHeight=640;

    // Images
    Image backgroundImg;
    Image birdImg;
    Image TpipeImg;
    Image BpipeImg;

    // bird
    int birdX=boardwidth/8;
    int birdy=boardHeight/2;
    int birdWidth=30;
    int birdHeight=24;

    // pipes
    int pipeX=boardwidth;
    int pipeY=0;
    int pipeWidth=64;
    int pipeHeight=512;

    class pipe{
        int x=pipeX;
        int y=pipeY;
        int width=pipeWidth;
        int height=pipeHeight;
        Image img;
        boolean passed=false;

        pipe(Image img){
            this.img=img;
        }
    }

    class Bird{
        int x=birdX;
        int y=birdy;
        int width=birdWidth;
        int height=birdHeight;
        Image Bimg;

        Bird(Image Bimg){
            this.Bimg=Bimg;
        }
    }

    // game logic
    Bird bird;
    int velocityX=-4;//move the pipes to the left speed(simulta\neously birds towords right)
    int velocityY=0; //movethe bird up-downspeed
    int gravity=1;
    ArrayList<pipe> pipes;
    Random random=new Random();

    Timer gameloop;
    Timer placePipesTimer;
    boolean gameOver=false;
    double Score=0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardwidth, boardHeight));
        setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        // load images
        backgroundImg = new ImageIcon(getClass().getResource("flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("flappybird.png")).getImage();
        TpipeImg = new ImageIcon(getClass().getResource("toppipe.png")).getImage();
        BpipeImg = new ImageIcon(getClass().getResource("bottompipe.png")).getImage();

        // bird
        bird= new Bird(birdImg);

        pipes=new ArrayList<pipe>();
        // place pipes timer
        placePipesTimer = new Timer(2000, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start(); 

        // game timer
        gameloop=new Timer(1000/60,this);
        gameloop.start();
    }

    public void placePipes(){
        int randomPipes=(int)(pipeY-pipeHeight/4-Math.random()*(pipeHeight/2));
        int openingSpace=boardHeight/4;

        pipe topPipe= new pipe(TpipeImg);
        topPipe.y=randomPipes;
        pipes.add(topPipe);

        pipe bottompipe=new pipe(BpipeImg);
        bottompipe.y=topPipe.y+pipeHeight+ openingSpace;
        pipes.add(bottompipe);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        // background
        g.drawImage(backgroundImg, 0, 0,boardwidth,boardHeight,null);
        // bird
        g.drawImage(bird.Bimg, bird.x, bird.y, bird.width, bird.height, null);

        // pipes
        for(int i=0;i<pipes.size();i++){
            pipe p=pipes.get(i);
            g.drawImage(p.img, p.x, p.y, p.width, p.height, null);
        }

        // score
        g.setColor(Color.black);   
        g.setFont(new Font("Arial", Font.BOLD, 26));
        if(gameOver){
            g.drawString("Game Over!",88,280);
            g.drawString("Your score is: "+ String.valueOf((int)Score),80,315);
        }
        else{
            g.drawString(String.valueOf((int)Score),10, 35); 
        }
        
    }
    public void move(){
        // bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y=Math.max(bird.y,0);

        // pipes
        for(int i=0;i<pipes.size();i++){
            pipe pipe=pipes.get(i);
            pipe.x+= velocityX;

            if(!pipe.passed&& bird.x>pipe.x+pipe.width){
                pipe.passed=true;
                Score+=0.5;
            }

            if(collision(bird, pipe)){
                gameOver=true;
            }
        }


        if(bird.y>boardHeight){
            gameOver=true;
        }
    }

    public boolean collision(Bird a,pipe b){
        return a.x<b.x+b.width &&
        a.x+a.width>b.x &&
        a.y<b.y+b.height &&
        a.y+a.height>b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
      repaint();
      if(gameOver){
        placePipesTimer.stop();
        gameloop.stop();
      }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_SPACE) {
            velocityY=-9;
            if(gameOver){
                // reset the game by reseting the values
                bird.y=birdy;
                velocityY=0;
                pipes.clear();
                Score=0;
                gameOver=false;
                gameloop.start();
                placePipesTimer.start();
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
