
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
 * Updated by JCasGen Fri Mar 06 09:39:01 CET 2015
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

  }
}



    