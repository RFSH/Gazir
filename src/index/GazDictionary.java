package index;

import java.io.Serializable;
import java.util.Collection;

public interface GazDictionary extends Serializable{
	void addTerm(String termString, GazTerm term);
	GazTerm findTerm(String termString);
	Collection<GazTerm> getTerms();
}
