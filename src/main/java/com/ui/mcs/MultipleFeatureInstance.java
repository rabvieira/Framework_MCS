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

public class MultipleFeatureInstance extends DenseInstance{
	
	ArrayList<AbstractInstance> instanceSet;
	int feature;

	public MultipleFeatureInstance(final ArrayList<AbstractInstance> instanceSet){
		super(instanceSet.get(0));
		this.instanceSet = new ArrayList<AbstractInstance>();
		int i;
		for(i=0;i<instanceSet.size();i++)
			this.instanceSet.add((AbstractInstance)instanceSet.get(i).copy());
		feature = 0;
	}

	public ArrayList<AbstractInstance> toArray(){
		return new ArrayList<AbstractInstance>(instanceSet);
	}

	public MultipleFeatureInstance clone(){
		MultipleFeatureInstance copy = new MultipleFeatureInstance(new ArrayList<AbstractInstance>(instanceSet));
		return copy;
	}

	public void addFeature(AbstractInstance feat){
		instanceSet.add(feat);
	}

	public Instance getFeature(int index){
		return instanceSet.get(index);
	}

	public void setFeature(int index, Instance inst){
		instanceSet.set(index, (AbstractInstance)inst);
	}

	public void selectFeature(int index){
		feature = index;
	}

	public int currentFeatureIndex(){
		return feature;
	}

	public int numberOfFeatures(){
		return instanceSet.size();
	}

	public java.lang.Object	copy(){
		return clone();
	}
	public Instance	copy(double[] values){
		return instanceSet.get(feature).copy(values);
	}
	public java.lang.String	getRevision(){
		return instanceSet.get(feature).getRevision();
	}
	public int	index(int position){
		return instanceSet.get(feature).index(position);
	}
	public Instance	mergeInstance(Instance inst){
		return instanceSet.get(feature).mergeInstance(inst);
	}
	public int	numAttributes(){
		return instanceSet.get(feature).numAttributes();
	}
	public int	numValues(){
		return instanceSet.get(feature).numValues();
	}
	public void	replaceMissingValues(double[] array){
		instanceSet.get(feature).replaceMissingValues(array);
	}
	public void	setValue(int attIndex, double value){
		instanceSet.get(feature).setValue(attIndex, value);
	}
	public void	setValueSparse(int indexOfIndex, double value){
		instanceSet.get(feature).setValueSparse(indexOfIndex, value);
	}
	public double[]	toDoubleArray(){
		return instanceSet.get(feature).toDoubleArray();
	}
	public java.lang.String	toStringNoWeight(){
		return instanceSet.get(feature).toStringNoWeight();
	}
	public java.lang.String	toStringNoWeight(int afterDecimalPoint){
		return instanceSet.get(feature).toStringNoWeight(afterDecimalPoint);
	}
	public double	value(int attIndex){
		return instanceSet.get(feature).value(attIndex);
	}
	public Attribute	attribute(int index){
		return instanceSet.get(feature).attribute(index);
	}
	public Attribute	attributeSparse(int indexOfIndex){
		return instanceSet.get(feature).attributeSparse(indexOfIndex);
	}
	public Attribute	classAttribute(){
		return instanceSet.get(feature).classAttribute();
	}
	public int	classIndex(){
		return instanceSet.get(feature).classIndex();
	}
	public boolean	classIsMissing(){
		return instanceSet.get(feature).classIsMissing();
	}
	public double	classValue(){
		return instanceSet.get(feature).classValue();
	}
	public Instances	dataset(){
		return instanceSet.get(feature).dataset();
	}
	public void	deleteAttributeAt(int position){
		instanceSet.get(feature).deleteAttributeAt(position);
	}
	public java.util.Enumeration<Attribute>	enumerateAttributes(){
		return instanceSet.get(feature).enumerateAttributes();
	}
	public boolean	equalHeaders(Instance inst){
		return instanceSet.get(feature).equalHeaders(inst);
	}
	public java.lang.String	equalHeadersMsg(Instance inst){
		return instanceSet.get(feature).equalHeadersMsg(inst);
	}
	public boolean	hasMissingValue(){
		return instanceSet.get(feature).hasMissingValue();
	}
	public void	insertAttributeAt(int position){
		instanceSet.get(feature).insertAttributeAt(position);
	}
	public boolean	isMissing(Attribute att){
		return instanceSet.get(feature).isMissing(att);
	}
	public boolean	isMissing(int attIndex){
		return instanceSet.get(feature).isMissing(attIndex);
	}
	public boolean	isMissingSparse(int indexOfIndex){
		return instanceSet.get(feature).isMissingSparse(indexOfIndex);
	}
	public int	numClasses(){
		return instanceSet.get(feature).numClasses();
	}
	public void	setClassMissing(){
		instanceSet.get(feature).setClassMissing();
	}
	public void	setClassValue(double value){
		instanceSet.get(feature).setClassValue(value);
	}
	public java.lang.String	toString(){
		return instanceSet.get(feature).toString();
	}
	public double	value(Attribute att){
		return instanceSet.get(feature).value(att);
	}
	public double	valueSparse(int indexOfIndex){
		return instanceSet.get(feature).valueSparse(indexOfIndex);
	}
	public boolean	equals(Object obj){
		return instanceSet.get(feature).equals(obj);
	}
}