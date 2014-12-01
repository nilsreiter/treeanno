

/* First created by JCasGen Mon Dec 01 13:35:29 CET 2014 */
package de.nilsreiter.pipeline.uima.entitydetection.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.AnnotationBase;


/** 
 * Updated by JCasGen Mon Dec 01 13:35:29 CET 2014
 * XML source: /Users/reiterns/Documents/Java/a10/de.nilsreiter.pipeline.entitydetection/src/main/java/de/nilsreiter/pipeline/uima/entitydetection/Entity.xml
 * @generated */
public class Entity extends AnnotationBase {
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

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: Name

  /** getter for Name - gets 
   * @generated
   * @return value of the feature 
   */
  public String getName() {
    if (Entity_Type.featOkTst && ((Entity_Type)jcasType).casFeat_Name == null)
      jcasType.jcas.throwFeatMissing("Name", "de.nilsreiter.pipeline.uima.entitydetection.type.Entity");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Entity_Type)jcasType).casFeatCode_Name);}
    
  /** setter for Name - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setName(String v) {
    if (Entity_Type.featOkTst && ((Entity_Type)jcasType).casFeat_Name == null)
      jcasType.jcas.throwFeatMissing("Name", "de.nilsreiter.pipeline.uima.entitydetection.type.Entity");
    jcasType.ll_cas.ll_setStringValue(addr, ((Entity_Type)jcasType).casFeatCode_Name, v);}    
  }

    