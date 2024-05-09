package application;

public class ShapeO extends Shape{
	public ShapeO() {
		this.states = new int[1][2][2];

		// TODO Auto-generated constructor stub
		this.states[0] = new int[][] {
			{1,1},
			{1,1}
		};
		
		this.x = 4;
		this.y = -2;
	}
}
