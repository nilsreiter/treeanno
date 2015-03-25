
/* First created by JCasGen Tue Mar 24 18:31:04 CET 2015 */
package de.ustu.narr;

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
 * Updated by JCasGen Tue Mar 24 18:31:04 CET 2015
 * @generated */
public class NarrativeLevel_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (NarrativeLevel_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = NarrativeLevel_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new NarrativeLevel(addr, NarrativeLevel_Type.this);
  			   NarrativeLevel_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new NarrativeLevel(addr, NarrativeLevel_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = NarrativeLevel.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.ustu.narr.NarrativeLevel");
 
  /** @generated */
  final Feature casFeat_LevelType;
  /** @generated */
  final int     casFeatCode_LevelType;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getLevelType(int addr) {
        if (featOkTst && casFeat_LevelType == null)
      jcas.throwFeatMissing("LevelType", "de.ustu.narr.NarrativeLevel");
    return ll_cas.ll_getStringValue(addr, casFeatCode_LevelType);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLevelType(int addr, String v) {
        if (featOkTst && casFeat_LevelType == null)
      jcas.throwFeatMissing("LevelType", "de.ustu.narr.NarrativeLevel");
    ll_cas.ll_setStringValue(addr, casFeatCode_LevelType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Narrator;
  /** @generated */
  final int     casFeatCode_Narrator;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getNarrator(int addr) {
        if (featOkTst && casFeat_Narrator == null)
      jcas.throwFeatMissing("Narrator", "de.ustu.narr.NarrativeLevel");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Narrator);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setNarrator(int addr, String v) {
        if (featOkTst && casFeat_Narrator == null)
      jcas.throwFeatMissing("Narrator", "de.ustu.narr.NarrativeLevel");
    ll_cas.ll_setStringValue(addr, casFeatCode_Narrator, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public NarrativeLevel_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_LevelType = jcas.getRequiredFeatureDE(casType, "LevelType", "uima.cas.String", featOkTst);
    casFeatCode_LevelType  = (null == casFeat_LevelType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_LevelType).getCode();

 
    casFeat_Narrator = jcas.getRequiredFeatureDE(casType, "Narrator", "uima.cas.String", featOkTst);
    casFeatCode_Narrator  = (null == casFeat_Narrator) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Narrator).getCode();

  }
}



    