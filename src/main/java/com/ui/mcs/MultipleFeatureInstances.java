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
			  
import weka.core.*;
import java.util.*;

public class MultipleFeatureInstances extends Instances{
	private int feature;
	private ArrayList<Instances> set;

	public MultipleFeatureInstances(ArrayList<Instances> dataset){
		super(dataset.get(0));
		set = new ArrayList<Instances>();
		int i;
		for(i=0;i<dataset.size();i++)
			set.add(new Instances(dataset.get(i)));
		feature = 0;
	}
	
	public MultipleFeatureInstances(ArrayList<MultipleFeatureInstance> dataset, ArrayList<ArrayList<Attribute>> attInfo){
		super("initialization", attInfo.get(0), 0); //vazio
		int i, j;
		set = new ArrayList<Instances>();
		for(i=0;i<dataset.get(0).numberOfFeatures();i++){
			set.add(new Instances("feature" + i, attInfo.get(i), dataset.size()));
		}
		for(i=0;i<dataset.get(0).numberOfFeatures();i++){	//organizou da maneira correta
			for(j=0;j<dataset.size();j++){
				set.get(i).add(dataset.get(j).getFeature(i));
			}
		}
		feature = 0;
	}

	public ArrayList<Instances> featuresToArray(){
		return (new ArrayList<Instances>(set));
	}

	public MultipleFeatureInstances clone(){
		MultipleFeatureInstances copy = new MultipleFeatureInstances(set);
		return copy;
	}

	public void addFeature(Instances dataset){
		set.add(dataset);
	}

	public Instances getFeature(int index){
		return set.get(index);
	}

	public void setFeature(int index, Instances inst){
		set.set(index, inst);
	}

	public void selectFeature(int index){
		feature = index;
	}

	public int currentFeatureIndex(){
		return feature;
	}

	public int numberOfFeatures(){
		return set.size();
	}

	public boolean add(Instance instance){
		if(instance instanceof MultipleFeatureInstance){
			MultipleFeatureInstance inst = (MultipleFeatureInstance) instance;
			int i;
			if(inst.numberOfFeatures() == numberOfFeatures()){
				for(i=0;i<set.size();i++)
					set.get(i).add(inst.getFeature(i));
				return true;
			}else{
				return false;
			}
		}else{
			return super.add(instance);
		}
	}
	public void add(int index, Instance instance){
		if(instance instanceof MultipleFeatureInstance){
			int i;
			MultipleFeatureInstance inst = (MultipleFeatureInstance) instance;
			if(inst.numberOfFeatures() == numberOfFeatures()){
				for(i=0;i<set.size();i++)
					set.get(i).add(index, inst.getFeature(i));
			}else
				System.err.println("Different number of features");
		}else{
			super.add(index, instance);
		}
	}
	public Attribute attribute(int index){
		return set.get(feature).attribute(index);
	}
	public Attribute attribute(java.lang.String name){
		return set.get(feature).attribute(name);
	}
	public AttributeStats attributeStats(int index){
		return set.get(feature).attributeStats(index);
	}
	public double[] attributeToDoubleArray(int index){
		return set.get(feature).attributeToDoubleArray(index);
	}
	public boolean	checkForAttributeType(int attType){
		return set.get(feature).checkForAttributeType(attType);
	}
	public boolean	checkForStringAttributes(){
		return set.get(feature).checkForStringAttributes();
	}
	public boolean	checkInstance(Instance instance){
		return set.get(feature).checkInstance(instance);
	}
	public Attribute classAttribute(){
		return set.get(feature).classAttribute();
	}
	public int	classIndex(){
		return set.get(feature).classIndex();
	}
	public void compactify(){
		set.get(feature).compactify();
	}
	public void delete(){
		int i;
		for(i=0;i<set.size();i++)
			set.get(i).delete();
	}
	public void delete(int index){
		int i;
		for(i=0;i<set.size();i++)
			set.get(i).delete(index);
	}
	public void deleteAttributeAt(int position){
		set.get(feature).deleteAttributeAt(position);
	}
	public void deleteAttributeType(int attType){
		set.get(feature).deleteAttributeType(attType);
	}
	public void deleteStringAttributes(){
		set.get(feature).deleteStringAttributes();
	}
	public void deleteWithMissing(Attribute att){
		set.get(feature).deleteWithMissing(att);
	}
	public void deleteWithMissing(int attIndex){
		set.get(feature).deleteWithMissing(attIndex);
	}
	public void deleteWithMissingClass(){
		set.get(feature).deleteWithMissingClass();
	}
	public java.util.Enumeration<Attribute> enumerateAttributes(){
		return set.get(feature).enumerateAttributes();
	}
	public java.util.Enumeration<Instance>	enumerateInstances(){
		return set.get(feature).enumerateInstances();
	}
	public boolean	equalHeaders(Instances dataset){
		return set.get(feature).equalHeaders(dataset);
	}
	public java.lang.String equalHeadersMsg(Instances dataset){
		return set.get(feature).equalHeadersMsg(dataset);
	}
	public Instance firstInstance(){
		return set.get(feature).firstInstance();
	}
	public Instance get(int index){
		return set.get(feature).get(index);
	}
	public java.util.Random getRandomNumberGenerator(long seed){
		return set.get(feature).getRandomNumberGenerator(seed);
	}
	public java.lang.String getRevision(){
		return set.get(feature).getRevision();
	}
	public void insertAttributeAt(Attribute att, int position){
		set.get(feature).insertAttributeAt(att, position);
	}
	public Instance instance(int index){
		int i;
		ArrayList<AbstractInstance> array = new ArrayList<AbstractInstance>();
		for(i=0;i<set.size();i++){
			array.add((AbstractInstance)set.get(i).instance(index).copy());
		}
		MultipleFeatureInstance r = new MultipleFeatureInstance(array);
		r.selectFeature(feature);
		return r;
	}
	public double kthSmallestValue(Attribute att, int k){
		return set.get(feature).kthSmallestValue(att, k);
	}
	public double kthSmallestValue(int attIndex, int k){
		return set.get(feature).kthSmallestValue(attIndex, k);
	}
	public Instance lastInstance(){
		return set.get(feature).lastInstance();
	}
	public double meanOrMode(Attribute att){
		return set.get(feature).meanOrMode(att);
	}
	public double meanOrMode(int attIndex){
		return set.get(feature).meanOrMode(attIndex);
	}
	public int	numAttributes(){
		return set.get(feature).numAttributes();
	}
	public int	numClasses(){
		return set.get(feature).numClasses();
	}
	public int	numDistinctValues(Attribute att){
		return set.get(feature).numDistinctValues(att);
	}
	public int	numDistinctValues(int attIndex){
		return set.get(feature).numDistinctValues(attIndex);
	}
	public int	numInstances(){
		return set.get(feature).numInstances();
	}
	public void randomize(java.util.Random random){
		set.get(feature).randomize(random);
	}
	public java.lang.String relationName(){
		return set.get(feature).relationName();
	}
	public void renameAttribute(Attribute att, java.lang.String name){
		set.get(feature).renameAttribute(att, name);
	}
	public void renameAttribute(int att, java.lang.String name){
		set.get(feature).renameAttribute(att, name);
	}
	public void renameAttributeValue(Attribute att, java.lang.String val, java.lang.String name){
		set.get(feature).renameAttributeValue(att, val, name);
	}
	public void renameAttributeValue(int att, int val, java.lang.String name){
		set.get(feature).renameAttributeValue(att, val, name);
	}
	public void replaceAttributeAt(Attribute att, int position){
		set.get(feature).replaceAttributeAt(att, position);
	}
	public Instances resample(java.util.Random random){
		return set.get(feature).resample(random);
	}
	public Instances resampleWithWeights(java.util.Random random){
		return set.get(feature).resampleWithWeights(random);
	}
	public Instances resampleWithWeights(java.util.Random random, boolean representUsingWeights){
		return set.get(feature).resampleWithWeights(random, representUsingWeights);
	}
	public Instances resampleWithWeights(java.util.Random random, boolean[] sampled){
		return set.get(feature).resampleWithWeights(random, sampled);
	}
	public Instances resampleWithWeights(java.util.Random random, boolean[] sampled, boolean representUsingWeights){
		return set.get(feature).resampleWithWeights(random, sampled, representUsingWeights);
	}
	public Instances resampleWithWeights(java.util.Random random, double[] weights){
		return set.get(feature).resampleWithWeights(random, weights);
	}
	public Instances resampleWithWeights(java.util.Random random, double[] weights, boolean[] sampled){
		return set.get(feature).resampleWithWeights(random, weights, sampled);
	}
	public Instances resampleWithWeights(java.util.Random random, double[] weights, boolean[] sampled, boolean representUsingWeights){
		return set.get(feature).resampleWithWeights(random, weights, sampled, representUsingWeights);
	}
	public Instance set(int index, Instance instance){
		return set.get(feature).set(index, instance);
	}
	public void setClass(Attribute att){
		set.get(feature).setClass(att);
	}
	public void setClassIndex(int classIndex){
		set.get(feature).setClassIndex(classIndex);
	}
	public void setRelationName(java.lang.String newName){
		set.get(feature).setRelationName(newName);
	}
	public int	size(){
		return set.get(feature).size();
	}
	public void sort(Attribute att){
		set.get(feature).sort(att);
	}
	public void sort(int attIndex){
		set.get(feature).sort(attIndex);
	}
	public void stableSort(Attribute att){
		set.get(feature).stableSort(att);
	}
	public void stableSort(int attIndex){
		set.get(feature).stableSort(attIndex);
	}
	public void stratify(int numFolds){
		set.get(feature).stratify(numFolds);
	}
	public Instances stringFreeStructure(){
		return set.get(feature).stringFreeStructure();
	}
	public double sumOfWeights(){
		return set.get(feature).sumOfWeights();
	}
	public void swap(int i, int j){
		set.get(feature).swap(i, j);
	}
	public Instances testCV(int numFolds, int numFold){
		return set.get(feature).testCV(numFolds, numFold);
	}
	public java.lang.String toString(){
		return set.get(feature).toString();
	}
	public java.lang.String toSummaryString(){
		return set.get(feature).toSummaryString();
	}
	public Instances trainCV(int numFolds, int numFold){
		return set.get(feature).trainCV(numFolds, numFold);
	}
	public Instances trainCV(int numFolds, int numFold, java.util.Random random){
		return set.get(feature).trainCV(numFolds, numFold, random);
	}
	public double variance(Attribute att){
		return set.get(feature).variance(att);
	}
	public double variance(int attIndex){
		return set.get(feature).variance(attIndex);
	}
	public double[] variances(){
		return set.get(feature).variances();
	}
	public boolean addAll(Collection<? extends Instance> c){
		return set.get(feature).addAll(c);
	}
	public void clear(){
		set.get(feature).clear();
	}
	public boolean equals(Object o){
		return set.get(feature).equals(o);
	}
	public int hashCode(){
		return set.get(feature).hashCode();
	}
	public int indexOf(Object o){
		return set.get(feature).indexOf(o);
	}
	public Iterator<Instance> iterator(){
		return set.get(feature).iterator();
	}
	public int	lastIndexOf(Object o){
		return set.get(feature).lastIndexOf(o);
	}
	public boolean	contains(Object o){
		return set.get(feature).contains(o);
	}
	public boolean	containsAll(Collection<?> c){
		return set.get(feature).containsAll(c);
	}
	public boolean	isEmpty(){
		return set.get(feature).isEmpty();
	}
	public boolean	remove(Object o){
		return set.get(feature).remove(o);
	}
	public boolean	removeAll(Collection<?> c){
		return set.get(feature).removeAll(c);
	}
	public boolean	retainAll(Collection<?> c){
		return set.get(feature).retainAll(c);
	}
	public Object[]	toArray(){
		return set.get(feature).toArray();
	}
	public <T> T[]	toArray(T[] a){
		return set.get(feature).toArray(a);
	}
}