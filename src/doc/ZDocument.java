package doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZDocument implements GazDocument{
	private int id;
	private File file;
	private BufferedReader reader;
	private boolean fileOpen;
	
	public ZDocument(File file) throws GazDocumentNotFoundException{
		if(!file.exists()){
			throw new GazDocumentNotFoundException(file);
		}
		this.file = file;
		fileOpen = false;
		
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(file.getName());
		if(m.find()){
			this.id = Integer.parseInt(m.group());
		}else{
			this.id = 0;
		}
	}
	
	public ZDocument(String fileName) throws GazDocumentNotFoundException{
		this(new File(fileName));
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
		if(!fileOpen && !openFile()){
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
	
	@Override
	public String readAll() {
		if(!fileOpen && !openFile()){
			return "";
		}
		char[] buf = new char[(int)file.length()];
		
		try {
			reader.read(buf);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
		return new String(buf);
	}
	
	public String readLine(){
		if(!fileOpen && !openFile()){
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
	public String getName() {
		return file.getName();
	}

	@Override
	public File getFile() {
		return this.file;
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GazDocument){
			GazDocument doc = (GazDocument)obj;
			return ((GazDocument) obj).getFile().equals(this.getFile());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return file.hashCode();
	}
	
	@Override
	public String toString() {
		return file.getName();
	}
}
