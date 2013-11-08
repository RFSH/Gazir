package gazir;

import index.GazIndexManager;
import index.GazPosting;
import index.GazTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import doc.GazCollection;
import doc.GazDocument;

public class ZGazIR implements GazIR {
	private GazCollection currenCollection;
	ArrayList<GazCollection> collections;
	private GazIndexManager indexManger;

	public ZGazIR(GazCollection currenCollection, GazIndexManager indexManger) {
		collections = new ArrayList<GazCollection>();
		this.currenCollection = currenCollection;
		this.indexManger = indexManger;
	}
	
	@Override
	public List<GazDocument> query(String query, int queryType) {
		return query(query, queryType, 10);
	}

	@Override
	public List<GazDocument> query(String query) {
		return query(query, 0, 10);
	}
	
	public List<GazDocument> booleanQuery(String queryTerms[], int maxResults){
		List<GazDocument> documents = new ArrayList<GazDocument>();
		Collection<GazPosting> temp = null;
		for (String string : queryTerms) {
			for (GazTerm term : indexManger.getCurrentIndex()
					.getDictionaryTerms()) {
				if (term.getToken().equals(string)) {
					if (temp == null)
						temp = term.getPostingList();
					else
						temp.retainAll(term.getPostingList());
				}
			}
		}
		for (GazPosting posting : temp) {
			documents.add(posting.getDocument().getDocument());
			if (documents.size() > maxResults)
				break;
		}
		return documents;
	}

	public List<GazDocument> rankDocs(String queryTerms[], int maxResults, boolean tfBased){
		Collection<Collection<GazPosting>> postings = findPostings(queryTerms);
		
		GazComparator gazCompare = new GazComparator();
		Map<GazDocument, Integer> queryTermFrequencies = new HashMap<GazDocument, Integer>();
		
		for (Collection<GazPosting> gazPosting : postings) {
			for (GazPosting posting : gazPosting) {
				GazDocument doc = posting.getDocument().getDocument();
				if (queryTermFrequencies.get(doc) == null){
					if(tfBased)
						queryTermFrequencies.put(doc, posting.getTermFrequency());
					else
						queryTermFrequencies.put(doc, 1);
				}else{
					if(tfBased)
						queryTermFrequencies.put(doc, queryTermFrequencies.get(doc)	+ posting.getTermFrequency());
					else
						queryTermFrequencies.put(doc, queryTermFrequencies.get(doc)	+ 1);
				}
			}
		}
		
		TreeMap<GazDocument, Integer> sortedMap = new TreeMap<GazDocument, Integer>(gazCompare);
		gazCompare.setFreqMap(queryTermFrequencies);
		sortedMap.putAll(queryTermFrequencies);
		ArrayList<GazDocument> results = new ArrayList<GazDocument>();
		
		results.addAll(sortedMap.keySet());
		if(maxResults == -1)
			return results;
		else
			return results.subList(0, maxResults);
	}
	
	public List<GazDocument> rankDocAdvanced(String[] queryTokens, int maxResults, boolean superAdvanced){
		HashMap<String, Integer> termId = new HashMap<String, Integer>();
		for(int i = 0; i < queryTokens.length; i++){
			if(termId.get(queryTokens[i]) == null)
				termId.put(queryTokens[i], termId.keySet().size());
		}
		
		int queryCount = termId.keySet().size();
		double[] queryVector = new double[queryCount];
		GazTerm[] queryTerms = new GazTerm[queryCount];
		for(int i = 0; i < queryTokens.length; i++){
			queryVector[termId.get(queryTokens[i])]++;
		}
		
		for(String token : termId.keySet()){
			GazTerm term = indexManger.getCurrentIndex().getTerm(token);
			int ind = termId.get(token);
			queryTerms[ind] = term;
			if(term != null){
				int df = term.getDocFrequency();
				System.out.println("token: "+token+"i: "+termId.get(token));
				System.out.println("N: "+currenCollection.getDocuments().size()+" DF:"+df+" TF:"+queryVector[ind]);
				queryVector[ind] = (Math.log10((double)currenCollection.getDocuments().size()/df)) * (1 + Math.log10(queryVector[ind]));
				System.out.println("w-idf: "+Math.log10((double)currenCollection.getDocuments().size()/df));
				System.out.println("W: "+queryVector[ind]);
				System.out.println("--------");
			}else{
				
				System.out.println("term is null");
				queryVector[ind] = 0;
			}
		}
		
		if(superAdvanced){
			double vecSize = 0;
			for(int i = 0; i < queryVector.length; i++){
//				System.out.println("queryVector[i] :"+ queryVector[i]);
				vecSize += queryVector[i] * queryVector[i];
			}
//			System.out.println("vecSize for query before sqrtl: "+vecSize);
			vecSize = Math.sqrt(vecSize);
//			System.out.println("vecSize for query: "+vecSize);
			for(int i = 0; i < queryVector.length; i++)
				queryVector[i] /= vecSize;
		}
		
		Map<Integer, Double[]> docVecMap = new HashMap<Integer, Double[]>();
		Map<Integer, GazDocument> docMap = new HashMap<Integer, GazDocument>();
		for (GazTerm term : queryTerms) {
			if(term == null)
				continue;
			for(GazPosting posting : term.getPostingList()){
				int docId = posting.getDocId();
				Double[] vec = docVecMap.get(docId);
				if(vec == null){
					vec = new Double[queryCount];
					for(int i = 0; i < queryCount;  i++)
						vec[i] = 0.0;
					docVecMap.put(docId, vec);
					docMap.put(docId, posting.getDocument().getDocument());
				}
				vec[termId.get(term.getToken())]++;
			}
		}
		
		Comparator<GazDocScore> gazCompare = new Comparator<GazDocScore>() {
			@Override
			public int compare(GazDocScore o1, GazDocScore o2) {
				return (o1.getScore() >= o2.getScore()) ? 1 : -1 ;
			}
		};
		TreeSet<GazDocScore> docScoreSet = new TreeSet<GazDocScore>(gazCompare);
		
		for(Integer docId : docVecMap.keySet()){
			Double[] docVec = docVecMap.get(docId);
			GazDocument document = docMap.get(docId);
			
			if(superAdvanced){
				double vecSize = 0;
				for(int i = 0; i < docVec.length; i++)
					vecSize += docVec[i] * docVec[i];
				vecSize = Math.sqrt(vecSize);
				for(int i = 0; i < docVec.length; i++)
					docVec[i] /= vecSize;
//				System.out.println("vecSize: "+vecSize);
			}
			
			// Compute score
			System.out.println("DOC :"+document);
			double score = 0;
			for(int i = 0; i < docVec.length; i++){				
//				System.out.println("docVec["+i+"]= "+docVec[i]+"\t queryVector["+i+"]= "+queryVector[i]);
				score += docVec[i] * queryVector[i];
				
			}
			System.out.println("------");
//			System.out.println("GazDocScore :"+score);
			docScoreSet.add(new GazDocScore(document, score));
		}
		
		ArrayList<GazDocument> sortedDocs = new ArrayList<GazDocument>();
		for(GazDocScore docScore : docScoreSet){
			System.out.println("Score for doc"+docScore.getDocument().getId()+" :"+docScore.getScore());
			sortedDocs.add(docScore.getDocument());
			if(maxResults != -1 && sortedDocs.size() >= maxResults)
				break;
		}
		
		return sortedDocs;
	}
	@Override
	public List<GazDocument> query(String query, int queryType,
			int maxResults) {
	
		String queryTokens[] = query.split(" ");

		// TODO handle stopwords!

//		Collection<Collection<GazPosting>> postings = null;
//		if (queryType == 2 && queryType==1) {
//			postings = findPostings(queryTerms);
//		}

		switch (queryType) {
		case 0:
			return booleanQuery(queryTokens, maxResults);
		case 1:
			return rankDocs(queryTokens, maxResults, false);
		case 2:
			return rankDocs(queryTokens, maxResults, true);
		case 3:
			return rankDocAdvanced(queryTokens, maxResults, false);
		case 4:
			return rankDocAdvanced(queryTokens, maxResults, true);
		default:
			// TODO create exception class
			System.err.println("query Type is not defined");
			break;
		}

		

		return null;
	}

	@SuppressWarnings("null")
	private Collection<Collection<GazPosting>> findPostings(String[] queryTerms) {
		Collection<Collection<GazPosting>> postings = new ArrayList<Collection<GazPosting>>();

		for (String string : queryTerms) {
			for (GazTerm term : indexManger.getCurrentIndex().getDictionaryTerms()) {
				if (term.getToken().equals(string)) {
					postings.add(term.getPostingList());
				}
			}
		}
		return postings;
	}
	


	@Override
	public void addCollection(GazCollection collection) {
		if (currenCollection == null)
			currenCollection = collection;
		collections.add(collection);
	}

	@Override
	public void switchCollection(GazCollection collection) {
		currenCollection = collection;
		if (!collections.contains(collection))
			collections.add(collection);
	}

	@Override
	public GazCollection getCurrentCollection() {
		return currenCollection;
	}

	@Override
	public ArrayList<GazCollection> getCollections() {
		return collections;
	}

	@Override
	public GazIndexManager getIndexManager() {
		return indexManger;
	}

}

class GazDocScore{
	private GazDocument document;
	private double score;
	public GazDocScore(GazDocument document, double score){
		this.document = document;
		this.score = score;
	}
	public GazDocument getDocument() {
		return document;
	}
	public void setDocument(GazDocument document) {
		this.document = document;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
}