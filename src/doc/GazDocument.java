package doc;

import java.io.File;

public interface GazDocument {
	int getId();
	String read(int amount);
	String readLine();
	void reset();
	File getFile();	
}
