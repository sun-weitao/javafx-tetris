package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class Shape {
	
	protected String color;
	protected int[][][] states;
	protected int state;
	protected int x;
	protected int y;
	protected Map<Integer,ArrayList<Point>> allBoxes = new HashMap<Integer, ArrayList<Point>>();
	
	private static final Tetris curTetris = Tetris.getInstance();
	
	public void init() {
		this.color = Constants.COLORS[new Random().nextInt(7)];
		this.allBoxes =  new HashMap<Integer, ArrayList<Point>>();
		this.state = 0;		
//		this.y = 0;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Shape() {
		this.init();
	}
	public String getColor() {
		return this.color;
	}
	
	public ArrayList<Point> getBoxes(int state) {
		ArrayList<Point> boxes = new ArrayList<Point>();
		if(allBoxes.get(state) != null){
			boxes = allBoxes.get(state);
		}
		if(boxes.size() > 0) {
			return boxes;
		}
		int[][] matrix = this.matrix(state);
		for(int i = 0;i<matrix.length;i++) {
			int[] row = matrix[i];
			for(int j = 0;j<row.length;j++)
			{
				if(row[j] == 1) {
				 	boxes.add(new Point(j, i));
				}
			}
		}
		allBoxes.put(state, boxes);
		return boxes;
	}
	
	public int getState() {
		return this.state;
	}

	public int nextState() {
		return (this.state + 1) % this.states.length;
	}

	// 判断能否下落
	public boolean canDown(int[][] matrix) {
		return Utils.isShapeCanMove(this, matrix, Action.DOWN);
	}

	// 向下进一格
	public void goDown(int[][] matrix) {
		if (Utils.isShapeCanMove(this, matrix, Action.DOWN)) {
			this.y += 1;
		}
	}
	// 直接到底
	public void goBottom(int[][] matrix) {
		while (Utils.isShapeCanMove(this, matrix, Action.DOWN)) {
			this.y += 1;
		}
	}

	public void goLeft(int[][] matrix) {
		if (Utils.isShapeCanMove(this, matrix, Action.LEFT)) {
			this.x -= 1;
		}
	}

	public void goRight(int[][] matrix) {
		if (Utils.isShapeCanMove(this, matrix, Action.RIGHT)) {
			this.x += 1;
		}
	}
	
	public void rotate(int[][] matrix) {
		
		if(Utils.isShapeCanMove(this, matrix, Action.ROTATE)) {
			this.state = this.nextState();
			int right = getRight();
			if(right >= Constants.COLUMN_COUNT) {
				this.x -= right - Constants.COLUMN_COUNT + 1;
			}
			
		}
	}
	
	private int getRight() {
		 ArrayList<Point> boxes = getBoxes(this.state);
		 int right = 0;
		 for(int i =0;i<boxes.size();i++)
		 {
			 right = Math.max(boxes.get(i).getX(), right);
		 }
		 return this.x + right;
	}

	public void copyTo(int[][] matrix) {
		int[][] smatrix = this.matrix(this.state);
		for (int i = 0; i < smatrix.length; i++) {
			int[] row = smatrix[i];
			for (int j = 0; j < row.length; j++) {
				if (row[j] == 1) {
					// 改颜色
					int x = this.x + j;
					int y = this.y + i;
					if (x >= 0 && x < matrix[0].length && y >= 0 && y < matrix.length) {
						curTetris.changeMatrixNodeColor(x, y, this.color);
					}
				}

			}
		}
	}
	
	
	
	public int[][] matrix(int state) {		
		return this.states[state];
	}

}
