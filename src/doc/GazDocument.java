package doc;

import java.io.File;
import java.io.Serializable;

public interface GazDocument extends Serializable{
	int getId();
	String read(int amount);
	String readLine();
	String readAll();
	void reset();
	void close();
	File getFile();
	String getName();
}
