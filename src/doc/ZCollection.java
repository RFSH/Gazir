package doc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZCollection implements GazCollection, Serializable{
	private static int nextId = 0;
	
	private ArrayList<GazDocument> documents;
	private Map<String, GazDocument> documentMap;
	private Map<Integer, GazDocument> idMap;
	private int id;
	private int nextDocId;
	private String name;
	
	public ZCollection(String name){
		documents = new ArrayList<GazDocument>();
		documentMap = new HashMap<String, GazDocument>();
		idMap = new HashMap<Integer, GazDocument>();
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
		if(document.getFile().getName().equals(".DS_Store"))
			return;
		documents.add(document);
		documentMap.put(document.getName(), document);
		idMap.put(document.getId(), document);
	}
	
	@Override
	public GazDocument getDocumentByName(String name) {
		return documentMap.get(name);
	}
	
	@Override
	public GazDocument getDocumentById(int id) {
		return idMap.get(id);
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
