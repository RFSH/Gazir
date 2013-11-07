package index;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ZHashDictionary implements GazDictionary{
	private Map<String, GazTerm> termMap;
	
	public ZHashDictionary(){
		termMap = new HashMap<String, GazTerm>();
	}
	
	@Override
	public void addTerm(String termString, GazTerm term) {
		termMap.put(termString, term);
	}
	
	@Override
	public GazTerm findTerm(String termString) {
		return termMap.get(termString);
	}
	
	@Override
	public Collection<GazTerm> getTerms() {
		return termMap.values();
	}
	
}
