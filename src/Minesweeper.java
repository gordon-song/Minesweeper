import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import processing.core.PApplet;

/**
 * 
 * Represents a Minesweeper grid.
 * 
 * 
 */

public class Minesweeper {


	private int[][] grid;
	private boolean[][] revealed;
	private int[][] mineLocations;
	private int flags;
	private int maxFlags;
	private int wrongFlags;
	private int hiddenCount;
	private boolean gameOver;

	/**
	 * Initialized the Game of Life grid to an empty 20x20 grid.
	 */
	public Minesweeper(int width, int height, int mines) {
		grid = new int[height][width];
		mineLocations = new int[mines][2];
		int count = 0;
		while (count < mines) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);
			if (grid[y][x] != -1) {
				mineLocations[count][0] = y;
				mineLocations[count][1] = x;
				grid[y][x] = -1;
				count++;
			}
		}
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] != -1) {
					grid[i][j] = countNeighbors(i, j);
				}
			}
		}

		maxFlags = mines;
		flags = 0;
		wrongFlags = 0;
		hiddenCount = height * width;
		gameOver = false;
		revealed = new boolean[height][width];

	}

	/**
	 * Initializes the Game of Life grid to a 20x20 grid and fills it with data from
	 * the file given.
	 * 
	 * @param filename The path to the text file.
	 */
	public Minesweeper(String filename) {

		grid = new int[10][10];
		readData(filename, grid);

	}

	public int countNeighbors(int i, int j) {
		int count = 0;

		if (grid[i][j] == -1) {
			return -1;
		}

		for (int y = i - 1; y < i + 2; y++) {
			for (int x = j - 1; x < j + 2; x++) {
				if (x < 0 || x == grid[0].length || y < 0 || y == grid.length) {

				} else if (grid[y][x] == -1) {
					count++;
				}
			}
		}

		return count;
	}

	public void reveal(int i, int j) {
		if (!revealed[i][j]) {
			revealed[i][j] = true;
			if (grid[i][j] == 0) {
				for (int y = i - 1; y < i + 2; y++) {
					for (int x = j - 1; x < j + 2; x++) {
						if (x < 0 || x == grid[0].length || y < 0 || y == grid.length) {

						} else if (!revealed[y][x]) {
							reveal(y, x);
						}
					}
				}
			}
			if (grid[i][j] == -1) {
				gameOver();
				for (int count = 0; count < maxFlags; count++) {
					grid[mineLocations[count][0]][mineLocations[count][1]] = -1;
					reveal(mineLocations[count][0], mineLocations[count][1]);
				}
			}
			if (!gameOver) {
				hiddenCount--;
			}
		}

		if (hiddenCount == maxFlags && !gameOver) {
			gameOver();
		}
	}

	public int getFlagsLeft() {
		return maxFlags - flags;
	}

	public int getHiddenCount() {
		return hiddenCount;
	}

	public int getMineCount() {
		return maxFlags;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void gameOver() {
		gameOver = true;
	}

	public void plantFlag(int i, int j) {
		if (grid[i][j] == -2) {
			grid[i][j] = countNeighbors(i, j);
			revealed[i][j] = false;
			boolean isBomb = false;
			for (int count = 0; count < maxFlags; count++) {
				if (mineLocations[count][0] == i && mineLocations[count][1] == j) {
					grid[i][j] = -1;
					isBomb = true;
				}
			}
			if (!isBomb) {
				wrongFlags--;
			}
			flags--;
		} else if (!revealed[i][j] && flags < maxFlags) {
			grid[i][j] = -2;
			revealed[i][j] = true;
			boolean isBomb = false;
			for (int count = 0; count < maxFlags; count++) {
				if (mineLocations[count][0] == i && mineLocations[count][1] == j) {
					isBomb = true;
				}
			}
			if (!isBomb) {
				wrongFlags++;
			}
			if (wrongFlags > 2) {
				gameOver();
			}
			flags++;
		}

	}

	/**
	 * Formats this Life grid as a String to be printed (one call to this method
	 * returns the whole multi-line grid)
	 * 
	 * @return The grid formatted as a String.
	 */
	public String toString() {
		String string = "";
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				string += grid[i][j];
			}
			string += "\n";
		}
		return string;
	}

	/**
	 * (Graphical UI) Draws the grid on a PApplet. The specific way the grid is
	 * depicted is up to the coder.
	 * 
	 * @param marker The PApplet used for drawing.
	 * @param x      The x pixel coordinate of the upper left corner of the grid
	 *               drawing.
	 * @param y      The y pixel coordinate of the upper left corner of the grid
	 *               drawing.
	 * @param width  The pixel width of the grid drawing.
	 * @param height The pixel height of the grid drawing.
	 */
	public void draw(PApplet marker, float x, float y, float width, float height) {
		float rw = width / grid[0].length;
		float rh = height / grid.length;

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {

				float rx = rw * j + x;
				float ry = rh * i + y;

				marker.fill(255);
				marker.rect(rx, ry, rw, rh);
				marker.fill(200, 200, 50);

				if (revealed[i][j]) {
					marker.textSize(20);
					if (grid[i][j] == -1) {
						marker.fill(200, 50, 50);
						marker.rect(rx, ry, rw, rh);
					} else if (grid[i][j] != -1 && grid[i][j] != -2 && grid[i][j] != 0) {
						int col = grid[i][j];
						marker.fill(col * 3, col * 31, 100);
						marker.text(grid[i][j], rx + (rw / 2) - 4, ry + (rh / 2) + 3);
					} else if (grid[i][j] == 0) {
						marker.fill(100, 220, 100);
						marker.rect(rx, ry, rw, rh);
					} else if (grid[i][j] == -2) {
						marker.fill(255, 102, 0);
						marker.text('?', rx + (rw / 2) - 4, ry + (rh / 2) + 3);
					} else {
						marker.fill(255);
					}
				}
			}
		}
	}

	/**
	 * (Graphical UI) Determines which element of the grid matches with a particular
	 * pixel coordinate. This supports interaction with the grid using mouse clicks
	 * in the window.
	 * 
	 * @param p      A Point object containing a graphical pixel coordinate.
	 * @param x      The x pixel coordinate of the upper left corner of the grid
	 *               drawing.
	 * @param y      The y pixel coordinate of the upper left corner of the grid
	 *               drawing.
	 * @param width  The pixel width of the grid drawing.
	 * @param height The pixel height of the grid drawing.
	 * @return A Point object representing a coordinate within the game of life
	 *         grid.
	 */
	public Point clickToIndex(Point p, float x, float y, float width, float height) {
		if (p.x <= x || p.x >= x + width || p.y <= y || p.y >= y + height) {
			return null;
		}
		int y1 = (int) (p.x * (grid[0].length - x) / width);
		int x1 = (int) (p.y * (grid.length - y) / height);
		return new Point(x1, y1);
	}

	/**
	 * (Graphical UI) Toggles a cell in the game of life grid between alive and
	 * dead. This allows the user to modify the grid.
	 * 
	 * @param i The x coordinate of the cell in the grid.
	 * @param j The y coordinate of the cell in the grid.
	 */
	public void setCell(int i, int j, char c) {
		grid[i][j] = c;
	}

	// Reads in array data from a simple text file containing asterisks (*)
	public void readData(String filename, int[][] gameData) {
		File dataFile = new File(filename);

		if (dataFile.exists()) {
			int count = 0;

			FileReader reader = null;
			Scanner in = null;
			try {
				reader = new FileReader(dataFile);
				in = new Scanner(reader);

				while (in.hasNext()) {
					String line = in.nextLine();
					for (int i = 0; i < line.length(); i++)
						if (count < gameData.length && i < gameData[count].length)
							gameData[count][i] = line.charAt(i);

					count++;
				}
			} catch (IOException ex) {
				throw new IllegalArgumentException("Data file " + filename + " cannot be read.");
			} finally {
				if (in != null)
					in.close();
			}

		} else {
			throw new IllegalArgumentException("Data file " + filename + " does not exist.");
		}
	}

}