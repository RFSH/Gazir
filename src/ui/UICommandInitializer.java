package ui;

import gazir.GazIR;

import java.util.ArrayList;

import doc.GazCollection;
import doc.GazDocument;

public class UICommandInitializer {
	private static GazIR gazir;
	
	private static UICommand makeShowCommand(){
		UICommand showCommand = new UICommand("show");
		
		showCommand.branch(new UICommand("collections") {
			@Override
			public void apply(UICommandOptions options) {
				ArrayList<GazCollection> collections = gazir.getCollections();
				for(GazCollection col : collections){
					System.out.println(col.getId() + " " + col.getDocuments().size() + " documents");
				}
			}
		});
		
		showCommand.branch(new UICommand("documents") {
			@Override
			public void apply(UICommandOptions options) {
				GazCollection currentCollection = gazir.getCurrentCollection();
				if(currentCollection == null){
					System.out.println("No collection selected.");
					return;
				}
				for(GazDocument document : currentCollection.getDocuments()){
					System.out.println(document.getId() + " " + document.getFile().getName());
				}
			}
		});
		
		return showCommand;
	}
	
	private static UICommand makeUseCommand(){
		UICommand useCommand = new UICommand("use");
		UICommand collectionCommand = new UICommand("collection");
		collectionCommand.branch(new UICommand() {
			@Override
			public boolean validateCommand(String command) {
				try{
					Integer.parseInt(command);
					return true;
				}catch(Exception e){
					return false;
				}
			}
			
			@Override
			public String acceptedCommand() {
				return "<collection-id>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("collection", command);
			}
			
			@Override
			public void apply(UICommandOptions options) {
				int collectionId = Integer.parseInt(options.get("collection"));
				try{
					ArrayList<GazCollection> collections = gazir.getCollections();
					GazCollection collection = collections.get(collectionId);
					System.out.println("Using collection " + collectionId);
				}catch(Exception e){
					System.out.println("Failed to use collection");
				}
				
			}
		});
		useCommand.branch(collectionCommand);
		
		UICommand indexCommand = new UICommand("index");
		indexCommand.branch(new UICommand(){
			@Override
			public boolean validateCommand(String command) {
				try{
					Integer.parseInt(command);
					return true;
				}catch(Exception e){
					return false;
				}
			}
			
			@Override
			public String acceptedCommand() {
				return "<index-id>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("index", command);
			}
			
			@Override
			public void apply(UICommandOptions options) {
				String index = options.get("index");
				System.out.println("Using index " + index);
			}
		});
		useCommand.branch(indexCommand);
		
		return useCommand;
	}

	
	public static UICommand makeLoadCommand(){
		UICommand loadCommand = new UICommand("load"){
			
		};
		return loadCommand;
	}
	
	
	public static UICommand initializeCommands(GazIR gaz){
		UICommand root = new UICommand("root") {
			public void apply(UICommandOptions options){
				System.out.println("Root");
			}
		};
		
		root.branch(new UICommand("exit") {
			@Override
			public void apply(UICommandOptions options) {
				System.exit(0);
			}
		});
		
		root.branch(makeShowCommand());
		root.branch(makeUseCommand());
		root.branch(makeLoadCommand());
		
		return root;
	}
}
