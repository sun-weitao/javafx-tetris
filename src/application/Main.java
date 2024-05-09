package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	private static Tetris tetris = null;

	@Override
	public void start(Stage primaryStage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			// 加载CSS
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// 必须使用loader.getController才能获得FXML的实例
			Views view = loader.getController();
			tetris.setView(view);
			tetris.start();
			// 按钮判断有没有按下去
			scene.setOnKeyPressed(event -> {
				KeyCode keyCode = event.getCode();
				switch (keyCode) {
				case LEFT:// 向左
					Platform.runLater(() -> {
						tetris.getShape().goLeft(tetris.getMatrix());
						view.updateShapePosition();
					});
					break;
				case RIGHT:// 向右
					Platform.runLater(() -> {
						tetris.getShape().goRight(tetris.getMatrix());
						view.updateShapePosition();
					});
					break;
				case UP:// 旋转
					Platform.runLater(() -> {
						tetris.getShape().rotate(tetris.getMatrix());
						view.updateRotateShape();
					});
					break;
				case SPACE:// 空格下落到底
					Platform.runLater(() -> {
						if (!tetris.isGameOver()) {
							tetris.getShape().goBottom(tetris.getMatrix());
							tetris._update();
						}
					});
					break;
				}

			});
			//fix bug java.io.FileNotFoundException
			Image icon = new Image(Main.class.getResourceAsStream("icon.png"));
			primaryStage.getIcons().add(icon);
			// 禁止放大缩小
			primaryStage.setResizable(false);
			primaryStage.setTitle("俄罗斯方块");
			primaryStage.setScene(scene);
			primaryStage.show();

			primaryStage.setOnCloseRequest(event -> {
				System.out.println("窗口关闭了，游戏结束了。");
				tetris.setGameOver(true);
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		tetris = Tetris.getInstance();
		launch(args);
	}
}
