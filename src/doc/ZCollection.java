package doc;

import java.util.ArrayList;
import java.util.Collection;

public class ZCollection implements GazCollection{
	private static int nextId = 0;
	
	private ArrayList<GazDocument> documents;
	private int id;
	private int nextDocId;
	
	public ZCollection(){
		documents = new ArrayList<GazDocument>();
		id = nextId++;
		nextId = 0;
	}
		
	public Collection<GazDocument> getDocuments() {
		return this.documents;
	}
	
	@Override
	public void addDocument(GazDocument document) {
		documents.add(document);
	}
	
	@Override
	public int nextDocId() {
		return nextDocId;
	}
	
	@Override
	public int getId() {
		return this.id;
	}
}