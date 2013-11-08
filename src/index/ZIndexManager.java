package index;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZIndexManager implements GazIndexManager{
	List<GazIndex> indexList;
	GazIndex currentIndex;
	
	public ZIndexManager(){
		indexList = new ArrayList<GazIndex>();
	}
	
	@Override
	public void addIndex(GazIndex index) {
		indexList.add(index);
	}
	
	@Override
	public GazIndex getCurrentIndex() {
		return currentIndex;
	}
	
	@Override
	public List<GazIndex> getIndexes() {
		return indexList;
	}
	
	@Override
	public void switchIndex(GazIndex index) {
		currentIndex = index;
	}
	
	@Override
	public GazIndex loadIndex(File file) {
		return null;
	}
	
	@Override
	public void saveIndex(GazIndex index, File file) {
		
	}
}
