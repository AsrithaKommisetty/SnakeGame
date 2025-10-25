import javax.swing.JFrame;
public class GameFrame extends JFrame{
	GameFrame() {
		this.add(new GamePanel());
		this.setTitle("Snake Game By Asritha");
		this.pack();//uses preferred size(600,600)
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocationRelativeTo(null);//keeps the details in center
		this.setResizable(false);
	}
	
	
}
