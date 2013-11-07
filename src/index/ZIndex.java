package index;

import java.util.Collection;

import doc.GazDocument;
import doc.GazTokenizer;
import doc.ZTokenizer;

public class ZIndex implements GazIndex {
	private GazDictionary dictionary;
	
	public ZIndex() {
		dictionary = new ZHashDictionary();
	}	
	
	@Override
	public void indexDocument(GazDocument document) {
		GazTokenizer tokenizer = new ZTokenizer(document);
		GazTokenProcessor processor = new ZTokenProcessor();
		while(tokenizer.hasNext()){
			String token = tokenizer.next();
			String pToken = processor.processToken(token);
			
			GazTerm term = dictionary.findTerm(pToken);
			if(term == null)
				term = new GazTerm(pToken);
			
			// Add to end posting
			GazPosting posting = term.getLastPosting();
			if(posting == null || !posting.getDocument().equals(document)){
				posting = new GazPosting(document);
				term.pushPosting(posting);
			}
			
			// Increase frequency
			posting.increment();
		}
	}
	
	@Override
	public Collection<GazTerm> getDictionaryTerms() {
		return dictionary.getTerms();
	}
	
	@Override
	public GazTerm getTerm(String termString) {
		return dictionary.findTerm(termString);
	}

}
