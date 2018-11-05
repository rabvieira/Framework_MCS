package com.ui.mcs;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.DefaultListModel;

import com.ui.Message;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.Range;
import weka.core.Utils;
import weka.experiment.ClassifierSplitEvaluator;
import weka.experiment.CrossValidationResultProducer;
import weka.experiment.Experiment;
import weka.experiment.InstancesResultListener;
import weka.experiment.PairedCorrectedTTester;
import weka.experiment.PairedTTester;
import weka.experiment.PropertyNode;
import weka.experiment.ResultMatrix;
import weka.experiment.ResultMatrixPlainText;
import weka.experiment.SplitEvaluator;

public class Experiments {
	StringBuilder buffer = new StringBuilder("test");
    private static final String PATH_FILE =     "C:\\Users\\rvieira\\Documents\\workspace-sts-3.8.4.RELEASE\\FrameworkMCS\\upload-dir\\";
    
	public String run_experiment(Message message, String fileName) throws Exception{
				
		print_stuff(message);
	    // 1. setup the experiment
	    System.out.println("Setting up...");
	    Experiment exp = new Experiment();
	    exp.setPropertyArray(new Classifier[0]);
	    exp.setUsePropertyIterator(true);

	    SplitEvaluator se = null;
	    Classifier sec    = null;
	    boolean classification = true;
	    se  = new ClassifierSplitEvaluator();
	    sec = ((ClassifierSplitEvaluator) se).getClassifier();

	    CrossValidationResultProducer cvrp = new CrossValidationResultProducer();
	    cvrp.setNumFolds((int) message.getFolds());
	    cvrp.setSplitEvaluator(se);
	        
	    PropertyNode[] propertyPath = new PropertyNode[2];
	    try {
	  	propertyPath[0] = new PropertyNode(
	  	    se, 
	  	    new PropertyDescriptor("splitEvaluator",
	  		CrossValidationResultProducer.class),
	  		CrossValidationResultProducer.class);
	  	propertyPath[1] = new PropertyNode(
	  	    sec, 
	  	    new PropertyDescriptor("classifier",
	  		se.getClass()),
	  		se.getClass());
	    }
	    catch (IntrospectionException e) {
	  	    e.printStackTrace();
	        }
	        
	    exp.setResultProducer(cvrp);
	    exp.setPropertyPath(propertyPath);

	    exp.setRunLower(1);
	    exp.setRunUpper(1);
//TODO: multiple classifiers
	    // classifier
	    //String option = "weka.classifiers.trees.J48";
	    String split[] = message.getContentClassifiers().split("\\s+");
	    String option = split[0];
	    String[] options = Utils.splitOptions(option);
	    String classname = options[0];
	    options[0]       = "";
	    Classifier c     = (Classifier) Utils.forName(Classifier.class, classname, options);
	    exp.setPropertyArray(new Classifier[]{c}); 
	    
	    // datasets
	    boolean data = false;
	    DefaultListModel model = new DefaultListModel();
	    do {
	      //option = Utils.getOption("t", args);
	      //option = PATH_FILE + "upload-dir\\" + fileName;
	    	option = PATH_FILE + fileName;
	      if (option.length() > 0) {
		File file = new File(option);
		if (!file.exists())
		  throw new IllegalArgumentException("File '" + option + "' does not exist!");
		data = true;
		model.addElement(file);
		//file.delete();
		//file.deleteOnExit();
	      }
	      option = "";
	    }
	    while (option.length() > 0);
	    if (!data)
	      throw new IllegalArgumentException("No data files provided!");
	    exp.setDatasets(model);
	    
	    // result
	    //option = Utils.getOption("result", args);
	    option = PATH_FILE + "result.arff";
	    if (option.length() == 0)
	      throw new IllegalArgumentException("No result file provided!");
	    InstancesResultListener irl = new InstancesResultListener();
	    irl.setOutputFile(new File(option));
	    exp.setResultListener(irl);
	    
	    // 2. run experiment
	    System.out.println("Initializing...");
	    exp.initialize();
	    System.out.println("Running...");
	    exp.runExperiment();
	    System.out.println("Finishing...");
	    exp.postProcess();
	    
	    // 3. calculate statistics and output them
	    System.out.println("Evaluating...");
	    PairedTTester tester = new PairedCorrectedTTester();
	    Instances result = new Instances(new BufferedReader(new FileReader(irl.getOutputFile())));
	    tester.setInstances(result);
	    tester.setSortColumn(-1);
	    tester.setRunColumn(result.attribute("Key_Run").index());
	    if (classification)
	      tester.setFoldColumn(result.attribute("Key_Fold").index());
	    tester.setDatasetKeyColumns(
		new Range(
		    "" 
		    + (result.attribute("Key_Dataset").index() + 1)));
	    tester.setResultsetKeyColumns(
		new Range(
		    "" 
		    + (result.attribute("Key_Scheme").index() + 1)
		    + ","
		    + (result.attribute("Key_Scheme_options").index() + 1)
		    + ","
		    + (result.attribute("Key_Scheme_version_ID").index() + 1)));
	    tester.setResultMatrix(new ResultMatrixPlainText());
	    tester.setDisplayedResultsets(null);       
	    tester.setSignificanceLevel(0.05);
	    tester.setShowStdDevs(true);
	    // fill result matrix (but discarding the output)
	    if (classification)
	      tester.multiResultsetFull(0, result.attribute("Percent_correct").index());
	    else
	      tester.multiResultsetFull(0, result.attribute("Correlation_coefficient").index());
	    // output results for reach dataset
	    //System.out.println("\nResult:");
	    buffer.append("\nResult:");
	    ResultMatrix matrix = tester.getResultMatrix();
	    for (int i = 0; i < matrix.getColCount(); i++) {
	      //System.out.println(matrix.getColName(i));
	    	buffer.append(matrix.getColName(i));
	      //System.out.println("    Perc. correct: " + matrix.getMean(i, 0));
	    	buffer.append("    Perc. correct: " + matrix.getMean(i, 0));
	      //System.out.println("    StdDev: " + matrix.getStdDev(i, 0));
	    	buffer.append("    StdDev: " + matrix.getStdDev(i, 0));
	    }
	    
	    
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
