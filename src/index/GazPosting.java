package index;

import doc.GazDocument;

public class GazPosting {
	private GazDocument document;
	private int termFrequency;
	
	public GazPosting(GazDocument document){
		this.document = document;
		this.termFrequency = 0;
	}
	
	public GazDocument getDocument(){
		return document;
	}
	
	public int getDocId() {
		return document.getId();
	}
	
	public void increment(){
		termFrequency++;
	}
	
	public int getTermFrequency() {
		return termFrequency;
	}
	
	public void setTermFrequency(int termFrequency) {
		this.termFrequency = termFrequency;
	}
}
