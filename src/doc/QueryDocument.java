package doc;

import java.io.File;

public class QueryDocument implements GazDocument{
	String query;
	boolean done = false;
	public QueryDocument(String query){
		this.query = query.trim();
	}
	
	@Override
	public int getId() {
		return 0;
	}
	
	@Override
	public String read(int amount) {
		if(done)
			return null;
		done = true;
		return query;
	}
	@Override
	public String readAll() {
		if(done)
			return null;
		done = true;
		return query;
	}
	@Override
	public String readLine() {
		if(done)
			return null;
		done = true;
		return query;
	}
	
	public void reset() {
		done = false;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public File getFile() {
		return null;
	}
	@Override
	public void close() {
		
	}
}
