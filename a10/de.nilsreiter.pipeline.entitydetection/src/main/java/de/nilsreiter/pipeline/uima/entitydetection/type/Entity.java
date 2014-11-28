

/* First created by JCasGen Fri Nov 28 10:40:44 CET 2014 */
package de.nilsreiter.pipeline.uima.entitydetection.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


import org.apache.uima.jcas.cas.AnnotationBase;


/** 
 * Updated by JCasGen Fri Nov 28 10:57:04 CET 2014
 * XML source: /Users/reiterns/Documents/Java/a10/de.nilsreiter.pipeline.entitydetection/src/main/java/de/nilsreiter/pipeline/uima/entitydetection/Entity.xml
 * @generated */
public class Entity extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Entity.class);
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
  protected Entity() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Entity(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Entity(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Entity(JCas jcas, int begin, int end) {
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
  //* Feature: Identifier

  /** getter for Identifier - gets 
   * @generated
   * @return value of the feature 
   */
  public String getIdentifier() {
    if (Entity_Type.featOkTst && ((Entity_Type)jcasType).casFeat_Identifier == null)
      jcasType.jcas.throwFeatMissing("Identifier", "de.nilsreiter.pipeline.uima.entitydetection.type.Entity");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Entity_Type)jcasType).casFeatCode_Identifier);}
    
  /** setter for Identifier - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setIdentifier(String v) {
    if (Entity_Type.featOkTst && ((Entity_Type)jcasType).casFeat_Identifier == null)
      jcasType.jcas.throwFeatMissing("Identifier", "de.nilsreiter.pipeline.uima.entitydetection.type.Entity");
    jcasType.ll_cas.ll_setStringValue(addr, ((Entity_Type)jcasType).casFeatCode_Identifier, v);}    
   
    
  //*--------------*
  //* Feature: Source

  /** getter for Source - gets 
   * @generated
   * @return value of the feature 
   */
  public AnnotationBase getSource() {
    if (Entity_Type.featOkTst && ((Entity_Type)jcasType).casFeat_Source == null)
      jcasType.jcas.throwFeatMissing("Source", "de.nilsreiter.pipeline.uima.entitydetection.type.Entity");
    return (AnnotationBase)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Entity_Type)jcasType).casFeatCode_Source)));}
    
  /** setter for Source - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSource(AnnotationBase v) {
    if (Entity_Type.featOkTst && ((Entity_Type)jcasType).casFeat_Source == null)
      jcasType.jcas.throwFeatMissing("Source", "de.nilsreiter.pipeline.uima.entitydetection.type.Entity");
    jcasType.ll_cas.ll_setRefValue(addr, ((Entity_Type)jcasType).casFeatCode_Source, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    