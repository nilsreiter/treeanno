
/* First created by JCasGen Thu Feb 12 18:27:18 CET 2015 */
package de.nilsreiter.pipeline.segmentation.clauselevel.type;

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
 * Updated by JCasGen Fri Feb 20 08:20:38 CET 2015
 * @generated */
public class DepRel_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (DepRel_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = DepRel_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new DepRel(addr, DepRel_Type.this);
  			   DepRel_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new DepRel(addr, DepRel_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = DepRel.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
 
  /** @generated */
  final Feature casFeat_Govenor;
  /** @generated */
  final int     casFeatCode_Govenor;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getGovenor(int addr) {
        if (featOkTst && casFeat_Govenor == null)
      jcas.throwFeatMissing("Govenor", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Govenor);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setGovenor(int addr, int v) {
        if (featOkTst && casFeat_Govenor == null)
      jcas.throwFeatMissing("Govenor", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    ll_cas.ll_setRefValue(addr, casFeatCode_Govenor, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Token;
  /** @generated */
  final int     casFeatCode_Token;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getToken(int addr) {
        if (featOkTst && casFeat_Token == null)
      jcas.throwFeatMissing("Token", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Token);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setToken(int addr, int v) {
        if (featOkTst && casFeat_Token == null)
      jcas.throwFeatMissing("Token", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    ll_cas.ll_setRefValue(addr, casFeatCode_Token, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Dependents;
  /** @generated */
  final int     casFeatCode_Dependents;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getDependents(int addr) {
        if (featOkTst && casFeat_Dependents == null)
      jcas.throwFeatMissing("Dependents", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Dependents);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setDependents(int addr, int v) {
        if (featOkTst && casFeat_Dependents == null)
      jcas.throwFeatMissing("Dependents", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    ll_cas.ll_setRefValue(addr, casFeatCode_Dependents, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getDependents(int addr, int i) {
        if (featOkTst && casFeat_Dependents == null)
      jcas.throwFeatMissing("Dependents", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Dependents), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Dependents), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Dependents), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setDependents(int addr, int i, int v) {
        if (featOkTst && casFeat_Dependents == null)
      jcas.throwFeatMissing("Dependents", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Dependents), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Dependents), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Dependents), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_Relation;
  /** @generated */
  final int     casFeatCode_Relation;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getRelation(int addr) {
        if (featOkTst && casFeat_Relation == null)
      jcas.throwFeatMissing("Relation", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Relation);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setRelation(int addr, String v) {
        if (featOkTst && casFeat_Relation == null)
      jcas.throwFeatMissing("Relation", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel");
    ll_cas.ll_setStringValue(addr, casFeatCode_Relation, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public DepRel_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Govenor = jcas.getRequiredFeatureDE(casType, "Govenor", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel", featOkTst);
    casFeatCode_Govenor  = (null == casFeat_Govenor) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Govenor).getCode();

 
    casFeat_Token = jcas.getRequiredFeatureDE(casType, "Token", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token", featOkTst);
    casFeatCode_Token  = (null == casFeat_Token) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Token).getCode();

 
    casFeat_Dependents = jcas.getRequiredFeatureDE(casType, "Dependents", "uima.cas.FSArray", featOkTst);
    casFeatCode_Dependents  = (null == casFeat_Dependents) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Dependents).getCode();

 
    casFeat_Relation = jcas.getRequiredFeatureDE(casType, "Relation", "uima.cas.String", featOkTst);
    casFeatCode_Relation  = (null == casFeat_Relation) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Relation).getCode();

  }
}



    