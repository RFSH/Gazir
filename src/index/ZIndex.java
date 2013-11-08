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
	public void indexDocument(GazDocument document, GazDictionary biword) {
		GazIndexedDocument indDoc = new GazIndexedDocument(documentSet.size(), document);
		if(documentSet.contains(indDoc)){
			System.out.println("Document already indexed");
			return;
		}
		
		documentSet.add(indDoc);
		GazTokenizer tokenizer = new ZTokenizer(document, biword);
		GazTokenProcessor processor = new ZTokenProcessor();
		while(tokenizer.hasNext()){
			String token = tokenizer.next();
			GazTerm term;
			String pToken;
			if(biword != null && biword.findTerm(token) != null){
				term = biword.findTerm(token);
				pToken = token;
				if(dictionary.findTerm(token) == null){
					dictionary.addTerm(token, term);
				}
			}else{
				pToken = processor.processToken(token);
				if(pToken == null){
					System.out.println("Stopword found " + token);
					continue;
				}
				term = dictionary.findTerm(pToken);
				if(term == null){
					term = new GazTerm(pToken);
					dictionary.addTerm(pToken, term);
				}
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
		tokenizer.getDocument().close();
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
