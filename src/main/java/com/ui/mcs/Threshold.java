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

public class Threshold<T extends Comparable<T>> extends GenericSelectionMethod<T>{

	private T t;
	private ArrayList<Boolean> out;
	
	public Threshold(T t){
		this.t = t;
	}


	public void setArray(ArrayList<T> array){
		out = new ArrayList<Boolean>();
		for(T element : array){
			if(element.compareTo(t) >= 0)
				out.add(true);
			else
				out.add(false);
		}
	}


	public ArrayList<Boolean> select(){
		return out;
	}
}