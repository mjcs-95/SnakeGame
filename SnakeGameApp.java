import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class SnakeGameApp extends JFrame {
	private JPanel contentPane;
	private JPanel panel = new JPanel();
	private JButton btnStartButton = new JButton("New game");
	private SnakeGameRules game;
	private Thread t;
	private InputMap input_map;
	private ActionMap action_map;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SnakeGameApp frame = new SnakeGameApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SnakeGameApp() {
		/*GUI*/
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 530, 655);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/*KEY BINDING*/
		input_map = contentPane.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);;
		input_map.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,    0), "UP"   );
		input_map.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT");
		input_map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,  0), "DOWN" );
		input_map.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,  0), "LEFT" );

		action_map = contentPane.getActionMap();
		action_map.put("UP"   , new SnakeKeyDirection("UP")   );
		action_map.put("RIGHT", new SnakeKeyDirection("RIGHT"));
		action_map.put("DOWN" , new SnakeKeyDirection("DOWN") );		
		action_map.put("LEFT" , new SnakeKeyDirection("LEFT") );
		/*END KEY BINDING*/
		
		panel.setBounds(0, 100, 512, 512);
		contentPane.add(panel);
		
		btnStartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t=new Thread( new Runnable(){ public void run(){ play(); } } );
				t.start();				
			}
		});
		btnStartButton.setBounds(0, 0, 512, 100);
		contentPane.add(btnStartButton);	
	}
	
	public void paintBoard(){
		Graphics g = panel.getGraphics();
		int[][] board = game.getBoard();
		int h=board.length;
		int w=board[0].length;
		BufferedImage im = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		for(int i=0;i < h; ++i){
			for(int j=0;j < w; ++j){
				switch( board[i][j] ){
					case 0: im.setRGB(j,i,Color.BLACK.getRGB()); break;
					case 1: im.setRGB(j,i,Color.WHITE.getRGB()); break;
					case 2: im.setRGB(j,i,Color.RED.getRGB());   break;
				}
			}
		}
		g.drawImage(im, 0, 0, panel.getHeight(), panel.getWidth(), panel);
	}
	
	public void play() {
		game = new SnakeGameRules();
		paintBoard();
		while(!game.defeat()) {
			game.snakemoves();
			paintBoard();
			try{ Thread.sleep(100); }catch(InterruptedException e1){}			
		}
	}
	
	public class SnakeKeyDirection extends AbstractAction {
		private String cmd;
	    
		public SnakeKeyDirection(String cmd){ this.cmd = cmd; }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	        if( cmd.equals("UP")   ){ game.setDirection(0); }
	        if( cmd.equals("RIGHT")){ game.setDirection(1); }
	        if( cmd.equals("DOWN") ){ game.setDirection(2); }
	        if( cmd.equals("LEFT") ){ game.setDirection(3); }
	    }
	}
	
}
