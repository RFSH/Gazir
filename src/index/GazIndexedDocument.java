package index;

import doc.GazDocument;

public class GazIndexedDocument {
	private int id;
	private GazDocument document;
	
	public GazIndexedDocument(int id, GazDocument document){
		this.id = id;
		this.document = document;
	}
	
	public GazDocument getDocument(){
		return this.document;
	}
	
	public int getId(){
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GazIndexedDocument){
			GazIndexedDocument indDoc = (GazIndexedDocument)obj;
			return document.equals(indDoc.getDocument());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return document.hashCode();
	}
	
	@Override
	public String toString() {
		return document.toString();
	}
}
