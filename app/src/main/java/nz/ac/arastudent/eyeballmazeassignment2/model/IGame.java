package nz.ac.arastudent.eyeballmazeassignment2.model;

public interface IGame {
	public Integer getRowCount();
	public Integer getcolumnCount();
	public void restartMaze();
	public void makeMove(String move);
	public Object[] whatsAt(int x, int y);
	public void updateMove();
	public void showNextMove();
	public void start();
	public void clearConsole();
	public boolean isComplete();
	public String getItem(int x, int y);
	public Integer[] getPlayerLocation();
}
