package application;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Views implements Initializable {
	
	private static final int mGridWidth = 400;
	private static final int mPreviewWidth = 150;
	@FXML
	private GridPane mShape;
	private int gridSize;
	@FXML
	private GridPane mGridPane;
	@FXML
	private GridPane mPreview;	
	private int previewGridSize;
	
	@FXML
	private Text mLevel;
	@FXML
	private Text mScore;
	
	private static Tetris curTetris = Tetris.getInstance();
	
	private Shape curShape;
	
	@FXML
	private VBox mMask;
	
	@FXML
	private Button resetBtn;
	@FXML
	private Button pauseBtn;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		mGridPane.setMinWidth(mGridWidth);
		int gridSize = mGridWidth / Constants.COLUMN_COUNT;
		int prevGridSize = mPreviewWidth / Constants.PREVIEW_COUNT;
		this.previewGridSize = prevGridSize;
		this.gridSize = gridSize;//网格大小
	    drawGrids(mGridPane,Constants.ROW_COUNT, Constants.COLUMN_COUNT,gridSize);
		drawGrids(mPreview, Constants.PREVIEW_COUNT, Constants.PREVIEW_COUNT,prevGridSize);
		initFallShape();
		setMaskVisible(false);
		
		resetBtn.setFocusTraversable(false);
		resetBtn.setOnAction(e -> {
			System.out.println("游戏重新开始");
			curTetris._reset();
			curTetris.start();
		});
		
		pauseBtn.setFocusTraversable(false);
		pauseBtn.setOnAction(e -> {
			boolean isRunning = curTetris.getIsRunning();
			if(isRunning) {
				pauseBtn.setText("继续");
				curTetris.pause(false);
			}else {
				pauseBtn.setText("暂停");
				curTetris.pause(true);
				curTetris.start();
			}
		});
		
	}
	//初始化正在下落的方块
	public void initFallShape() {
		this.curShape = curTetris.getShape();
		this.updateViews();
		mShape.getChildren().clear();//清空网格内部所有组件
		mGridPane.getChildren().clear();
	    drawGrids(mGridPane,Constants.ROW_COUNT, Constants.COLUMN_COUNT,gridSize);
	    drawShape();
	}
	public void updateViews() {
		this.updateShapePosition();
	}
	
	public void setMaskVisible(boolean visible){
		mMask.setVisible(visible);
	}
	

	
	
	//方块到底之后更新网格颜色
	public void changeMatrixColor() {
		if(curTetris != null) {
			int[][] matrix = curTetris.getMatrix();
			for(int i = 0;i<matrix.length;i++)
			{
				int[] row = matrix[i];
				for(int j = 0;j<row.length;j++) {
					if(row[j] != 0) {
						int colorIndex = row[j];//颜色序列
						String color = Constants.COLORS[colorIndex - 1];
						//获取网格单个组件的位置设置颜色
							int rIndex = i*(matrix[0].length) + j;
						Rectangle rectangle = (Rectangle)mGridPane.getChildren().get(rIndex);
						rectangle.setFill(Color.web(color));
					}
				}
			}
		}
	}

	public void updateShapePosition() {
		if(curShape != null) {
			mShape.setTranslateX(this.gridSize * curShape.x);
			mShape.setTranslateY(this.gridSize * (curShape.y - 1));
		}
	}

	public void updatePreviewShape(Shape previewShape){
		if(previewShape != null){
			mPreview.getChildren().clear();
			drawGrids(mPreview, Constants.PREVIEW_COUNT, Constants.PREVIEW_COUNT,this.previewGridSize);
			int[][] matrix = previewShape.matrix(previewShape.getState());
			for(int i = 0;i<matrix.length;i++){
				int[] row = matrix[i];
				for (int j = 0;j<row.length;j++)
				{
					if(row[j] == 1){
						int rIndex = i * Constants.PREVIEW_COUNT + j;
						Rectangle rectangle = (Rectangle) mPreview.getChildren().get(rIndex);
						rectangle.setFill(Color.web(previewShape.getColor()));
					}
				}
			}
		}
	}


	public void updateRotateShape() {
		if(curShape != null) {
			mShape.getChildren().clear();
			this.drawShape();
			this.updateShapePosition();
		}
	}
	//设置分数
	public void setScore(int scoreNumber) {
		mScore.setText(String.valueOf(scoreNumber));
	}
	
	public void drawPreviewShape(Shape shape) {
		if(shape != null) {
			int[][] matrix = shape.matrix(shape.getState());
			int gsize = this.previewGridSize;
			
			
		}
	}
	
	
	private void drawShape() {
		Shape shape = curTetris.getShape();
		if(shape == null) {
			return;
		}
		int[][] matrix = shape.matrix(shape.getState());
		int gsize = this.gridSize;
		for(int i = 0;i<matrix.length;i++) {
			RowConstraints rowConstraints = new RowConstraints();
			mShape.getRowConstraints().add(rowConstraints);
		}
		for(int j = 0;j<matrix[0].length;j++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			mShape.getColumnConstraints().add(columnConstraints);
		}
		for(int i = 0;i<matrix.length;i++) {
			for(int j = 0;j<matrix[i].length;j++)
			{
				int value = matrix[i][j];
				if(value == 1) {
					Rectangle rectangle = createRectangle(this.gridSize,shape.color);
					mShape.setConstraints(rectangle, j, i);
					mShape.getChildren().add(rectangle);
				}
			}
		}
		
	}
	
	
	public void setLevel(int level) {
		mLevel.setText(String.valueOf(level));
	}

	
	// 绘制网格
	private void drawGrids(GridPane gridPane,int rowCount, int colCount,int gridSize) {
		int row = rowCount;
		int col = colCount;

		for (int i = 0; i < row; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			gridPane.getRowConstraints().add(rowConstraints);
		}
		for (int j = 0; j < col; j++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			gridPane.getColumnConstraints().add(columnConstraints);
		}

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				Rectangle rectangle = createRectangle(gridSize,"#e0e0e0");
				gridPane.setConstraints(rectangle, j, i);
				gridPane.getChildren().add(rectangle);
			}
		}
	}
	
	
	
	
	
	// 绘制矩形
	private Rectangle createRectangle(int width,String color) {
		Rectangle rectangle = new Rectangle();
		rectangle.setWidth(width - 1);
		rectangle.setHeight(width - 1);
		rectangle.setStrokeWidth(1);
		rectangle.setStroke(Color.WHITE);
		rectangle.setFill(Color.web(color));
		return rectangle;
	}

	public double getMGridPaneHeight(){
		double gridPaneHeight = 0;
		for(RowConstraints row:mGridPane.getRowConstraints()){
			gridPaneHeight += row.getPercentHeight();
		}
		return gridPaneHeight;
	}
	

}
