package doc;

import java.util.List;

public interface GazCollection {
	int getId();
	List<GazDocument> getDocuments();
	void addDocument(GazDocument document);
	GazDocument getDocumentByName(String name);
	GazDocument getDocumentById(int id);
	int nextDocId();
	String getName();
}
