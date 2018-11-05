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

public class IndexSort<T extends Comparable<T>> implements Comparator<Integer>{
	private ArrayList<T> array;

	public IndexSort(ArrayList<T> array){
		this.array = array;
	}

	public int compare(Integer o1, Integer o2) {
		return (array.get(o1).compareTo(array.get(o2)));
	}
}