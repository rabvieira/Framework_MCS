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

import java.io.Serializable;
import java.util.*;


public abstract class AbstractSelectionMetrics implements SelectionMethod, Serializable{

	private ArrayList<Double> measures;
	private ArrayList<Instances> train, test;

	public void initialize(ArrayList<Instances> train, ArrayList<AbstractClassifier> classifiers, ArrayList<Instances> test) throws Exception{
		int i, baseClassifiersSize, j;
		baseClassifiersSize = classifiers.size()/train.size();
		measures = new ArrayList<Double>();
		for(i=0, j=0;i<classifiers.size();i++){
			measures.add(applyMeasure(train.get(j), test.get(j), classifiers.get(i)));

			if((i+1) % baseClassifiersSize == 0)
	            j++;
		}
	}

	public ArrayList<Double> getMeasures(){
		return new ArrayList<Double>(measures);
	}

	public abstract Double applyMeasure(Instances train, Instances test, AbstractClassifier classifier) throws Exception;
}