
/* First created by JCasGen Mon May 04 21:11:55 CEST 2015 */
package webanno.custom;

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
 * Updated by JCasGen Mon May 04 21:11:55 CEST 2015
 * @generated */
public class Zusammenfassung_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Zusammenfassung_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Zusammenfassung_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Zusammenfassung(addr, Zusammenfassung_Type.this);
  			   Zusammenfassung_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Zusammenfassung(addr, Zusammenfassung_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Zusammenfassung.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("webanno.custom.Zusammenfassung");
 
  /** @generated */
  final Feature casFeat_Zusammenfassung;
  /** @generated */
  final int     casFeatCode_Zusammenfassung;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getZusammenfassung(int addr) {
        if (featOkTst && casFeat_Zusammenfassung == null)
      jcas.throwFeatMissing("Zusammenfassung", "webanno.custom.Zusammenfassung");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Zusammenfassung);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setZusammenfassung(int addr, String v) {
        if (featOkTst && casFeat_Zusammenfassung == null)
      jcas.throwFeatMissing("Zusammenfassung", "webanno.custom.Zusammenfassung");
    ll_cas.ll_setStringValue(addr, casFeatCode_Zusammenfassung, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Zusammenfassung_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Zusammenfassung = jcas.getRequiredFeatureDE(casType, "Zusammenfassung", "uima.cas.String", featOkTst);
    casFeatCode_Zusammenfassung  = (null == casFeat_Zusammenfassung) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Zusammenfassung).getCode();

  }
}



    