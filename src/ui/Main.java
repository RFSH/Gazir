package ui;

import gazir.GazIR;
import gazir.ZGazIR;
import index.ZIndexManager;

import java.util.Scanner;



public class Main {
	private static GazIR gazir;
	private static UICommand rootCommand;
	
	
	public static void main(String[] args){
		gazir = new ZGazIR(null, new ZIndexManager());
		rootCommand = UICommandInitializer.initializeCommands(gazir);
		Scanner scanner = new Scanner(System.in);
		while(true){
			System.out.print("> ");
			String command = scanner.nextLine();
			rootCommand.doCommand("", command, new UICommandOptions());
		}
	}
}
