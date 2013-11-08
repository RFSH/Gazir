package doc;

import index.GazDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ZTokenizer implements GazTokenizer {
	private GazDocument document;
	String nextLine;
	StringTokenizer tokenizer;
	GazDictionary biword;
	String tempTok;
	
	public ZTokenizer(GazDocument document){
		this(document, null);
	}
	
	public ZTokenizer(GazDocument document, GazDictionary biword){
		this.setDocument(document);
		this.biword = biword;
	}
	
	public GazDocument getDocument() {
		return this.document;
	}
	
	public boolean hasNext() {
		return nextLine != null;
	}
	
	public String next_() {
		if(!tokenizer.hasMoreTokens()){
			nextLine = document.readLine();
			if(!hasNext())
				return "";
			tokenizer = new StringTokenizer(nextLine);
		}
		String s = tokenizer.nextToken().toLowerCase();
		if(!s.matches("[.@,\"]*\\w+(\\w*[.@,\"]*)*")){
			return (s != null && !s.equals(".") ? s : next_());
		}
		return s;
	}
	
	public String next(){
		if(biword == null)
			return next_();
		
		if(tempTok == null){
			tempTok = next_();
			return tempTok;
		}
		String s = next_();
		String b = tempTok + " " + s;
		tempTok = s;
		if(biword.findTerm(b) != null)
			return b;
		return s;
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
