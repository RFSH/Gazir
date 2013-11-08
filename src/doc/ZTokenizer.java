package doc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ZTokenizer implements GazTokenizer {
	private GazDocument document;
	String nextLine;
	StringTokenizer tokenizer;
	
	public ZTokenizer(GazDocument document){
		this.setDocument(document);
	}
	
	public GazDocument getDocument() {
		return this.document;
	}
	
	public boolean hasNext() {
		return nextLine != null;
	}
	
	public String next() {
		if(!tokenizer.hasMoreTokens()){
			nextLine = document.readLine();
			if(!hasNext())
				return "";
			tokenizer = new StringTokenizer(nextLine);
		}
		return tokenizer.nextToken();
	}
	
	public void setDocument(GazDocument document){
		this.document = document;
		document.reset();
		nextLine = document.readLine();
		if(nextLine != null)
			tokenizer = new StringTokenizer(nextLine);
	}

	
	public Collection<String> tokenizeAll() {
		ArrayList<String> list = new ArrayList<String>();
		while(hasNext()){
			list.add(next());
		}
		return list;
	}
}
