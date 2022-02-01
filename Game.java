import javax.swing.*;

public class Game extends JFrame{

	Board board;

	Game() {

		setSize(530, 560);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setFocusable(true);
		setVisible(true);

		board = new Board(6, 6);
		add(board);

		addKeyListener(new Keyboard(board));

		board.generateTile();

		new AITreeSolver(2, board);

	}

	public static void main(String[] args){

		new Game();

	}

}