package ui;

import gazir.GazIR;
import index.GazIndex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
		
		showCommand.branch(new UICommand("index"){
			@Override
			public void apply(UICommandOptions options){
				UIActions.showIndex(gazir, options);
			}
		});
		
		showCommand.branch(new UICommand("dictionary"){
			@Override
			public void apply(UICommandOptions options){
				UIActions.showDictionary(gazir, options);
			}
		});
		
		UICommand showPosting = new UICommand("posting");
		showPosting.branch(new UICommand("posting"){
			@Override
			public boolean validateCommand(String command) {
				return command.length() > 0;
			}
			
			@Override
			public String acceptedCommand() {
				return "<token>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("term", command);
			}
			
			@Override
			public void apply(UICommandOptions options){
				UIActions.showPosting(gazir, options);
			}
		});
		showCommand.branch(showPosting);
		
		showCommand.branch(new UICommand("stopwords"){
			@Override
			public void apply(UICommandOptions options){
				UIActions.showStopWords(gazir, options);
			}
		});
		showCommand.branch(showPosting);
		
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
		
		UICommand stopwordLoad = new UICommand("stopwords");
		loadCommand.branch(stopwordLoad);
		
		stopwordLoad.branch(new UICommand(){
			@Override
			public boolean validateCommand(String command) {
				return command.length() > 0;
			}
			
			@Override
			public String acceptedCommand() {
				return "<stopword file name>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("fileName", command);
			}
			
			@Override
			public void apply(UICommandOptions options) {
				UIActions.loadStopWords(gazir, options);
			}
		});
		
		UICommand indexCommand = new UICommand("index");
		loadCommand.branch(indexCommand);
		
		indexCommand.branch(new UICommand(){
			@Override
			public boolean validateCommand(String command) {
				return command.length() > 0;
			}
			
			@Override
			public String acceptedCommand() {
				return "<index file name>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("fileName", command);
			}
			
			@Override
			public void apply(UICommandOptions options) {
				UIActions.loadIndex(gazir, options);
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
	
	public static UICommand makeQueryCommand(){
		UICommand queryCommand = new UICommand("query");
		UICommand queryText = new UICommand(){
			@Override
			public boolean validateCommand(String command) {
				return command.length() > 0;
			}
			@Override
			public String acceptedCommand() {
				return "<query string>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("query", command);
			}
			
			@Override
			public void apply(UICommandOptions options) {
				UIActions.query(gazir, options);
			}
		};
		queryCommand.branch(queryText);
		
		UICommand queryType = new UICommand(){
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
				return "<query method>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("type", command);
			}
			
			@Override
			public void apply(UICommandOptions options) {
				UIActions.query(gazir, options);
			}
		};
		queryText.branch(queryType);
		
		UICommand queryMax = new UICommand(){
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
				return "<max results>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("max", command);
			}
			
			@Override
			public void apply(UICommandOptions options) {
				UIActions.query(gazir, options);
			}
		};
		queryType.branch(queryMax);
		
		return queryCommand;
	}
	
	public static UICommand makeEvalCommand(){
		UICommand evalCommand = new UICommand("eval");
		UICommand queryCommand = new UICommand(){
			@Override
			public boolean validateCommand(String command) {
				return command.length() > 0;
			}
			@Override
			public String acceptedCommand() {
				return "<query directory path>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("queryDir", command);
			}
		};
		evalCommand.branch(queryCommand);
		
		queryCommand.branch(new UICommand(){
			@Override
			public boolean validateCommand(String command) {
				return command.length() > 0;
			}
			@Override
			public String acceptedCommand() {
				return "<relevancy judgement file>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("relFile", command);
			}
			
			@Override
			public void apply(UICommandOptions options) {
				UIActions.eval(gazir, options);
			}
		});
		
		return evalCommand;
	}
	
	public static UICommand makeTokenizeCommand(){
		UICommand tokCommand = new UICommand("tokenize");
		tokCommand.branch(new UICommand(){
			@Override
			public boolean validateCommand(String command) {
				return command.length() > 0;
			}
			@Override
			public String acceptedCommand() {
				return "<document>";
			}
			
			@Override
			public void inCommand(String command, UICommandOptions options) {
				super.inCommand(command, options);
				options.set("document", command);
			}
			@Override
			public void apply(UICommandOptions options) {
				UIActions.tokenize(gazir, options);
			}
		});
		return tokCommand;
	}
	
	public static UICommand makeBiwordCommand(){
		UICommand biwordCommand = new UICommand("biword");
		biwordCommand.branch(new UICommand("init"){
			@Override
			public void apply(UICommandOptions options) {
				UIActions.biwordInit(gazir, options);
			}
		});
		biwordCommand.branch(new UICommand("list"){
			@Override
			public void apply(UICommandOptions options) {
				UIActions.biwordList(gazir, options);
			}
		});
		return biwordCommand;
	}
	
	public static UICommand makeSaveCommand(){
		UICommand saveCommand = new UICommand("save");
		UICommand saveCommand2 = new UICommand("index");
		saveCommand.branch(saveCommand2);
		saveCommand2.branch(new UICommand(){
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
				UIActions.saveIndex(gazir, options);
			}
		});
	
		return saveCommand;
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
		root.branch(makeTokenizeCommand());
		root.branch(makeIndexCommand());
		root.branch(makeQueryCommand());
		root.branch(makeEvalCommand());
		root.branch(makeBiwordCommand());
		root.branch(makeSaveCommand());
		
		return root;
	}
}// load documents /home/hadi/Uni/MIR/project/test/Hadi

