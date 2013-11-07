package doc;

import java.util.ArrayList;
import java.util.Collection;

public class ZTokenizer implements GazTokenizer {
	private GazDocument document;
	String nextLine;
	
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
		String ret = nextLine;
		nextLine = document.readLine();
		return ret;
	}
	
	public void setDocument(GazDocument document){
		this.document = document;
		document.reset();
		nextLine = document.readLine();
	}

	
	public Collection<String> tokenizeAll() {
		ArrayList<String> list = new ArrayList<String>();
		while(hasNext()){
			list.add(next());
		}
		return list;
	}
}
