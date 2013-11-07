package doc;

import gazir.GazException;

import java.io.File;

public class GazDocumentNotFoundException extends GazException{
	private File file;
	
	public GazDocumentNotFoundException(File file){
		this.file = file;
	}
	
	public File getFile(){
		return this.file;
	}
}
