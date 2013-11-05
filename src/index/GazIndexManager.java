package index;

import java.io.File;
import java.util.Collection;

public interface GazIndexManager {
	Collection<GazIndex> getIndexes();
	void addIndex(GazIndex index);
	void saveIndex(GazIndex index, File file);
	GazIndex loadIndex(File file);
	void switchIndex(GazIndex index);
	GazIndex getCurrentIndex();
}
