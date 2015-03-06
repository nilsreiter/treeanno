

/* First created by JCasGen Fri Mar 06 09:31:00 CET 2015 */
package de.nilsreiter.pipeline.segmentation.type.v1;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Mar 06 09:31:00 CET 2015
 * XML source: /Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/main/java/de/nilsreiter/pipeline/segmentation/Segmentation.xml
 * @generated */
public class FeatureVector extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(FeatureVector.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected FeatureVector() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public FeatureVector(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public FeatureVector(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public FeatureVector(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: PreviousTense

  /** getter for PreviousTense - gets 
   * @generated
   * @return value of the feature 
   */
  public String getPreviousTense() {
    if (FeatureVector_Type.featOkTst && ((FeatureVector_Type)jcasType).casFeat_PreviousTense == null)
      jcasType.jcas.throwFeatMissing("PreviousTense", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    return jcasType.ll_cas.ll_getStringValue(addr, ((FeatureVector_Type)jcasType).casFeatCode_PreviousTense);}
    
  /** setter for PreviousTense - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPreviousTense(String v) {
    if (FeatureVector_Type.featOkTst && ((FeatureVector_Type)jcasType).casFeat_PreviousTense == null)
      jcasType.jcas.throwFeatMissing("PreviousTense", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    jcasType.ll_cas.ll_setStringValue(addr, ((FeatureVector_Type)jcasType).casFeatCode_PreviousTense, v);}    
   
    
  //*--------------*
  //* Feature: CurrentTense

  /** getter for CurrentTense - gets 
   * @generated
   * @return value of the feature 
   */
  public String getCurrentTense() {
    if (FeatureVector_Type.featOkTst && ((FeatureVector_Type)jcasType).casFeat_CurrentTense == null)
      jcasType.jcas.throwFeatMissing("CurrentTense", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    return jcasType.ll_cas.ll_getStringValue(addr, ((FeatureVector_Type)jcasType).casFeatCode_CurrentTense);}
    
  /** setter for CurrentTense - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCurrentTense(String v) {
    if (FeatureVector_Type.featOkTst && ((FeatureVector_Type)jcasType).casFeat_CurrentTense == null)
      jcasType.jcas.throwFeatMissing("CurrentTense", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    jcasType.ll_cas.ll_setStringValue(addr, ((FeatureVector_Type)jcasType).casFeatCode_CurrentTense, v);}    
  }

    