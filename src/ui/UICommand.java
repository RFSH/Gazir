package ui;

import java.util.ArrayList;

public class UICommand {
	String commandText;
	ArrayList<UICommand> subcommands;
	
	public UICommand(String commandText){
		this.commandText = commandText;
		subcommands = new ArrayList<UICommand>();
	}
	
	public UICommand(){
		this("");
	}
	
	public void branch(UICommand command){
		subcommands.add(command);
	}
	
	private void printPossibleOptions(){
		if(subcommands.size() == 0)
			return;
		System.out.println("Possible options are:");
		for(UICommand com : subcommands){
			System.out.println("\t" + com.acceptedCommand());
		}
	}
	
	public void doCommand(String command, String restCommand, UICommandOptions options){
		inCommand(command, options);
		restCommand = restCommand.trim();
		if(restCommand.isEmpty()){
			apply(options);
		}else{
			String rest;
			String nextCommand;
			if(!restCommand.contains(" ")){
				rest = "";
				nextCommand = restCommand; 
			}else{
				rest = restCommand.substring(restCommand.indexOf(" "));
				nextCommand = restCommand.substring(0, restCommand.indexOf(" "));
			}
		
			for(UICommand com : subcommands){
				if(com.validateCommand(nextCommand)){
					com.doCommand(nextCommand, rest, options);
					return;
				}
			}
			
			System.out.println("Unknown command " + nextCommand);
			printPossibleOptions();
		}
	}
	
	public String acceptedCommand(){
		return commandText;
	}
	
	public boolean validateCommand(String command){
		return command.equals(commandText);
	}
	
	public void inCommand(String command, UICommandOptions options){
		
	}
	
	public void apply(UICommandOptions options){
		if(!subcommands.isEmpty()){
			System.out.println("Incomplete command");
			printPossibleOptions();
		}
	}
	
	@Override
	public int hashCode() {
		return commandText.hashCode();
	}
}

