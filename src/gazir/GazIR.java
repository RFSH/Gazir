package gazir;

import index.GazBiword;
import index.GazDictionary;
import index.GazIndexManager;

import java.util.ArrayList;
import java.util.List;

import doc.GazCollection;
import doc.GazDocument;

public interface GazIR {
	
	void addCollection(GazCollection collection);
	void switchCollection(GazCollection collection);
	
	
	List<GazDocument> query(String query, int queryType, int maxResults);
	List<GazDocument> query(String query, int queryType);
	List<GazDocument> query(String query);
	
	GazCollection getCurrentCollection();
	ArrayList<GazCollection> getCollections();
	GazIndexManager getIndexManager();
	GazDictionary  getBiword();
	void setBiword(GazDictionary  biword);
}
