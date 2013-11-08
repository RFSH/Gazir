package index;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import doc.GazDocument;
import doc.GazTokenizer;
import doc.ZTokenizer;

public class ZIndex implements GazIndex {
	private GazDictionary dictionary;
	private Set<GazIndexedDocument> documentSet;
	
	public ZIndex() {
		dictionary = new ZHashDictionary();
		documentSet = new HashSet<GazIndexedDocument>();
	}	
	
	@Override
	public void indexDocument(GazDocument document) {
		GazIndexedDocument indDoc = new GazIndexedDocument(documentSet.size(), document);
		if(documentSet.contains(indDoc)){
			System.out.println("Document already indexed");
			return;
		}
		
		documentSet.add(indDoc);
		GazTokenizer tokenizer = new ZTokenizer(document);
		GazTokenProcessor processor = new ZTokenProcessor();
		while(tokenizer.hasNext()){
			String token = tokenizer.next();
			String pToken = processor.processToken(token);
			
			GazTerm term = dictionary.findTerm(pToken);
			if(term == null){
				term = new GazTerm(pToken);
				dictionary.addTerm(pToken, term);
			}
			
			// Add to end posting
			GazPosting posting = term.getLastPosting();
//			System.out.println(pToken + " " + posting);
			if(posting == null || !posting.getDocument().equals(document)){
				posting = new GazPosting(indDoc);
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
