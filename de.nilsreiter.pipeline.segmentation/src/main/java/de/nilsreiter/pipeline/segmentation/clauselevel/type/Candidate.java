

/* First created by JCasGen Fri Feb 20 08:20:38 CET 2015 */
package de.nilsreiter.pipeline.segmentation.clauselevel.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryCandidate;


/** 
 * Updated by JCasGen Fri Feb 20 08:20:38 CET 2015
 * XML source: /Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/main/java/de/nilsreiter/pipeline/segmentation/clauselevel/CL.xml
 * @generated */
public class Candidate extends SegmentBoundaryCandidate {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Candidate.class);
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
  protected Candidate() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Candidate(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Candidate(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Candidate(JCas jcas, int begin, int end) {
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
  //* Feature: BeforeTense

  /** getter for BeforeTense - gets 
   * @generated
   * @return value of the feature 
   */
  public String getBeforeTense() {
    if (Candidate_Type.featOkTst && ((Candidate_Type)jcasType).casFeat_BeforeTense == null)
      jcasType.jcas.throwFeatMissing("BeforeTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Candidate");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Candidate_Type)jcasType).casFeatCode_BeforeTense);}
    
  /** setter for BeforeTense - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setBeforeTense(String v) {
    if (Candidate_Type.featOkTst && ((Candidate_Type)jcasType).casFeat_BeforeTense == null)
      jcasType.jcas.throwFeatMissing("BeforeTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Candidate");
    jcasType.ll_cas.ll_setStringValue(addr, ((Candidate_Type)jcasType).casFeatCode_BeforeTense, v);}    
   
    
  //*--------------*
  //* Feature: AfterTense

  /** getter for AfterTense - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAfterTense() {
    if (Candidate_Type.featOkTst && ((Candidate_Type)jcasType).casFeat_AfterTense == null)
      jcasType.jcas.throwFeatMissing("AfterTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Candidate");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Candidate_Type)jcasType).casFeatCode_AfterTense);}
    
  /** setter for AfterTense - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAfterTense(String v) {
    if (Candidate_Type.featOkTst && ((Candidate_Type)jcasType).casFeat_AfterTense == null)
      jcasType.jcas.throwFeatMissing("AfterTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Candidate");
    jcasType.ll_cas.ll_setStringValue(addr, ((Candidate_Type)jcasType).casFeatCode_AfterTense, v);}    
  }

    