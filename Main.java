/**
 * @authors:Danish Bangash
 * date:20.10.2011
 */
 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import nb.connect4.api.GameBoard;
import nb.connect4.player.ai.simplebloor.SimpleBloor;

public class Main extends JFrame {
	//The size of game board slots in pixels, used for drawing
	//and determining which slot was clicked.
	private static final int SlotSize = 32;

	//The dimensions of the window client area
	private static final int WindowWidth = GameBoard.BoardWidth * SlotSize;
	private static final int WindowHeight = (GameBoard.BoardHeight * SlotSize) + 86;

	//The window insets, used in drawing and setting the window size
	private Insets insets;
	
	//An image used as a back buffer for smooth drawing
	private Image backBuffer;

	//Controls used on the window
    private ButtonGroup radioGroup;
    private JRadioButton humanFirst;
    private JRadioButton aiFirst;
    private JButton startButton;
    private JLabel messageArea;

	//Game objects/vars
	private GameBoard board;
	private SimpleBloor ai;
	private boolean gameEnded;
	private int humanPlayer;
	private int aiPlayer;
	private int result;
	
    /**
	 * Create the application window
     */
    public static void main(String[] args) {
		new Main();
    }

	/**
	 * Initialise all data, setup the window, and create event handlers.
	 */
	public Main() {
		//Initialise game data
		board = new GameBoard();
		humanPlayer = 1; //Human goes first by default
		aiPlayer = 2;
		ai = new SimpleBloor(aiPlayer, humanPlayer, GameBoard.BoardWidth, GameBoard.BoardHeight);
		gameEnded = true;
		result = -1;
		
		//Initialise the window
		setTitle("SimpleBloor Connect 4 AI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		//Set the window size - need a client area of 224x278 so the
		//insets are added to the desired size
		setVisible(true);
		insets = getInsets();
		setSize(WindowWidth + insets.left + insets.right, WindowHeight + insets.top + insets.bottom);

		//Create the back buffer image
		backBuffer = this.createImage(WindowWidth, GameBoard.BoardHeight * SlotSize);

		//Create a label for game messages
		messageArea = new JLabel("Select player and start the game.");
		messageArea.setBounds(0, (GameBoard.BoardHeight * SlotSize) + 2, WindowWidth, 14);
		add(messageArea);

		//Create check boxes to choose whether to play first or second
        radioGroup = new ButtonGroup();
		humanFirst = new JRadioButton("You play first");
		humanFirst.setBounds(SlotSize, (GameBoard.BoardHeight * SlotSize) + 30, 98, 12);
		humanFirst.setSelected(true);
		aiFirst = new JRadioButton("You play second");
		aiFirst.setBounds(SlotSize, (GameBoard.BoardHeight * SlotSize) + 30 + SlotSize, 117, 12);
		radioGroup.add(humanFirst);
		radioGroup.add(aiFirst);
        add(humanFirst);
        add(aiFirst);

		//Create the start game button
		startButton = new JButton("Start");
		startButton.setBounds(150, (GameBoard.BoardHeight * SlotSize) + 20, 70, 24);
		add(startButton);

		//Add a click handler to the start game button
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//Reset the game
				board.reset();
				repaint();

				//Set player numbers based on radio buttons
				if(aiFirst.isSelected()) {
					aiPlayer = 1; humanPlayer = 2;
					ai.setPlayers(aiPlayer, humanPlayer);
					
					//Play the AI's first move
					int move = ai.playMove(board);
					board.playMove(move, aiPlayer);
					messageArea.setText("I played at " + (move + 1) + ", your move.");
					repaint();
				} else {
					aiPlayer = 2; humanPlayer = 1;
					ai.setPlayers(aiPlayer, humanPlayer);
					messageArea.setText("Your move.");
				}

				//Disable controls
				gameEnded = false;
				humanFirst.setEnabled(false);
				aiFirst.setEnabled(false);
				startButton.setEnabled(false);
			}
		});

		//Add a mouse listener for the human player
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				//Make sure the game hasn't ended and the board was clicked
				if(gameEnded == true) { return; }
				if(me.getY() >= (GameBoard.BoardHeight * SlotSize) + insets.top) { return; }

				//Determine which column was clicked and make sure there's free space
				int column = (me.getX() - (me.getX() % SlotSize)) / SlotSize;
				if(board.isLegalMove(column) == false) {
					messageArea.setText("Column " + (column + 1) + " is full.");
				}

				//Play the move
				board.playMove(column, humanPlayer); repaint();

				//If the game hasn't ended, play an AI move
				result = board.gameOver();
				if(result == -1) {
					//Play an AI move
					int move = ai.playMove(board);
					board.playMove(move, aiPlayer);
					messageArea.setText("I played at " + move + ", your move.");
					repaint();
				}

				//Check if the game has ended
				result = board.gameOver();
				if(result != -1) {
					//Game has ended
					gameEnded = true;
					
					//Check for a draw result
					if(result == 0) {
						messageArea.setText("The game was a draw!");
						JOptionPane.showMessageDialog(null, "Draw game!");
					}

					//Check if the human won
					if(result == humanPlayer) {
						messageArea.setText("You won the game!");
						JOptionPane.showMessageDialog(null, "You won the game!");
					}

					//Check if the AI won
					if(result == aiPlayer) {
						messageArea.setText("The AI won with a 1-move lookahead!");
						JOptionPane.showMessageDialog(null, "You lost the game!");
					}

					//Re-enable the controls
					humanFirst.setEnabled(true);
					aiFirst.setEnabled(true);
					startButton.setEnabled(true);
				}
			}
		});
	}

	/**
	 * Override the JFrame paint method to draw the Connect 4
	 * game board.
	 */
	@Override
	public void paint(Graphics g)
	{
		//Paint the super class first
		super.paint(g);

		//Get the raw game board data
		ArrayList<ArrayList<Integer> > boardData = board.getBoard();
		int piece = 0;
		
		//Get and clear the graphics object from the back buffer
		Graphics backG;
		backG = backBuffer.getGraphics();
		backG.setColor(Color.WHITE);
		backG.fillRect(0, 0, backBuffer.getWidth(null), backBuffer.getHeight(null));

		//Draw the game board
        for(int i = GameBoard.BoardHeight - 1; i >= 0; i--)
        {
            for(int j = 0; j < GameBoard.BoardWidth; j++)
            {
                //Draw a rectangle
                backG.setColor(Color.BLACK);
                backG.fillRect(j * SlotSize, (GameBoard.BoardHeight - i - 1) * SlotSize, SlotSize, SlotSize);
                backG.setColor(Color.LIGHT_GRAY);
                backG.fillRect((j * SlotSize) + 1, ((GameBoard.BoardHeight - i - 1) * SlotSize) + 1, SlotSize - 2, SlotSize - 2);

                //Draw a piece of it exists here
                piece = boardData.get(i).get(j);
                if(piece == 1)
                {
                    backG.setColor(Color.RED);
                    backG.fillOval((j * SlotSize), ((GameBoard.BoardHeight - i - 1) * SlotSize), SlotSize - 1, SlotSize - 1);
                }
                if(piece == 2)
                {
                    backG.setColor(Color.YELLOW);
                    backG.fillOval((j * SlotSize), ((GameBoard.BoardHeight - i - 1) * SlotSize), SlotSize - 1, SlotSize - 1);
                }
            }
        }

		//Copy the back buffer to the window
		g.drawImage(backBuffer, insets.left, insets.top, null);
		
		//Draw the pieces next to the radio buttons directly on to the window
		//so as not to draw over the buttons and label
        g.setColor(Color.RED);
        g.fillOval(0, (GameBoard.BoardHeight * SlotSize) + 20 + insets.top, SlotSize, SlotSize);
        g.setColor(Color.YELLOW);
        g.fillOval(0, (GameBoard.BoardHeight * SlotSize) + 20 + SlotSize + insets.top, SlotSize, SlotSize);
	}
}
