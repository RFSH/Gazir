package gazir;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import doc.GazDocument;
import doc.GazDocumentNotFoundException;
import doc.ZDocument;

public class GazComparator implements Comparator<GazDocument>{
	Map<GazDocument, Integer> freqMap;
	
	public void setFreqMap(Map<GazDocument, Integer> freqMap){
		this.freqMap = freqMap;
	}
	
	public int compare(GazDocument d1, GazDocument d2) {
		Integer if1 = freqMap.get(d1);
		Integer if2 = freqMap.get(d2);
		int f1 = (if1 == null) ? 0 : if1;
		int f2 = (if2 == null) ? 0 : if2;
		return (((f1 <= f2) ? 1 : -1));
	}
	
	
//	public static void main(String[] args) throws GazDocumentNotFoundException {
//		GazComparator gazCompare = new GazComparator();
//		TreeMap<GazDocument, Integer> queryTermFrequencies = new TreeMap<GazDocument, Integer>(gazCompare);
//		gazCompare.setFreqMap(queryTermFrequencies);
//		System.out.println("HELLO");
////		System.out.println(gazCOm);
//		System.out.println("HAH");
//		queryTermFrequencies.put(new ZDocument("/home/hadi"), 2);
//		queryTermFrequencies.put(new ZDocument("/home/hadi"), 3);		
//	}
}

