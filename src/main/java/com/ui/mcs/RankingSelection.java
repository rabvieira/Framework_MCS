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

public class RankingSelection<T extends Comparable<T>> extends GenericSelectionMethod<T>{

	private int t;
	private ArrayList<Boolean> out;
	private ArrayList<Integer> ranking;
	private Boolean invert;
	
	public RankingSelection(int t){
		this.t = t;
		invert = false;
	}

	public void setArray(ArrayList<T> array){
		ranking = new ArrayList<Integer>();
		out = new ArrayList<Boolean>(Collections.nCopies(array.size(), false));
		int i, size;
		for(i=0;i<array.size();i++)
			ranking.add(i);
		Collections.sort(ranking, new IndexSort<T>(array));
		if(t > ranking.size()){
			t = ranking.size();
		}
	}

	public void setInverted(Boolean invert){
		this.invert = invert;
	}

	public ArrayList<Boolean> select(){
		int i;
		if(invert){
			for(i=0;i<t;i++){
				out.set(ranking.get(ranking.size()-1-i), true);
			}
		}else{
			for(i=0;i<t;i++){
				out.set(ranking.get(i), true);
			}
		}
		return out;
	}
}