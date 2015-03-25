

/* First created by JCasGen Tue Mar 24 18:31:04 CET 2015 */
package de.ustu.narr;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Mar 24 18:31:04 CET 2015
 * XML source: /Users/reiterns/Documents/Java/de.nilsreiter.segmentation/src/main/java/de/nilsreiter/segmentation/main/eckbert/typesystem.xml
 * @generated */
public class StoryPart extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(StoryPart.class);
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
  protected StoryPart() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public StoryPart(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public StoryPart(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public StoryPart(JCas jcas, int begin, int end) {
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
  //* Feature: LevelType

  /** getter for LevelType - gets 
   * @generated
   * @return value of the feature 
   */
  public String getLevelType() {
    if (StoryPart_Type.featOkTst && ((StoryPart_Type)jcasType).casFeat_LevelType == null)
      jcasType.jcas.throwFeatMissing("LevelType", "de.ustu.narr.StoryPart");
    return jcasType.ll_cas.ll_getStringValue(addr, ((StoryPart_Type)jcasType).casFeatCode_LevelType);}
    
  /** setter for LevelType - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLevelType(String v) {
    if (StoryPart_Type.featOkTst && ((StoryPart_Type)jcasType).casFeat_LevelType == null)
      jcasType.jcas.throwFeatMissing("LevelType", "de.ustu.narr.StoryPart");
    jcasType.ll_cas.ll_setStringValue(addr, ((StoryPart_Type)jcasType).casFeatCode_LevelType, v);}    
   
    
  //*--------------*
  //* Feature: Timespan

  /** getter for Timespan - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTimespan() {
    if (StoryPart_Type.featOkTst && ((StoryPart_Type)jcasType).casFeat_Timespan == null)
      jcasType.jcas.throwFeatMissing("Timespan", "de.ustu.narr.StoryPart");
    return jcasType.ll_cas.ll_getStringValue(addr, ((StoryPart_Type)jcasType).casFeatCode_Timespan);}
    
  /** setter for Timespan - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTimespan(String v) {
    if (StoryPart_Type.featOkTst && ((StoryPart_Type)jcasType).casFeat_Timespan == null)
      jcasType.jcas.throwFeatMissing("Timespan", "de.ustu.narr.StoryPart");
    jcasType.ll_cas.ll_setStringValue(addr, ((StoryPart_Type)jcasType).casFeatCode_Timespan, v);}    
  }

    