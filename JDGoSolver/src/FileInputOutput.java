import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileInputOutput {


	int[] targetCoords=new int[2];
	int size=19;
	char[][] loadedBoard= new char[size][size];
	public FileInputOutput() {

	}
	//method to load a board
	public boolean loadBoard(String in) {
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(in));

			int i=0;
			//String line = reader.readLine();
			while (i<size)
			{
				String line = reader.readLine();
				for (int j=0; j<size; j++) {
					loadedBoard[i][j]=line.charAt(j);
				}

				i++;
			}



			//read the target capture
			int captureCoords[] = new int[2]; 
			String line=reader.readLine();
			captureCoords[0] = Integer.parseInt(line);
			line=reader.readLine();
			captureCoords[1] = Integer.parseInt(line);
			targetCoords[0]= captureCoords[0];
			targetCoords[1]=captureCoords[1];
			System.out.println("boardloaded");		
			return true;
		} catch (IOException e)
		{
			return false;


		}
	}
	//return the loaded board
	public char[][] getLoadedBoard(){
		return loadedBoard;
	}
    //return the target
	public int[] getTargetCoords() {
		return targetCoords;
	}

	//save a board
	public void outputFile(char[][] sBoard, int[] sTar, String fName) throws IOException  
	{
		String substring = fName.substring(Math.max(fName.length() - 4, 0));
		if (!substring.equals(".txt")){
			fName=fName+".txt";
		}
		
		
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter("Custom Saved Boards/"+fName));
		for (int i = 0; i <19; i++) {
			for(int j = 0; j<19; j++) {

				outputWriter.write(sBoard[i][j]);
			}
			outputWriter.newLine();
		}
		outputWriter.write(Integer.toString(sTar[0]));
		outputWriter.newLine();
		outputWriter.write(Integer.toString(sTar[1]));
		//outputWriter.flush();  
		outputWriter.close();  
	}
}

