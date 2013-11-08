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
				System.out.println("Model " + i + ": R=" + recalls[i] + " P="
						+ precissions[i]);
			else
				System.out.println("Model " + i + ": R=" + recalls[i] + " P="
						+ precissions[i] + " MAP=" + maps[i]);
		}
	}

	private void evaluateAll(int method) {
		// int p = 0, r = 0;
		for (int i = 0; i < tests.size(); i++) {
			ArrayList<GazDocument> retrieved = (ArrayList<GazDocument>) gazir
					.query(tests.get(i).getQueryText(), method, 20);
			List<GazDocument> relevant = tests.get(i).getRelevantDocuments();
			ArrayList<GazDocument> relevantRetrieved = new ArrayList<GazDocument>();

			for (GazDocument gazDocument : retrieved) {
				if (relevant.contains(gazDocument)) {
					relevantRetrieved.add(gazDocument);
				}
			}
			precissions[method] += relevantRetrieved.size() / retrieved.size();
			recalls[method] += relevantRetrieved.size() / relevant.size();
			
			
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
					pre += retRel / j;
				}
				maps[method]+= pre / relevant.size();
			}
		
		}

		maps[method] /= tests.size();
		precissions[method] /= tests.size();
		recalls[method] /= tests.size();

	}
}
