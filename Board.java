import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Board extends JPanel{

	private Tile[][] board;

	private int score;

	boolean gameOver = false;

	int tileSize = 110;
	int fontSize = 40;
	int offset = 15;

	public Board(int sizeX, int sizeY) {

		board = new Tile[sizeX][sizeY];
		tileSize = tileSize * 4 / ((sizeX + sizeY) / 2);
		fontSize = fontSize * 4 / ((sizeX + sizeY) / 2);
		offset = offset * 4 / ((sizeX + sizeY) / 2);

	}

	public Tile[][] getBoard(){
	    return board;
	}

	public int getScore() {
	    return score;
	}

	public void paint(Graphics g){

		g.clearRect(0, 0, 530, 560);

        for(int i = 0; i < board.length; i++){

			for(int j = 0; j < board[0].length; j++){

                if(board[i][j] != null){
                    g.setColor(new Color(255 - (255/board[i][j].getValue()), 255 - (255/board[i][j].getValue()), 0));
                    g.fillRect((j * tileSize + offset), (i * tileSize + offset), tileSize, tileSize);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, fontSize - (int)(Math.log(board[i][j].getValue()))));
                    g.drawString(Integer.toString(board[i][j].getValue()), (j * tileSize + tileSize/2), ((i+1) * tileSize - tileSize/2));
                }

			}

		}

		for(int i = 0; i < board.length; i++){

			for(int j = 0; j < board[0].length; j++){

				g.setColor(Color.BLACK);
				g.drawRect((i * tileSize + offset), (j * tileSize + offset), tileSize, tileSize);

			}

		}
		
		if(gameOver) {

			g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", 150, 250);       

		}
		
	}

	public void doMove(String direction) {

		switch(direction) {

		case "up":
			moveUp(board, true);
			break;
		case "down":
			moveDown(board, true);
			break;
		case "left":
			moveLeft(board, true);
			break;
		case "right":
			moveRight(board, true);
			break;

		}
		
		System.out.println("Score is: " + score);
		generateTile();
		//repaint();

	}

	public void generateTile() {

		int r, c, loopChecker = 0;
		do {
			r = (int)(Math.random() * board.length);
			c = (int)(Math.random() * board[0].length);
			loopChecker++;
			if(loopChecker == board.length * board[0].length) {
				if(!checkBoard(board)) {
					gameOver = true;
					repaint();
					return;
				}
			}
		} while(board[r][c] != null);
		board[r][c] = new Tile(2);

		repaint();

	}

	private int moveDown(Tile[][] boardTemp, boolean orig) {

		Tile[][] newBoard = new Tile[boardTemp.length][boardTemp[0].length];
		for(int c = 0; c < boardTemp[0].length; c++) {
			boolean tracker = false;
			for(int r = boardTemp.length - 1, counter = boardTemp.length - 1; r >= 0; r--) {

				if(boardTemp[r][c] != null) {
					newBoard[counter][c] = boardTemp[r][c];
					if(counter < boardTemp.length - 1 && !tracker && Tile.compareTile(newBoard[counter][c], newBoard[counter + 1][c]) == 0) {
						newBoard[counter + 1][c].addTile(newBoard[counter][c]);
						score += newBoard[counter + 1][c].getValue();
						newBoard[counter][c] = null;
						//System.out.println("tresdfgh");
						tracker = true;
					}
					else {
						counter--;
						tracker = false;
					}
				}

			}
		}

		boardTemp = newBoard;

		if(orig) {
			board = newBoard;
		}

		return score;

	}

	private int moveUp(Tile[][] boardTemp, boolean orig) {

		Tile[][] newBoard = new Tile[boardTemp.length][boardTemp[0].length];
		for(int c = 0; c < boardTemp[0].length; c++) {
			boolean tracker = false;
			for(int r = 0, counter = 0; r < boardTemp.length; r++) {

				if(boardTemp[r][c] != null) {
					newBoard[counter][c] = boardTemp[r][c];
					if(counter > 0 && !tracker && Tile.compareTile(newBoard[counter - 1][c], newBoard[counter][c]) == 0) {
						newBoard[counter - 1][c].addTile(newBoard[counter][c]);
						score += newBoard[counter - 1][c].getValue();
						newBoard[counter][c] = null;
						//System.out.println("tresdfgh");
						tracker = true;
					}
					else {
						counter++;
						tracker = false;
					}
				}

			}
		}

		boardTemp = newBoard;

		if(orig) {
			board = newBoard;
		}
		
		return score;

	}

	private int moveLeft(Tile[][] boardTemp, boolean orig) {

		Tile[][] newBoard = new Tile[boardTemp.length][boardTemp[0].length];
		for(int c = 0; c < boardTemp.length; c++) {
			boolean tracker = false;
			for(int r = 0, counter = 0; r < boardTemp[0].length; r++) {

				if(boardTemp[c][r] != null) {
					newBoard[c][counter] = boardTemp[c][r];
					if(counter > 0 && !tracker && Tile.compareTile(newBoard[c][counter - 1], newBoard[c][counter]) == 0) {
						newBoard[c][counter - 1].addTile(newBoard[c][counter]);
						score += newBoard[c][counter - 1].getValue();
						newBoard[c][counter] = null;
						//System.out.println("tresdfgh");
						tracker = true;
					}
					else {
						counter++;
						tracker = false;
					}
				}

			}
		}

		boardTemp = newBoard;

		if(orig) {
			board = newBoard;
		}

		return score;

	}

	private int moveRight(Tile[][] boardTemp, boolean orig) {

		Tile[][] newBoard = new Tile[boardTemp.length][boardTemp[0].length];
		for(int c = 0; c < boardTemp.length; c++) {
			boolean tracker = false;
			for(int r = boardTemp[0].length - 1, counter = boardTemp[0].length - 1; r >= 0; r--) {

				if(boardTemp[c][r] != null) {
					newBoard[c][counter] = boardTemp[c][r];
					if(counter < boardTemp[0].length - 1 && !tracker && Tile.compareTile(newBoard[c][counter], newBoard[c][counter + 1]) == 0) {
						newBoard[c][counter + 1].addTile(newBoard[c][counter]);
						score += newBoard[c][counter + 1].getValue();
						newBoard[c][counter] = null;
						//System.out.println("tresdfgh");
						tracker = true;
					}
					else {
						counter--;
						tracker = false;
					}
				}

			}
		}

		boardTemp = newBoard;

		if(orig) {
			board = newBoard;
		}

		return score;

	}
	
	public static boolean checkBoard(Tile[][] boardTemp) {
		int nc = 0;
		for (int r = 0; r < boardTemp.length; r++) {
			for (int c = 0; c < boardTemp[0].length; c++) {
				if(boardTemp[r][c] == null) {
					nc++;
				}
				if(nc >= 2) {
					return true;
				}
			}
		}
		for (int r = 0; r < boardTemp.length; r++) {
			for (int c = 1; c < boardTemp[0].length; c++) {
				if(boardTemp[r][c] == boardTemp[r][c-1] || boardTemp[r][c] == null && boardTemp[r][c-1].getValue() == 2 || boardTemp[r][c] != null && boardTemp[r][c].getValue() == 2 && boardTemp[r][c-1] == null) {
					return true;
				}
			}
		}
		for (int c = 0; c < boardTemp[0].length; c++) {
			for (int r = 1; r < boardTemp.length; r++) {
				if(boardTemp[r][c] == boardTemp[r-1][c] || boardTemp[r][c] == null && boardTemp[r-1][c].getValue() == 2 || boardTemp[r][c] != null && boardTemp[r][c].getValue() == 2 && boardTemp[r-1][c] == null) {
					return true;
				}
			}
		}
		return false;
	}

}