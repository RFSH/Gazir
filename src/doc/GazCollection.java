package doc;

import java.io.Serializable;
import java.util.List;

public interface GazCollection extends Serializable {
	int getId();
	List<GazDocument> getDocuments();
	void addDocument(GazDocument document);
	GazDocument getDocumentByName(String name);
	GazDocument getDocumentById(int id);
	int nextDocId();
	String getName();
}
