package index;

import java.util.Collection;

public interface GazDictionary {
	void addTerm(String termString, GazTerm term);
	GazTerm findTerm(String termString);
	Collection<GazTerm> getTerms();
}
