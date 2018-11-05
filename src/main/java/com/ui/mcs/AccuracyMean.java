package com.ui.mcs;
/* 
	@author Victor Lúcio
	Federal University of São Paulo - ICT/UNIFESP
	"A Classifiers Fusion System Applied to Fenology"

	Multiple Classifier System
	from: Faria, Fabio "A Framework for Pattern Classifier Selection and Fusion", 2014

	Advisors: Jurandy Gomes de Almeida Junior <http://buscatextual.cnpq.br/buscatextual/visualizacv.do?id=K4736755E0>
			  Fabio Augusto Faria <http://buscatextual.cnpq.br/buscatextual/visualizacv.do?id=K4266712J6>
*/

import weka.classifiers.*;
import weka.core.*;
import java.util.*;

public class AccuracyMean extends AbstractSelectionMetrics{

	private Evaluation eval;

	public AccuracyMean(){
	}

	public Double applyMeasure(Instances train, Instances test, AbstractClassifier classifier) throws Exception{
		eval = new Evaluation(train);
		eval.evaluateModel(classifier, test);
		return (eval.correct()/test.size()*100);
	}

	public ArrayList<Boolean> select() throws Exception{
		Double mean = 0.0;
		ArrayList<Double> m = getMeasures();
		ArrayList<Boolean> r = new ArrayList<Boolean>();
		Threshold<Double> t;
		for(Double i : m)
			mean += i;
		mean = (Double) mean/m.size();
		//System.out.println("media: " + mean);
		t = new Threshold<Double>(mean);
		t.setArray(m);
		return (t.select());
	}

}