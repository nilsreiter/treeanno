

/* First created by JCasGen Mon Feb 16 11:38:56 CET 2015 */
package de.nilsreiter.pipeline.segmentation.clauselevel.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Feb 20 08:20:38 CET 2015
 * XML source: /Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/main/java/de/nilsreiter/pipeline/segmentation/clauselevel/CL.xml
 * @generated */
public class Clause extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Clause.class);
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
  protected Clause() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Clause(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Clause(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Clause(JCas jcas, int begin, int end) {
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
  //* Feature: Tense

  /** getter for Tense - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTense() {
    if (Clause_Type.featOkTst && ((Clause_Type)jcasType).casFeat_Tense == null)
      jcasType.jcas.throwFeatMissing("Tense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Clause_Type)jcasType).casFeatCode_Tense);}
    
  /** setter for Tense - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTense(String v) {
    if (Clause_Type.featOkTst && ((Clause_Type)jcasType).casFeat_Tense == null)
      jcasType.jcas.throwFeatMissing("Tense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    jcasType.ll_cas.ll_setStringValue(addr, ((Clause_Type)jcasType).casFeatCode_Tense, v);}    
   
    
  //*--------------*
  //* Feature: Extent

  /** getter for Extent - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getExtent() {
    if (Clause_Type.featOkTst && ((Clause_Type)jcasType).casFeat_Extent == null)
      jcasType.jcas.throwFeatMissing("Extent", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Clause_Type)jcasType).casFeatCode_Extent)));}
    
  /** setter for Extent - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setExtent(FSArray v) {
    if (Clause_Type.featOkTst && ((Clause_Type)jcasType).casFeat_Extent == null)
      jcasType.jcas.throwFeatMissing("Extent", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    jcasType.ll_cas.ll_setRefValue(addr, ((Clause_Type)jcasType).casFeatCode_Extent, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for Extent - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public Token getExtent(int i) {
    if (Clause_Type.featOkTst && ((Clause_Type)jcasType).casFeat_Extent == null)
      jcasType.jcas.throwFeatMissing("Extent", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Clause_Type)jcasType).casFeatCode_Extent), i);
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Clause_Type)jcasType).casFeatCode_Extent), i)));}

  /** indexed setter for Extent - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setExtent(int i, Token v) { 
    if (Clause_Type.featOkTst && ((Clause_Type)jcasType).casFeat_Extent == null)
      jcasType.jcas.throwFeatMissing("Extent", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Clause_Type)jcasType).casFeatCode_Extent), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Clause_Type)jcasType).casFeatCode_Extent), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: Head

  /** getter for Head - gets 
   * @generated
   * @return value of the feature 
   */
  public DepRel getHead() {
    if (Clause_Type.featOkTst && ((Clause_Type)jcasType).casFeat_Head == null)
      jcasType.jcas.throwFeatMissing("Head", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    return (DepRel)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Clause_Type)jcasType).casFeatCode_Head)));}
    
  /** setter for Head - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setHead(DepRel v) {
    if (Clause_Type.featOkTst && ((Clause_Type)jcasType).casFeat_Head == null)
      jcasType.jcas.throwFeatMissing("Head", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    jcasType.ll_cas.ll_setRefValue(addr, ((Clause_Type)jcasType).casFeatCode_Head, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    