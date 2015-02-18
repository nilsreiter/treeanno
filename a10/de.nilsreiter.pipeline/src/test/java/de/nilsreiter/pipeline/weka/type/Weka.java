

/* First created by JCasGen Wed Feb 18 16:33:28 CET 2015 */
package de.nilsreiter.pipeline.weka.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Feb 18 16:33:28 CET 2015
 * XML source: /Users/reiterns/Documents/Java/a10/de.nilsreiter.pipeline/src/test/java/de/nilsreiter/pipeline/weka/Weka.xml
 * @generated */
public class Weka extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Weka.class);
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
  protected Weka() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Weka(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Weka(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Weka(JCas jcas, int begin, int end) {
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
  //* Feature: Feature1

  /** getter for Feature1 - gets 
   * @generated
   * @return value of the feature 
   */
  public String getFeature1() {
    if (Weka_Type.featOkTst && ((Weka_Type)jcasType).casFeat_Feature1 == null)
      jcasType.jcas.throwFeatMissing("Feature1", "de.nilsreiter.pipeline.weka.type.Weka");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Weka_Type)jcasType).casFeatCode_Feature1);}
    
  /** setter for Feature1 - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setFeature1(String v) {
    if (Weka_Type.featOkTst && ((Weka_Type)jcasType).casFeat_Feature1 == null)
      jcasType.jcas.throwFeatMissing("Feature1", "de.nilsreiter.pipeline.weka.type.Weka");
    jcasType.ll_cas.ll_setStringValue(addr, ((Weka_Type)jcasType).casFeatCode_Feature1, v);}    
   
    
  //*--------------*
  //* Feature: Feature2

  /** getter for Feature2 - gets 
   * @generated
   * @return value of the feature 
   */
  public int getFeature2() {
    if (Weka_Type.featOkTst && ((Weka_Type)jcasType).casFeat_Feature2 == null)
      jcasType.jcas.throwFeatMissing("Feature2", "de.nilsreiter.pipeline.weka.type.Weka");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Weka_Type)jcasType).casFeatCode_Feature2);}
    
  /** setter for Feature2 - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setFeature2(int v) {
    if (Weka_Type.featOkTst && ((Weka_Type)jcasType).casFeat_Feature2 == null)
      jcasType.jcas.throwFeatMissing("Feature2", "de.nilsreiter.pipeline.weka.type.Weka");
    jcasType.ll_cas.ll_setIntValue(addr, ((Weka_Type)jcasType).casFeatCode_Feature2, v);}    
   
    
  //*--------------*
  //* Feature: Feature3

  /** getter for Feature3 - gets 
   * @generated
   * @return value of the feature 
   */
  public double getFeature3() {
    if (Weka_Type.featOkTst && ((Weka_Type)jcasType).casFeat_Feature3 == null)
      jcasType.jcas.throwFeatMissing("Feature3", "de.nilsreiter.pipeline.weka.type.Weka");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Weka_Type)jcasType).casFeatCode_Feature3);}
    
  /** setter for Feature3 - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setFeature3(double v) {
    if (Weka_Type.featOkTst && ((Weka_Type)jcasType).casFeat_Feature3 == null)
      jcasType.jcas.throwFeatMissing("Feature3", "de.nilsreiter.pipeline.weka.type.Weka");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Weka_Type)jcasType).casFeatCode_Feature3, v);}    
   
    
  //*--------------*
  //* Feature: Feature4

  /** getter for Feature4 - gets 
   * @generated
   * @return value of the feature 
   */
  public String getFeature4() {
    if (Weka_Type.featOkTst && ((Weka_Type)jcasType).casFeat_Feature4 == null)
      jcasType.jcas.throwFeatMissing("Feature4", "de.nilsreiter.pipeline.weka.type.Weka");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Weka_Type)jcasType).casFeatCode_Feature4);}
    
  /** setter for Feature4 - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setFeature4(String v) {
    if (Weka_Type.featOkTst && ((Weka_Type)jcasType).casFeat_Feature4 == null)
      jcasType.jcas.throwFeatMissing("Feature4", "de.nilsreiter.pipeline.weka.type.Weka");
    jcasType.ll_cas.ll_setStringValue(addr, ((Weka_Type)jcasType).casFeatCode_Feature4, v);}    
  }

    