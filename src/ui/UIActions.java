package ui;

import gazir.GazIR;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

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
		if(collection == null){
			System.out.println("No collection selected");
			return;
		}
		if(document != null){
			try{
				GazDocument doc = collection.getDocumentByName(document);
				if(doc == null){
					try{
						int docId = Integer.parseInt(document);
					}catch(Exception e){
						
					}
				}
				
			}
			
		}
	}
}
