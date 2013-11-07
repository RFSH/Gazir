package gazir;

import index.GazIndexManager;
import index.GazPosting;
import index.GazTerm;

import java.util.Collection;

import doc.GazCollection;
import doc.GazDocument;

public class ZGazIR implements GazIR {
	private GazCollection currenCollection;
	Collection<GazCollection> collections;
	private GazIndexManager indexManger;

	public ZGazIR(GazCollection currenCollection, GazIndexManager indexManger) {
		this.currenCollection = currenCollection;
		this.indexManger = indexManger;
	}

	// TODO these functions should change indexManager too!

	@Override
	public void addCollection(GazCollection collection) {
		if (currenCollection == null)
			currenCollection = collection;
		collections.add(collection);
	}

	@Override
	public void addDocument(GazDocument document) {
		// TODO if there's no currentCollection new and then addDoc
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
	public Collection<GazCollection> getCollections() {
		return collections;
	}

	@Override
	public GazIndexManager getIndexManager() {
		return indexManger;
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
		Collection<Integer> docIDs = null;
		Collection<GazDocument> docs = null;
		Collection<GazPosting> temp = null;
		String queryTerms[] = query.split(" ");

		// TODO handle stopwords!

		// indexManger.getCurrentIndex().getDictionary()
		switch (queryType) {
		case 0:
			for (String string : queryTerms) {
				for (GazTerm term : indexManger.getCurrentIndex()
						.getDictionary()) {
					if (term.getToken().equals(string)) {
						if (temp == null)
							temp = term.getPostingList();
						else
							// TODO change equals for posting
							temp.retainAll(term.getPostingList());
					}
				}
			}
			break;
		case 1:

			break;
		case 2:

			break;
		case 3:

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

}