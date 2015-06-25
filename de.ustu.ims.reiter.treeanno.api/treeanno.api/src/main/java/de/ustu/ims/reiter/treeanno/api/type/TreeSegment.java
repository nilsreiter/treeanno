

/* First created by JCasGen Wed Jun 03 14:38:16 CEST 2015 */
package de.ustu.ims.reiter.treeanno.api.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Jun 25 09:23:07 CEST 2015
 * XML source: /Users/reiterns/Documents/treeanno/treeanno.git/de.ustu.ims.reiter.treeanno.api/treeanno.api/src/main/java/de/ustu/ims/reiter/treeanno/api/typesystem.xml
 * @generated */
public class TreeSegment extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TreeSegment.class);
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
  protected TreeSegment() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public TreeSegment(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public TreeSegment(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public TreeSegment(JCas jcas, int begin, int end) {
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
  //* Feature: Category

  /** getter for Category - gets 
   * @generated
   * @return value of the feature 
   */
  public String getCategory() {
    if (TreeSegment_Type.featOkTst && ((TreeSegment_Type)jcasType).casFeat_Category == null)
      jcasType.jcas.throwFeatMissing("Category", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TreeSegment_Type)jcasType).casFeatCode_Category);}
    
  /** setter for Category - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCategory(String v) {
    if (TreeSegment_Type.featOkTst && ((TreeSegment_Type)jcasType).casFeat_Category == null)
      jcasType.jcas.throwFeatMissing("Category", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    jcasType.ll_cas.ll_setStringValue(addr, ((TreeSegment_Type)jcasType).casFeatCode_Category, v);}    
   
    
  //*--------------*
  //* Feature: Parent

  /** getter for Parent - gets 
   * @generated
   * @return value of the feature 
   */
  public TreeSegment getParent() {
    if (TreeSegment_Type.featOkTst && ((TreeSegment_Type)jcasType).casFeat_Parent == null)
      jcasType.jcas.throwFeatMissing("Parent", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    return (TreeSegment)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TreeSegment_Type)jcasType).casFeatCode_Parent)));}
    
  /** setter for Parent - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setParent(TreeSegment v) {
    if (TreeSegment_Type.featOkTst && ((TreeSegment_Type)jcasType).casFeat_Parent == null)
      jcasType.jcas.throwFeatMissing("Parent", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    jcasType.ll_cas.ll_setRefValue(addr, ((TreeSegment_Type)jcasType).casFeatCode_Parent, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Id

  /** getter for Id - gets 
   * @generated
   * @return value of the feature 
   */
  public int getId() {
    if (TreeSegment_Type.featOkTst && ((TreeSegment_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    return jcasType.ll_cas.ll_getIntValue(addr, ((TreeSegment_Type)jcasType).casFeatCode_Id);}
    
  /** setter for Id - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(int v) {
    if (TreeSegment_Type.featOkTst && ((TreeSegment_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    jcasType.ll_cas.ll_setIntValue(addr, ((TreeSegment_Type)jcasType).casFeatCode_Id, v);}    
   
    
  //*--------------*
  //* Feature: Mark1

  /** getter for Mark1 - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getMark1() {
    if (TreeSegment_Type.featOkTst && ((TreeSegment_Type)jcasType).casFeat_Mark1 == null)
      jcasType.jcas.throwFeatMissing("Mark1", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((TreeSegment_Type)jcasType).casFeatCode_Mark1);}
    
  /** setter for Mark1 - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setMark1(boolean v) {
    if (TreeSegment_Type.featOkTst && ((TreeSegment_Type)jcasType).casFeat_Mark1 == null)
      jcasType.jcas.throwFeatMissing("Mark1", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((TreeSegment_Type)jcasType).casFeatCode_Mark1, v);}    
  }

    