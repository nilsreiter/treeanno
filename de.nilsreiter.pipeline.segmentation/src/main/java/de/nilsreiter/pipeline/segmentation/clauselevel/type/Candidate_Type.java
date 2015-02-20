
/* First created by JCasGen Fri Feb 20 08:20:38 CET 2015 */
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
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryCandidate_Type;

/** 
 * Updated by JCasGen Fri Feb 20 08:20:38 CET 2015
 * @generated */
public class Candidate_Type extends SegmentBoundaryCandidate_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Candidate_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Candidate_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Candidate(addr, Candidate_Type.this);
  			   Candidate_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Candidate(addr, Candidate_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Candidate.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.segmentation.clauselevel.type.Candidate");
 
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
      jcas.throwFeatMissing("BeforeTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Candidate");
    return ll_cas.ll_getStringValue(addr, casFeatCode_BeforeTense);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setBeforeTense(int addr, String v) {
        if (featOkTst && casFeat_BeforeTense == null)
      jcas.throwFeatMissing("BeforeTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Candidate");
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
      jcas.throwFeatMissing("AfterTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Candidate");
    return ll_cas.ll_getStringValue(addr, casFeatCode_AfterTense);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setAfterTense(int addr, String v) {
        if (featOkTst && casFeat_AfterTense == null)
      jcas.throwFeatMissing("AfterTense", "de.nilsreiter.pipeline.segmentation.clauselevel.type.Candidate");
    ll_cas.ll_setStringValue(addr, casFeatCode_AfterTense, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Candidate_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_BeforeTense = jcas.getRequiredFeatureDE(casType, "BeforeTense", "uima.cas.String", featOkTst);
    casFeatCode_BeforeTense  = (null == casFeat_BeforeTense) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_BeforeTense).getCode();

 
    casFeat_AfterTense = jcas.getRequiredFeatureDE(casType, "AfterTense", "uima.cas.String", featOkTst);
    casFeatCode_AfterTense  = (null == casFeat_AfterTense) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_AfterTense).getCode();

  }
}



    