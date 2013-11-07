package ui;

public class UnknownCommandException extends Exception{
	public UnknownCommandException(String commandText){
		super("Unknown command " + commandText);
	}
}

