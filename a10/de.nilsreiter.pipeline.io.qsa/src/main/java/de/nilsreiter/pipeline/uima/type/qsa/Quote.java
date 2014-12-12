

/* First created by JCasGen Fri Dec 12 10:16:34 CET 2014 */
package de.nilsreiter.pipeline.uima.type.qsa;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Dec 12 10:16:34 CET 2014
 * XML source: /Users/reiterns/Documents/Java/a10/de.nilsreiter.pipeline.io.qsa/src/main/java/de/nilsreiter/pipeline/io/QSATypes.xml
 * @generated */
public class Quote extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Quote.class);
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
  protected Quote() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Quote(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Quote(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Quote(JCas jcas, int begin, int end) {
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
  //* Feature: Id

  /** getter for Id - gets 
   * @generated
   * @return value of the feature 
   */
  public String getId() {
    if (Quote_Type.featOkTst && ((Quote_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "de.nilsreiter.pipeline.uima.type.qsa.Quote");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Quote_Type)jcasType).casFeatCode_Id);}
    
  /** setter for Id - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(String v) {
    if (Quote_Type.featOkTst && ((Quote_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "de.nilsreiter.pipeline.uima.type.qsa.Quote");
    jcasType.ll_cas.ll_setStringValue(addr, ((Quote_Type)jcasType).casFeatCode_Id, v);}    
   
    
  //*--------------*
  //* Feature: Speaker

  /** getter for Speaker - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSpeaker() {
    if (Quote_Type.featOkTst && ((Quote_Type)jcasType).casFeat_Speaker == null)
      jcasType.jcas.throwFeatMissing("Speaker", "de.nilsreiter.pipeline.uima.type.qsa.Quote");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Quote_Type)jcasType).casFeatCode_Speaker);}
    
  /** setter for Speaker - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSpeaker(String v) {
    if (Quote_Type.featOkTst && ((Quote_Type)jcasType).casFeat_Speaker == null)
      jcasType.jcas.throwFeatMissing("Speaker", "de.nilsreiter.pipeline.uima.type.qsa.Quote");
    jcasType.ll_cas.ll_setStringValue(addr, ((Quote_Type)jcasType).casFeatCode_Speaker, v);}    
  }

    