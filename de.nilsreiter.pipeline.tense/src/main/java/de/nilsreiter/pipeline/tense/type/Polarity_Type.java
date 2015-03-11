
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
public class Polarity_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Polarity_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Polarity_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Polarity(addr, Polarity_Type.this);
  			   Polarity_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Polarity(addr, Polarity_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Polarity.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.tense.type.Polarity");
 
  /** @generated */
  final Feature casFeat_Polarity;
  /** @generated */
  final int     casFeatCode_Polarity;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getPolarity(int addr) {
        if (featOkTst && casFeat_Polarity == null)
      jcas.throwFeatMissing("Polarity", "de.nilsreiter.pipeline.tense.type.Polarity");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Polarity);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPolarity(int addr, String v) {
        if (featOkTst && casFeat_Polarity == null)
      jcas.throwFeatMissing("Polarity", "de.nilsreiter.pipeline.tense.type.Polarity");
    ll_cas.ll_setStringValue(addr, casFeatCode_Polarity, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Polarity_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Polarity = jcas.getRequiredFeatureDE(casType, "Polarity", "uima.cas.String", featOkTst);
    casFeatCode_Polarity  = (null == casFeat_Polarity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Polarity).getCode();

  }
}



    