

/* First created by JCasGen Fri Dec 12 10:16:34 CET 2014 */
package de.nilsreiter.pipeline.uima.type.qsa;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;


/** 
 * Updated by JCasGen Fri Dec 12 10:16:34 CET 2014
 * XML source: /Users/reiterns/Documents/Java/a10/de.nilsreiter.pipeline.io.qsa/src/main/java/de/nilsreiter/pipeline/io/QSATypes.xml
 * @generated */
public class QSAType extends NamedEntity {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(QSAType.class);
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
  protected QSAType() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public QSAType(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public QSAType(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public QSAType(JCas jcas, int begin, int end) {
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
  //* Feature: Gender

  /** getter for Gender - gets 
   * @generated
   * @return value of the feature 
   */
  public String getGender() {
    if (QSAType_Type.featOkTst && ((QSAType_Type)jcasType).casFeat_Gender == null)
      jcasType.jcas.throwFeatMissing("Gender", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    return jcasType.ll_cas.ll_getStringValue(addr, ((QSAType_Type)jcasType).casFeatCode_Gender);}
    
  /** setter for Gender - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGender(String v) {
    if (QSAType_Type.featOkTst && ((QSAType_Type)jcasType).casFeat_Gender == null)
      jcasType.jcas.throwFeatMissing("Gender", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    jcasType.ll_cas.ll_setStringValue(addr, ((QSAType_Type)jcasType).casFeatCode_Gender, v);}    
   
    
  //*--------------*
  //* Feature: EntityId

  /** getter for EntityId - gets 
   * @generated
   * @return value of the feature 
   */
  public String getEntityId() {
    if (QSAType_Type.featOkTst && ((QSAType_Type)jcasType).casFeat_EntityId == null)
      jcasType.jcas.throwFeatMissing("EntityId", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    return jcasType.ll_cas.ll_getStringValue(addr, ((QSAType_Type)jcasType).casFeatCode_EntityId);}
    
  /** setter for EntityId - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEntityId(String v) {
    if (QSAType_Type.featOkTst && ((QSAType_Type)jcasType).casFeat_EntityId == null)
      jcasType.jcas.throwFeatMissing("EntityId", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    jcasType.ll_cas.ll_setStringValue(addr, ((QSAType_Type)jcasType).casFeatCode_EntityId, v);}    
   
    
  //*--------------*
  //* Feature: Modifier

  /** getter for Modifier - gets 
   * @generated
   * @return value of the feature 
   */
  public String getModifier() {
    if (QSAType_Type.featOkTst && ((QSAType_Type)jcasType).casFeat_Modifier == null)
      jcasType.jcas.throwFeatMissing("Modifier", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    return jcasType.ll_cas.ll_getStringValue(addr, ((QSAType_Type)jcasType).casFeatCode_Modifier);}
    
  /** setter for Modifier - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setModifier(String v) {
    if (QSAType_Type.featOkTst && ((QSAType_Type)jcasType).casFeat_Modifier == null)
      jcasType.jcas.throwFeatMissing("Modifier", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    jcasType.ll_cas.ll_setStringValue(addr, ((QSAType_Type)jcasType).casFeatCode_Modifier, v);}    
   
    
  //*--------------*
  //* Feature: MentionType

  /** getter for MentionType - gets 
   * @generated
   * @return value of the feature 
   */
  public String getMentionType() {
    if (QSAType_Type.featOkTst && ((QSAType_Type)jcasType).casFeat_MentionType == null)
      jcasType.jcas.throwFeatMissing("MentionType", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    return jcasType.ll_cas.ll_getStringValue(addr, ((QSAType_Type)jcasType).casFeatCode_MentionType);}
    
  /** setter for MentionType - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setMentionType(String v) {
    if (QSAType_Type.featOkTst && ((QSAType_Type)jcasType).casFeat_MentionType == null)
      jcasType.jcas.throwFeatMissing("MentionType", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    jcasType.ll_cas.ll_setStringValue(addr, ((QSAType_Type)jcasType).casFeatCode_MentionType, v);}    
   
    
  //*--------------*
  //* Feature: Id

  /** getter for Id - gets 
   * @generated
   * @return value of the feature 
   */
  public String getId() {
    if (QSAType_Type.featOkTst && ((QSAType_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    return jcasType.ll_cas.ll_getStringValue(addr, ((QSAType_Type)jcasType).casFeatCode_Id);}
    
  /** setter for Id - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(String v) {
    if (QSAType_Type.featOkTst && ((QSAType_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    jcasType.ll_cas.ll_setStringValue(addr, ((QSAType_Type)jcasType).casFeatCode_Id, v);}    
  }

    