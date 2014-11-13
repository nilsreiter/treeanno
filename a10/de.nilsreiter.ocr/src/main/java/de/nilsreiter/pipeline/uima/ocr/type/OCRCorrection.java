

/* First created by JCasGen Wed Jul 16 15:46:54 CEST 2014 */
package de.nilsreiter.pipeline.uima.ocr.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Sep 04 08:21:46 CEST 2014
 * XML source: /Users/reiterns/Documents/Workspace/a10/de.nilsreiter.ocr/src/main/java/de/nilsreiter/pipeline/uima/ocr/types.xml
 * @generated */
public class OCRCorrection extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(OCRCorrection.class);
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
  protected OCRCorrection() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public OCRCorrection(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public OCRCorrection(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public OCRCorrection(JCas jcas, int begin, int end) {
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
  //* Feature: Correction

  /** getter for Correction - gets 
   * @generated
   * @return value of the feature 
   */
  public String getCorrection() {
    if (OCRCorrection_Type.featOkTst && ((OCRCorrection_Type)jcasType).casFeat_Correction == null)
      jcasType.jcas.throwFeatMissing("Correction", "de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection");
    return jcasType.ll_cas.ll_getStringValue(addr, ((OCRCorrection_Type)jcasType).casFeatCode_Correction);}
    
  /** setter for Correction - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCorrection(String v) {
    if (OCRCorrection_Type.featOkTst && ((OCRCorrection_Type)jcasType).casFeat_Correction == null)
      jcasType.jcas.throwFeatMissing("Correction", "de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection");
    jcasType.ll_cas.ll_setStringValue(addr, ((OCRCorrection_Type)jcasType).casFeatCode_Correction, v);}    
   
    
  //*--------------*
  //* Feature: Level

  /** getter for Level - gets 
   * @generated
   * @return value of the feature 
   */
  public int getLevel() {
    if (OCRCorrection_Type.featOkTst && ((OCRCorrection_Type)jcasType).casFeat_Level == null)
      jcasType.jcas.throwFeatMissing("Level", "de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection");
    return jcasType.ll_cas.ll_getIntValue(addr, ((OCRCorrection_Type)jcasType).casFeatCode_Level);}
    
  /** setter for Level - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLevel(int v) {
    if (OCRCorrection_Type.featOkTst && ((OCRCorrection_Type)jcasType).casFeat_Level == null)
      jcasType.jcas.throwFeatMissing("Level", "de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection");
    jcasType.ll_cas.ll_setIntValue(addr, ((OCRCorrection_Type)jcasType).casFeatCode_Level, v);}    
  }

    