package application;

public class Constants {
	private Constants() {

	}
	
	public final static String[] COLORS = { "#00af9d", "#ffb652", "#cd66cc", "#66bc29", "#0096db", "#3a7dda",
			"#ffe100" };
	public final static int SIDE_WIDTH = 120;
	// 游戏区域 10 列
	public final static int COLUMN_COUNT = 10;
	// 游戏区域 20 行
	public final static int ROW_COUNT = 20;

	public final static int PREVIEW_COUNT = 4;
	// 默认游戏速度
	public final static int DEFAULT_INTERVAL = 600;
	// 等级提高后速度提升
	public final static int LEVEL_INTERVAL = 120 * 1000;

}
