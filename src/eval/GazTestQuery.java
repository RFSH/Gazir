package eval;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import doc.GazDocument;
import doc.GazDocumentNotFoundException;
import doc.ZDocument;

public class GazTestQuery extends ZDocument {
	private String queryString;
	private List<GazDocument> documents;
	
	public GazTestQuery(File file) throws GazDocumentNotFoundException {
		super(file);
		documents = new ArrayList<GazDocument>();
	}
	
	public void setQueryText(String queryString){
		this.queryString = queryString;
	}
	
	public String getQueryText(){
		return this.queryString;
	}
	
	public void addRelevantDocument(GazDocument document){
		documents.add(document);
	}
	
	public List<GazDocument> getRelevantDocuments(){
		return documents;
	}
}
