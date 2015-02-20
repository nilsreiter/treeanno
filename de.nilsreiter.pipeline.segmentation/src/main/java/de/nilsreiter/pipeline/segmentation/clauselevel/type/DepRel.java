

/* First created by JCasGen Thu Feb 12 18:27:18 CET 2015 */
package de.nilsreiter.pipeline.segmentation.clauselevel.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Feb 20 08:15:36 CET 2015
 * XML source: /Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/main/java/de/nilsreiter/pipeline/segmentation/clauselevel/CL.xml
 * @generated */
public class DepRel extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DepRel.class);
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
  protected DepRel() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public DepRel(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public DepRel(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public DepRel(JCas jcas, int begin, int end) {
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
  //* Feature: Govenor

  /** getter for Govenor - gets 
   * @generated
   * @return value of the feature 
   */
  public DepRel getGovenor() {
    if (DepRel_Type.featOkTst && ((DepRel_Type)jcasType).casFeat_Govenor == null)
      jcasType.jcas.throwFeatMissing("Govenor", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    return (DepRel)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DepRel_Type)jcasType).casFeatCode_Govenor)));}
    
  /** setter for Govenor - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGovenor(DepRel v) {
    if (DepRel_Type.featOkTst && ((DepRel_Type)jcasType).casFeat_Govenor == null)
      jcasType.jcas.throwFeatMissing("Govenor", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    jcasType.ll_cas.ll_setRefValue(addr, ((DepRel_Type)jcasType).casFeatCode_Govenor, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Token

  /** getter for Token - gets 
   * @generated
   * @return value of the feature 
   */
  public Token getToken() {
    if (DepRel_Type.featOkTst && ((DepRel_Type)jcasType).casFeat_Token == null)
      jcasType.jcas.throwFeatMissing("Token", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DepRel_Type)jcasType).casFeatCode_Token)));}
    
  /** setter for Token - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setToken(Token v) {
    if (DepRel_Type.featOkTst && ((DepRel_Type)jcasType).casFeat_Token == null)
      jcasType.jcas.throwFeatMissing("Token", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    jcasType.ll_cas.ll_setRefValue(addr, ((DepRel_Type)jcasType).casFeatCode_Token, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Dependents

  /** getter for Dependents - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getDependents() {
    if (DepRel_Type.featOkTst && ((DepRel_Type)jcasType).casFeat_Dependents == null)
      jcasType.jcas.throwFeatMissing("Dependents", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DepRel_Type)jcasType).casFeatCode_Dependents)));}
    
  /** setter for Dependents - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setDependents(FSArray v) {
    if (DepRel_Type.featOkTst && ((DepRel_Type)jcasType).casFeat_Dependents == null)
      jcasType.jcas.throwFeatMissing("Dependents", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    jcasType.ll_cas.ll_setRefValue(addr, ((DepRel_Type)jcasType).casFeatCode_Dependents, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for Dependents - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public DepRel getDependents(int i) {
    if (DepRel_Type.featOkTst && ((DepRel_Type)jcasType).casFeat_Dependents == null)
      jcasType.jcas.throwFeatMissing("Dependents", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DepRel_Type)jcasType).casFeatCode_Dependents), i);
    return (DepRel)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DepRel_Type)jcasType).casFeatCode_Dependents), i)));}

  /** indexed setter for Dependents - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setDependents(int i, DepRel v) { 
    if (DepRel_Type.featOkTst && ((DepRel_Type)jcasType).casFeat_Dependents == null)
      jcasType.jcas.throwFeatMissing("Dependents", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DepRel_Type)jcasType).casFeatCode_Dependents), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DepRel_Type)jcasType).casFeatCode_Dependents), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: Relation

  /** getter for Relation - gets 
   * @generated
   * @return value of the feature 
   */
  public String getRelation() {
    if (DepRel_Type.featOkTst && ((DepRel_Type)jcasType).casFeat_Relation == null)
      jcasType.jcas.throwFeatMissing("Relation", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DepRel_Type)jcasType).casFeatCode_Relation);}
    
  /** setter for Relation - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setRelation(String v) {
    if (DepRel_Type.featOkTst && ((DepRel_Type)jcasType).casFeat_Relation == null)
      jcasType.jcas.throwFeatMissing("Relation", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    jcasType.ll_cas.ll_setStringValue(addr, ((DepRel_Type)jcasType).casFeatCode_Relation, v);}    
  }

    