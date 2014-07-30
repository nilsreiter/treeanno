
/* First created by JCasGen Wed Jul 16 15:46:54 CEST 2014 */
package de.nilsreiter.pipeline.uima.ocr.type;

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
 * Updated by JCasGen Mon Jul 21 11:46:42 CEST 2014
 * @generated */
public class OCRCorrection_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (OCRCorrection_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = OCRCorrection_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new OCRCorrection(addr, OCRCorrection_Type.this);
  			   OCRCorrection_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new OCRCorrection(addr, OCRCorrection_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = OCRCorrection.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection");
 
  /** @generated */
  final Feature casFeat_Correction;
  /** @generated */
  final int     casFeatCode_Correction;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getCorrection(int addr) {
        if (featOkTst && casFeat_Correction == null)
      jcas.throwFeatMissing("Correction", "de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Correction);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setCorrection(int addr, String v) {
        if (featOkTst && casFeat_Correction == null)
      jcas.throwFeatMissing("Correction", "de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection");
    ll_cas.ll_setStringValue(addr, casFeatCode_Correction, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Level;
  /** @generated */
  final int     casFeatCode_Level;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getLevel(int addr) {
        if (featOkTst && casFeat_Level == null)
      jcas.throwFeatMissing("Level", "de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection");
    return ll_cas.ll_getIntValue(addr, casFeatCode_Level);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLevel(int addr, int v) {
        if (featOkTst && casFeat_Level == null)
      jcas.throwFeatMissing("Level", "de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection");
    ll_cas.ll_setIntValue(addr, casFeatCode_Level, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public OCRCorrection_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Correction = jcas.getRequiredFeatureDE(casType, "Correction", "uima.cas.String", featOkTst);
    casFeatCode_Correction  = (null == casFeat_Correction) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Correction).getCode();

 
    casFeat_Level = jcas.getRequiredFeatureDE(casType, "Level", "uima.cas.Integer", featOkTst);
    casFeatCode_Level  = (null == casFeat_Level) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Level).getCode();

  }
}



    