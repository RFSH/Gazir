package index;

import java.util.Collection;

import doc.GazDocument;

public interface GazIndex {
	Collection<GazTerm> getDictionaryTerms();
	void indexDocument( GazDocument document, GazDictionary biword);
	GazTerm getTerm(String token);
}
