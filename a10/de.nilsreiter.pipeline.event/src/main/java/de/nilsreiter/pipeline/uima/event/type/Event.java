

/* First created by JCasGen Fri Sep 12 17:14:42 CEST 2014 */
package de.nilsreiter.pipeline.uima.event.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Sep 12 17:14:42 CEST 2014
 * XML source: /Users/reiterns/Documents/Workspace/a10/de.nilsreiter.pipeline.event/src/main/java/de/nilsreiter/pipeline/uima/event/Event.xml
 * @generated */
public class Event extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Event.class);
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
  protected Event() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Event(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Event(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Event(JCas jcas, int begin, int end) {
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
  //* Feature: arguments

  /** getter for arguments - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getArguments() {
    if (Event_Type.featOkTst && ((Event_Type)jcasType).casFeat_arguments == null)
      jcasType.jcas.throwFeatMissing("arguments", "de.nilsreiter.pipeline.uima.event.type.Event");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Event_Type)jcasType).casFeatCode_arguments)));}
    
  /** setter for arguments - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setArguments(FSArray v) {
    if (Event_Type.featOkTst && ((Event_Type)jcasType).casFeat_arguments == null)
      jcasType.jcas.throwFeatMissing("arguments", "de.nilsreiter.pipeline.uima.event.type.Event");
    jcasType.ll_cas.ll_setRefValue(addr, ((Event_Type)jcasType).casFeatCode_arguments, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for arguments - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public Role getArguments(int i) {
    if (Event_Type.featOkTst && ((Event_Type)jcasType).casFeat_arguments == null)
      jcasType.jcas.throwFeatMissing("arguments", "de.nilsreiter.pipeline.uima.event.type.Event");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Event_Type)jcasType).casFeatCode_arguments), i);
    return (Role)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Event_Type)jcasType).casFeatCode_arguments), i)));}

  /** indexed setter for arguments - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setArguments(int i, Role v) { 
    if (Event_Type.featOkTst && ((Event_Type)jcasType).casFeat_arguments == null)
      jcasType.jcas.throwFeatMissing("arguments", "de.nilsreiter.pipeline.uima.event.type.Event");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Event_Type)jcasType).casFeatCode_arguments), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Event_Type)jcasType).casFeatCode_arguments), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: name

  /** getter for name - gets 
   * @generated
   * @return value of the feature 
   */
  public String getName() {
    if (Event_Type.featOkTst && ((Event_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "de.nilsreiter.pipeline.uima.event.type.Event");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Event_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setName(String v) {
    if (Event_Type.featOkTst && ((Event_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "de.nilsreiter.pipeline.uima.event.type.Event");
    jcasType.ll_cas.ll_setStringValue(addr, ((Event_Type)jcasType).casFeatCode_name, v);}    
   
    
  //*--------------*
  //* Feature: Anchor

  /** getter for Anchor - gets 
   * @generated
   * @return value of the feature 
   */
  public Annotation getAnchor() {
    if (Event_Type.featOkTst && ((Event_Type)jcasType).casFeat_Anchor == null)
      jcasType.jcas.throwFeatMissing("Anchor", "de.nilsreiter.pipeline.uima.event.type.Event");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Event_Type)jcasType).casFeatCode_Anchor)));}
    
  /** setter for Anchor - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAnchor(Annotation v) {
    if (Event_Type.featOkTst && ((Event_Type)jcasType).casFeat_Anchor == null)
      jcasType.jcas.throwFeatMissing("Anchor", "de.nilsreiter.pipeline.uima.event.type.Event");
    jcasType.ll_cas.ll_setRefValue(addr, ((Event_Type)jcasType).casFeatCode_Anchor, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    