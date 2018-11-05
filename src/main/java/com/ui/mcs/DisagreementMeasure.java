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

public class DisagreementMeasure extends AbstractDiversityMeasure{
	
	public DisagreementMeasure(){
		super();
	}

	public double measure(ArrayList<Boolean> c1, ArrayList<Boolean> c2){
		double[] v = new double[4];
		v = this.correlation(c1,c2);
		return((v[1]+v[2])/(v[0]+v[1]+v[2]+v[3]));
	}

	public int compare(Double o1, Double o2){ //quanto menor, maior a diversidade
		return (Double.compare(o1, o2));
	}
}