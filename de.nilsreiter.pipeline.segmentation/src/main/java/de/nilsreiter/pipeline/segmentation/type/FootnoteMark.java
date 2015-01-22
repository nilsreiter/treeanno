

/* First created by JCasGen Thu Jan 15 10:25:43 CET 2015 */
package de.nilsreiter.pipeline.segmentation.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Jan 22 12:02:01 CET 2015
 * XML source: /Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/main/java/de/nilsreiter/pipeline/segmentation/Segmentation.xml
 * @generated */
public class FootnoteMark extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(FootnoteMark.class);
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
  protected FootnoteMark() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public FootnoteMark(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public FootnoteMark(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public FootnoteMark(JCas jcas, int begin, int end) {
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
  //* Feature: Number

  /** getter for Number - gets 
   * @generated
   * @return value of the feature 
   */
  public int getNumber() {
    if (FootnoteMark_Type.featOkTst && ((FootnoteMark_Type)jcasType).casFeat_Number == null)
      jcasType.jcas.throwFeatMissing("Number", "de.nilsreiter.pipeline.segmentation.type.FootnoteMark");
    return jcasType.ll_cas.ll_getIntValue(addr, ((FootnoteMark_Type)jcasType).casFeatCode_Number);}
    
  /** setter for Number - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setNumber(int v) {
    if (FootnoteMark_Type.featOkTst && ((FootnoteMark_Type)jcasType).casFeat_Number == null)
      jcasType.jcas.throwFeatMissing("Number", "de.nilsreiter.pipeline.segmentation.type.FootnoteMark");
    jcasType.ll_cas.ll_setIntValue(addr, ((FootnoteMark_Type)jcasType).casFeatCode_Number, v);}    
  }

    