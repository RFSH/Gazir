package doc;

import java.io.File;

public interface GazDocument {
	int getId();
	String read(int amount);
	String readLine();
	String readAll();
	void reset();
	void close();
	File getFile();
	String getName();
}
