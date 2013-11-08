package index;

import java.util.ArrayList;
import java.util.List;

public class GazTerm {
	private String token;
	private int frequency;
	private ArrayList<GazPosting> postingList;
	
	public GazTerm(String token, int frequency){
		this.token = token;
		this.frequency = frequency;
		postingList = new ArrayList<GazPosting>();
	}
	
	public GazTerm(String token){
		this(token, 0);
	}
	
	
	public String getToken(){
		return token;
	}
	
	public int getFrequency(){
		return this.frequency;
	}
	
	public int getDocFrequency(){
		return postingList.size();
	}
	
	public void increment(){
		frequency++;
	}
	
	public List<GazPosting> getPostingList(){
		return postingList;
	}
	
	public GazPosting getLastPosting(){
		if(postingList.isEmpty())
			return null;
		return postingList.get(postingList.size()-1);
	}
	
	public void pushPosting(GazPosting posting){
		postingList.add(posting);
	}
	
	@Override
	public String toString() {
		return token;
	}
}
