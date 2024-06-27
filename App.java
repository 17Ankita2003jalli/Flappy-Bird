import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
       int boardwidth=360;
       int boardHeight=640;

       JFrame f=new JFrame("Flappy Bird");
       f.setSize(boardwidth, boardHeight);
       f.setLocationRelativeTo(null);
       f.setResizable(false);
       f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       FlappyBird flappyBird=new FlappyBird();
       f.add(flappyBird);
       f.pack();
       flappyBird.requestFocus();
       f.setVisible(true);
    }
}
