package index;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import doc.GazCollection;
import doc.GazDocument;
import doc.GazTokenizer;
import doc.ZTokenizer;

class Frequency implements Comparable<Frequency>{
	private int frequency;
	private String token;
	public Frequency(String token){
		frequency = 0;
		this.token = token;
	}
	
	public void inc(){
		frequency++;
	}
	
	public int getFrequency(){
		return frequency;
	}
	
	public String getToken(){
		return token;
	}
	
	@Override
	public int compareTo(Frequency o) {
		return ((frequency == o.frequency) ? 0 : (frequency > o.frequency) ? 1 : -1); 
	}
}

public class GazBiword {
	private Map<String, Frequency> freqMap;

	public GazBiword(){
		freqMap = new HashMap<String, Frequency>();
	}
	
	public void initDocument(GazDocument document) {
		GazTokenizer tokenizer = new ZTokenizer(document);
		String[] tokens = new String[2];
		
		int x = 0;
		tokens[0] = tokenizer.next();
		while(tokenizer.hasNext()){
			tokens[(x+1)%2] = tokenizer.next();
			String s = tokens[x%2] + " " + tokens[(x+1)%2];
			Frequency f = freqMap.get(s);
			if(f == null)
				freqMap.put(s, new Frequency(s));
			else
				f.inc();
			x++;
		}
		tokenizer.getDocument().close();
	}
	
	public GazDictionary initialize(GazCollection collection){
		freqMap.clear();
		Collection<GazDocument> documents = collection.getDocuments();
		int count = 0;
		for(GazDocument document : documents){
			count++;
			System.out.print(String.format("\rInitializing biword... \t [%d/%d]", count, documents.size()));
			initDocument(document);
		}
	
		System.out.println();
		System.out.println("Processing biwords...");
		TreeSet<Frequency> set = new TreeSet<Frequency>();
		set.addAll(freqMap.values());
		 
		GazDictionary dic = new ZHashDictionary(); 
		int c = 0;
		for(Frequency f : set){
			if(c > 1000)
				break;
			GazTerm term = new GazTerm(f.getToken(), f.getFrequency());
			dic.addTerm(f.getToken(), term);
			c++;
		}
		System.out.println("Biword initialized with " + dic.getTerms().size() + " tokens");
		return dic;
	}
}
