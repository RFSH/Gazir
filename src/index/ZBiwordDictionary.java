package index;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ZBiwordDictionary implements GazDictionary{
	private Map<String, GazTerm> termMap;
	private Map<String, Integer> freqMap;
	
	public ZBiwordDictionary(){
		termMap = new HashMap<String, GazTerm>();
		freqMap = new HashMap<String, Integer>();
	}
	
	@Override
	public void addTerm(String termString, GazTerm term) {
		addTerm(termString, term, 0);
	}
	
	public void addTerm(String termString, GazTerm term, int frequency) {
		termMap.put(termString, term);
		freqMap.put(termString, frequency);
	}
	
	@Override
	public GazTerm findTerm(String termString) {
		return termMap.get(termString);
	}
	
	@Override
	public Collection<GazTerm> getTerms() {
		return termMap.values();
	}
	
	public int getFrequency(String token){
		Integer f = freqMap.get(token);
		if(f == null)
			return 0;
		return f;
	}
	
	public int getFrequency(GazTerm term){
		return getFrequency(term.getToken());
	}
}
