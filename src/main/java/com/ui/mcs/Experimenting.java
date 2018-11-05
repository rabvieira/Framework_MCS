package com.ui.mcs;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import com.ui.Message;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;

public class Experimenting {
	
	public String run_experiment(Message message, String fileName) throws Exception{	
		StringBuilder buffer = new StringBuilder("");
		DataSource source;
		int n;
		String pathname = new String("upload-dir/");
		ArrayList<String> train = new ArrayList<String>();
		ArrayList<Instances> trainInst = new ArrayList<Instances>();
		File path = new File(pathname);
		File[] feat = path.listFiles();
		
		//prints message object which contains user's parameters
		print_stuff(message);
		
		//adds the input arffs to a array of strings
		for(File i : feat){
			if(i.getName().endsWith("arff"))
			{
				train.add(i.getPath());
			}
		}
		
		//creates an array of Instances for each arff
		for(n=0; n<train.size(); n++){
			source = new DataSource(train.get(n));
			trainInst.add(source.getDataSet());
			trainInst.get(n).setClassIndex(trainInst.get(n).numAttributes()-1);
			//trainInst.get(n).deleteAttributeAt(trainInst.get(n).numAttributes()-2);
			source = null;
		}
		
		//creates an array of MultipleFeatureInstances
		MultipleFeatureInstances trainArray = new MultipleFeatureInstances(trainInst);
		
		//creates an array of classifiers
		ArrayList<AbstractClassifier> classifiers = new ArrayList<AbstractClassifier>();
		//classifiers.add(new IBk());
		
		//weka.classifiers.lazy.IBk -K 1 -W 0 -A "weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R first-last\"" ref
		String str = message.getContentClassifiers().replace("\'", "\"");
		String split[] = str.split("\\s+");
		
		int numClassifier = 0;
		int i,j,k, count, size;
		
		for(j=0 ; j<split.length ; j++)
		{
			if(split[j].contains("weka.classifiers")) //weka.classifiers //weka-stable-3.8.0.classifiers
			{
				numClassifier++;
			}
		}
		
		k=0;
		count=0;
		size=200;
		String classname;
		String[] options_ = new String[size];
		
		for(j=0 ; j<numClassifier ; j++)
		{
			if(split[k].contains("weka.classifiers"))
			{
				classname = split[k];
				if(k+1 < split.length)
				{
					
					while(!split[k+1].contains("weka.classifiers"))
					{
						options_[count] = split[k+1];
						k++;
						count++;
						if(k+1 >= split.length)
							break;
					}
				}
					
				String[] copy = new String[count];
				for(i=0 ; i<count ; i++)
				{
					copy[i] = options_[i];
					//System.out.print(options_[i] + " ");
					//options_[i] = "";
				}
				Classifier c     = (Classifier) Utils.forName(Classifier.class, classname, copy);
				classifiers.add((AbstractClassifier) c);
				System.out.println();
				k++;
				count=0;
			}
		}
		
		//creates the AbstractDiversityMeasure object
 		ArrayList<AbstractDiversityMeasure> dm = new ArrayList<AbstractDiversityMeasure>();
		dm.add(new QStatistic());
		dm.add(new DoubleFaultMeasure());
		dm.add(new DisagreementMeasure());
		dm.add(new CorrelationCoefficient());
		dm.add(new InterraterAgreement());

		//creates the metrics object
		AverageAccuracyMean metrics = new AverageAccuracyMean();

		//selects the best classifiers
		int c_star = 9; //todo: prunning
		Concensus con = new Concensus(dm, 100, c_star, metrics);
		
		//SVM for fusion
		SMO svm = new SMO();
		
		//adds classifiers to MCS
		MCSClassifier MCS;
		MCS = new MCSClassifier(classifiers, con, 75, svm); //75*depends on the folds

		//trains MCS Classifier
		MCS.buildClassifier(trainArray);
		
	    //result file
		String option = pathname + "result.arff";
		PrintWriter writer = new PrintWriter(option, "UTF-8");
		
	    // other options
	    int runs = 5;
	    //int folds = 5;
	    int folds = (int)message.getFolds();
		
	    // perform cross-validation
	    for (i = 0; i < runs; i++) {
	      // randomize data
	      int seed = i + 1;
	      Random rand = new Random(seed);
	      Instances randData = new Instances(trainInst.get(0));
	      randData.randomize(rand);
	      if (randData.classAttribute().isNominal())
	        randData.stratify(folds);

	      Evaluation eval = new Evaluation(randData);
	      for (n = 0; n < folds; n++) {
	        Instances train_ = randData.trainCV(folds, n);
	        Instances test_ = randData.testCV(folds, n);

	        // build and evaluate classifier
	        Classifier clsCopy = AbstractClassifier.makeCopy(MCS.getClassifier(0)); //cls
	        clsCopy.buildClassifier(train_);	 //train
	        eval.evaluateModel(clsCopy, test_);	 //test
	      }
	    
	      // output evaluation
	      System.out.println();
	      System.out.println("=== Setup run " + (i+1) + " ===");
	      buffer.append("=== Setup run " + (i+1) + " ===\n");
	      //System.out.println("Classifier: " + MCS.getClass().getName() + " " + Utils.joinOptions(MCS.getOptions()));
	      System.out.println("Dataset: " + trainInst.get(i).relationName());
	      buffer.append("Dataset: " + trainInst.get(i).relationName() + "\n");
	      System.out.println("Folds: " + folds);
	      buffer.append("Folds: " + folds + "\n");
	      System.out.println("Seed: " + seed);
	      //System.out.println("Classifier: " + MCS.getClassifier(i));
	      System.out.println();
	      System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation run " + (i+1) + "===", true)+eval.toMatrixString());
	      buffer.append(eval.toSummaryString("=== " + folds + "-fold Cross-validation run " + (i+1) + "===", true)+eval.toMatrixString()); 
	      buffer.append("\n");
	    }
	    
	    writer.println(buffer);
		writer.close();
	
		System.out.println("done");
		return buffer.toString();
	}

	private void print_stuff(Message message)
	{
    	System.err.println("Folds: " + message.getFolds());
    	System.err.println("Classifiers");
    	System.err.println(message.getContentClassifiers());
    	System.err.println("Fusion");
    	System.err.println(message.getContentFusion());
    	System.err.println("Evaluation");
    	System.err.println(message.getCheckbox_1());
    	System.err.println(message.getCheckbox_2());
    	System.err.println("Statistic");
    	System.err.println(message.getCheckbox_3());
    	System.err.println(message.getCheckbox_4());
	}
}
