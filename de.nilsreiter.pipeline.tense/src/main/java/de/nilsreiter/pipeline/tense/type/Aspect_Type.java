
/* First created by JCasGen Wed Mar 11 14:56:11 CET 2015 */
package de.nilsreiter.pipeline.tense.type;

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
 * Updated by JCasGen Wed Mar 11 14:56:11 CET 2015
 * @generated */
public class Aspect_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Aspect_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Aspect_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Aspect(addr, Aspect_Type.this);
  			   Aspect_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Aspect(addr, Aspect_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Aspect.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.tense.type.Aspect");
 
  /** @generated */
  final Feature casFeat_Aspect;
  /** @generated */
  final int     casFeatCode_Aspect;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getAspect(int addr) {
        if (featOkTst && casFeat_Aspect == null)
      jcas.throwFeatMissing("Aspect", "de.nilsreiter.pipeline.tense.type.Aspect");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Aspect);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setAspect(int addr, String v) {
        if (featOkTst && casFeat_Aspect == null)
      jcas.throwFeatMissing("Aspect", "de.nilsreiter.pipeline.tense.type.Aspect");
    ll_cas.ll_setStringValue(addr, casFeatCode_Aspect, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Aspect_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Aspect = jcas.getRequiredFeatureDE(casType, "Aspect", "uima.cas.String", featOkTst);
    casFeatCode_Aspect  = (null == casFeat_Aspect) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Aspect).getCode();

  }
}



    