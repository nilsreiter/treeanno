

/* First created by JCasGen Wed Mar 11 13:47:39 CET 2015 */
package de.nilsreiter.pipeline.tense.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Mar 18 18:16:14 CET 2015
 * XML source: /Users/reiterns/Documents/Java/de.nilsreiter.pipeline.tense/src/main/java/de/nilsreiter/pipeline/tense/Tense.xml
 * @generated */
public class Tense extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Tense.class);
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
  protected Tense() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Tense(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Tense(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Tense(JCas jcas, int begin, int end) {
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
  //* Feature: Tense

  /** getter for Tense - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTense() {
    if (Tense_Type.featOkTst && ((Tense_Type)jcasType).casFeat_Tense == null)
      jcasType.jcas.throwFeatMissing("Tense", "de.nilsreiter.pipeline.tense.type.Tense");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Tense_Type)jcasType).casFeatCode_Tense);}
    
  /** setter for Tense - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTense(String v) {
    if (Tense_Type.featOkTst && ((Tense_Type)jcasType).casFeat_Tense == null)
      jcasType.jcas.throwFeatMissing("Tense", "de.nilsreiter.pipeline.tense.type.Tense");
    jcasType.ll_cas.ll_setStringValue(addr, ((Tense_Type)jcasType).casFeatCode_Tense, v);}    
  }

    