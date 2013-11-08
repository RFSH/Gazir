package index;

import java.io.File;
import java.util.List;

public interface GazIndexManager {
	List<GazIndex> getIndexes();
	void addIndex(GazIndex index);
	void saveIndex(GazIndex index, File file);
	GazIndex loadIndex(File file);
	void switchIndex(GazIndex index);
	GazIndex getCurrentIndex();
}