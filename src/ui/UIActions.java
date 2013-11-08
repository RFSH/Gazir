package ui;

import gazir.GazIR;
import index.GazIndex;
import index.GazPosting;
import index.GazTerm;
import index.GazTokenProcessor;
import index.ZIndex;
import index.ZTokenProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import doc.GazCollection;
import doc.GazDocument;
import doc.GazDocumentNotFoundException;
import doc.ZCollection;
import doc.ZDocument;

public class UIActions {
	
	public static void showCollections(GazIR gazir, UICommandOptions options){
		ArrayList<GazCollection> collections = gazir.getCollections();
		for(GazCollection col : collections){
			if(col.equals(gazir.getCurrentCollection()))
				System.out.print("* ");
			else
				System.out.print("  ");
			System.out.println(col.getName() + ": " + col.getDocuments().size() + " documents");
		}
	}
	
	public static void showDocuments(GazIR gazir, UICommandOptions options){
		GazCollection currentCollection = gazir.getCurrentCollection();
		if(currentCollection == null){
			System.out.println("No collection selected.");
			return;
		}
		for(GazDocument document : currentCollection.getDocuments()){
			System.out.println(document.getFile().getName() + ": " + document.getFile().getTotalSpace());
		}
	}
	
	public static void useCollection(GazIR gazir, UICommandOptions options){
		int collectionId = Integer.parseInt(options.get("collection"));
		try{
			ArrayList<GazCollection> collections = gazir.getCollections();
			GazCollection collection = collections.get(collectionId);
			System.out.println("Using collection " + collectionId);
		}catch(Exception e){
			System.out.println("Failed to use collection");
		}
	}
	
	public static void useIndex(GazIR gazir, UICommandOptions options){
		String index = options.get("index");
		System.out.println("Using index " + index);
	}
	
	private static void addDocument(GazCollection collection, File file, boolean print){
		try {
			GazDocument document = new ZDocument(file);
			collection.addDocument(document);
			if(print)
				System.out.println("Document " + file.getName() + " added");
		} catch (GazDocumentNotFoundException e) {
			System.out.println("No such file: " + file.getPath());
		}
	}
	
	public static void newCollection(GazIR gazir, UICommandOptions options){
		String name = options.get("name");
		GazCollection collection = new ZCollection(name);
		System.out.println("Using new collection " + name);
		gazir.switchCollection(collection);
	}
	
	public static void loadDocuments(GazIR gazir, UICommandOptions options){
		String fileName = options.get("fileName");
		File file = new File(fileName);
		
		if(!file.exists()){
			System.out.println("No such file or directory");
			return;
		}
		
		if(gazir.getCurrentCollection() == null){
			System.out.println("No collection selected, creating new collection...");
			GazCollection collection = new ZCollection();
			gazir.switchCollection(collection);
		}
		
		if(file.isFile()){
			addDocument(gazir.getCurrentCollection(), file, true);
		}else{
			File[] files = file.listFiles(); 
			int count = 0;
			for(File f : files){
				addDocument(gazir.getCurrentCollection(), f, false);
				count++;
				System.out.print("\rLoading documents in directory " + file.getPath() + "\t[" + count+"/"+files.length+"]");				
			}
			System.out.println();
		}
	}
	
	public static void indexDocument(GazIR gazir, UICommandOptions options){
		String document = options.get("document");
		GazCollection collection = gazir.getCurrentCollection();
		GazIndex index = gazir.getIndexManager().getCurrentIndex();
		
		if(index == null){
			System.out.println("No index selected, creating new index...");
			index = new ZIndex();
			gazir.getIndexManager().addIndex(index);
			gazir.getIndexManager().switchIndex(index);
		}
		
		if(collection == null){
			System.out.println("No collection selected");
			return;
		}
		
		if(document != null){
			GazDocument doc = collection.getDocumentByName(document);
			if(doc == null){
				try{
					int docId = Integer.parseInt(document);
					doc = collection.getDocuments().get(docId);
				}catch(Exception e){
					System.out.println("Invalid document");
					return;
				}
			}
			System.out.println("Indexing document " + doc.getName());
			index.indexDocument(doc);
		}else{
			List<GazDocument> documents = collection.getDocuments();
			int count = 0;
			for(GazDocument doc : documents){
				count++;
				
				System.out.print(String.format("\rIndexing   %-30s [%d/%d]", doc.getName(), count, documents.size()));
				index.indexDocument(doc);
			}
			System.out.println();
		}
	}
	
	public static void showIndex(GazIR gazir, UICommandOptions options){
		List<GazIndex> indices = gazir.getIndexManager().getIndexes();
		int count = 1;
		for(GazIndex index : indices){
			if(index.equals(gazir.getIndexManager().getCurrentIndex()))
				System.out.print("* ");
			else
				System.out.print("  ");
			System.out.println(count + ": " + index.getDictionaryTerms().size() + " terms");
			count++;
		}
	}
	
	public static void showDictionary(GazIR gazir, UICommandOptions options){
		GazIndex index = gazir.getIndexManager().getCurrentIndex();
		
		if(index == null){
			System.out.println("No index selected");
			return;
		}
		
		Collection<GazTerm> terms = index.getDictionaryTerms();
		for(GazTerm term : terms){
			System.out.println(term);
		}
	}
	
	public static void showPosting(GazIR gazir, UICommandOptions options){
		GazIndex index = gazir.getIndexManager().getCurrentIndex();
		String token = options.get("term");
		if(index == null){
			System.out.println("No index selected");
			return;
		}
		
		GazTokenProcessor processor = new ZTokenProcessor();
		String pToken = processor.processToken(token);
		if(pToken == null){
			System.out.println("Token " + token + " not accepted");
			return;
		}
		if(!token.equals(pToken))
			System.out.println("Token changed to " + pToken);
		
		GazTerm term = index.getTerm(token);
		if(term == null){
			System.out.println(pToken + " not found in dictionary");
			return;
		}
		
		List<GazPosting> postings = term.getPostingList();
		System.out.print(pToken + ": " + postings.size() + " --  ");
		for(int i = 0; i < postings.size(); i++){
			if(i != 0)
				System.out.print(", ");
			System.out.print(postings.get(i).getDocument() + " [" + postings.get(i).getTermFrequency() + "]");
		}
		System.out.println();
	}
	
	public static void query(GazIR gazir, UICommandOptions options){
		String queryString = options.get("query");
		System.out.println("Querying " + queryString);
		Collection<GazDocument> results = gazir.query(queryString, 3);
		for(GazDocument doc : results){
			System.out.print(doc + " ");
		}
		System.out.println();
	}
}
