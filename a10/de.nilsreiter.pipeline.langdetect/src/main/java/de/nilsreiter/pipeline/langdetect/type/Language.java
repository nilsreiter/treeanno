

/* First created by JCasGen Mon Oct 13 18:17:47 CEST 2014 */
package de.nilsreiter.pipeline.langdetect.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Oct 13 18:39:53 CEST 2014
 * XML source: /Users/reiterns/Documents/Java/de.nilsreiter.pipeline.langdetect/src/main/java/de/nilsreiter/pipeline/langdetect/Language.xml
 * @generated */
public class Language extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Language.class);
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
  protected Language() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Language(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Language(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Language(JCas jcas, int begin, int end) {
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
  //* Feature: Language

  /** getter for Language - gets 
   * @generated
   * @return value of the feature 
   */
  public String getLanguage() {
    if (Language_Type.featOkTst && ((Language_Type)jcasType).casFeat_Language == null)
      jcasType.jcas.throwFeatMissing("Language", "de.nilsreiter.pipeline.langdetect.type.Language");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Language_Type)jcasType).casFeatCode_Language);}
    
  /** setter for Language - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLanguage(String v) {
    if (Language_Type.featOkTst && ((Language_Type)jcasType).casFeat_Language == null)
      jcasType.jcas.throwFeatMissing("Language", "de.nilsreiter.pipeline.langdetect.type.Language");
    jcasType.ll_cas.ll_setStringValue(addr, ((Language_Type)jcasType).casFeatCode_Language, v);}    
   
    
  //*--------------*
  //* Feature: Confidence

  /** getter for Confidence - gets 
   * @generated
   * @return value of the feature 
   */
  public double getConfidence() {
    if (Language_Type.featOkTst && ((Language_Type)jcasType).casFeat_Confidence == null)
      jcasType.jcas.throwFeatMissing("Confidence", "de.nilsreiter.pipeline.langdetect.type.Language");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Language_Type)jcasType).casFeatCode_Confidence);}
    
  /** setter for Confidence - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setConfidence(double v) {
    if (Language_Type.featOkTst && ((Language_Type)jcasType).casFeat_Confidence == null)
      jcasType.jcas.throwFeatMissing("Confidence", "de.nilsreiter.pipeline.langdetect.type.Language");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Language_Type)jcasType).casFeatCode_Confidence, v);}    
  }

    