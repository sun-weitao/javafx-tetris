package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javafx.animation.AnimationTimer;

public class Utils {
	// 判断 俄罗斯方块 是否还能继续移动
	public static boolean isShapeCanMove(Shape shape, int[][] matrix, Action action) {
		int rows = matrix.length;
		int cols = matrix[0].length;

		ArrayList<Point> boxes = new ArrayList<Point>();
		if (action == Action.ROTATE) {
			boxes = shape.getBoxes(shape.nextState());
		} else {
			boxes = shape.getBoxes(shape.getState());
		}
		for (int i = 0; i < boxes.size(); i++) {
			Point box = boxes.get(i);
			if (box != null) {
				if (!isBoxCanMove(matrix, cols, rows, shape, box, action)) {
					return false;
				}
			}
		}
		return true;
	}

	private static boolean isBoxCanMove(int[][] matrix, int cols, int rows, Shape shape, Point box, Action action) {
		int x = shape.x + box.getX();
		int y = shape.y + box.getY();
		if (y < 0) {
			return true;
		}
		if (y < rows && x < cols && y >= 0 && x >= 0) {
			if (action == Action.LEFT) {
				x -= 1;
				if(x < 0) {
					return false;
				}
				return x >= 0 && x < cols && matrix[y][x] == 0;
			} else if (action == Action.RIGHT) {
				x += 1;
				if(x >= cols) {
					return false;
				}
				return x >= 0 && x < cols && matrix[y][x] == 0;
			} else if (action == Action.DOWN) {
				y += 1;
				if(y >= rows) {
					return false;
				}
				return y < rows && matrix[y][x] == 0;
			} else if (action == Action.ROTATE) {
				// 如果数组越界
				return y < rows && !(y < 0 || y > matrix.length || x < 0 || x > matrix[0].length);
			}
		}
		return false;
	}

	// 产生随机的俄罗斯方块
	public static Shape randomShape() {
		int result = new Random().nextInt(7);
		Shape shape = null;
		switch (result) {
		case 0:
			shape = new ShapeL();
			break;
		case 1:
			shape = new ShapeLR();
			break;
		case 2:
			shape = new ShapeO();
			break;
		case 3:
			shape = new ShapeT();
			break;
		case 4:
			shape = new ShapeZ();
			break;
		case 5:
			shape = new ShapeI();
			break;
		case 6:
			shape = new ShapeZR();
			break;
		}
		shape.init();
		return shape;
	}

	public static int findColorIndex(String color) {
		for (int i = 0; i < Constants.COLORS.length; i++) {
			if (Constants.COLORS[i].equals(color)) {
				return i;
			}
		}
		return -1;
	}

	public static void printMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println(); // 打印换行
		}
	}

	
}
