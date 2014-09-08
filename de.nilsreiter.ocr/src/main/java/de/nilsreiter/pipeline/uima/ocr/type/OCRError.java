

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
public class OCRError extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(OCRError.class);
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
  protected OCRError() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public OCRError(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public OCRError(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public OCRError(JCas jcas, int begin, int end) {
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
  //* Feature: Description

  /** getter for Description - gets 
   * @generated
   * @return value of the feature 
   */
  public String getDescription() {
    if (OCRError_Type.featOkTst && ((OCRError_Type)jcasType).casFeat_Description == null)
      jcasType.jcas.throwFeatMissing("Description", "de.nilsreiter.pipeline.uima.ocr.type.OCRError");
    return jcasType.ll_cas.ll_getStringValue(addr, ((OCRError_Type)jcasType).casFeatCode_Description);}
    
  /** setter for Description - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setDescription(String v) {
    if (OCRError_Type.featOkTst && ((OCRError_Type)jcasType).casFeat_Description == null)
      jcasType.jcas.throwFeatMissing("Description", "de.nilsreiter.pipeline.uima.ocr.type.OCRError");
    jcasType.ll_cas.ll_setStringValue(addr, ((OCRError_Type)jcasType).casFeatCode_Description, v);}    
   
    
  //*--------------*
  //* Feature: Detector

  /** getter for Detector - gets 
   * @generated
   * @return value of the feature 
   */
  public String getDetector() {
    if (OCRError_Type.featOkTst && ((OCRError_Type)jcasType).casFeat_Detector == null)
      jcasType.jcas.throwFeatMissing("Detector", "de.nilsreiter.pipeline.uima.ocr.type.OCRError");
    return jcasType.ll_cas.ll_getStringValue(addr, ((OCRError_Type)jcasType).casFeatCode_Detector);}
    
  /** setter for Detector - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setDetector(String v) {
    if (OCRError_Type.featOkTst && ((OCRError_Type)jcasType).casFeat_Detector == null)
      jcasType.jcas.throwFeatMissing("Detector", "de.nilsreiter.pipeline.uima.ocr.type.OCRError");
    jcasType.ll_cas.ll_setStringValue(addr, ((OCRError_Type)jcasType).casFeatCode_Detector, v);}    
  }

    