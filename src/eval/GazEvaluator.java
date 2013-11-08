package eval;

import gazir.GazIR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import doc.GazDocument;

public class GazEvaluator {
	private List<GazTestQuery> tests;
	private GazIR gazir;
	private double[] recalls = new double[5];
	private double[] precissions = new double[5];
	private double[] maps = new double[5];

	public GazEvaluator(List<GazTestQuery> tests, GazIR gazir) {
		this.tests = tests;
		this.gazir = gazir;
	}

	public void evaluate() {
		for (int i = 0; i < 5; i++) {
			evaluateAll(i);
		}

		print();
	}

	private void print() {
		for (int i = 0; i < 5; i++) {
			if (i == 0)
				System.out.println(String.format("Model %d: R = %.3f   P = %.3f", i, recalls[i], precissions[i]));
			else
				System.out.println(String.format("Model %d: R = %.3f   P = %.3f  MAP = %.3f", i, recalls[i], precissions[i], maps[i]));
		}
	}

	private void evaluateAll(int method) {
		// int p = 0, r = 0;
		for (int i = 0; i < tests.size(); i++) {
			System.out.print(String.format("\rEvaulating method %d \t[%d/%d]", method, i+1, tests.size()));
			List<GazDocument> retrieved =  gazir.query(tests.get(i).getQueryText(), method, 20);
			List<GazDocument> relevant = tests.get(i).getRelevantDocuments();
			ArrayList<GazDocument> relevantRetrieved = new ArrayList<GazDocument>();
//			System.out.println("_____________________");
//			System.out.println(tests.get(i).getQueryText());
//			System.out.println("RETRIEVED");
//			System.out.println(retrieved);
//			System.out.println("RELEVANT");
//			System.out.println(relevant);
//			
			for (GazDocument gazDocument : retrieved) {
				if (relevant.contains(gazDocument)) {
					relevantRetrieved.add(gazDocument);
				}
			}
			if(retrieved.size() != 0)
				precissions[method] += (double)relevantRetrieved.size() / retrieved.size();
			else
				precissions[method] += 1;
			
			if(relevant.size() != 0)				
				recalls[method] += (double)relevantRetrieved.size() / relevant.size();
			else
				recalls[method] += 1; 
			
			if(method!=0){
				int pre=0;
				for (GazDocument gazDocument : relevant){
					int retRel = 0, j;
					for (j=0; j < retrieved.size(); j++) {
						if(relevant.contains(retrieved.get(j)) ){
							retRel++;
						}
						if(gazDocument.equals(retrieved.get(j)))
							break; 
					}
					if(j!=0)
						pre += (double)retRel / j;
					else
						pre += 1;
				}
				if(relevant.size()!=0)
					maps[method]+= (double)pre / relevant.size();
			}
		
		}
		System.out.println();
		maps[method] /= tests.size();
		precissions[method] /= tests.size();
		recalls[method] /= tests.size();
	}
}
