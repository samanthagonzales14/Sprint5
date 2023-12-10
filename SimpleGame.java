package sprint5_0.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleGame extends Board {
    private List<SOSPattern> formedSOSPatterns = new ArrayList<>();
	private Map<SOSPattern, Character> sosPatternPlayers = new HashMap<>();
    
    public SimpleGame() {
    	super();
        formedSOSPatterns.clear();
    }
    public SimpleGame(int size) {
    	super(size);
        formedSOSPatterns.clear();
    }
    @Override
    public void setBoardSize(int size) {
    	super.setBoardSize(size);
    }
    
    public int getBoardSize() {
    	return boardSize;
    }
    
    public void initializeBoard() {
    	super.initializeBoard();
    }
    @Override
    public void newGame() {
    	super.newGame();
    	formedSOSPatterns.clear();
    }
    
    public Cell[][] getBoard() {
        return board;
    }
    
    public void printBoard() {
        super.printBoard();
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
    @Override
    public Cell getCell(int row, int column) {
    	return super.getCell(row, column);
    }
    
    public int getBlueCount() {
    	return blueCount;
    }
    
    public int getRedCount() {
    	return redCount;
    }
    
    public List<SOSPattern> getFormedSOSPatterns() {
        return formedSOSPatterns;
    }
    
    public Map<SOSPattern, Character> getSOSPatternPlayers() {
        return sosPatternPlayers;
    }
    
    /**
   	 * @precond: none
   	 * @postcond: if (row, column) is a valid empty cell, then the player's token
   	 *            has been placed in the cell and the turn has changed to the other
   	 *            player
   	 * 
   	 */
    @Override
    public boolean makeMove(int row, int col, Cell cell) {
        if (currentGameState != GameState.PLAYING) {
        	System.out.println("Game is already over");
            return false; // Game is already over
            
        }

        if (row < 0 || row >= boardSize || col < 0 || col >= boardSize || board[row][col] != Cell.EMPTY) {
            return false; // Invalid move
        }

        board[row][col] = cell;
        
        if (recording) {
        	 recordMove(currentPlayer, row, col, cell);
        }
       
        
        updateGameState(currentPlayer);
        if (currentGameState == GameState.PLAYING) {
	        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';
	        System.out.println("Switch players");
        }
        
        return true;
    }
    
    public void countSOS() {
    	super.countSOS();
    }    

    public void updateGameState(char turn) {
    	if (checkForSOS()) {
            currentGameState = (turn == 'B') ? GameState.BLUE_WON : GameState.RED_WON;
            System.out.println("Winner is " + currentGameState);
        } else if (isBoardFull()) {
            currentGameState = GameState.DRAW;
            System.out.println("Game is a Draw");
        }
    	// Otherwise, no change to current state (still GameState.PLAYING).
    }
    
    public boolean isBoardFull() {
    	return super.isBoardFull();
    }
   
   /**
  	 * @precond: a valid move was made
  	 * @postcond: if an SOS pattern is found on the board then store starting symbol ('S'), 
  	 *            start row and column, and direction (row, column, diagonal (from top-left 
  	 *            to bottom-right), diagonal (from top-right to bottom-left)). Then add it 
  	 *            to the list, store the player who formed the SOS Pattern, increment player 
  	 *            score, and return true (SOS found). Otherwise return false (no SOS found).
  	 * 
  	 */
    public boolean checkForSOS() {   
    	Cell[] symbols = { Cell.LETTERS, Cell.LETTERO, Cell.LETTERS }; // The SOS pattern to check for

        // Check rows for SOS
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize - 2; col++) {
                if (board[row][col] == symbols[0] &&
                    board[row][col + 1] == symbols[1] &&
                    board[row][col + 2] == symbols[2]) {
                	SOSPattern newPattern = new SOSPattern(symbols[0], row, col, "row");
                	formedSOSPatterns.add(newPattern); // Add formed SOS pattern to list
            		sosPatternPlayers.put(newPattern, currentPlayer); // Store player who formed SOS Pattern
                	countSOS(); // increment player score
                    return true;
                }
            }
        }

        // Check columns for SOS
        for (int col = 0; col < boardSize; col++) {
            for (int row = 0; row < boardSize - 2; row++) {
                if (board[row][col] == symbols[0] &&
                    board[row + 1][col] == symbols[1] &&
                    board[row + 2][col] == symbols[2]) {
                	SOSPattern newPattern = new SOSPattern(symbols[0], row, col, "column");
                	formedSOSPatterns.add(newPattern);
            		sosPatternPlayers.put(newPattern, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }

        // Check diagonals (from top-left to bottom-right)
        for (int row = 0; row < boardSize - 2; row++) {
            for (int col = 0; col < boardSize - 2; col++) {
                if (board[row][col] == symbols[0] &&
                    board[row + 1][col + 1] == symbols[1] &&
                    board[row + 2][col + 2] == symbols[2]) {
                	SOSPattern newPattern = new SOSPattern(symbols[0], row, col, "diagTlBr");
                	formedSOSPatterns.add(newPattern);
            		sosPatternPlayers.put(newPattern, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }

        // Check diagonals (from top-right to bottom-left)
        for (int row = 0; row < boardSize - 2; row++) {
            for (int col = boardSize - 1; col >= 2; col--) {
                if (board[row][col] == symbols[0] &&
                    board[row + 1][col - 1] == symbols[1] &&
                    board[row + 2][col - 2] == symbols[2]) {
                	SOSPattern newPattern = new SOSPattern(symbols[0], row, col, "diagTrBl");
                	formedSOSPatterns.add(newPattern);
            		sosPatternPlayers.put(newPattern, currentPlayer);
                	countSOS();
                    return true;
                }
            }
        }

        return false;
    }
    
    public void recordMove(char player, int row, int col, Cell cell) {
    	super.recordMove(player, row, col, cell);
    }
    public void startRecording() {
	   super.startRecording();
	}

	public void stopRecording() {
	    super.stopRecording();
	}
}
