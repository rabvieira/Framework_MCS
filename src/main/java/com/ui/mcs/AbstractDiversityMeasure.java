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

import java.io.Serializable;
import java.util.*;

public abstract class AbstractDiversityMeasure implements Comparator<Double>, Serializable{

	public abstract double measure(ArrayList<Boolean> c1, ArrayList<Boolean> c2);
	public abstract int compare(Double o1, Double o2);

	public AbstractDiversityMeasure(){
	}

	public double[] correlation(ArrayList<Boolean> c1, ArrayList<Boolean> c2){
		int i;
		double ca = 0, cb = 0, cc = 0, cd = 0, a, b, c, d, total, aux;
		
		for(i=0;i<c1.size();i++){
			if(c1.get(i) && c1.get(i) == c2.get(i))
				ca++;
			else if(c1.get(i))
				cb++;
			else if(c2.get(i))
				cc++;
			else
				cd++;
		}

		total = ca+cb+cc+cd;
		aux = 100 / total;
		a = ca * aux;
		b = cb * aux;
		c = cc * aux;
		d = cd * aux;
		double[] r = {a,b,c,d};
		return(r);
	}

}
// a - acerto duplo, b - c1 acertou e c2 errou, c - c2 acertou e c1 errou, d - erro duplo}