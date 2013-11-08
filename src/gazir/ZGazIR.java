package gazir;

import index.GazIndexManager;
import index.GazPosting;
import index.GazTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

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
	public Collection<GazDocument> query(String query, int queryType) {
		return query(query, queryType, 10);
	}

	@Override
	public Collection<GazDocument> query(String query) {
		return query(query, 0, 10);
	}

	@Override
	public Collection<GazDocument> query(String query, int queryType,
			int maxResults) {
		Collection<Integer> docIDs = new ArrayList<Integer>();
		Collection<GazDocument> docs = new ArrayList<GazDocument>();
		String queryTerms[] = query.split(" ");

		// TODO handle stopwords!

		Collection<Collection<GazPosting>> postings = null;
		if (queryType == 2 && queryType==1) {
			postings = findPostings(queryTerms);
		}

		switch (queryType) {
		case 0:
			Collection<GazPosting> temp = null;
			for (String string : queryTerms) {
				for (GazTerm term : indexManger.getCurrentIndex()
						.getDictionaryTerms()) {
					if (term.getToken().equals(string)) {
						if (temp == null)
							temp = term.getPostingList();
						else
							// TODO change equals for posting
							temp.retainAll(term.getPostingList());
					}
				}
			}
			for (GazPosting posting : temp) {
				docIDs.add(posting.getDocId());
				if (docIDs.size() > maxResults)
					break;
			}

			break;
		case 1:
			TreeMap<Integer, Integer> queryTermFrequencies = new TreeMap<Integer, Integer>();
			for (Collection<GazPosting> gazPosting : postings) {
				for (GazPosting posting : gazPosting) {
					if (queryTermFrequencies.get(posting.getDocId()) == null)
						queryTermFrequencies.put(posting.getDocId(),
								1);
					else
						queryTermFrequencies.put(posting.getDocId(),
								queryTermFrequencies.get(posting.getDocId())
										+1);
				}
			}
			
			//TODO sort by value
			
			break;
		case 2:
			TreeMap<Integer, Integer> termFrequencies = new TreeMap<Integer, Integer>();
			for (Collection<GazPosting> gazPosting : postings) {
				for (GazPosting posting : gazPosting) {
					if (termFrequencies.get(posting.getDocId()) == null)
						termFrequencies.put(posting.getDocId(),
								posting.getTermFrequency());
					else
						termFrequencies.put(posting.getDocId(),
								termFrequencies.get(posting.getDocId())
										+ posting.getTermFrequency());
				}
			}

			// TODO sort termFrequencies by value and those are the fucking
			// answers modafucka!

			break;
		case 3:
			TreeMap<GazTerm, Double> tfidfQuery = new TreeMap<GazTerm, Double>();
			for (String string : queryTerms) {
				for (GazTerm term : indexManger.getCurrentIndex()
						.getDictionaryTerms()) {
					if (term.getToken().equals(string)) {
						if (tfidfQuery.get(term) == null) {
							tfidfQuery.put(term, 0.0);
						} else {
							tfidfQuery.put(term, tfidfQuery.get(term) + 1);
						}
					}
				}
			}
			String[] termMap = new String[tfidfQuery.entrySet().size()];
			int i=0;
			for (Map.Entry<GazTerm, Double> entry : tfidfQuery.entrySet()) {
				// System.out.println(entry.getKey() + "/" + entry.getValue());
				termMap[i]= entry.getKey().getToken();
				i++;
				tfidfQuery.put(
						entry.getKey(),
						(Math.log10(currenCollection.getDocuments().size()/entry.getKey()
								.getFrequency()))
								* (1 + Math.log10(entry.getValue())));
			}
			
			//TODO complete this shit!
			// tfdoc< docID, termha
			TreeMap<Integer, int[]> tfDoc = new TreeMap<Integer, int[]>();
//			ArrayList<Integer> tfDocID = new ArrayList<Integer>();
			for (GazTerm term : tfidfQuery.keySet()) {
				for (GazPosting post : term.getPostingList()) {
					if(tfDocID.contains(post.getDocId())){
						for (int j = 0; j < termMap.length; j++) {
							if(term.getToken().equals(termMap[j]))
								tfDoc.get(post.getDocId())[j] = post.getTermFrequency();							
						}
					}else{
						tfDocID.add((post.getDocId()));
//						tfDoc.add()
					}
				}
			}
			break;
		case 4:

			break;

		default:
			// TODO create exception class
			System.err.println("query Type is not defined");
			break;
		}

		for (GazDocument doc : currenCollection.getDocuments()) {
			for (Integer docId : docIDs) {
				if (docId == doc.getId())
					docs.add(doc);
			}
		}

		return docs;
	}

	@SuppressWarnings("null")
	private Collection<Collection<GazPosting>> findPostings(String[] queryTerms) {
		Collection<Collection<GazPosting>> postings = null;

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