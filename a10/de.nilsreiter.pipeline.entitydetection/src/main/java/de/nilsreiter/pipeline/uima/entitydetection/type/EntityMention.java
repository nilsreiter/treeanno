

/* First created by JCasGen Mon Dec 01 13:32:14 CET 2014 */
package de.nilsreiter.pipeline.uima.entitydetection.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.cas.AnnotationBase;


/** 
 * Updated by JCasGen Mon Dec 01 18:22:46 CET 2014
 * XML source: /Users/reiterns/Documents/Java/a10/de.nilsreiter.pipeline.entitydetection/src/main/java/de/nilsreiter/pipeline/uima/entitydetection/Entity.xml
 * @generated */
public class EntityMention extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(EntityMention.class);
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
  protected EntityMention() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public EntityMention(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public EntityMention(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public EntityMention(JCas jcas, int begin, int end) {
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
    if (EntityMention_Type.featOkTst && ((EntityMention_Type)jcasType).casFeat_Identifier == null)
      jcasType.jcas.throwFeatMissing("Identifier", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    return jcasType.ll_cas.ll_getStringValue(addr, ((EntityMention_Type)jcasType).casFeatCode_Identifier);}
    
  /** setter for Identifier - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setIdentifier(String v) {
    if (EntityMention_Type.featOkTst && ((EntityMention_Type)jcasType).casFeat_Identifier == null)
      jcasType.jcas.throwFeatMissing("Identifier", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    jcasType.ll_cas.ll_setStringValue(addr, ((EntityMention_Type)jcasType).casFeatCode_Identifier, v);}    
   
    
  //*--------------*
  //* Feature: Source

  /** getter for Source - gets 
   * @generated
   * @return value of the feature 
   */
  public AnnotationBase getSource() {
    if (EntityMention_Type.featOkTst && ((EntityMention_Type)jcasType).casFeat_Source == null)
      jcasType.jcas.throwFeatMissing("Source", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    return (AnnotationBase)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((EntityMention_Type)jcasType).casFeatCode_Source)));}
    
  /** setter for Source - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSource(AnnotationBase v) {
    if (EntityMention_Type.featOkTst && ((EntityMention_Type)jcasType).casFeat_Source == null)
      jcasType.jcas.throwFeatMissing("Source", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((EntityMention_Type)jcasType).casFeatCode_Source, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Head

  /** getter for Head - gets 
   * @generated
   * @return value of the feature 
   */
  public Token getHead() {
    if (EntityMention_Type.featOkTst && ((EntityMention_Type)jcasType).casFeat_Head == null)
      jcasType.jcas.throwFeatMissing("Head", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((EntityMention_Type)jcasType).casFeatCode_Head)));}
    
  /** setter for Head - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setHead(Token v) {
    if (EntityMention_Type.featOkTst && ((EntityMention_Type)jcasType).casFeat_Head == null)
      jcasType.jcas.throwFeatMissing("Head", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((EntityMention_Type)jcasType).casFeatCode_Head, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Entity

  /** getter for Entity - gets 
   * @generated
   * @return value of the feature 
   */
  public Entity getEntity() {
    if (EntityMention_Type.featOkTst && ((EntityMention_Type)jcasType).casFeat_Entity == null)
      jcasType.jcas.throwFeatMissing("Entity", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    return (Entity)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((EntityMention_Type)jcasType).casFeatCode_Entity)));}
    
  /** setter for Entity - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEntity(Entity v) {
    if (EntityMention_Type.featOkTst && ((EntityMention_Type)jcasType).casFeat_Entity == null)
      jcasType.jcas.throwFeatMissing("Entity", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((EntityMention_Type)jcasType).casFeatCode_Entity, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    