
/* First created by JCasGen Mon Feb 16 11:38:56 CET 2015 */
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
public class Clause_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Clause_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Clause_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Clause(addr, Clause_Type.this);
  			   Clause_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Clause(addr, Clause_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Clause.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
 
  /** @generated */
  final Feature casFeat_Tense;
  /** @generated */
  final int     casFeatCode_Tense;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTense(int addr) {
        if (featOkTst && casFeat_Tense == null)
      jcas.throwFeatMissing("Tense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Tense);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTense(int addr, String v) {
        if (featOkTst && casFeat_Tense == null)
      jcas.throwFeatMissing("Tense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    ll_cas.ll_setStringValue(addr, casFeatCode_Tense, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Extent;
  /** @generated */
  final int     casFeatCode_Extent;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getExtent(int addr) {
        if (featOkTst && casFeat_Extent == null)
      jcas.throwFeatMissing("Extent", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Extent);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setExtent(int addr, int v) {
        if (featOkTst && casFeat_Extent == null)
      jcas.throwFeatMissing("Extent", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    ll_cas.ll_setRefValue(addr, casFeatCode_Extent, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getExtent(int addr, int i) {
        if (featOkTst && casFeat_Extent == null)
      jcas.throwFeatMissing("Extent", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Extent), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Extent), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Extent), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setExtent(int addr, int i, int v) {
        if (featOkTst && casFeat_Extent == null)
      jcas.throwFeatMissing("Extent", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Extent), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Extent), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Extent), i, v);
  }
 
 
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
      jcas.throwFeatMissing("Head", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Head);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setHead(int addr, int v) {
        if (featOkTst && casFeat_Head == null)
      jcas.throwFeatMissing("Head", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause");
    ll_cas.ll_setRefValue(addr, casFeatCode_Head, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Clause_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Tense = jcas.getRequiredFeatureDE(casType, "Tense", "uima.cas.String", featOkTst);
    casFeatCode_Tense  = (null == casFeat_Tense) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Tense).getCode();

 
    casFeat_Extent = jcas.getRequiredFeatureDE(casType, "Extent", "uima.cas.FSArray", featOkTst);
    casFeatCode_Extent  = (null == casFeat_Extent) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Extent).getCode();

 
    casFeat_Head = jcas.getRequiredFeatureDE(casType, "Head", "de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel", featOkTst);
    casFeatCode_Head  = (null == casFeat_Head) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Head).getCode();

  }
}



    