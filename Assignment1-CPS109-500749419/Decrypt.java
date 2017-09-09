import java.util.* ;
import java.io.*;


public class Decrypt
{
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner in = new Scanner(System.in);
		
		String key = args[0] ; //Get Key string
		String input_name = args[1]; //Get file path
		
		fileWriter(stringDecryption(fileReader(input_name),key),"output.txt");

	}
	public static StringBuffer subPattern(String key)
	{
		/*
		 Creates a substitution pattern using a key.
		 subPattern, returns a StringBuffer object allowing the string to be changed. 
		 All characters remain capitalized.
		 */
		Random seed = new Random(key.hashCode()); // Creates a Random object named seed using the given key's hashcode.
		StringBuffer alpha = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ "); //Alphabet to be turned into a substituion pattern.
		
		for (int i = 0 ; i < 100; i++)
		{
			int randPos1 = seed.nextInt(27); //Generates random integer between 0 and 27 using the seed.
			int randPos2 = seed.nextInt(27); //Generates second random integer ""  ""  "" ""
			
			char pos1 = alpha.charAt(randPos1); //Gets the position of the first character to be switched using the first random integer generated
			char pos2 = alpha.charAt(randPos2); //Gets the position of the second character to be switched
			
            alpha.setCharAt(randPos1, pos2); //Using the random positions switches the two values in those positions in the alphabet
			alpha.setCharAt(randPos2, pos1); 
		}
		return alpha  ;
	} 
	public static int[] columnOrder(String key)
	{
		/*
		Creates a length 8 integer array representing the order in which the 
		transposition columns are ordered, using the key value.
		 */
		Random seed = new Random(key.hashCode());
		int[] columnOrder = {0,1,2,3,4,5,6,7};
		for (int i = 0 ; i < 100; i++)
		{
		int randPos1 = seed.nextInt(8); //Gets first switch position
		int randPos2 = seed.nextInt(8); //Gets second switch position
		
		int element1 = columnOrder[randPos1]; //Gets the element at first position
		int element2 = columnOrder[randPos2]; //Gets element at second position
		
		columnOrder[randPos1] = element2; //Switches both elements
		columnOrder[randPos2] = element1;
		
			
		}
		return columnOrder;
		
	}
	
	public static String fileReader(String file_name) throws FileNotFoundException
	{
		/*
		 File reading method which returns a long String divisable by 64.
         */
        //String read = "";
        
		File txt = new File(file_name);
        Scanner in = new Scanner(txt);
        String read = "";
        
		while (in.hasNextLine())
        {
			read += (in.nextLine()).replace(",","").replace(".",""); //Reads the line and replaces common irrelevant characters.
            
        }
		
		int runOff = (64-read.length()%64); //Since we cannot assume every file can be evenly read in 64 "chunks" at a time, we have to obtain extra "chars".
		
		for (int i = 0 ; i < runOff ; i++ )//Adds runOff characters in order to maintain 8x8 transposition integrity.
		{
			read += '|'; 
		}
		
		return read ;
	}
    public static String revSubstitution(String input, StringBuffer pattern)
    {
        /*
		Performs a reverse substituion on the String using a key
		*/
        String alpha = "abcdefghijklmnopqrstuvwxyz ";
        input = input.toUpperCase();
        input = input.trim();
        
		for (int index = 0 ; index < alpha.length() ; index++)
        {
            char replace = Character.toLowerCase(alpha.charAt(index)); //Get replace character at the index in the alphabet
            input = input.replace(pattern.charAt(index),replace); //Replace's the character in input at index with the replace character
        }
		return input.toUpperCase() ;
    }
	public static String stringDecryption(String file,String key) 
	{
		/*
		Takes a really long String of the file from fileReader and turns it into a decrypted string
		*/
		StringBuffer decrypt = new StringBuffer(); //New StringBuffer object for the decrypted string

		final int originalLength = file.replace("|","").length(); //Find the original number of characters without runOff
		final int num64 = (file.length()/64); //Find the number of 64 character chunks
		final int runOff = (originalLength % 64); //Find the number of characters you have to cutoff at the end
		int nlineCount = 0;

		for (int i = 0 ; i < num64 ; i++ ) //Iterate for the number of 64 chunks
		{
			String currLine = file.substring(0,64); //Take the first 64 char chunk
			file = file.substring(64,file.length()); //Remove the 64 char chunk from the original string
			String[] lineArr = new String[8]; //Create an Array of 8 character rows for the transposition
			int[] columns = columnOrder(key); //Create the columnOrder
			
			for (int x = 0 ; x < 8; x++) //Iterate 8 times to add 8 rows to the line array using the columOrder.
			{
				lineArr[columns[x]] = currLine.substring(0,8); //Add the columns to the array by the order we read them.
				currLine = currLine.substring(8); //Cut off the first 8 characters in the 64 chunk
			}
			
			
			String revTranspose = ""; //Intialize the String where the reverseTransposed 64 char chunk goes
			
			for (int row = 0 ; row < 8 ; row++) 
			{
				for (int col = 0 ; col < 8 ; col++)
				{
					revTranspose += lineArr[col].charAt(row); //Add all the characters at position row in the array at position col  to String revTranspose
				}
				revTranspose = revTranspose.replace("|",""); //Replace the extra characters needed for uneven file lengths
				decrypt.append(revTranspose); //Append the 64 character chunk to the StringBuffer
				revTranspose = ""; //Reset the Transpose
			}
		
		decrypt.append("\n"); //Add a newline character after every 64 char
		nlineCount += 1; //Count each newline character added
		} 
		
		decrypt.delete(((num64-1)*64)+runOff+nlineCount,decrypt.length()); //Delete all extra characters starting from the end of the runOff+number of escape characters.
		
		String decryption = decrypt.toString(); //Turn the String Buffer into a string
		decryption.trim(); //Trim unnecessary white space
		return revSubstitution(decryption,subPattern(key)); //Return the Return value from the String after reverse Substitution using the subPattern with the key
	}
	
	public static void fileWriter(String toWrite , String name)
	/*
	Takes two strings as arguments, one for the contents of the file and the second for the file name.
	*/
	{
		try
		{
		PrintWriter fileName = new PrintWriter(name,"UTF-8");
		fileName.println(toWrite);
		fileName.close();
		}
		catch (IOException e)
		{
			System.out.println("Invalid File");
		}
		
	}

}
