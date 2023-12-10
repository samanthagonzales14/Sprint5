package sprint5_0.project;

import java.util.List;
import java.util.Map;

public abstract class Board {
    
    public static enum Cell { EMPTY, LETTERS, LETTERO } // Represents an empty cell, 'S', and 'O'
    public enum GameState {
		PLAYING, DRAW, BLUE_WON, RED_WON
    }
        
    protected Cell[][] board;
    protected char currentPlayer;
    protected int boardSize;
    protected int blueCount;
    protected int redCount;
    private boolean isSimpleGame;
    protected GameState currentGameState;
    protected RecordGame recordGame;
	protected boolean recording = false;
    
    public Board() {
    	 this.boardSize = 3; // Default board size
         this.board = new Cell[3][3]; // Default grid size
         this.isSimpleGame = true; // Default game mode
         recordGame = new RecordGame("game_moves.txt"); // Initialize file to store player moves
         initializeBoard();
    }
    public Board(int size) {
    	this.boardSize = size; // set boardSize with parameter size
        this.board = new Cell[size][size]; // create new board with new size dimensions
        this.isSimpleGame = true; // Default game mode   
        recordGame = new RecordGame("game_moves.txt"); // Initialize file to store player moves
        initializeBoard();
    }
        
    public Board(int size, boolean mode) {
    	this.boardSize = size;
        this.isSimpleGame = mode;
        this.currentPlayer = 'B';
        this.board = new Cell[size][size];
        this.currentGameState = GameState.PLAYING;
        initializeBoard();
    }
    
    public void setBoardSize(int size) {
    	 this.boardSize = size;
         this.board = new Cell[size][size];
         initializeBoard();
    }
    
    public int getBoardSize() {
    	return boardSize;
    }
    public void newGame() {
    	recordGame.deleteRecordedFile();
    	recordGame = new RecordGame("game_moves.txt");
    	initializeBoard();
    }
    public void initializeBoard() {
    	// Initialize board with EMPTY cells
    	 for (int i = 0; i < boardSize; i++) {
             for (int j = 0; j < boardSize; j++) {
                 board[i][j] = Cell.EMPTY;
             }
         }
    	 currentGameState = GameState.PLAYING; // Set starting gameState to Playing
         currentPlayer = 'B';	// Set first player to Blue Player
         redCount = 0;			// Set players scores to zero
         blueCount = 0;
    }
    
    public Cell[][] getBoard() {
        return board;
    }
    
    public void printBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void setGameMode(boolean mode) {
    	this.isSimpleGame = mode; // true for Simple game, false for General game mode
    }
    
    public boolean getGameMode() {
    	return isSimpleGame;
    }
    
    public void setCurrentPlayer(char player) {
        this.currentPlayer = player;
    }
    
    public char getCurrentPlayer() {
		return currentPlayer;
	}
    
    public GameState getGameState() {
		return currentGameState;
	}
    
    /**
	 * 
	 * @precond: none
	 * @postcond: returns Cell.LETTERS, or Cell.LETTERO if row >= 0 && row
	 *            < boardSize && column >= 0 && column < boardSize otherwise Cell.EMPTY 
	 * 
	 */
    public Cell getCell(int row, int column) {
        if (row >= 0 && row < boardSize && column >= 0 && column < boardSize)
            return board[row][column];
        else
            return Cell.EMPTY;
    }
      
    /**
	 * @precond: none
	 * @postcond: if (row, column) is a valid empty cell, then the player's token
	 *            has been placed in the cell and the turn has changed to the other
	 *            player
	 * 
	 */
    public boolean makeMove(int row, int col, Cell cell) {
        if (row < 0 || row >= boardSize || col < 0 || col >= boardSize || board[row][col] != Cell.EMPTY) {
            return false; // Invalid move
        }
        board[row][col] = cell;
        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';
        return true;
    }
    
    public void countSOS() {
    	// increment player score if SOS is formed
    	if (currentPlayer == 'B') {
    		blueCount += 1;
    	}
    	else {
    		redCount += 1;
    	}
    }
    
    protected boolean isBoardFull() {
    	for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == Cell.EMPTY) {
                    return false;	// If an empty cell is found, the board is not full
                }
            }
    	}
    	return true; // If no empty cell is found, the board is full
    }
    protected void recordMove(char player, int row, int col, Cell cell) {
    	Cell symbol = cell;
    	String letter = symbol.toString();
    	String move = String.format("%c %d %d %s",player, row, col, letter);
    	
    	recordGame.recordMove(move);
    }
    protected void startRecording() {
	    recording = true;
	}

	protected void stopRecording() {
	    recording = false;
	}
    
  protected abstract boolean checkForSOS();
	protected abstract void updateGameState(char turn);
	protected abstract int getBlueCount();
	protected abstract int getRedCount();
	protected abstract List<SOSPattern> getFormedSOSPatterns();
	protected abstract Map<SOSPattern, Character> getSOSPatternPlayers();
}
