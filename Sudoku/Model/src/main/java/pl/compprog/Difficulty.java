package pl.compprog;

import java.util.Random;

public abstract class Difficulty {
	static Random generator = new Random();
	static final int MAX = 8;
	static final int MIN = 0;
	int numbersToDeleteCount;
	
	public void prepareBoard(SudokuBoard board) {
		for (int i = 0; i < numbersToDeleteCount; i++) {
			int num1 = generator.nextInt(MAX - MIN + 1) + MIN;
			int num2 = generator.nextInt(MAX - MIN + 1) + MIN;
			board.set(num1, num2, 0);
		}
	}
}
