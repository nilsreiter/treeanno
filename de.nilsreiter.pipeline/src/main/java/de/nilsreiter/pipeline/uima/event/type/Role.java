

/* First created by JCasGen Thu Jul 10 13:35:45 CEST 2014 */
package de.nilsreiter.pipeline.uima.event.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Jul 10 13:44:43 CEST 2014
 * XML source: /Users/reiterns/Documents/Workspace/a10/de.nilsreiter.pipeline/src/main/java/de/nilsreiter/pipeline/uima/event/Event.xml
 * @generated */
public class Role extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Role.class);
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
  protected Role() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Role(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Role(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Role(JCas jcas, int begin, int end) {
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
  //* Feature: name

  /** getter for name - gets 
   * @generated
   * @return value of the feature 
   */
  public String getName() {
    if (Role_Type.featOkTst && ((Role_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "de.nilsreiter.pipeline.uima.event.type.Role");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Role_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setName(String v) {
    if (Role_Type.featOkTst && ((Role_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "de.nilsreiter.pipeline.uima.event.type.Role");
    jcasType.ll_cas.ll_setStringValue(addr, ((Role_Type)jcasType).casFeatCode_name, v);}    
  }

    