
/* First created by JCasGen Mon Dec 01 13:32:14 CET 2014 */
package de.nilsreiter.pipeline.uima.entitydetection.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Thu Dec 11 20:54:14 CET 2014
 * @generated */
public class EntityMention_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (EntityMention_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = EntityMention_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new EntityMention(addr, EntityMention_Type.this);
  			   EntityMention_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new EntityMention(addr, EntityMention_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = EntityMention.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
 
  /** @generated */
  final Feature casFeat_Identifier;
  /** @generated */
  final int     casFeatCode_Identifier;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getIdentifier(int addr) {
        if (featOkTst && casFeat_Identifier == null)
      jcas.throwFeatMissing("Identifier", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Identifier);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIdentifier(int addr, String v) {
        if (featOkTst && casFeat_Identifier == null)
      jcas.throwFeatMissing("Identifier", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    ll_cas.ll_setStringValue(addr, casFeatCode_Identifier, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Source;
  /** @generated */
  final int     casFeatCode_Source;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getSource(int addr) {
        if (featOkTst && casFeat_Source == null)
      jcas.throwFeatMissing("Source", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Source);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSource(int addr, int v) {
        if (featOkTst && casFeat_Source == null)
      jcas.throwFeatMissing("Source", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    ll_cas.ll_setRefValue(addr, casFeatCode_Source, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Head;
  /** @generated */
  final int     casFeatCode_Head;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getHead(int addr) {
        if (featOkTst && casFeat_Head == null)
      jcas.throwFeatMissing("Head", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Head);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setHead(int addr, int v) {
        if (featOkTst && casFeat_Head == null)
      jcas.throwFeatMissing("Head", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    ll_cas.ll_setRefValue(addr, casFeatCode_Head, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Entity;
  /** @generated */
  final int     casFeatCode_Entity;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getEntity(int addr) {
        if (featOkTst && casFeat_Entity == null)
      jcas.throwFeatMissing("Entity", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Entity);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEntity(int addr, int v) {
        if (featOkTst && casFeat_Entity == null)
      jcas.throwFeatMissing("Entity", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    ll_cas.ll_setRefValue(addr, casFeatCode_Entity, v);}
    
  
 
  /** @generated */
  final Feature casFeat_MentionType;
  /** @generated */
  final int     casFeatCode_MentionType;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getMentionType(int addr) {
        if (featOkTst && casFeat_MentionType == null)
      jcas.throwFeatMissing("MentionType", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    return ll_cas.ll_getStringValue(addr, casFeatCode_MentionType);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setMentionType(int addr, String v) {
        if (featOkTst && casFeat_MentionType == null)
      jcas.throwFeatMissing("MentionType", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    ll_cas.ll_setStringValue(addr, casFeatCode_MentionType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Gender;
  /** @generated */
  final int     casFeatCode_Gender;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getGender(int addr) {
        if (featOkTst && casFeat_Gender == null)
      jcas.throwFeatMissing("Gender", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Gender);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setGender(int addr, String v) {
        if (featOkTst && casFeat_Gender == null)
      jcas.throwFeatMissing("Gender", "de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention");
    ll_cas.ll_setStringValue(addr, casFeatCode_Gender, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public EntityMention_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Identifier = jcas.getRequiredFeatureDE(casType, "Identifier", "uima.cas.String", featOkTst);
    casFeatCode_Identifier  = (null == casFeat_Identifier) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Identifier).getCode();

 
    casFeat_Source = jcas.getRequiredFeatureDE(casType, "Source", "uima.cas.AnnotationBase", featOkTst);
    casFeatCode_Source  = (null == casFeat_Source) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Source).getCode();

 
    casFeat_Head = jcas.getRequiredFeatureDE(casType, "Head", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token", featOkTst);
    casFeatCode_Head  = (null == casFeat_Head) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Head).getCode();

 
    casFeat_Entity = jcas.getRequiredFeatureDE(casType, "Entity", "de.nilsreiter.pipeline.uima.entitydetection.type.Entity", featOkTst);
    casFeatCode_Entity  = (null == casFeat_Entity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Entity).getCode();

 
    casFeat_MentionType = jcas.getRequiredFeatureDE(casType, "MentionType", "uima.cas.String", featOkTst);
    casFeatCode_MentionType  = (null == casFeat_MentionType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_MentionType).getCode();

 
    casFeat_Gender = jcas.getRequiredFeatureDE(casType, "Gender", "uima.cas.String", featOkTst);
    casFeatCode_Gender  = (null == casFeat_Gender) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Gender).getCode();

  }
}



    