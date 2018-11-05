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

public abstract class AbstractInstanceBasedSelection extends AbstractClassifierSelection implements Serializable{

	public int baseClassifiersSize; // baseClassifiersSize = classifiers.size()/instances.size()
	public ArrayList<Instances> instancesArray, train;
	public ArrayList<AbstractDiversityMeasure> dms;
	public ArrayList<AbstractClassifier> classifiers;

	public AbstractInstanceBasedSelection(ArrayList<AbstractDiversityMeasure> dms){ //can be null
		this.dms = dms;
		instancesArray = null;
		classifiers = null;
	}

	public abstract ArrayList<Boolean> select() throws Exception;

	public void setClassifiers(ArrayList<AbstractClassifier> classifiers){
		this.classifiers = new ArrayList<AbstractClassifier>(classifiers);
	}

	public void setInstances(ArrayList<Instances> instancesArray){
		this.instancesArray = new ArrayList<Instances>(instancesArray);
	}

	public void setTrainingSet(ArrayList<Instances> train){ //if necessary
		this.train = new ArrayList<Instances>(train);
	}

	public Boolean check(){
		if(classifiers != null && instancesArray != null)
			return true;
		else
			return false;
	}

}