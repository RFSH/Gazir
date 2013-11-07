package index;

import java.util.Collection;

import doc.GazDocument;

public interface GazIndex {
	Collection<GazTerm> getDictionaryTerms();
	void indexDocument( GazDocument document);
	GazTerm getTerm(String token);
}
