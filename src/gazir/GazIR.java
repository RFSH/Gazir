package gazir;

import index.GazIndexManager;
import index.GazPosting;
import index.GazTerm;

import java.util.Collection;

import com.sun.tools.javac.util.List;

import doc.GazCollection;
import doc.GazDocument;

public interface GazIR {
//	void initialize();
	
	void addCollection(GazCollection collection);
	void addDocument(GazDocument document);
	void switchCollection(GazCollection collection);
	
	
	Collection<GazDocument> query(String query, int queryType, int maxResults);
	Collection<GazDocument> query(String query, int queryType);
	Collection<GazDocument> query(String query);
	
	GazCollection getCurrentCollection();
	Collection<GazCollection> getCollections();
	GazIndexManager getIndexManager();
}
