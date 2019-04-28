package pl.compprog;

public class DifficultyNormal implements Difficulty {
	@Override
	public void prepareBoard(SudokuBoard board) {
		for(int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU; i++) {
			board.set(1, i, 0);
			board.set(2, i, 0);
			board.set(4, i, 0);
			board.set(5, i, 0);
			board.set(7, i, 0);
			board.set(8, i, 0);
		}
	}
}
