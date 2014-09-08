
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
 * Updated by JCasGen Thu Sep 04 08:21:46 CEST 2014
 * @generated */
public class OCRError_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (OCRError_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = OCRError_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new OCRError(addr, OCRError_Type.this);
  			   OCRError_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new OCRError(addr, OCRError_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = OCRError.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.uima.ocr.type.OCRError");
 
  /** @generated */
  final Feature casFeat_Description;
  /** @generated */
  final int     casFeatCode_Description;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getDescription(int addr) {
        if (featOkTst && casFeat_Description == null)
      jcas.throwFeatMissing("Description", "de.nilsreiter.pipeline.uima.ocr.type.OCRError");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Description);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setDescription(int addr, String v) {
        if (featOkTst && casFeat_Description == null)
      jcas.throwFeatMissing("Description", "de.nilsreiter.pipeline.uima.ocr.type.OCRError");
    ll_cas.ll_setStringValue(addr, casFeatCode_Description, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Detector;
  /** @generated */
  final int     casFeatCode_Detector;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getDetector(int addr) {
        if (featOkTst && casFeat_Detector == null)
      jcas.throwFeatMissing("Detector", "de.nilsreiter.pipeline.uima.ocr.type.OCRError");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Detector);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setDetector(int addr, String v) {
        if (featOkTst && casFeat_Detector == null)
      jcas.throwFeatMissing("Detector", "de.nilsreiter.pipeline.uima.ocr.type.OCRError");
    ll_cas.ll_setStringValue(addr, casFeatCode_Detector, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public OCRError_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Description = jcas.getRequiredFeatureDE(casType, "Description", "uima.cas.String", featOkTst);
    casFeatCode_Description  = (null == casFeat_Description) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Description).getCode();

 
    casFeat_Detector = jcas.getRequiredFeatureDE(casType, "Detector", "uima.cas.String", featOkTst);
    casFeatCode_Detector  = (null == casFeat_Detector) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Detector).getCode();

  }
}



    