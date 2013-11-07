package doc;

import java.util.Collection;

public interface GazCollection {
	int getId();
	Collection<GazDocument> getDocuments();
	void addDocument(GazDocument document);
	int nextDocId();
}
