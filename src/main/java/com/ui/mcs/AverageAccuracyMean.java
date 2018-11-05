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

public class AverageAccuracyMean extends AbstractSelectionMetrics{

	private Evaluation eval;

	public AverageAccuracyMean(){
	}

	public Double applyMeasure(Instances train, Instances test, AbstractClassifier classifier) throws Exception{
		Double avg_acc=0.0;
		int i, nc = test.numClasses();
		eval = new Evaluation(train);
		eval.evaluateModel(classifier, test);
		for(i=0;i<nc;i++)
			avg_acc += (eval.numTruePositives(i)/(eval.numTruePositives(i)+eval.numFalseNegatives(i)));
		avg_acc /= nc;
		avg_acc *= 100;		
		//System.out.println(avg_acc);
		return (avg_acc);
	}

	public ArrayList<Boolean> select() throws Exception{
		Double mean = 0.0;
		ArrayList<Double> m = getMeasures();
		ArrayList<Boolean> r = new ArrayList<Boolean>();
		Threshold<Double> t;
		for(Double i : m)
			mean += i;
		mean = mean/m.size();
		t = new Threshold<Double>(mean);
		t.setArray(m);
		return (t.select());
	}

}