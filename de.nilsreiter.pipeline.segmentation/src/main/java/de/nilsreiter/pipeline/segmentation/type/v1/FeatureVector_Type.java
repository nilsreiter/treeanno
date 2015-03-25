
/* First created by JCasGen Fri Mar 06 09:31:00 CET 2015 */
package de.nilsreiter.pipeline.segmentation.type.v1;

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
 * Updated by JCasGen Wed Mar 25 16:55:48 CET 2015
 * @generated */
public class FeatureVector_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (FeatureVector_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = FeatureVector_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new FeatureVector(addr, FeatureVector_Type.this);
  			   FeatureVector_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new FeatureVector(addr, FeatureVector_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = FeatureVector.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
 
  /** @generated */
  final Feature casFeat_PreviousTense;
  /** @generated */
  final int     casFeatCode_PreviousTense;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getPreviousTense(int addr) {
        if (featOkTst && casFeat_PreviousTense == null)
      jcas.throwFeatMissing("PreviousTense", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    return ll_cas.ll_getStringValue(addr, casFeatCode_PreviousTense);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPreviousTense(int addr, String v) {
        if (featOkTst && casFeat_PreviousTense == null)
      jcas.throwFeatMissing("PreviousTense", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    ll_cas.ll_setStringValue(addr, casFeatCode_PreviousTense, v);}
    
  
 
  /** @generated */
  final Feature casFeat_CurrentTense;
  /** @generated */
  final int     casFeatCode_CurrentTense;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getCurrentTense(int addr) {
        if (featOkTst && casFeat_CurrentTense == null)
      jcas.throwFeatMissing("CurrentTense", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    return ll_cas.ll_getStringValue(addr, casFeatCode_CurrentTense);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setCurrentTense(int addr, String v) {
        if (featOkTst && casFeat_CurrentTense == null)
      jcas.throwFeatMissing("CurrentTense", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    ll_cas.ll_setStringValue(addr, casFeatCode_CurrentTense, v);}
    
  
 
  /** @generated */
  final Feature casFeat_NewSegment;
  /** @generated */
  final int     casFeatCode_NewSegment;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getNewSegment(int addr) {
        if (featOkTst && casFeat_NewSegment == null)
      jcas.throwFeatMissing("NewSegment", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_NewSegment);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setNewSegment(int addr, boolean v) {
        if (featOkTst && casFeat_NewSegment == null)
      jcas.throwFeatMissing("NewSegment", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_NewSegment, v);}
    
  
 
  /** @generated */
  final Feature casFeat_PreviousAspect;
  /** @generated */
  final int     casFeatCode_PreviousAspect;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getPreviousAspect(int addr) {
        if (featOkTst && casFeat_PreviousAspect == null)
      jcas.throwFeatMissing("PreviousAspect", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    return ll_cas.ll_getStringValue(addr, casFeatCode_PreviousAspect);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPreviousAspect(int addr, String v) {
        if (featOkTst && casFeat_PreviousAspect == null)
      jcas.throwFeatMissing("PreviousAspect", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    ll_cas.ll_setStringValue(addr, casFeatCode_PreviousAspect, v);}
    
  
 
  /** @generated */
  final Feature casFeat_CurrentAspect;
  /** @generated */
  final int     casFeatCode_CurrentAspect;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getCurrentAspect(int addr) {
        if (featOkTst && casFeat_CurrentAspect == null)
      jcas.throwFeatMissing("CurrentAspect", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    return ll_cas.ll_getStringValue(addr, casFeatCode_CurrentAspect);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setCurrentAspect(int addr, String v) {
        if (featOkTst && casFeat_CurrentAspect == null)
      jcas.throwFeatMissing("CurrentAspect", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    ll_cas.ll_setStringValue(addr, casFeatCode_CurrentAspect, v);}
    
  
 
  /** @generated */
  final Feature casFeat_TimeAdverb;
  /** @generated */
  final int     casFeatCode_TimeAdverb;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTimeAdverb(int addr) {
        if (featOkTst && casFeat_TimeAdverb == null)
      jcas.throwFeatMissing("TimeAdverb", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    return ll_cas.ll_getStringValue(addr, casFeatCode_TimeAdverb);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTimeAdverb(int addr, String v) {
        if (featOkTst && casFeat_TimeAdverb == null)
      jcas.throwFeatMissing("TimeAdverb", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    ll_cas.ll_setStringValue(addr, casFeatCode_TimeAdverb, v);}
    
  
 
  /** @generated */
  final Feature casFeat_TimeNoun;
  /** @generated */
  final int     casFeatCode_TimeNoun;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTimeNoun(int addr) {
        if (featOkTst && casFeat_TimeNoun == null)
      jcas.throwFeatMissing("TimeNoun", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    return ll_cas.ll_getStringValue(addr, casFeatCode_TimeNoun);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTimeNoun(int addr, String v) {
        if (featOkTst && casFeat_TimeNoun == null)
      jcas.throwFeatMissing("TimeNoun", "de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector");
    ll_cas.ll_setStringValue(addr, casFeatCode_TimeNoun, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public FeatureVector_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_PreviousTense = jcas.getRequiredFeatureDE(casType, "PreviousTense", "uima.cas.String", featOkTst);
    casFeatCode_PreviousTense  = (null == casFeat_PreviousTense) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_PreviousTense).getCode();

 
    casFeat_CurrentTense = jcas.getRequiredFeatureDE(casType, "CurrentTense", "uima.cas.String", featOkTst);
    casFeatCode_CurrentTense  = (null == casFeat_CurrentTense) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_CurrentTense).getCode();

 
    casFeat_NewSegment = jcas.getRequiredFeatureDE(casType, "NewSegment", "uima.cas.Boolean", featOkTst);
    casFeatCode_NewSegment  = (null == casFeat_NewSegment) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_NewSegment).getCode();

 
    casFeat_PreviousAspect = jcas.getRequiredFeatureDE(casType, "PreviousAspect", "uima.cas.String", featOkTst);
    casFeatCode_PreviousAspect  = (null == casFeat_PreviousAspect) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_PreviousAspect).getCode();

 
    casFeat_CurrentAspect = jcas.getRequiredFeatureDE(casType, "CurrentAspect", "uima.cas.String", featOkTst);
    casFeatCode_CurrentAspect  = (null == casFeat_CurrentAspect) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_CurrentAspect).getCode();

 
    casFeat_TimeAdverb = jcas.getRequiredFeatureDE(casType, "TimeAdverb", "uima.cas.String", featOkTst);
    casFeatCode_TimeAdverb  = (null == casFeat_TimeAdverb) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_TimeAdverb).getCode();

 
    casFeat_TimeNoun = jcas.getRequiredFeatureDE(casType, "TimeNoun", "uima.cas.String", featOkTst);
    casFeatCode_TimeNoun  = (null == casFeat_TimeNoun) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_TimeNoun).getCode();

  }
}



    