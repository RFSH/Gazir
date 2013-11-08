package doc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZCollection implements GazCollection{
	private static int nextId = 0;
	
	private ArrayList<GazDocument> documents;
	private Map<String, GazDocument> documentMap;
	private int id;
	private int nextDocId;
	private String name;
	
	public ZCollection(String name){
		documents = new ArrayList<GazDocument>();
		documentMap = new HashMap<String, GazDocument>();
		id = nextId++;
		nextId = 0;
		this.name = name;
	}
	
	public ZCollection(){
		this("---");
	}
		
	public List<GazDocument> getDocuments() {
		return this.documents;
	}
	
	@Override
	public void addDocument(GazDocument document) {
		documents.add(document);
		documentMap.put(document.getName(), document);
	}
	
	@Override
	public GazDocument getDocumentByName(String name) {
		return documentMap.get(name);
	}
	
	@Override
	public int nextDocId() {
		return nextDocId;
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
	public String getName(){
		return name;
	}
}
