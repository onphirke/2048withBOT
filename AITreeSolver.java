public class AITreeSolver {

	int depthSight;
	public Board board;

	public AITreeSolver(int depthSight, Board board) {
		this.depthSight = depthSight;
		this.board = board;

		new PlayGame(this).start();
	}

}

class PlayGame extends Thread {

	AITreeSolver ai;

	public PlayGame(AITreeSolver ai) {
		this.ai = ai;
	}

	public void run() {

		Tree tree;

		while (!ai.board.gameOver) {

			tree = new Tree(createNode(null, 0, ai.board.getBoard(), ai.board.getScore()));
			String dir = calculateDirection(tree);
			ai.board.doMove(dir);

//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

		}

	}

	public String calculateDirection(Tree tree) {

		double[] avgs = new double[4];

		avgs[0] = (recurseTotal(tree.root.up) / recurseCount(tree.root.up));
		avgs[1] = (recurseTotal(tree.root.down) / recurseCount(tree.root.down));
		avgs[2] = (recurseTotal(tree.root.left) / recurseCount(tree.root.left));
		avgs[3] = (recurseTotal(tree.root.right) / recurseCount(tree.root.right));

		double max = avgs[0];
		int index = 0;

		for (int i = 0; i < avgs.length; i++) {
			if (avgs[i] > max) {
				index = i;
				max = avgs[i];
			}
			System.out.print(avgs[i] + " ");
		}
		System.out.println();
		

		String direction = "";

		switch (index) {

		case 0:
			direction = "up";
			break;
		case 1:
			direction = "down";
			break;
		case 2:
			direction = "left";
			break;
		case 3:
			direction = "right";
			break;

		}

		System.out.println(direction);
		
		return direction;

	}

	public double recurseTotal(Node startNode) {

		if (startNode == null) {
			return 0;
		}

		double up = recurseTotal(startNode.up);
		double down = recurseTotal(startNode.down);
		double left = recurseTotal(startNode.left);
		double right = recurseTotal(startNode.right);

		return (startNode.getScore() + up + down + left + right);

	}

	public int recurseCount(Node startNode) {

		if (startNode == null) {
			return 0;
		}

		int up = recurseCount(startNode.up);
		int down = recurseCount(startNode.down);
		int left = recurseCount(startNode.left);
		int right = recurseCount(startNode.right);

		return (1 + up + down + left + right);

	}

	public Node createNode(Node current, int depth, Tile[][] board, int score) {

		if (depth > ai.depthSight) {
			return current;
		}

		if (current == null) {
			current = new Node(depth, score, board);
		}

		Pair up = AIBoardHelper.moveUp(current.nodeBoard, current.score);
		if(!AIBoardHelper.checkBoard(up.board))
			up.score = Integer.MIN_VALUE;
		current.up = createNode(current.up, depth + 1, up.board, up.score);

		Pair down = AIBoardHelper.moveDown(current.nodeBoard, current.score);
		if(!AIBoardHelper.checkBoard(down.board))
			down.score = Integer.MIN_VALUE;
		current.down = createNode(current.down, depth + 1, down.board, down.score);

		Pair left = AIBoardHelper.moveLeft(current.nodeBoard, current.score);
		if(!AIBoardHelper.checkBoard(left.board))
			left.score = Integer.MIN_VALUE;
		current.left = createNode(current.left, depth + 1, left.board, left.score);

		Pair right = AIBoardHelper.moveRight(current.nodeBoard, current.score);
		if(!AIBoardHelper.checkBoard(right.board))
			right.score = Integer.MIN_VALUE;
		current.right = createNode(current.right, depth + 1, right.board, right.score);

		return current;

	}

}

class Tree {

	Node root;

	public Tree(Node root) {
		this.root = root;
	}

	public Node getRoot() {
		return root;
	}

}

class Node {

	public Node up;
	public Node down;
	public Node left;
	public Node right;

	int depth;
	int score;
	Tile[][] nodeBoard;

	public int getScore() {
		return score;
	}

	public Node(Node up, Node down, Node left, Node right, int depth, int score, Tile[][] nodeBoard) {
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		this.depth = depth;
		this.score = score;
		this.nodeBoard = nodeBoard;
	}

	public Node(int depth, int score, Tile[][] nodeBoard) {
		this.up = null;
		this.down = null;
		this.left = null;
		this.right = null;
		this.depth = depth;
		this.score = score;
		this.nodeBoard = nodeBoard;
	}

}

class Pair {

	public int score;
	public Tile[][] board;

	public Pair(Tile[][] board, int score) {
		this.score = score;
		this.board = board;
	}

}

class AIBoardHelper {
	
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

	public static Pair moveDown(Tile[][] boardTemp, int score) {

		Tile[][] newBoard = new Tile[boardTemp.length][boardTemp[0].length];
		for (int c = 0; c < boardTemp[0].length; c++) {
			boolean tracker = false;
			for (int r = boardTemp.length - 1, counter = boardTemp.length - 1; r >= 0; r--) {

				if (boardTemp[r][c] != null) {
					newBoard[counter][c] = boardTemp[r][c].copy();
					if (counter < boardTemp.length - 1 && !tracker && Tile.compareTile(newBoard[counter][c], newBoard[counter + 1][c]) == 0) {
						newBoard[counter + 1][c].addTile(newBoard[counter][c]);
						score += newBoard[counter + 1][c].getValue();
						newBoard[counter][c] = null;
						// System.out.println("tresdfgh");
						tracker = true;
					} else {
						counter--;
						tracker = false;
					}
				}

			}
		}
		
		return new Pair(newBoard, score);
	}

	public static Pair moveUp(Tile[][] boardTemp, int score) {

		Tile[][] newBoard = new Tile[boardTemp.length][boardTemp[0].length];
		for (int c = 0; c < boardTemp[0].length; c++) {
			boolean tracker = false;
			for (int r = 0, counter = 0; r < boardTemp.length; r++) {

				if (boardTemp[r][c] != null) {
					newBoard[counter][c] = boardTemp[r][c].copy();
					if (counter > 0 && !tracker	&& Tile.compareTile(newBoard[counter - 1][c], newBoard[counter][c]) == 0) {
						newBoard[counter - 1][c].addTile(newBoard[counter][c]);
						score += newBoard[counter - 1][c].getValue();
						newBoard[counter][c] = null;
						// System.out.println("tresdfgh");
						tracker = true;
					} else {
						counter++;
						tracker = false;
					}
				}

			}
		}
		return new Pair(newBoard, score);
	}

	public static Pair moveLeft(Tile[][] boardTemp, int score) {

		Tile[][] newBoard = new Tile[boardTemp.length][boardTemp[0].length];
		for (int c = 0; c < boardTemp.length; c++) {
			boolean tracker = false;
			for (int r = 0, counter = 0; r < boardTemp[0].length; r++) {

				if (boardTemp[c][r] != null) {
					newBoard[c][counter] = boardTemp[c][r].copy();
					if (counter > 0 && !tracker
							&& Tile.compareTile(newBoard[c][counter - 1], newBoard[c][counter]) == 0) {
						newBoard[c][counter - 1].addTile(newBoard[c][counter]);
						score += newBoard[c][counter - 1].getValue();
						newBoard[c][counter] = null;
						// System.out.println("tresdfgh");
						tracker = true;
					} else {
						counter++;
						tracker = false;
					}
				}

			}
		}
		return new Pair(newBoard, score);
	}

	public static Pair moveRight(Tile[][] boardTemp, int score) {

		Tile[][] newBoard = new Tile[boardTemp.length][boardTemp[0].length];
		for (int c = 0; c < boardTemp.length; c++) {
			boolean tracker = false;
			for (int r = boardTemp[0].length - 1, counter = boardTemp[0].length - 1; r >= 0; r--) {

				if (boardTemp[c][r] != null) {
					newBoard[c][counter] = boardTemp[c][r].copy();
					if (counter < boardTemp[0].length - 1 && !tracker
							&& Tile.compareTile(newBoard[c][counter], newBoard[c][counter + 1]) == 0) {
						newBoard[c][counter + 1].addTile(newBoard[c][counter]);
						score += newBoard[c][counter + 1].getValue();
						newBoard[c][counter] = null;
						// System.out.println("tresdfgh");
						tracker = true;
					} else {
						counter--;
						tracker = false;
					}
				}

			}
		}

		return new Pair(newBoard, score);
	}

}