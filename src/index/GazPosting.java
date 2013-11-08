package index;


public class GazPosting {
	private GazIndexedDocument document;
	private int termFrequency;
	
	public GazPosting(GazIndexedDocument document){
		this.document = document;
		this.termFrequency = 0;
	}
	
	public GazIndexedDocument getDocument(){
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GazPosting){
			GazPosting pos = (GazPosting)obj;
			return pos.document.equals(document);
		}
		return super.equals(obj);
	}
}
