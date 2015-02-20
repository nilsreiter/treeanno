
/* First created by JCasGen Fri Feb 20 08:23:20 CET 2015 */
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
 * Updated by JCasGen Fri Feb 20 08:25:39 CET 2015
 * @generated */
public class ClauseStart_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (ClauseStart_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = ClauseStart_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new ClauseStart(addr, ClauseStart_Type.this);
  			   ClauseStart_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new ClauseStart(addr, ClauseStart_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = ClauseStart.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.segmentation.clauselevel.type.ClauseStart");
 
  /** @generated */
  final Feature casFeat_BeforeTense;
  /** @generated */
  final int     casFeatCode_BeforeTense;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getBeforeTense(int addr) {
        if (featOkTst && casFeat_BeforeTense == null)
      jcas.throwFeatMissing("BeforeTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.ClauseStart");
    return ll_cas.ll_getStringValue(addr, casFeatCode_BeforeTense);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setBeforeTense(int addr, String v) {
        if (featOkTst && casFeat_BeforeTense == null)
      jcas.throwFeatMissing("BeforeTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.ClauseStart");
    ll_cas.ll_setStringValue(addr, casFeatCode_BeforeTense, v);}
    
  
 
  /** @generated */
  final Feature casFeat_AfterTense;
  /** @generated */
  final int     casFeatCode_AfterTense;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getAfterTense(int addr) {
        if (featOkTst && casFeat_AfterTense == null)
      jcas.throwFeatMissing("AfterTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.ClauseStart");
    return ll_cas.ll_getStringValue(addr, casFeatCode_AfterTense);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setAfterTense(int addr, String v) {
        if (featOkTst && casFeat_AfterTense == null)
      jcas.throwFeatMissing("AfterTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.ClauseStart");
    ll_cas.ll_setStringValue(addr, casFeatCode_AfterTense, v);}
    
  
 
  /** @generated */
  final Feature casFeat_SegmentStart;
  /** @generated */
  final int     casFeatCode_SegmentStart;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getSegmentStart(int addr) {
        if (featOkTst && casFeat_SegmentStart == null)
      jcas.throwFeatMissing("SegmentStart", "de.nilsreiter.pipeline.segmentation.clauselevel.type.ClauseStart");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_SegmentStart);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSegmentStart(int addr, boolean v) {
        if (featOkTst && casFeat_SegmentStart == null)
      jcas.throwFeatMissing("SegmentStart", "de.nilsreiter.pipeline.segmentation.clauselevel.type.ClauseStart");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_SegmentStart, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public ClauseStart_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_BeforeTense = jcas.getRequiredFeatureDE(casType, "BeforeTense", "uima.cas.String", featOkTst);
    casFeatCode_BeforeTense  = (null == casFeat_BeforeTense) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_BeforeTense).getCode();

 
    casFeat_AfterTense = jcas.getRequiredFeatureDE(casType, "AfterTense", "uima.cas.String", featOkTst);
    casFeatCode_AfterTense  = (null == casFeat_AfterTense) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_AfterTense).getCode();

 
    casFeat_SegmentStart = jcas.getRequiredFeatureDE(casType, "SegmentStart", "uima.cas.Boolean", featOkTst);
    casFeatCode_SegmentStart  = (null == casFeat_SegmentStart) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_SegmentStart).getCode();

  }
}



    