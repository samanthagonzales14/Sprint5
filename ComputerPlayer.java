package sprint5_0.project;

import java.util.Random;

import sprint5_0.project.Board.Cell;

public class ComputerPlayer {
	Random random;
	private char autoPlayer;
	private int boardSize;
	private Board board;
	Board.GameState currentGameState;
	
	public ComputerPlayer(Board board, char turn) {
		this.board = board;
		boardSize = board.getBoardSize();
		autoPlayer = turn;
    }
	 public void newGame() {
        board.newGame();
        if (autoPlayer == 'B') {
            makeFirstMove();
        }
	 }

	public void makeFirstMove() {
		Random random = new Random();
        int row, col;
        row = random.nextInt(boardSize);
        col = random.nextInt(boardSize);
        board.makeMove(row, col, Cell.LETTERS);
	}
		
	public void makeAutoMove() {
		currentGameState = board.getGameState();
		if (currentGameState == Board.GameState.PLAYING && !board.isBoardFull()) {
			if (!makeWinningMove()) {
				if (!blockOpponentWinningMove())
					makeRandomMove();
			}
		}
	}

	/**
  	 * @precond: none
  	 * @postcond: check rows, columns, and diagonals (in that order) for possible SOS 
  	 * 			  formation. If there exists a move that will form an SOS place an S 
  	 *            or O, respectively, to complete an SOS formation and return true.
  	 *            Otherwise return false if no possible SOS formations.
  	 * 
  	 */
	public boolean makeWinningMove() {
		Cell[] symbols = { Cell.LETTERS, Cell.LETTERO, Cell.LETTERS }; // The SOS pattern to check for

        // Check rows for SOS
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize - 2; col++) {
                if (board.getCell(row,col) == symbols[0] &&
                    board.getCell(row, col + 1) == Cell.EMPTY &&
                    board.getCell(row, col + 2) == symbols[2]) {
                	board.makeMove(row, col +1, Cell.LETTERO);
                    return true;
                }
                if (board.getCell(row,col) == Cell.EMPTY &&
                    board.getCell(row, col + 1) == symbols[1] &&
                    board.getCell(row, col + 2) == symbols[2]) {
                	board.makeMove(row, col, Cell.LETTERS);
                    return true;
                }
                if (board.getCell(row,col) == symbols[0] &&
                    board.getCell(row, col + 1) == symbols[1] &&
                    board.getCell(row, col + 2) == Cell.EMPTY) {
                	board.makeMove(row, col + 2, Cell.LETTERS);
                    return true;
                }
            }
        }
        
        
        // Check columns for SOS
        for (int col = 0; col < boardSize; col++) {
            for (int row = 0; row < boardSize - 2; row++) {
                if (board.getCell(row, col) == symbols[0] &&
                    board.getCell(row + 1,col) == Cell.EMPTY &&
                    board.getCell(row + 2, col) == symbols[2]) {
                	board.makeMove(row + 1, col, Cell.LETTERO);
                    return true;
                }
                if (board.getCell(row, col) == Cell.EMPTY &&
                    board.getCell(row + 1,col) == symbols[1] &&
                    board.getCell(row + 2, col) == symbols[2]) {
                	board.makeMove(row, col, Cell.LETTERS);
                    return true;
                }
                if (board.getCell(row, col) == symbols[0] &&
                    board.getCell(row + 1,col) == symbols[1] &&
                    board.getCell(row + 2, col) == Cell.EMPTY) {
                	board.makeMove(row + 2, col, Cell.LETTERS);
                    return true;
                }
            }
        }
        
     // Check diagonals (from top-left to bottom-right)
        for (int row = 0; row < boardSize - 2; row++) {
            for (int col = 0; col < boardSize - 2; col++) {
                if (board.getCell(row, col) == symbols[0] &&
            		board.getCell(row + 1, col + 1) == Cell.EMPTY &&
            		board.getCell(row + 2, col + 2) == symbols[2]) {
                	board.makeMove(row + 1, col + 1, Cell.LETTERO);
                    return true;
                }
                if (board.getCell(row, col) == Cell.EMPTY &&
            		board.getCell(row + 1, col + 1) == symbols[1] &&
            		board.getCell(row + 2, col + 2) == symbols[2]) {
                	board.makeMove(row, col, Cell.LETTERS);
                    return true;
                }
                if (board.getCell(row, col) == symbols[0] &&
            		board.getCell(row + 1, col + 1) == symbols[1] &&
            		board.getCell(row + 2, col + 2) == Cell.EMPTY) {
                	board.makeMove(row + 2, col + 2, Cell.LETTERS);
                    return true;
                }
            }
        }
     // Check diagonals (from top-right to bottom-left)
        for (int row = 0; row < boardSize - 2; row++) {
            for (int col = boardSize - 1; col >= 2; col--) {
                if (board.getCell(row, col) == symbols[0] &&
                    board.getCell(row + 1, col - 1) == Cell.EMPTY &&
                    board.getCell(row + 2, col - 2) == symbols[2]) {
                	board.makeMove(row + 1, col - 1, Cell.LETTERO);
                    return true;
                }
                if (board.getCell(row, col) == Cell.EMPTY &&
                    board.getCell(row + 1, col - 1) == symbols[1] &&
                    board.getCell(row + 2, col - 2) == symbols[2]) {
                	board.makeMove(row, col, Cell.LETTERS);
                    return true;
                }
                if (board.getCell(row, col) == symbols[0] &&
                    board.getCell(row + 1, col - 1) == symbols[1] &&
                    board.getCell(row + 2, col - 2) == Cell.EMPTY) {
                	board.makeMove(row + 2, col - 2, Cell.LETTERS);
                    return true;
                }
            }
        }
		return false;
	}

	private boolean blockOpponentWinningMove() {
		return false;
	}
	
	public void makeRandomMove() {
		int numberOfEmptyCells = getNumberOfEmptyCells();
		Random random = new Random();
		int targetMove = random.nextInt(numberOfEmptyCells); // random target generated
		int randomNumber = random.nextInt(2);
		Cell symbol = null;
		// Randomly pick S or O to place on board
		if (randomNumber == 0) {
			symbol = Cell.LETTERS;
		}
		else if (randomNumber == 1) {
			symbol = Cell.LETTERO;
		}
		
		int index=0;
		for (int row = 0; row < boardSize; ++row) {
			for (int col = 0; col < boardSize; ++col) {
				if (board.getCell(row, col) == Cell.EMPTY) {
					if (targetMove == index) { // if targetMove is same as index
						board.makeMove(row, col, symbol);	// make random move
						return;
					} else
						index++;
				}
			}
		}
	}

	public int getNumberOfEmptyCells() {
		int numberOfEmptyCells = 0;
		for (int row = 0; row < boardSize; ++row) {
			for (int col = 0; col < boardSize; ++col) {
				if (board.getCell(row, col) == Cell.EMPTY) {
					numberOfEmptyCells++;
				}
			}
		}
		return numberOfEmptyCells;
	}
}
