package index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ZTokenProcessor implements GazTokenProcessor{
	private static Set<String> stopwordSet;
	static{
		stopwordSet = new HashSet<String>();
	}
	
	public static Collection<String> getStopWords(){
		return stopwordSet;
	}
	
	public static void loadStopWords(File file){
		try {
			int count = 0;
			Scanner scanner = new Scanner(new FileInputStream(file));
			while(scanner.hasNextLine()){
				String word = scanner.nextLine().trim();
				if(word.isEmpty())
					continue;
				word = word.toLowerCase();
				stopwordSet.add(word);
				count++;
			}
			System.out.println(count + " words added to stop word set");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String processToken(String token) {
		if(stopwordSet.contains(token))
			return null;
		return token;
	}
}
