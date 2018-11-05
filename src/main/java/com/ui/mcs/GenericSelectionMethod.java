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

public abstract class GenericSelectionMethod<T extends Comparable> implements SelectionMethod{

	public abstract ArrayList<Boolean> select();
	
	public abstract void setArray(ArrayList<T> array);

	public ArrayList<Boolean> selectInverted(){
		ArrayList<Boolean> s = select();
		int i;
		for(i=0;i<s.size();i++){
			if(s.get(i))
				s.set(i, false);
			else
				s.set(i, true);
		}
		return s;
	}

}