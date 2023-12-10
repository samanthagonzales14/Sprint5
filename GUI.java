package sprint5_0.project;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import sprint5_0.project.Board.Cell;
import sprint5_0.project.Board.GameState;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import java.awt.Component;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JCheckBox;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	protected Board board; // Field to store an instance of Board
	private SimpleGame simpleGame; // Field to store an instance of SimpleGame
    private GeneralGame generalGame; // Field to store an instance of GeneralGame
    private ComputerPlayer computerBlue; // Field to store an instance of a computer player
    private ComputerPlayer computerRed; // Field to store an instance of a computer player
	protected int boardSize; // Variable to store boardSize and its changes
	public static final int SYMBOL_STROKE_WIDTH = 8; 
	public static final int CANVAS_SIZE = 300;
	public int cellSize;
	public int cellPadding;
	public int symbolSize;
	public char currentPlayerTurn;
	Cell currentPlayerSymbol;
		
	protected GameBoardCanvas gameBoardCanvas; // Field to store an instance of the grid
	protected JTextField txtCurrentTurn;
	
	protected JRadioButton bluePlayerSButton;
	protected JRadioButton bluePlayerOButton;
	protected ButtonGroup bluePlayerLetterSelection;// For Blue Player's letter radio buttons
	protected ButtonGroup bluePlayerSelection = new ButtonGroup(); // For Blue Player's radio buttons
	
	protected JRadioButton redPlayerSButton;
	protected JRadioButton redPlayerOButton;
	protected ButtonGroup redPlayerLetterSelection; // For Red Player's letter radio buttons
    protected ButtonGroup redPlayerSelection = new ButtonGroup();  // For Red Player's radio buttons
    protected ButtonGroup gameModeSelection; // For game mode radio buttons
    protected JSpinner boardSizeSpinner; // To adjust boardSize
    private JTextField ScoreDisplay;	// To display score
    
    public GUI() {
    	setResizable(false);
    	setVisible(true);
    	setSize(341, 337); 
		getContentPane().setBackground(new Color(255, 255, 255));
		setBackground(new Color(255, 255, 255));		
		generalGame = new GeneralGame();
        simpleGame = new SimpleGame();
        board = simpleGame;
        computerBlue = new ComputerPlayer(board, 'B');
        computerRed = new ComputerPlayer(board, 'R');
		setContentPane();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack(); 
		setTitle("SOS Game");
		
		// Delete file that has recorded game moves upon exit of application
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String filePath = "game_moves.txt"; 
            try {
                Files.deleteIfExists(Paths.get(filePath));
                System.out.println("File deleted successfully on application shutdown.");
            } catch (IOException e) {
                System.out.println("Error deleting file on application shutdown: " + e.getMessage());
            }
        }));
		
	}
	
	public Board getBoard(){
		return board;
	}
	
	protected void setContentPane(){	
		// Set a fixed size for the gameBoardCanvas
        gameBoardCanvas = new GameBoardCanvas();
        gameBoardCanvas.setPreferredSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
        gameBoardCanvas.setVisible(true);
        Border gridBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        gameBoardCanvas.setBorder(gridBorder);
        
		
		// panels that surround the board
        JPanel northPanel = new JPanel();
        northPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        northPanel.setBackground(new Color(255, 255, 255));
        JPanel southPanel = new JPanel();
        southPanel.setBackground(new Color(255, 255, 255));
        JPanel westPanel = new JPanel();
        westPanel.setBackground(new Color(255, 255, 255));
        JPanel eastPanel = new JPanel();
        eastPanel.setBackground(new Color(255, 255, 255));
        
    
        JLabel spaceLabel = new JLabel("       ");
    
        // Simple or general game mode option
        JLabel gameModeLabel = new JLabel("SOS ");
        gameModeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JRadioButton simpleGameButton = new JRadioButton("Simple", true);
        simpleGameButton.setHorizontalAlignment(SwingConstants.CENTER);
        simpleGameButton.setBackground(new Color(255, 255, 255));
        JRadioButton generalGameButton = new JRadioButton("General");
        generalGameButton.setHorizontalAlignment(SwingConstants.CENTER);
        generalGameButton.setBackground(new Color(255, 255, 255));
        gameModeSelection = new ButtonGroup();
        gameModeSelection.add(simpleGameButton);
        gameModeSelection.add(generalGameButton);
        
        simpleGameButton.addActionListener(e -> {	
            board = simpleGame;					// Sets game mode to Simple Game 
            generalGameButton.setEnabled(false); // To prevent game mode switches in mid game
        });

        generalGameButton.addActionListener(e -> {
            board = generalGame;				// Sets game mode to General Game 
            simpleGameButton.setEnabled(false);	// To prevent game mode switches in mid game
        });
            
        // Board size label
        JLabel boardSizeLabel = new JLabel("Board Size");
        boardSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        boardSizeLabel.setBackground(new Color(255, 255, 255));  
    
        //  Button options for blue player
        JLabel bluePlayerLabel = new JLabel("Blue Player");
        bluePlayerSButton = new JRadioButton("S", true);
        bluePlayerSButton.setActionCommand("S");
        bluePlayerSButton.setBackground(new Color(255, 255, 255));
        bluePlayerOButton = new JRadioButton("O");
        bluePlayerOButton.setActionCommand("O");
        bluePlayerOButton.setBackground(new Color(255, 255, 255));
        JRadioButton blueHumanButton = new JRadioButton("Human", true);
        blueHumanButton.setBackground(new Color(255, 255, 255));
        JRadioButton blueComputerButton = new JRadioButton("Computer");
        blueComputerButton.setBackground(new Color(255, 255, 255));
        bluePlayerLetterSelection = new ButtonGroup();
        bluePlayerLetterSelection.add(bluePlayerSButton);
        bluePlayerLetterSelection.add(bluePlayerOButton);
        bluePlayerSelection.add(blueHumanButton);
        bluePlayerSelection.add(blueComputerButton);
        
        blueComputerButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) 
            /* If Blue Player is a computer player,
             * then make automatic moves each time computer button is selected 
             * update canvas after each move 
             */
            { 
            	computerBlue = new ComputerPlayer(board, 'B');
                bluePlayerLetterSelection.clearSelection();
                bluePlayerSButton.setEnabled(false);
                bluePlayerOButton.setEnabled(false);
                blueHumanButton.setEnabled(false);
	            if (board.getCurrentPlayer() == 'B') {
	                // Make the computer's move
	                computerBlue.makeAutoMove();
	                gameBoardCanvas.repaint();
	                handleGameResult();
	            }
            }
        });
        
        // Button options for red player
        JLabel redPlayerLabel = new JLabel("Red Player");
        redPlayerSButton = new JRadioButton("S", true);
        redPlayerSButton.setActionCommand("S");
        redPlayerSButton.setBackground(new Color(255, 255, 255));
        redPlayerOButton = new JRadioButton("O");   
        redPlayerOButton.setActionCommand("O");
        redPlayerOButton.setBackground(new Color(255, 255, 255));
        JRadioButton redHumanButton = new JRadioButton("Human", true);
        redHumanButton.setBackground(new Color(255, 255, 255));
        JRadioButton redComputerButton = new JRadioButton("Computer");
        redComputerButton.setBackground(new Color(255, 255, 255));
        redPlayerLetterSelection = new ButtonGroup();
        redPlayerLetterSelection.add(redPlayerSButton);
        redPlayerLetterSelection.add(redPlayerOButton);
        redPlayerSelection = new ButtonGroup();
        redPlayerSelection.add(redHumanButton);
        redPlayerSelection.add(redComputerButton);
        
        redComputerButton.addActionListener(e -> {
        	/* If Red Player is a computer player,
             * then make automatic moves each time computer button is selected 
             * update canvas after each move 
             */
            computerRed = new ComputerPlayer(board, 'R');
            redPlayerLetterSelection.clearSelection();
            redPlayerSButton.setEnabled(false);
            redPlayerOButton.setEnabled(false);
            redHumanButton.setEnabled(false);
            if (board.getCurrentPlayer() == 'R') {
                // Make the computer's move
                computerRed.makeAutoMove();
                gameBoardCanvas.repaint();
                handleGameResult();
            }
        });
        
        JCheckBox recordCheckBox = new JCheckBox("Record Game");
        recordCheckBox.setBorder(null);
        recordCheckBox.setHorizontalAlignment(SwingConstants.LEFT);
        recordCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
        recordCheckBox.setBackground(new Color(255, 255, 255));
        recordCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                board.startRecording();
            } else {
                board.stopRecording();
            }
        });
        
    
        // Add to south panel
        txtCurrentTurn = new JTextField();
        txtCurrentTurn.setPreferredSize(new Dimension(35, 19));
        txtCurrentTurn.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtCurrentTurn.setBorder(null);
        txtCurrentTurn.setBackground(new Color(255, 255, 255));
        txtCurrentTurn.setColumns(10);
    
        // Add panels to ContentPane
        Container ContentPane = getContentPane();
        BorderLayout borderLayout = new BorderLayout();
        ContentPane.setLayout(borderLayout);
        ContentPane.add(northPanel, BorderLayout.NORTH);
        ContentPane.add(southPanel, BorderLayout.SOUTH);
        ContentPane.add(westPanel, BorderLayout.WEST);
        ContentPane.add(eastPanel, BorderLayout.EAST);
        ContentPane.add(gameBoardCanvas, BorderLayout.CENTER);
        northPanel.setPreferredSize(new Dimension(300, 100));
        
        // board size options
        boardSizeSpinner = new JSpinner();
        boardSizeSpinner.setModel(new SpinnerNumberModel(3, 3, 10, 1));
        
        boardSizeSpinner.addChangeListener(e -> {
            int newSize = (int) boardSizeSpinner.getValue();
            board.setBoardSize(newSize); // Update the board size
            board.newGame();			// Update game to reflect new size
            gameBoardCanvas.repaint(); // Repaint the canvas
        });
        
        southPanel.setPreferredSize(new Dimension(300, 100));
        
        // Button to have computer versus computer play automatically
        JRadioButton CompVsCompButton = new JRadioButton("Computer Vs Computer");
        CompVsCompButton.setBackground(new Color(255, 255, 255));
        CompVsCompButton.addActionListener((ActionListener) new ActionListener() {
        	@Override
        	public void actionPerformed (ActionEvent e) {
        		bluePlayerLetterSelection.clearSelection();
                bluePlayerSButton.setEnabled(false);
                bluePlayerOButton.setEnabled(false);
                blueHumanButton.setEnabled(false);
                blueComputerButton.setEnabled(false);
                redPlayerLetterSelection.clearSelection();
        		redPlayerSButton.setEnabled(false);
                redPlayerOButton.setEnabled(false);
                redHumanButton.setEnabled(false);
                redComputerButton.setEnabled(false);
                System.out.println(board.getGameMode());
                System.out.println(board.getBoardSize());
                playComputerVsComputerGame(board);
        		gameBoardCanvas.repaint();
        	}
        });
        
        // Score label and textField to display player scores
        JLabel lblNewJgoodiesLabel = DefaultComponentFactory.getInstance().createLabel("Score: ");
        
        ScoreDisplay = new JTextField();
        ScoreDisplay.setScrollOffset(1);
        ScoreDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        ScoreDisplay.setBorder(null);
        ScoreDisplay.setPreferredSize(new Dimension(80, 30));
        ScoreDisplay.setColumns(15);
        GroupLayout gl_northPanel = new GroupLayout(northPanel);
        gl_northPanel.setHorizontalGroup(
        	gl_northPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_northPanel.createSequentialGroup()
        			.addGroup(gl_northPanel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_northPanel.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(CompVsCompButton))
        				.addGroup(gl_northPanel.createSequentialGroup()
        					.addGap(53)
        					.addGroup(gl_northPanel.createParallelGroup(Alignment.TRAILING)
        						.addGroup(gl_northPanel.createSequentialGroup()
        							.addComponent(gameModeLabel)
        							.addGap(6)
        							.addComponent(simpleGameButton)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(generalGameButton)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(spaceLabel)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(boardSizeLabel)
        							.addGap(4))
        						.addGroup(gl_northPanel.createSequentialGroup()
        							.addComponent(lblNewJgoodiesLabel)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(ScoreDisplay, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)))
        					.addGap(6)
        					.addComponent(boardSizeSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gl_northPanel.setVerticalGroup(
        	gl_northPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_northPanel.createSequentialGroup()
        			.addGap(9)
        			.addGroup(gl_northPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(boardSizeLabel)
        				.addComponent(boardSizeSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(spaceLabel)
        				.addComponent(generalGameButton)
        				.addComponent(simpleGameButton)
        				.addComponent(gameModeLabel))
        			.addGap(6)
        			.addComponent(CompVsCompButton)
        			.addGap(4)
        			.addGroup(gl_northPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblNewJgoodiesLabel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
        				.addComponent(ScoreDisplay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        northPanel.setLayout(gl_northPanel);
        
        // Button to reset the board and start new game
        JButton newGameButton = new JButton("New Game");
        southPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 36, 5));
        southPanel.add(recordCheckBox);
        southPanel.add(txtCurrentTurn);
        
        JButton replayButton = new JButton("Replay");
        replayButton.setHorizontalTextPosition(SwingConstants.CENTER);
        replayButton.setToolTipText("");
        replayButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// Replay game from text file, if error caught display pop-up error window
        		try {
        			replayGame(board, "game_moves.txt");
        		} catch (IOException ex) {
        			System.out.println("Exception caught!");
        			ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: File Not Found", "IO Error", JOptionPane.ERROR_MESSAGE);
        		}
            }
        });
        replayButton.setMinimumSize(new Dimension(90, 28));
        replayButton.setMaximumSize(new Dimension(90, 28));
        replayButton.setPreferredSize(new Dimension(94, 27));
        southPanel.add(replayButton);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setBorder(null);
        lblNewLabel.setBackground(new Color(255, 255, 255));
        lblNewLabel.setPreferredSize(new Dimension(285, 20));
        lblNewLabel.setMinimumSize(new Dimension(60, 20));
        lblNewLabel.setMaximumSize(new Dimension(60, 20));
        southPanel.add(lblNewLabel);
        southPanel.add(newGameButton);
        
        
        newGameButton.addActionListener((ActionListener) new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		// Reset the board and initialize a new one
                board.newGame();

                // Reset the board size spinner to its default value (3)
                boardSizeSpinner.setValue(3);
                // Reset Computer and Human Player Selections
                bluePlayerSButton.setEnabled(true);
                bluePlayerOButton.setEnabled(true);
                blueHumanButton.setEnabled(true);
                blueComputerButton.setEnabled(true);
                redPlayerSButton.setEnabled(true);
                redPlayerOButton.setEnabled(true);
                redHumanButton.setEnabled(true);
                redComputerButton.setEnabled(true);
                // Reset game mode Selection
                generalGameButton.setEnabled(true);
                simpleGameButton.setEnabled(true);
                // Clear the radio button selections
                CompVsCompButton.setSelected(false);
                bluePlayerSButton.setSelected(true);
                redPlayerSButton.setSelected(true);
                simpleGameButton.setSelected(true);
                board = new SimpleGame();
                blueHumanButton.setSelected(true);
                redHumanButton.setSelected(true);
                recordCheckBox.setSelected(false);

                // Clear the text field for the current turn
                txtCurrentTurn.setText("");
                // Clear the text field for the current turn
                ScoreDisplay.setText("");
                
                // Repaint the canvas to clear the board
                gameBoardCanvas.repaint();
               
        	}
        });
        
        westPanel.setPreferredSize(new Dimension(150, 150));
        GroupLayout gl_westPanel = new GroupLayout(westPanel);
        gl_westPanel.setHorizontalGroup(
        	gl_westPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_westPanel.createSequentialGroup()
        			.addGroup(gl_westPanel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_westPanel.createSequentialGroup()
        					.addGap(18)
        					.addComponent(bluePlayerLabel))
        				.addGroup(gl_westPanel.createSequentialGroup()
        					.addGap(37)
        					.addGroup(gl_westPanel.createParallelGroup(Alignment.LEADING)
        						.addComponent(blueHumanButton)
        						.addGroup(gl_westPanel.createParallelGroup(Alignment.TRAILING)
        							.addComponent(blueComputerButton)
        							.addGroup(gl_westPanel.createSequentialGroup()
        								.addComponent(bluePlayerSButton)
        								.addPreferredGap(ComponentPlacement.UNRELATED)
        								.addComponent(bluePlayerOButton))))))
        			.addGap(40))
        );
        gl_westPanel.setVerticalGroup(
        	gl_westPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_westPanel.createSequentialGroup()
        			.addGap(9)
        			.addComponent(bluePlayerLabel)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(blueHumanButton)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(gl_westPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(bluePlayerSButton)
        				.addComponent(bluePlayerOButton))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(blueComputerButton)
        			.addGap(55))
        );
        westPanel.setLayout(gl_westPanel);
        eastPanel.setPreferredSize(new Dimension(150, 150));		
        GroupLayout gl_eastPanel = new GroupLayout(eastPanel);
        gl_eastPanel.setHorizontalGroup(
        	gl_eastPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_eastPanel.createSequentialGroup()
        			.addGroup(gl_eastPanel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_eastPanel.createSequentialGroup()
        					.addGap(19)
        					.addComponent(redPlayerLabel))
        				.addGroup(gl_eastPanel.createSequentialGroup()
        					.addGap(28)
        					.addGroup(gl_eastPanel.createParallelGroup(Alignment.LEADING)
        						.addComponent(redHumanButton)
        						.addGroup(gl_eastPanel.createParallelGroup(Alignment.TRAILING)
        							.addGroup(gl_eastPanel.createSequentialGroup()
        								.addComponent(redPlayerSButton)
        								.addPreferredGap(ComponentPlacement.RELATED)
        								.addComponent(redPlayerOButton))
        							.addComponent(redComputerButton, Alignment.LEADING)))))
        			.addContainerGap(51, Short.MAX_VALUE))
        );
        gl_eastPanel.setVerticalGroup(
        	gl_eastPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_eastPanel.createSequentialGroup()
        			.addGap(9)
        			.addComponent(redPlayerLabel)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(redHumanButton)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_eastPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(redPlayerOButton)
        				.addComponent(redPlayerSButton))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(redComputerButton)
        			.addContainerGap(57, Short.MAX_VALUE))
        );
        eastPanel.setLayout(gl_eastPanel);
	}

	class GameBoardCanvas extends JPanel {			
		GameBoardCanvas(){	 
			
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {  
					if (board.getGameState() == GameState.PLAYING) {				
						int rowSelected = e.getY() / cellSize;
						int colSelected = e.getX() / cellSize;
				        
				        currentPlayerTurn = board.getCurrentPlayer();
				        
				        if (board.getCell(rowSelected, colSelected) == Cell.EMPTY) {
				        ButtonModel blueSelection = bluePlayerLetterSelection.getSelection();
				        ButtonModel redSelection = redPlayerLetterSelection.getSelection();
				        
				        if (currentPlayerTurn == 'B') {
			                
			                    if (blueSelection != null && 
			                    	blueSelection.getActionCommand() != null && 
			                    	blueSelection.getActionCommand().equals("S")) {
			                        currentPlayerSymbol = Cell.LETTERS; // Blue Player selected 'S'
			                    } else if (blueSelection != null && 
			                    		   blueSelection.getActionCommand() != null && 
			                    		   blueSelection.getActionCommand().equals("O")) {
			                        currentPlayerSymbol = Cell.LETTERO; // Blue Player selected 'O'
			                    }
			                    board.makeMove(rowSelected, colSelected, currentPlayerSymbol);
			                    
			            } else {
			                    if (redSelection != null && 
			                    	redSelection.getActionCommand() != null && 
			                    	redSelection.getActionCommand().equals("S")) {
			                        currentPlayerSymbol = Cell.LETTERS; // Red Player selected 'S'
			                    } else if (redSelection != null && 
			                    		   redSelection.getActionCommand() != null && 
			                    		   redSelection.getActionCommand().equals("O")) {
			                        currentPlayerSymbol = Cell.LETTERO; // Red Player selected 'O'
			                    }
			                    board.makeMove(rowSelected, colSelected, currentPlayerSymbol);
			            }
				        gameBoardCanvas.repaint();
				        handleGameResult();
				        }
					}
				}
			});										
		}
		
		@Override
		public void paintComponent(Graphics g) { 
			super.paintComponent(g);   
			setBackground(Color.WHITE);
			// Calculate cellSize based on canvas size and board size
		    int canvasSize = Math.min(getWidth(), getHeight());
		    cellSize = canvasSize / board.getBoardSize();

		    // Calculate cellPadding based on cellSize
		    cellPadding = cellSize / 20;

		    // Calculate symbolSize based on cellSize
		    symbolSize = cellSize - cellPadding * 2;
		    
			drawGridLines(g);
			drawBoard(g);
			drawSOSLines(g);
		}
		
		private void drawGridLines(Graphics g) {
            g.setColor(Color.BLACK);
            int boardSize = board.getBoardSize();
            int gridSize = CANVAS_SIZE / boardSize;

            for (int row = 1; row < boardSize; row++) {
                int y = row * gridSize;
                g.drawLine(0, y, CANVAS_SIZE, y);
            }

            for (int col = 1; col < boardSize; col++) {
                int x = col * gridSize;
                g.drawLine(x, 0, x, CANVAS_SIZE);
            }
        }

		private void drawBoard(Graphics g){
			Graphics2D g2d = (Graphics2D)g;		    
		    g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); 
		    
			for (int row = 0; row < board.getBoardSize(); row++) {
				for (int col = 0; col < board.getBoardSize(); col++) {
					int x1 = col * cellSize + cellPadding;
					int y1 = row * cellSize + cellPadding;
					Cell cellValue = board.getCell(row, col);
					if (cellValue == Cell.LETTERS) {
						drawSymbol(g2d, 'S', x1,y1);
					}
					else if (cellValue == Cell.LETTERO) {
						drawSymbol(g2d, 'O', x1,y1);
					}
				}	
			}
		}
	}
	
	private void drawSOSLines(Graphics g) {
	    List<SOSPattern> formedSOSPatterns = board.getFormedSOSPatterns();
	    Map<SOSPattern, Character> sosPatternPlayers = board.getSOSPatternPlayers();

	    for (SOSPattern pattern : formedSOSPatterns) {
	        char player = sosPatternPlayers.get(pattern);

	        // Set the color based on the player
	        if (player == 'B') {
	            g.setColor(Color.BLUE);
	        } else {
	            g.setColor(Color.RED);
	        }

	        // Draw a line over the formed SOS
	        drawLine(g, pattern);
	    }
	}
	 private void drawLine(Graphics g, SOSPattern pattern) {
	        
	        int startRow = pattern.getRow()* cellSize + cellSize/2;
	        int startCol = pattern.getCol()* cellSize + cellSize/2;
	        String direction = pattern.getDirection();

	        int lineLength = (int) (cellSize * 2.3);
	        ((Graphics2D) g).setStroke(new BasicStroke(5));

	        // Draw a line based on the type of the SOS pattern
	        switch (direction) {
	        case "row":
	            g.drawLine(startCol, startRow, startCol + lineLength, startRow);
	            break;
	        case "column":
	            g.drawLine(startCol, startRow, startCol, startRow + lineLength);
	            break;
	        case "diagTlBr":
	            g.drawLine(startCol, startRow, startCol + lineLength, startRow + lineLength);
	            break;
	        case "diagTrBl":
	            g.drawLine(startCol, startRow, startCol - lineLength, startRow + lineLength);
	            break;
	        }
	 }

	
	private void drawSymbol(Graphics2D g2d, char symbol, int x, int y) {
	    String letter = String.valueOf(symbol);
	    Font font;

	    // Check the cell size, and adjust the font size accordingly
	    if (cellSize > 30) {
	        font = new Font("Arial", Font.BOLD, symbolSize);
	    } else {
	        font = new Font("Arial", Font.BOLD, 24); 
	    }

	    // Calculate the center position for the text within the cell
	    int centerX = x + (cellSize - g2d.getFontMetrics(font).stringWidth(letter)) / 2;
	    int centerY = y + (cellSize + g2d.getFontMetrics(font).getHeight()) / 2 - g2d.getFontMetrics(font).getDescent();

	    g2d.setFont(font);
	    g2d.setColor(Color.BLACK);
	    g2d.drawString(letter, centerX, centerY);
	}
	
	private void playComputerVsComputerGame(Board board) {
	    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() { // create new swing worker
	        @Override
	        protected Void doInBackground() throws Exception {
	        	computerBlue = new ComputerPlayer(board, 'B');
	        	computerRed = new ComputerPlayer(board, 'R');
	            while (board.getGameState() == GameState.PLAYING) {
	                // Make the blue computer's move
	                if (board.getCurrentPlayer() == 'B') {
	                    computerBlue.makeAutoMove();
	                }
	                // Make the red computer's move
	                if (board.getGameState() == GameState.PLAYING && board.getCurrentPlayer() == 'R') {
	                    computerRed.makeAutoMove();
	                }
	                // Publish intermediate results (this triggers the process method)
	                publish();
	                
	                // Delay for a short period to visualize the moves
	                try {
	                    Thread.sleep(500);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }

	            return null;
	        }

	        @Override
	        protected void process(List<Void> chunks) {
	            // Update the GUI
	        	handleGameResult();
	            gameBoardCanvas.repaint();
	        }

	        @Override
	        protected void done() {
	            // Handle the game result here (e.g., display the score and game result)
	            gameBoardCanvas.repaint();
	            handleGameResult();
	        }
	    };

	    // Execute the SwingWorker
	    worker.execute();
	}
	public void replayGame(Board board, String filePath) throws IOException {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			RecordGame recordGame = new RecordGame(filePath); // create an instance of RecordGame class
		    List<String> recordedMoves = recordGame.readRecordedMoves(); // retrieve recorded game
            @Override
            protected Void doInBackground() throws Exception {
            	board.initializeBoard(); // reset board to cell.EMPTY
            	board.getFormedSOSPatterns().clear(); // clear list of formed SOS Patterns
            	if (recordedMoves != null) {
        	        for (String move : recordedMoves) {
        	            // Format is: "player row col cell"
        	            String[] parts = move.split(" ");
        	            char player = parts[0].charAt(0);
        	            int row = Integer.parseInt(parts[1]);
        	            int col = Integer.parseInt(parts[2]);
        	            Cell cell = Cell.valueOf(parts[3]);

        	            // Apply the move to the game
        	            board.makeMove(row, col, cell);
        	            // Publish intermediate results (this triggers the process method)
        	            publish();
        	            
        	            System.out.println("Replaying move: Player " + player + " at (" + row + "," + col + ") with cell " + cell);
        	            
        	            try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
        	        }
        	    } else {
        	        System.out.println("No recorded moves found.");
        	    }
                return null;
            }
            @Override
	        protected void process(List<Void> chunks) {
	            // Update the GUI in between moves
            	handleGameResult();
	            gameBoardCanvas.repaint();
	        }
            @Override
            protected void done() {
                // Update the GUI after the replay is finished
                gameBoardCanvas.repaint();
            }
        };
        // Execute the SwingWorker
        worker.execute();
	}

	public void handleGameResult() {
		String blueScore = "";
		String redScore = "";
		// Display game state to player
	    if (board.getGameState() == GameState.BLUE_WON) {
	        txtCurrentTurn.setText("Blue Player Wins!");
	    } else if (board.getGameState() == GameState.RED_WON) {
	        txtCurrentTurn.setText("Red Player Wins!");
	    } else if (board.getGameState() == GameState.DRAW) {
	        txtCurrentTurn.setText("It's a draw!");
	    } else {
	        // Alternate turns according to gameMode logic
	        txtCurrentTurn.setText("Current turn: " + (currentPlayerTurn == 'B' ? "Blue" : "Red"));
	    }
	    // Display score to player
	    currentPlayerTurn = board.getCurrentPlayer();
        if (currentPlayerTurn == 'B') {
        	blueScore = Integer.toString(board.getBlueCount());
        	redScore = Integer.toString(board.getRedCount());
        	ScoreDisplay.setText("Blue Player: " + blueScore + "  Red Player: " + redScore);
        }
        else {
        	blueScore = Integer.toString(board.getBlueCount());
        	redScore = Integer.toString(board.getRedCount());
        	ScoreDisplay.setText("Blue Player: " + blueScore + "  Red Player: " + redScore);
        }
	    repaint();
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
                gui.board.initializeBoard();
            }
		});
	}
}
