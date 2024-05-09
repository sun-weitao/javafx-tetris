package application;

public class ShapeZR extends Shape{
	public ShapeZR() {
		this.states = new int[2][3][3];
		this.states[0] = new int[][]{
			{0,1,1},
			{1,1,0},
			{0,0,0}
		};
		this.states[1] = new int[][] {
			{1,0,0},
			{1,1,0},
			{0,1,0}
		};
		this.x = 4;
		this.y = -2;
	}
}
