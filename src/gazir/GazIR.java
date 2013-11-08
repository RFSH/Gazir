package gazir;

import index.GazIndexManager;

import java.util.ArrayList;
import java.util.Collection;

import doc.GazCollection;
import doc.GazDocument;

public interface GazIR {
	
	void addCollection(GazCollection collection);
	void switchCollection(GazCollection collection);
	
	
	Collection<GazDocument> query(String query, int queryType, int maxResults);
	Collection<GazDocument> query(String query, int queryType);
	Collection<GazDocument> query(String query);
	
	GazCollection getCurrentCollection();
	ArrayList<GazCollection> getCollections();
	GazIndexManager getIndexManager();

}
