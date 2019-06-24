package nz.ac.arastudent.eyeballmazeassignment2.model;

public interface IGame {
	Integer getRowCount();
	Integer getcolumnCount();
	void restartMaze();
	String makeMove(String move, int space);
	Object[] whatsAt(int x, int y);
	void updateMove();
	void updateMaze();
	void showNextMove();
	//void start();
	void clearConsole();
	boolean isComplete();
	String getItem(int x, int y);
	Integer[] getPlayerLocation();
    String getGoalCount();
    String getMoveCount();
    void setMazeCharacter(int x, int y, String item);
}
