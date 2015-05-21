
/* First created by JCasGen Thu May 21 07:27:23 CEST 2015 */
package de.ustu.ims.reiter.tense.api.type;

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
 * Updated by JCasGen Thu May 21 07:27:23 CEST 2015
 * @generated */
public class Tense_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Tense_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Tense_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Tense(addr, Tense_Type.this);
  			   Tense_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Tense(addr, Tense_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Tense.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.ustu.ims.reiter.tense.api.type.Tense");
 
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
      jcas.throwFeatMissing("Tense", "de.ustu.ims.reiter.tense.api.type.Tense");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Tense);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTense(int addr, String v) {
        if (featOkTst && casFeat_Tense == null)
      jcas.throwFeatMissing("Tense", "de.ustu.ims.reiter.tense.api.type.Tense");
    ll_cas.ll_setStringValue(addr, casFeatCode_Tense, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Tense_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Tense = jcas.getRequiredFeatureDE(casType, "Tense", "uima.cas.String", featOkTst);
    casFeatCode_Tense  = (null == casFeat_Tense) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Tense).getCode();

  }
}



    