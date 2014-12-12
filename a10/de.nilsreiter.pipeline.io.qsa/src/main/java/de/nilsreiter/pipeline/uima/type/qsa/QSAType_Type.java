
/* First created by JCasGen Fri Dec 12 10:16:34 CET 2014 */
package de.nilsreiter.pipeline.uima.type.qsa;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity_Type;

/** 
 * Updated by JCasGen Fri Dec 12 10:16:34 CET 2014
 * @generated */
public class QSAType_Type extends NamedEntity_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (QSAType_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = QSAType_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new QSAType(addr, QSAType_Type.this);
  			   QSAType_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new QSAType(addr, QSAType_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = QSAType.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.uima.type.qsa.QSAType");
 
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
      jcas.throwFeatMissing("Gender", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Gender);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setGender(int addr, String v) {
        if (featOkTst && casFeat_Gender == null)
      jcas.throwFeatMissing("Gender", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    ll_cas.ll_setStringValue(addr, casFeatCode_Gender, v);}
    
  
 
  /** @generated */
  final Feature casFeat_EntityId;
  /** @generated */
  final int     casFeatCode_EntityId;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getEntityId(int addr) {
        if (featOkTst && casFeat_EntityId == null)
      jcas.throwFeatMissing("EntityId", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    return ll_cas.ll_getStringValue(addr, casFeatCode_EntityId);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEntityId(int addr, String v) {
        if (featOkTst && casFeat_EntityId == null)
      jcas.throwFeatMissing("EntityId", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    ll_cas.ll_setStringValue(addr, casFeatCode_EntityId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Modifier;
  /** @generated */
  final int     casFeatCode_Modifier;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getModifier(int addr) {
        if (featOkTst && casFeat_Modifier == null)
      jcas.throwFeatMissing("Modifier", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Modifier);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setModifier(int addr, String v) {
        if (featOkTst && casFeat_Modifier == null)
      jcas.throwFeatMissing("Modifier", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    ll_cas.ll_setStringValue(addr, casFeatCode_Modifier, v);}
    
  
 
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
      jcas.throwFeatMissing("MentionType", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    return ll_cas.ll_getStringValue(addr, casFeatCode_MentionType);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setMentionType(int addr, String v) {
        if (featOkTst && casFeat_MentionType == null)
      jcas.throwFeatMissing("MentionType", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    ll_cas.ll_setStringValue(addr, casFeatCode_MentionType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Id;
  /** @generated */
  final int     casFeatCode_Id;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getId(int addr) {
        if (featOkTst && casFeat_Id == null)
      jcas.throwFeatMissing("Id", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Id);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setId(int addr, String v) {
        if (featOkTst && casFeat_Id == null)
      jcas.throwFeatMissing("Id", "de.nilsreiter.pipeline.uima.type.qsa.QSAType");
    ll_cas.ll_setStringValue(addr, casFeatCode_Id, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public QSAType_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Gender = jcas.getRequiredFeatureDE(casType, "Gender", "uima.cas.String", featOkTst);
    casFeatCode_Gender  = (null == casFeat_Gender) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Gender).getCode();

 
    casFeat_EntityId = jcas.getRequiredFeatureDE(casType, "EntityId", "uima.cas.String", featOkTst);
    casFeatCode_EntityId  = (null == casFeat_EntityId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_EntityId).getCode();

 
    casFeat_Modifier = jcas.getRequiredFeatureDE(casType, "Modifier", "uima.cas.String", featOkTst);
    casFeatCode_Modifier  = (null == casFeat_Modifier) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Modifier).getCode();

 
    casFeat_MentionType = jcas.getRequiredFeatureDE(casType, "MentionType", "uima.cas.String", featOkTst);
    casFeatCode_MentionType  = (null == casFeat_MentionType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_MentionType).getCode();

 
    casFeat_Id = jcas.getRequiredFeatureDE(casType, "Id", "uima.cas.String", featOkTst);
    casFeatCode_Id  = (null == casFeat_Id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Id).getCode();

  }
}



    