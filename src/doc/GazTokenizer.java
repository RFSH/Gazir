package doc;

import java.util.Collection;

public interface GazTokenizer {
	String next();
	boolean hasNext();
	Collection<String> tokenizeAll();
	void setDocument(GazDocument document);
	GazDocument getDocument(); 
}
