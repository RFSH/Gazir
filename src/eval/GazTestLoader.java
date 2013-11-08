package eval;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import doc.GazCollection;
import doc.GazDocument;

public class GazTestLoader {
	private File queryDir;
	private File relFile;
	private GazCollection collection;
	public GazTestLoader(GazCollection collection, File queryDir, File relFile){
		this.collection = collection;
		this.queryDir = queryDir;
		this.relFile = relFile;
	}
	
	public List<GazTestQuery> loadTests(){
		Map<Integer, GazTestQuery> tests = new HashMap<Integer, GazTestQuery>();
		try{
			int count = 0;
			File[] files = queryDir.listFiles();
			for(File file : files){
				count++;
				String out = String.format("\rLoading test queries from %s... \t[%d/%d]", queryDir.getPath(), count, files.length);
				System.out.print(out);
				
				GazTestQuery test = new GazTestQuery(file);
				String query = test.readAll();
				System.out.println(query.length());
				test.setQueryText(query);
				test.close();
				if(test.getId() != 0)
					tests.put(test.getId(), test);
			}
			System.out.println();
			
			count = 0;
			int total = tests.values().size();
			Scanner scanner = new Scanner(new FileInputStream(relFile));
			while(scanner.hasNextLine()){
				String line = scanner.nextLine().trim();
				if(line.isEmpty())
					continue;
				
				count++;
				String out = String.format("\rLoading relevancy file... \t[%d/%d]", count, total);
				System.out.print(out);
				
				Scanner lineScan = new Scanner(line);
				int testId = lineScan.nextInt();
				GazTestQuery test = tests.get(testId);
				
				while(lineScan.hasNextInt()){
					GazDocument doc = collection.getDocumentById(lineScan.nextInt());
					if(doc == null){
						System.out.println();
						System.out.println("Document " + doc + " not found in collection");
						continue;
					}
					test.addRelevantDocument(doc);
				}
			}
			System.out.println();
			scanner.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		ArrayList<GazTestQuery> testList = new ArrayList<GazTestQuery>(tests.values());
		for (int i = 0; i < testList.size(); i++) {
			System.out.println(testList.get(i).getId()+":  " + testList.get(i).getRelevantDocuments().size());
		}
		return testList;
	}
}
