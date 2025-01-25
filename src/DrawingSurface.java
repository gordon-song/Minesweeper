
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import processing.core.PApplet;

public class DrawingSurface extends PApplet {

	private Minesweeper board;

	public DrawingSurface() {
		// Consider using the file reading code for testing purposes. Sometimes, it's
		// nice to run the program and have some grid contents already present that you
		// can
		// mess with to see if things are working.
		board = new Minesweeper(10, 10, 15);

	}

	public void settings() {
		setSize(800, 600);
	}

	// The statements in the setup() function
	// execute once when the program begins
	public void setup() {

	}

	// The statements in draw() are executed until the
	// program is stopped. Each statement is executed in
	// sequence and after the last line is read, the first
	// line is executed again.
	public void draw() {
		background(255); // Clear the screen with a white background
		fill(0);
		textAlign(LEFT);
		textSize(12);

		text("Left click to open cells \nRight click to place a flag \nAvoid bombs", height + 20, 70);
		text("If three flags are \nplaced incorrectly you lose \n", height + 20, 190);
		text("Can only place as many flags \nas the amount of bombs", height + 20, 140);
		text("Flags left to place:\n" + board.getFlagsLeft(), height + 20, 400);
		board.draw(this, 0, 0, height, height);
		if (board.isGameOver()) {
			textSize(60);
			if (board.getHiddenCount() == board.getMineCount()) {
				text("WIN", height + 20, 300);
			} else {
				text("LOSE", height + 20, 300);
			}
		}
	}

	public void mousePressed() {
		if (mouseButton == LEFT) {
			Point click = new Point(mouseX, mouseY);
			float dimension = height;
			Point cellCoord = board.clickToIndex(click, 0, 0, dimension, dimension);
			if (!board.isGameOver()) {
				if (cellCoord != null) {
					board.reveal(cellCoord.x, cellCoord.y);
				}
			}

		}
		if (mouseButton == RIGHT) {
			Point click = new Point(mouseX, mouseY);
			float dimension = height;
			Point cellCoord = board.clickToIndex(click, 0, 0, dimension, dimension);
			if (!board.isGameOver()) {
				if (cellCoord != null) {
					board.plantFlag(cellCoord.x, cellCoord.y);
				}
			}
		}
	}

	// In general, it's better to use mouse interactions for interacting with the
	// grid (like placing a piece).
	// Key interactions can be used if it really makes sense, such as for uncommon
	// tasks (like resetting the game).
	// If you'd like to do basic gameplay interaction via keyboard, consider asking
	// Mr Shelby about it.
	public void keyPressed() {
		if (keyCode == 'R') {
			
		}
	}

}
