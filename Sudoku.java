import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 * Sudoku represents a Sudoku puzzle
 * @author Evan Norsworthy
 */
public class Sudoku {
	private SudokuTile[][] map = new SudokuTile[9][9];
	private JTextField[][] textFields = new JTextField[9][9];
	private int timer = 0;
	private final int TIMEOUT = 10000;
	private Stack<SudokuTile[][]> original;
	
	/**
	 * Creates a Sudoku puzzle from an input array
	 * @param digits
	 */
	public Sudoku(SudokuTile[][] map) {
		this.map = cloneMap(map);
	}
	
	/**
	 * Opens up a GUI that allows the user to enter a Sudoku puzzle
	 */
	public Sudoku() {
		original = new Stack<SudokuTile[][]>();
		JFrame frame = new JFrame("Norsworthy Sudoku Puzzle Solver");
		GridLayout mainLayout = new GridLayout(2,1);
		GridLayout layout = new GridLayout(9,9);
		JPanel panel = new JPanel(layout);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(mainLayout);
		panel.setLayout(layout);
		GridLayout controlLayout = new GridLayout(1,0);
		JPanel controlPanel = new JPanel(controlLayout);
		JButton startButton = new JButton("Solve");
		Font font = new Font("Verdana", Font.BOLD, 25);
		startButton.setFont(font);
		Dimension buttonDimension = new Dimension(50,50);
		startButton.setPreferredSize(buttonDimension);
		startButton.addActionListener(new PuzzleListener());
		controlPanel.add(startButton);
		startButton.setVisible(true);
		JButton resetButton = new JButton("Reset");
		resetButton.setPreferredSize(buttonDimension);
		resetButton.addActionListener(new ResetListener());
		resetButton.setFont(font);
		controlPanel.add(resetButton);
		controlPanel.setLayout(new GridLayout(2,1));
		
		Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY);
		CompoundBorder topBorder = new CompoundBorder(
				BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK),
				BorderFactory.createMatteBorder(0, 1, 1, 1, Color.GRAY));
		CompoundBorder leftBorder = new CompoundBorder(
			BorderFactory.createMatteBorder(0, 2, 0, 0, Color.BLACK),
			BorderFactory.createMatteBorder(1, 0, 1, 1, Color.GRAY));
		CompoundBorder bottomBorder = new CompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK),
			BorderFactory.createMatteBorder(1, 1, 0, 1, Color.GRAY));
		CompoundBorder rightBorder = new CompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK),
				BorderFactory.createMatteBorder(1, 1, 1, 0, Color.GRAY));
		CompoundBorder topLeftBorder = new CompoundBorder(
				BorderFactory.createMatteBorder(2, 2, 0, 0, Color.BLACK),
				BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));
		CompoundBorder topRightBorder = new CompoundBorder(
				BorderFactory.createMatteBorder(2, 0, 0, 2, Color.BLACK),
				BorderFactory.createMatteBorder(0, 1, 1, 0, Color.GRAY));
		CompoundBorder bottomLeftBorder = new CompoundBorder(
				BorderFactory.createMatteBorder(0, 2, 2, 0, Color.BLACK),
				BorderFactory.createMatteBorder(1, 0, 0, 1, Color.GRAY));
		CompoundBorder bottomRightBorder = new CompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 2, 2, Color.BLACK),
				BorderFactory.createMatteBorder(1, 1, 0, 0, Color.GRAY));
		Dimension dimension = new Dimension(35,35);
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				JTextField field = new JTextField();
				field.setPreferredSize(dimension);
				field.setFont(font);
				field.setHorizontalAlignment(JTextField.CENTER);
				if(i%3 == 0) {
					if(j%3 == 0) {
						field.setBorder(topLeftBorder);
					}
					else if(j%3 == 1) {
						field.setBorder(topBorder);
					}
					else if(j%3 == 2) {
						field.setBorder(topRightBorder);
					}
				}
				else if(i%3 == 1) {
					if(j%3 == 0) {
						field.setBorder(leftBorder);
					}
					else if(j%3 == 1) {
						field.setBorder(border);
					}
					else if(j%3 == 2) {
						field.setBorder(rightBorder);
					}
				}
				else if(i%3 == 2) {
					if(j%3 == 0) {
						field.setBorder(bottomLeftBorder);
					}
					else if(j%3 == 1) {
						field.setBorder(bottomBorder);
					}
					else if(j%3 == 2) {
						field.setBorder(bottomRightBorder);
					}
				}
				textFields[i][j] = field;
				field.getBorder();
				panel.add(textFields[i][j]);
			}
		}
		mainPanel.add(panel);
		mainPanel.add(controlPanel);
		frame.add(mainPanel);
		panel.setVisible(true);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Resets the puzzle to it's last value before "Solve" was pressed
	 */
	private class ResetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(original.empty()) {
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < 9; j++) {
						map[i][j] = new SudokuTile();
					}
				}
			}
			else {
				map = cloneMap(original.pop());
			}
			writeSolution();
		}
		
		private void writeSolution() {
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					if(map[i][j].value == 0) {
						textFields[i][j].setText("");
					}
					else {
						textFields[i][j].setText(((Integer)map[i][j].value).toString());
					}
				}
			}
		}
	}
	
	/**
	 * Reads in user input and outputs the found solution
	 */
	private class PuzzleListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			readInput();
			solvePuzzle();
			writeSolution();
		}
		
		private void writeSolution() {
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					if(map[i][j].value == 0) {
						textFields[i][j].setText("");
					}
					else {
						textFields[i][j].setText(((Integer)map[i][j].value).toString());
					}
				}
			}
		}

		private void readInput() throws NumberFormatException {
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					try {
						Integer.decode(textFields[i][j].getText());
					}
					catch (NumberFormatException e) {
						System.out.println("Invalid Input");
					}
					if(textFields[i][j].getText().equals("")) {
						map[i][j] = new SudokuTile();
					}
					else if(Integer.decode(textFields[i][j].getText()) < 1 || Integer.decode(textFields[i][j].getText()) > 9){
						map[i][j] = new SudokuTile();
					}
					else{
						map[i][j] = new SudokuTile(Integer.decode(textFields[i][j].getText()));
					}
				}
			}
			original.push(cloneMap(map));
		}
	}
	
	/**
	 * Attempts to solve the puzzle
	 */
	public void solvePuzzle() {
		solve();
		if(timer > TIMEOUT) {
			System.out.println("Solution attempt\ntimed out");
			timer = 0;
		}
		else if(checkContradiction()) {
			System.out.println("No solution\nexists");
		}
		else if(!solved()) {
			map = guess(map);
		}
		
		else {
			System.out.println("Solution attempt\ncomplete");
		}
	}
	
	public void solve() {
		boolean progress = true;
		while(!checkContradiction() && !solved() && progress){
			progress = false;
			progress |= basicScanRows();
			progress |= basicScanColumns();
			progress |= basicScanBlocks();
			progress |= singleEliminationRow();
			progress |= singleEliminationColumn();
			progress |= singleEliminationBlock();
			progress |= checkPossibles();
		}
	}
	
	public SudokuTile[][] guess(SudokuTile[][] map) {
		timer++;
		if(timer > TIMEOUT) {
			return map;
		}
		Sudoku sudoku = new Sudoku(cloneMap(map));
		Sudoku checkSudoku;
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(sudoku.map[i][j].size() > 0) {
					while(sudoku.map[i][j].size() > 0) {
						Sudoku newSudoku = new Sudoku(cloneMap(sudoku.map));
						newSudoku.map[i][j].setValue(sudoku.map[i][j].possibles.remove(0));
						newSudoku.solve();
						if(newSudoku.solved()) {
							return cloneMap(newSudoku.map);
						}
						else if(!newSudoku.checkContradiction()) {
							checkSudoku = new Sudoku(guess(cloneMap(newSudoku.map)));
							if(checkSudoku.solved()) {
								return cloneMap(checkSudoku.map);
							}
						}
						else {
							//do nothing
						}
					}
					return map;
				}
			}
		}
		return null;
	}
	
	public static SudokuTile[][] cloneMap(SudokuTile[][] map) {
		SudokuTile[][] newMap = new SudokuTile[9][9];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				newMap[i][j] = map[i][j].clone();
			}
		}
		return newMap;
	}
	
	/**
	 * Checks to see if the puzzle has been solved
	 * @return true if the puzzle has been solved
	 */
	public boolean solved() {
		boolean solved = true;
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(map[i][j].value == 0){
					solved = false;
				}
			}
		}
		if(checkContradiction()) {
			solved = false;
		}
		return solved;
	}
	
	public boolean checkPossibles() {
		boolean change = false;
		for(SudokuTile[] array : map) {
			for(SudokuTile tile : array) {
				if(tile.size() == 1) {
					tile.setValue(tile.possibles.get(0));
					change = true;
				}
			}
		}
		return change;
	}
	
	/**
	 * Updates the possible values for each cell based on its row
	 * @return whether the map was updated
	 */
	public boolean basicScanRows() {
		boolean change = false;
		ArrayList<Integer> possibilities;
		for(int i = 0; i < 9; i++) {
			possibilities = generateArrayList(9);
			for(int j = 0; j < 9; j++) {
				if(map[i][j].value != 0) {
					possibilities.remove((Integer)map[i][j].value);
				}
			}
			for(int j = 0; j < 9; j++){
				change |= map[i][j].mergePossibles(possibilities);
			}
		}
		return change;
	}
	
	/**
	 * Updates the possible values for each cell based on its column
	 * @return whether the map was updated
	 */
	public boolean basicScanColumns() {
		boolean change = false;
		ArrayList<Integer> possibilities;
		for(int j = 0; j < 9; j++) {
			possibilities = generateArrayList(9);
			for(int i = 0; i < 9; i++) {
				if(map[i][j].value != 0) {
					possibilities.remove((Integer)map[i][j].value);
				}
			}
			for(int i = 0; i < 9; i++){
				change |= map[i][j].mergePossibles(possibilities);
			}
		}
		return change;
	}
	
	/**
	 * Updates the possible values for each cell based on its block
	 * @return whether the map was updated
	 */
	public boolean basicScanBlocks() {
		boolean change = false;
		ArrayList<Integer> possibilities;
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 3; y++) {
				possibilities = generateArrayList(9);
				for(int i = 3*y; i < 3*y+3; i++) {
					for(int j = 3*x; j < 3*x+3; j++) {
						if(map[i][j].value != 0) {
							possibilities.remove((Integer)map[i][j].value);
						}
					}
				}
				for(int i = 3*y; i < 3*y+3; i++) {
					for(int j = 3*x; j < 3*x+3; j++) {
						change |= map[i][j].mergePossibles(possibilities);
					}
				}
			}
		}
		return change;
	}
	
	/**
	 * Fills in values that can only be put into a single cell
	 * @return true if the map is updated
	 */
	public boolean singleEliminationRow() {
		boolean change = false;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < 9; i++) {
			for(int k = 1; k < 10; k++) {
				list.clear();
				for(int j = 0; j < 9; j++) {
					if(map[i][j].possibles.contains(k)) {
						list.add(j);
					}
				}
				if(list.size() == 1) {
					change = true;
					map[i][list.get(0)].setValue(k);
				}
			}
		}
		return change;
	}
	
	/**
	 * Fills in values that can only be put into a single cell
	 * @return true if the map is updated
	 */
	public boolean singleEliminationColumn() {
		boolean change = false;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int j = 0; j < 9; j++) {
			for(int k = 1; k < 10; k++) {
				list.clear();
				for(int i = 0; i < 9; i++) {
					if(map[i][j].possibles.contains(k)) {
						list.add(i);
					}
				}
				if(list.size() == 1) {
					change = true;
					map[list.get(0)][j].setValue(k);
				}
			}
		}
		return change;
	}
	
	/**
	 * Fills in values that can only be put into a single cell
	 * @return true if the map is updated
	 */
	public boolean singleEliminationBlock() {
		boolean change = false;
		ArrayList<Integer> ilist = new ArrayList<Integer>();
		ArrayList<Integer> jlist = new ArrayList<Integer>();
		for(int x = 0; x < 3; x++) {	
			for(int y = 0; y < 3; y++) {
				for(int k = 1; k < 10; k++) {
					ilist.clear();
					jlist.clear();
					for(int j = 3*x; j < 3*x+3; j++) {
						for(int i = 3*y; i < 3*y+3; i++) {
							if(map[i][j].possibles.contains(k)) {
								ilist.add(i);
								jlist.add(j);
							}
						}
					}
					if(ilist.size() == 1) {
						change = true;
						map[ilist.get(0)][jlist.get(0)].setValue(k);
					}
				}
			}
		}
		return change;
	}
	
	/**
	 * Checks each square for a zero possible values
	 * Checks each unit for duplicate values
	 * @return true if a contradiction is found
	 */
	public boolean checkContradiction() {
		boolean contradiction = false;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < 9; i++) {
			list.clear();
			for(int j = 0; j < 9; j++) {
				if(map[i][j].size() == 0 && map[i][j].value == 0) {
					contradiction = true;
				}
				if(list.contains(map[i][j].value)) {
					contradiction = true;
				}
				else if(map[i][j].value != 0) {
					list.add(map[i][j].value);
				}
			}
		}
		for(int j = 0; j < 9; j++) {
			list.clear();
			for(int i = 0; i < 9; i++) {
				if(list.contains(map[i][j].value)) {
					contradiction = true;
				}
				else if(map[i][j].value != 0) {
					list.add(map[i][j].value);
				}
			}
		}
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 3; y++) {
				list.clear();
				for(int i = 3*y; i < 3*y+3; i++) {
					for(int j = 3*x; j < 3*x+3; j++) {
						if(list.contains(map[i][j].value)) {
							contradiction = true;
						}
						else if(map[i][j].value != 0) {
							list.add(map[i][j].value);
						}
					}
				}
			}
		}
		
		if(contradiction == true) {
		}
		return contradiction;
	}
	
	public ArrayList<Integer> generateArrayList(int length) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 1; i <= length; i++) {
			list.add(i);
		}
		return list;
	}
	
	public ArrayList<Integer>[] generateArrayListArray(int length) {
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] list = new ArrayList[length];
		for(int i = 1; i <= length; i++) {
			list[i] = new ArrayList<Integer>();
		}
		return list;
	}
	
	/**
	 * concatenates the values of all of the cells into a nice-looking output string
	 */
	public String toString() {
		String solution = "";
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(j == 8 && i == 8) {
					solution += ((Integer)map[i][j].value).toString();
				}
				else if (j == 8) {
					solution += ((Integer)map[i][j].value).toString() + "\n";
				}
				else {
					solution += ((Integer)map[i][j].value).toString() + "  ";
				}
			}
		}
		return solution;
	}
	
	public Sudoku clone() {
		Sudoku sudoku = new Sudoku();
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				sudoku.map[i][j] = map[i][j].clone();
			}
		}
		return sudoku;
	}
	
	/**
	 * SudokuTile represents a single tile in the 9x9 grid of tiles
	 */
	private class SudokuTile {
		public int value = 0;
		public ArrayList<Integer> possibles = new ArrayList<Integer>();
		
		public SudokuTile() {
			Random rand = new Random();
			ArrayList<Integer> list = generateArrayList(9);
			for(int i = 0; i < 9; i++) {
				possibles.add(list.remove(rand.nextInt(list.size())));
			}
		}
		
		public SudokuTile(int value) {
			this.value = value;
		}
		
		public int size() {
			return possibles.size();
		}
		
		public void setValue(int i) {
			int saveValue = value;
			value = i;
			if(checkContradiction()) {
				System.out.println("Invalid assignment");
				value = saveValue;
			}
			else {
				possibles.clear();
			}
		}
		
		public boolean mergePossibles(ArrayList<Integer> newPossibles) {
			boolean change = possibles.retainAll(newPossibles);
			return change;
		}
		
		public SudokuTile clone() {
			SudokuTile tile = new SudokuTile();
			tile.value = value;
			tile.possibles = arrayListClone(possibles);
			return tile;
		}
		
		public ArrayList<Integer> arrayListClone(ArrayList<Integer> arrayList) {
			ArrayList<Integer> newList = new ArrayList<Integer>();
			for(Integer i : arrayList) {
				newList.add(i);
			}
			return newList;
		}
	}
	
	public static void main(String[] args) {		
		Sudoku sudoku = new Sudoku();
	}
}