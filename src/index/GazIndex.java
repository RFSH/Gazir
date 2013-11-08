package index;

import java.io.Serializable;
import java.util.Collection;

import doc.GazDocument;

public interface GazIndex extends Serializable{
	Collection<GazTerm> getDictionaryTerms();
	void indexDocument( GazDocument document, GazDictionary biword);
	GazTerm getTerm(String token);
}
