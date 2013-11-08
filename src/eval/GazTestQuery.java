package eval;

import java.util.List;

import doc.GazDocument;

public class GazTestQuery {
	private String queryString;
	private List<GazDocument> documents;
	
	public String getQueryText(){
		return this.queryString;
	}
	
	public List<GazDocument> getRelevantDocuments(){
		return documents;
	}
}
