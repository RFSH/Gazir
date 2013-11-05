package index;

import java.util.Collection;

import doc.GazDocument;

public interface GazIndex {
	Collection<GazTerm> getDictionary();
	void addTerm(String token, GazDocument document);
	GazTerm getTerm(String token);
}
