package index;

import java.util.Collection;

public interface GazTerm {
	String getToken();
	int getFrequency();
	Collection<GazPosting> getPostingList();
}
