package doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ZDocument implements GazDocument{
	private int id;
	private File file;
	private BufferedReader reader;
	private boolean fileOpen;
	
	public ZDocument(int id, File file) throws GazDocumentNotFoundException{
		this.id = id;
		if(!file.exists()){
			throw new GazDocumentNotFoundException(file);
		}
		this.file = file;
		fileOpen = false;
	}
	
	public ZDocument(int id, String fileName) throws GazDocumentNotFoundException{
		this(id, new File(fileName));
	}
	
	private boolean openFile(){
		try {	
			 reader = new BufferedReader(new FileReader(file));
			 fileOpen = true;
			 return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public String read(int amount) {
		if(fileOpen && !openFile()){
			return "";
		}
		char[] buf = new char[amount];
		
		try {
			reader.read(buf);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
		return new String(buf);
	}
	
	public String readLine(){
		if(fileOpen && !openFile()){
			return "";
		}
		
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public void reset(){
		if(fileOpen){
			try {
				reader.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileOpen = false;
		
	}

	@Override
	public File getFile() {
		return this.file;
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
}
