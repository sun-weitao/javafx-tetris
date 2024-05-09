package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Tetris {
	private static Tetris instance;
	private int interval;// 默认时间间隔
	private static int[][] MATRIX = new int[Constants.ROW_COUNT][Constants.COLUMN_COUNT];
	private boolean isRunning = false;
	private Date currentTime = new Date();
	private Date prevTime = new Date();
	private Date levelTime = new Date();
	private int level = 1;
	private int score = 0;//总分数
	
	private boolean isGameOver = false;
	private Shape shape;
	private Shape preparedShape;
	private Views view;// 视图资源
	
	public Views getView() {
		return view;
	}

	public void setView(Views view) {
		this.view = view;
	}

	public Shape getShape() {
		return shape;
	}
	
	public boolean getIsRunning() {
		return this.isRunning;
	}
	
	public void pause(boolean isRunning) {
		this.isRunning = isRunning;
		this.currentTime = new Date();
		this.prevTime = this.currentTime;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public static synchronized Tetris getInstance() {
		if (instance == null) {
			instance = new Tetris();
		}
		return instance;
	}

	public int[][] getMatrix() {
		return this.MATRIX;
	}

	private Tetris() {
		this.interval = Constants.DEFAULT_INTERVAL;
		this.MATRIX = initMatrix();
		// 初始化下落的方块
		this._fireShape();
	}

	public void start() {
		this.isRunning = true;
		if(this.view != null && this.preparedShape != null) {
			this.view.updatePreviewShape(this.preparedShape);
		}
		this._refresh();
	}

	private void _refresh() {
		if (!this.isRunning) {
			return;
		}
		this.currentTime = new Date();
		if (this.currentTime.getTime() - this.prevTime.getTime() > this.interval) {
			this._update();
			this.prevTime = this.currentTime;
			this._checkLevel();
		}
		if (!this.isGameOver) {
			Platform.runLater(() -> {
				this._refresh();
			});
		}
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

	// 更新俄罗斯方块画布
	public void _update() {
		if (this.shape.canDown(this.getMatrix())) {
			this.shape.goDown(this.getMatrix());
		} else {
			this.shape.copyTo(this.getMatrix());
			this._check();
			this._fireShape();
		}
	
		// 判断游戏是否结束
		this.isGameOver = checkGameOver();
		if(this.view != null) {
			this.view.setMaskVisible(isGameOver);
		}	
	// 更新游戏画面
		this.view.updateViews();
		// 设置GameOver
		if (this.isGameOver) {
			// 设置最后游戏分数
			
		}

	}

	private boolean checkGameOver() {
		int[] firstRow = this.MATRIX[0];
		for (int i = 0; i < firstRow.length; i++) {
			if (firstRow[i] != 0) {
				return true;
			}
		}
		return false;
	}

	private void _fireShape() {
		if (this.preparedShape != null) {
			this.shape = this.preparedShape;
		} else {
			this.shape = Utils.randomShape();
		}
		this.preparedShape = Utils.randomShape();

		// 更新画面
		if (this.view != null) {
			this.view.initFallShape();
			this.view.changeMatrixColor();
			// 更新下一个Shape的画面
			this.view.updatePreviewShape(this.preparedShape);
		}

	}

	private void _check() {
		ArrayList<Integer> rows = checkFullRows();
		if (rows.size() > 0) {
			removeRows(rows);
			// 计算分数
			int score = calScore(rows);
			int reward = calcRewards(rows);
			this.score += score + reward;
			// 设置分数
			if(this.view != null) {
				view.setScore(score);
			}
		}
	}
	public int calcIntervalByLevel(int level) {
		return Constants.DEFAULT_INTERVAL - (level - 1)*60;
	}

	private void _checkLevel() {
		Date now = new Date();
		long currentTime = now.getTime();
		if(currentTime - this.levelTime.getTime() > Constants.LEVEL_INTERVAL) {
			this.level += 1;
			this.interval = calcIntervalByLevel(this.level);
			if(this.view != null) {
				view.setLevel(this.level);
				this.levelTime = new Date(currentTime);
			}
		}
	}

	public void _reset() {
		this.isRunning = false;
		this.isGameOver = false;
		this.level = 1;
		Date startTime = new Date();
		this.currentTime = startTime;
		this.prevTime = startTime;
		this.levelTime = startTime;
		this.clearMatrix();
		if(this.view != null) {
			this.view.setLevel(this.level);
			this.view.setScore(this.score);
			this.view.setMaskVisible(this.isGameOver);
			
			//更新画面
			this.view.initFallShape();
			this.view.changeMatrixColor();
			this.view.updatePreviewShape(this.preparedShape);
		}
	}
	
	

	public void removeOneRow(int row) {
		int colCount = this.MATRIX[0].length;
		for (int i = row; i >= 0; i--) {
			for (int j = 0; j < colCount; j++) {
				if (i > 0) {
					this.MATRIX[i][j] = this.MATRIX[i - 1][j];
				} else {
					this.MATRIX[i][j] = 0;
				}
			}
		}
	}
	
	
	public void removeRows(ArrayList<Integer> rows) {		
		rows.forEach(e -> {
			removeOneRow(e);
		});
	}

	public ArrayList<Integer> checkFullRows() {
		ArrayList<Integer> rowNumbers = new ArrayList<Integer>();
		for (int i = 0; i < this.MATRIX.length; i++) {
			int[] row = this.MATRIX[i];
			boolean full = true;
			for (int j = 0; j < row.length; j++) {
				if(row[j] != 0 && full) {
					full = true;
				}else {
					full = false;
				}
			}
			if (full) {
				rowNumbers.add(i);
			}
		}
		return rowNumbers;
	}

	public void changeMatrixNodeColor(int x, int y, String color) {
		int colorIndex = Utils.findColorIndex(color);
		this.MATRIX[y][x] = colorIndex + 1;
	}

	// 初始化方块矩阵
	private int[][] initMatrix() {
		for (int i = 0; i < Constants.ROW_COUNT; i++) {
			for (int j = 0; j < Constants.COLUMN_COUNT; j++) {
				MATRIX[i][j] = 0;
			}
		}
		return MATRIX;
	}

	// 清空方块矩阵
	private void clearMatrix() {
		for (int i = 0; i < MATRIX.length; i++) {
			for (int j = 0; j < MATRIX[i].length; j++) {
				MATRIX[i][j] = 0;
			}
		}
	}
	
	private int calScore(ArrayList<Integer> rows) {
		if(rows.size() > 0) {
			return rows.size() * 100;
		}
		return 0;
	}
	
	private int calcRewards(ArrayList<Integer> rows) {
		if(rows.size() > 0) {
			return (int) (Math.pow(2, rows.size() - 1) * 100);
		}
		return 0;
	}

}
