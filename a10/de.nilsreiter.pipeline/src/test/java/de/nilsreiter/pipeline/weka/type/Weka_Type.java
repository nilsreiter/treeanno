
/* First created by JCasGen Wed Feb 18 16:33:28 CET 2015 */
package de.nilsreiter.pipeline.weka.type;

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
 * Updated by JCasGen Wed Feb 18 16:33:28 CET 2015
 * @generated */
public class Weka_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Weka_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Weka_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Weka(addr, Weka_Type.this);
  			   Weka_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Weka(addr, Weka_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Weka.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.weka.type.Weka");
 
  /** @generated */
  final Feature casFeat_Feature1;
  /** @generated */
  final int     casFeatCode_Feature1;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getFeature1(int addr) {
        if (featOkTst && casFeat_Feature1 == null)
      jcas.throwFeatMissing("Feature1", "de.nilsreiter.pipeline.weka.type.Weka");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Feature1);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setFeature1(int addr, String v) {
        if (featOkTst && casFeat_Feature1 == null)
      jcas.throwFeatMissing("Feature1", "de.nilsreiter.pipeline.weka.type.Weka");
    ll_cas.ll_setStringValue(addr, casFeatCode_Feature1, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Feature2;
  /** @generated */
  final int     casFeatCode_Feature2;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getFeature2(int addr) {
        if (featOkTst && casFeat_Feature2 == null)
      jcas.throwFeatMissing("Feature2", "de.nilsreiter.pipeline.weka.type.Weka");
    return ll_cas.ll_getIntValue(addr, casFeatCode_Feature2);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setFeature2(int addr, int v) {
        if (featOkTst && casFeat_Feature2 == null)
      jcas.throwFeatMissing("Feature2", "de.nilsreiter.pipeline.weka.type.Weka");
    ll_cas.ll_setIntValue(addr, casFeatCode_Feature2, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Feature3;
  /** @generated */
  final int     casFeatCode_Feature3;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public double getFeature3(int addr) {
        if (featOkTst && casFeat_Feature3 == null)
      jcas.throwFeatMissing("Feature3", "de.nilsreiter.pipeline.weka.type.Weka");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_Feature3);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setFeature3(int addr, double v) {
        if (featOkTst && casFeat_Feature3 == null)
      jcas.throwFeatMissing("Feature3", "de.nilsreiter.pipeline.weka.type.Weka");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_Feature3, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Feature4;
  /** @generated */
  final int     casFeatCode_Feature4;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getFeature4(int addr) {
        if (featOkTst && casFeat_Feature4 == null)
      jcas.throwFeatMissing("Feature4", "de.nilsreiter.pipeline.weka.type.Weka");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Feature4);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setFeature4(int addr, String v) {
        if (featOkTst && casFeat_Feature4 == null)
      jcas.throwFeatMissing("Feature4", "de.nilsreiter.pipeline.weka.type.Weka");
    ll_cas.ll_setStringValue(addr, casFeatCode_Feature4, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Weka_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Feature1 = jcas.getRequiredFeatureDE(casType, "Feature1", "uima.cas.String", featOkTst);
    casFeatCode_Feature1  = (null == casFeat_Feature1) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Feature1).getCode();

 
    casFeat_Feature2 = jcas.getRequiredFeatureDE(casType, "Feature2", "uima.cas.Integer", featOkTst);
    casFeatCode_Feature2  = (null == casFeat_Feature2) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Feature2).getCode();

 
    casFeat_Feature3 = jcas.getRequiredFeatureDE(casType, "Feature3", "uima.cas.Double", featOkTst);
    casFeatCode_Feature3  = (null == casFeat_Feature3) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Feature3).getCode();

 
    casFeat_Feature4 = jcas.getRequiredFeatureDE(casType, "Feature4", "uima.cas.String", featOkTst);
    casFeatCode_Feature4  = (null == casFeat_Feature4) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Feature4).getCode();

  }
}



    