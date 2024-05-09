package application;

public class ShapeI extends Shape{
	public ShapeI() {
		this.states = new int[2][4][4];
		this.states[0] = new int[][] {
			{1,0,0,0},
			{1,0,0,0},
			{1,0,0,0},
			{1,0,0,0}
		};
		this.states[1] = new int[][] {
			{1,1,1,1},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0}
		};
		
		this.x = 5;
		this.y = -4;		
	}
}
