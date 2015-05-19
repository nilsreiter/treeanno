

/* First created by JCasGen Tue May 19 17:46:17 CEST 2015 */
package de.ustu.creta.segmentation.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue May 19 17:46:17 CEST 2015
 * XML source: /Users/reiterns/Documents/Java/de.ustu.creta.segmentation.api/segmentation.api/src/main/java/de/ustu/ims/segmentation/api/Segmentation.xml
 * @generated */
public class SegmentBoundary extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SegmentBoundary.class);
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
  protected SegmentBoundary() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public SegmentBoundary(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public SegmentBoundary(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public SegmentBoundary(JCas jcas, int begin, int end) {
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
  //* Feature: Level

  /** getter for Level - gets 
   * @generated
   * @return value of the feature 
   */
  public int getLevel() {
    if (SegmentBoundary_Type.featOkTst && ((SegmentBoundary_Type)jcasType).casFeat_Level == null)
      jcasType.jcas.throwFeatMissing("Level", "de.ustu.creta.segmentation.type.SegmentBoundary");
    return jcasType.ll_cas.ll_getIntValue(addr, ((SegmentBoundary_Type)jcasType).casFeatCode_Level);}
    
  /** setter for Level - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLevel(int v) {
    if (SegmentBoundary_Type.featOkTst && ((SegmentBoundary_Type)jcasType).casFeat_Level == null)
      jcasType.jcas.throwFeatMissing("Level", "de.ustu.creta.segmentation.type.SegmentBoundary");
    jcasType.ll_cas.ll_setIntValue(addr, ((SegmentBoundary_Type)jcasType).casFeatCode_Level, v);}    
  }

    