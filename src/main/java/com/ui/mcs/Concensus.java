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

public class Concensus extends AbstractInstanceBasedSelection{
	
	private ArrayList<ArrayList<Boolean>> matrixV, ranking;
	private ArrayList<ArrayList<ClassifierPairStruct>> matrixDM;
	private ArrayList<Boolean> selected;
	private ArrayList<Integer> histogram;
	private ArrayList<Double> secondPriority;
	private int firstFilter, c;
	private Threshold<Integer> threshold;
	private AbstractSelectionMetrics metrics;

	public Concensus(ArrayList<AbstractDiversityMeasure> dms, int firstFilter, int c, 
		AbstractSelectionMetrics metrics){
		super(dms);
		this.firstFilter = firstFilter;
		this.c = c;
		this.metrics = metrics;
	}

	public ArrayList<Boolean> select() throws Exception{
		makeMatrix();
	    metrics.initialize(train, classifiers, instancesArray);
		diversityCombination();
		rank();
		makeHistogram();
		return selected;
	}

	private void makeMatrix() throws Exception{
		matrixV = new ArrayList<ArrayList<Boolean>>();   //inicialização da matriz
	    int i, contC = 0, j = 0, k;
	    double sum = 0.0;

	    baseClassifiersSize = classifiers.size()/instancesArray.size();
	    //System.out.println(baseClassifiersSize);
	    for(i=0;i<classifiers.size();i++)
			matrixV.add(new ArrayList<Boolean>());

	    for(i=0;i<classifiers.size();i++){
			for(k=0;k<instancesArray.get(j).size();k++){
				if((classifiers.get(i).classifyInstance(instancesArray.get(j).instance(k))) == instancesArray.get(j).instance(k).classValue()){
					matrixV.get(i).add(true);
				}else{
					matrixV.get(i).add(false);
				}
			}

			if((i+1) % baseClassifiersSize == 0)
	            j++;
	    }

	}

	private void diversityCombination(){
		int i, j, d;

		matrixDM = new ArrayList<ArrayList<ClassifierPairStruct>>();
		for(d=0;d<dms.size();d++){
			matrixDM.add(new ArrayList<ClassifierPairStruct>());
			for(i=0;i<matrixV.size();i++){
				for(j=i;j<matrixV.size();j++){
					matrixDM.get(d).add(new ClassifierPairStruct(dms.get(d), dms.get(d).measure(matrixV.get(i), matrixV.get(j)), i, j));
				}
			}
		}
	}

	private void rank(){
		int d, i;
		ranking = new ArrayList<ArrayList<Boolean>>();
		RankingSelection<ClassifierPairStruct> rs = new RankingSelection<ClassifierPairStruct>(firstFilter);

		rs.setInverted(true);

		for(d=0;d<dms.size();d++){
			rs.setArray(matrixDM.get(d));
			ranking.add(rs.select());
		}
	}

	private void makeHistogram() throws Exception{
		RankingSelection<Double> rv = new RankingSelection<Double>(c);
		ArrayList<Double> score = new ArrayList<Double>();
		ArrayList<Boolean> measureApplied = metrics.select();
		secondPriority = metrics.getMeasures();
		histogram = new ArrayList<Integer>(Collections.nCopies(classifiers.size(), 0));
		int i,j,m;

		for(i=0;i<dms.size();i++){
			for(j=0;j<ranking.get(i).size();j++){
				if(ranking.get(i).get(j) == true){
					histogram.set(matrixDM.get(i).get(j).c1, histogram.get(matrixDM.get(i).get(j).c1)+1);
					histogram.set(matrixDM.get(i).get(j).c2, histogram.get(matrixDM.get(i).get(j).c2)+1);
				}
			}
		}

		//System.out.println("8: " + histogram.get(8) + "\n" + histogram);
		//System.out.println(secondPriority);
		for(i=0;i<classifiers.size();i++){
			if(measureApplied.get(i)){
				m = 1;
				//System.out.println(">=");
			}else{
				m = 0;
				//System.out.println("<");
			}
			score.add((double) ((100*histogram.get(i)*m) + secondPriority.get(i)));
		}
		//System.out.println(score);

		rv.setInverted(true);
		rv.setArray(score);

		selected = rv.select();
		//System.out.println(selected);
	}
}