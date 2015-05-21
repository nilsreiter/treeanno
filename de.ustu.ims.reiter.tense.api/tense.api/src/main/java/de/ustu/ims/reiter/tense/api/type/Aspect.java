

/* First created by JCasGen Thu May 21 07:27:23 CEST 2015 */
package de.ustu.ims.reiter.tense.api.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu May 21 07:27:23 CEST 2015
 * XML source: /Users/reiterns/Documents/Java/de.ustu.ims.reiter.tense.api/tense.api/src/main/java/de/ustu/ims/reiter/tense/api/Tense.xml
 * @generated */
public class Aspect extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Aspect.class);
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
  protected Aspect() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Aspect(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Aspect(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Aspect(JCas jcas, int begin, int end) {
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
  //* Feature: Aspect

  /** getter for Aspect - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAspect() {
    if (Aspect_Type.featOkTst && ((Aspect_Type)jcasType).casFeat_Aspect == null)
      jcasType.jcas.throwFeatMissing("Aspect", "de.ustu.ims.reiter.tense.api.type.Aspect");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Aspect_Type)jcasType).casFeatCode_Aspect);}
    
  /** setter for Aspect - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAspect(String v) {
    if (Aspect_Type.featOkTst && ((Aspect_Type)jcasType).casFeat_Aspect == null)
      jcasType.jcas.throwFeatMissing("Aspect", "de.ustu.ims.reiter.tense.api.type.Aspect");
    jcasType.ll_cas.ll_setStringValue(addr, ((Aspect_Type)jcasType).casFeatCode_Aspect, v);}    
  }

    