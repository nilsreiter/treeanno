
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
public class StoryPart_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (StoryPart_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = StoryPart_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new StoryPart(addr, StoryPart_Type.this);
  			   StoryPart_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new StoryPart(addr, StoryPart_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = StoryPart.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.ustu.narr.StoryPart");
 
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
      jcas.throwFeatMissing("LevelType", "de.ustu.narr.StoryPart");
    return ll_cas.ll_getStringValue(addr, casFeatCode_LevelType);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLevelType(int addr, String v) {
        if (featOkTst && casFeat_LevelType == null)
      jcas.throwFeatMissing("LevelType", "de.ustu.narr.StoryPart");
    ll_cas.ll_setStringValue(addr, casFeatCode_LevelType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Timespan;
  /** @generated */
  final int     casFeatCode_Timespan;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTimespan(int addr) {
        if (featOkTst && casFeat_Timespan == null)
      jcas.throwFeatMissing("Timespan", "de.ustu.narr.StoryPart");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Timespan);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTimespan(int addr, String v) {
        if (featOkTst && casFeat_Timespan == null)
      jcas.throwFeatMissing("Timespan", "de.ustu.narr.StoryPart");
    ll_cas.ll_setStringValue(addr, casFeatCode_Timespan, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public StoryPart_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_LevelType = jcas.getRequiredFeatureDE(casType, "LevelType", "uima.cas.String", featOkTst);
    casFeatCode_LevelType  = (null == casFeat_LevelType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_LevelType).getCode();

 
    casFeat_Timespan = jcas.getRequiredFeatureDE(casType, "Timespan", "uima.cas.String", featOkTst);
    casFeatCode_Timespan  = (null == casFeat_Timespan) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Timespan).getCode();

  }
}



    