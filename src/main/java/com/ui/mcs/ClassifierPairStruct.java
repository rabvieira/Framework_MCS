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

import java.util.*;
import java.io.Serializable;
import java.lang.*;

public class ClassifierPairStruct implements Comparable<ClassifierPairStruct>, Serializable{

	public Comparator<Double> comp;
	public double dmv; //diversity measure value
	public int c1, c2; //index of classifiers;

	public ClassifierPairStruct(){
	}

	public ClassifierPairStruct(Comparator<Double> comp, double dmv, int c1, int c2){
		this.comp = comp;
		this.dmv = dmv;
		this.c1 = c1;
		this.c2 = c2;
	}

	public int compareTo(ClassifierPairStruct o){
		return comp.compare(this.dmv, o.dmv);
	}

	public String toString() {
    	return String.valueOf(dmv);
	}
}