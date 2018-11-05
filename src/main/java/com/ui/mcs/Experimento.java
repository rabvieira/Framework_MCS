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
import weka.classifiers.lazy.*;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.*;
import weka.classifiers.bayes.*;
import weka.core.*;
import java.util.*;
import java.io.*;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.*;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.SMO;

public class Experimento{
	//public static void main(String args[]) throws Exception{
	public String run_experiment(long dataSet, ArrayList<Boolean> checkBoxClassifiers) throws Exception{
		int k, m, n;
		StringBuilder buffer = new StringBuilder();
		DataSource source;
		Scanner tec = new Scanner(System.in);
		String pathname = new String("data/");
		ArrayList<ArrayList<String>> test = new ArrayList<ArrayList<String>>(), train = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<Instances>> trainInst = new ArrayList<ArrayList<Instances>>(), testInst = new ArrayList<ArrayList<Instances>>();
		File path = new File(pathname);
		File[] fold, folder, feat = path.listFiles();
		for(File i : feat){
			if(i.getPath().equals("data" + File.separator + "new_feat_tes")){
				k=0;
				folder = i.listFiles();
				Arrays.sort(folder);
				for(File l : folder){
					test.add(new ArrayList<String>());
					fold = l.listFiles();
					Arrays.sort(fold);
					for(File j : fold){
						test.get(k).add(j.getPath());
					}
					k++;
				}
			}else if(i.isDirectory()){
				k=0;
				folder = i.listFiles();
				Arrays.sort(folder);
				for(File l : folder){
					train.add(new ArrayList<String>());
					fold = l.listFiles();
					Arrays.sort(fold);
					for(File j : fold){
						train.get(k).add(j.getPath());
					}
					k++;
				}
			}
		}
		for(n=0;n<train.get(0).size();n++){
			//System.out.println(n + " - train");
			trainInst.add(new ArrayList<Instances>());
			for(m=0;m<train.size();m++){
				source = new DataSource(train.get(m).get(n));
				trainInst.get(n).add(source.getDataSet());
				trainInst.get(n).get(m).setClassIndex(trainInst.get(n).get(m).numAttributes()-1);
				trainInst.get(n).get(m).deleteAttributeAt(trainInst.get(n).get(m).numAttributes()-2);
				source = null;
			}
		}
		for(n=0;n<test.get(0).size();n++){
			//System.out.println(n + " - test");
			testInst.add(new ArrayList<Instances>());
			for(m=0;m<test.size();m++){
				source = new DataSource(test.get(m).get(n));
				testInst.get(n).add(source.getDataSet());
				testInst.get(n).get(m).setClassIndex(testInst.get(n).get(m).numAttributes()-1);
				testInst.get(n).get(m).deleteAttributeAt(testInst.get(n).get(m).numAttributes()-2);
				source = null;
			}
		}
		
		ArrayList<MultipleFeatureInstances> trainArray = new ArrayList<MultipleFeatureInstances>();
		ArrayList<MultipleFeatureInstances> testArray = new ArrayList<MultipleFeatureInstances>();
		for(n=0;n<trainInst.size();n++){
			trainArray.add(new MultipleFeatureInstances(trainInst.get(n)));
			testArray.add(new MultipleFeatureInstances(testInst.get(n)));
		}

		ArrayList<AbstractClassifier> classifiers = new ArrayList<AbstractClassifier>();
		
		if(checkBoxClassifiers.get(0) == true)
			classifiers.add(new IBk());
		if(checkBoxClassifiers.get(1) == true)
			classifiers.add(new Bagging()); 		//add another classifier
		if(checkBoxClassifiers.get(2) == true)
			classifiers.add(new RandomTree()); 		//add another classifier
		if(checkBoxClassifiers.get(3) == true)
			classifiers.add(new J48()); 			//add another classifier

 		ArrayList<AbstractDiversityMeasure> dm = new ArrayList<AbstractDiversityMeasure>();
		dm.add(new QStatistic());
		dm.add(new DoubleFaultMeasure());
		dm.add(new DisagreementMeasure());
		dm.add(new CorrelationCoefficient());
		dm.add(new InterraterAgreement());

		AverageAccuracyMean metrics = new AverageAccuracyMean();
		//int x = Integer.parseInt(arg);
		//int x = tec.nextInt();
		int x = 4;
		Concensus con = new Concensus(dm, 100, x, metrics);

		MCSClassifier MCS;
		Evaluation eval;
		SMO svm = new SMO();
		System.out.println("running");
		//n = Integer.parseInt(arg);
		//n=tec.nextInt();
		n = 4;
		MCS = new MCSClassifier(classifiers, con, 75, svm);
		
		long startTime1 = System.currentTimeMillis();
		long startTime2 = System.currentTimeMillis();

		MCS.buildClassifier(trainArray.get(n));
		
		long stopTime1 = System.currentTimeMillis();

		eval = new Evaluation(trainArray.get(n));
		eval.evaluateModel(MCS, testArray.get(n));

		long stopTime2 = System.currentTimeMillis();
      	long elapsedTime1 = stopTime1 - startTime1;
      	long elapsedTime2 = stopTime2 - startTime2;

		//System.out.println(eval.toSummaryString());
		buffer.append(eval.toSummaryString());
		Double specificity, sensitivity, r=0.0;
		int i, nc = testArray.get(n).numClasses();
		
		Double avg_acc=0.0;
		for(i=0;i<nc;i++)
			avg_acc += (eval.numTruePositives(i)/(eval.numTruePositives(i)+eval.numFalseNegatives(i)));
		avg_acc /= nc;
		avg_acc *= 100;	

		//System.out.println("tempo treino: "+ elapsedTime1/1000.00 + " segundos");
		buffer.append("\n\ntempo treino: "+ elapsedTime1/1000.00 + " segundos");
		//System.out.println("tempo teste: "+ elapsedTime2/1000.00 + " segundos");
		buffer.append("\n\ntempo teste: "+ elapsedTime2/1000.00 + " segundos");
		//System.out.println("average: " + avg_acc);
		buffer.append("\n\naverage: " + avg_acc);
		return buffer.toString();
	}
}