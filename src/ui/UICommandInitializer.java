package ui;

import gazir.GazIR;

public class UICommandInitializer {
	private static GazIR gazir;
	
	private static UICommand makeShowCommand(){
		UICommand showCommand = new UICommand("show");
		
		showCommand.branch(new UICommand("collections") {
			@Override
			public void apply(UICommandOptions options) {
				UIActions.showCollections(gazir, options);
			}
		});
		
		showCommand.branch(new UICommand("documents") {
			@Override
			public void apply(UICommandOptions options) {
				UIActions.showDocuments(gazir, options);
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
				UIActions.useCollection(gazir, options);
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
				UIActions.useIndex(gazir, options);
			}
		});
		useCommand.branch(indexCommand);
		
		return useCommand;
	}

	
	public static UICommand makeLoadCommand(){
		UICommand loadCommand = new UICommand("load");
		
		UICommand documentLoad = new UICommand("documents");
		loadCommand.branch(documentLoad);
		
		documentLoad.branch(new UICommand(){
			@Override
			public boolean validateCommand(String command) {
				return command.length() > 0;
			}
			
			@Override
			public String acceptedCommand() {
				return "<file name>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("fileName", command);
			}
			
			@Override
			public void apply(UICommandOptions options) {
				UIActions.loadDocuments(gazir, options);
			}
		});
		return loadCommand;
	}
	
	public static UICommand makeNewCommand(){
		UICommand newCommand = new UICommand("new");
		
		UICommand newCollection = new UICommand("collection");
		newCommand.branch(newCollection);
		
		newCollection.branch(new UICommand(){
			@Override
			public boolean validateCommand(String command) {
				return command.length() > 0;
			}
			
			@Override
			public String acceptedCommand() {
				return "<collection name>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("name", command);
			}
			
			@Override
			public void apply(UICommandOptions options) {
				UIActions.newCollection(gazir, options);
			}
		});
		return newCommand;
	}
	
	public static UICommand makeIndexCommand(){
		UICommand indexCommand = new UICommand("index");
		indexCommand.branch(new UICommand("all"){
			@Override
			public String acceptedCommand() {
				return "all";
			}
			
			@Override
			public void apply(UICommandOptions options) {
				UIActions.indexDocument(gazir, options);
			}
		});
		
		indexCommand.branch(new UICommand("all"){
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
				return "<document-id>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("document", command);
			}
			
			@Override
			public void apply(UICommandOptions options) {
				UIActions.indexDocument(gazir, options);
			}
		});
		
		
		return indexCommand;
	}
	
	public static UICommand initializeCommands(GazIR gaz){
		gazir = gaz;
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
		root.branch(makeNewCommand());
		return root;
	}
}
