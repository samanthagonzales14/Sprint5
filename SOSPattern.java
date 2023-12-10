package sprint5_0.project;

import java.util.Objects;

import sprint5_0.project.Board.Cell;

public class SOSPattern {
    private Cell cell;
    private int row;
    private int col;
    private String direction;

    public SOSPattern(Cell cell, int row, int col, String direction) {
        this.cell = cell;
        this.row = row;
        this.col = col;
        this.direction = direction;
    }
    
    public void setCell(Cell cell) {
    	this.cell = cell;
    }
    public void setRow(int row) {
    	this.row = row;
    }
    public void setColumn(int col) {
    	this.col = col;
    }
    public Cell getCell() {
    	return cell;
    }
    public int getRow() {
    	return row;
    }
    public int getCol() {
    	return col;
    }
    public String getDirection() {
        return direction;
    }
    @Override
    public String toString() {
        return "Cell: " + cell + ", Row: " + row + ", Column: " + col + ", Direction: " + direction;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        SOSPattern otherPattern = (SOSPattern) obj;
        return cell == otherPattern.cell && row == otherPattern.row && col == otherPattern.col
                && direction.equals(otherPattern.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell, row, col, direction);
    }
}
