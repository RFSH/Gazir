package ui;

import java.util.HashMap;
import java.util.Map;

public class UICommandOptions {
	private Map<String, String> map;
	
	public UICommandOptions(){
		map = new HashMap<String, String>();
	}
	
	public void set(String key, String val){
		map.put(key, val);
	}
	
	public String get(String key){
		return map.get(key);
	}
}
