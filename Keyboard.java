import java.awt.event.*;

public class Keyboard extends KeyAdapter {

	Board board;
	
	public Keyboard(Board board) {
		this.board = board;
	}
	
	public void keyPressed(KeyEvent e){

		int keycode = e.getKeyCode();

		switch(keycode) {

			case 'W':
				board.doMove("up");
				break;
			case 'S':
				board.doMove("down");
				break;
			case 'A':
				board.doMove("left");
				break;
			case 'D':
				board.doMove("right");
				break;

		}

	}

}