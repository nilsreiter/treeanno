
/* First created by JCasGen Fri Nov 28 10:40:44 CET 2014 */
package de.nilsreiter.pipeline.uima.entitydetection.type;

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
 * Updated by JCasGen Fri Nov 28 10:57:04 CET 2014
 * @generated */
public class Entity_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Entity_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Entity_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Entity(addr, Entity_Type.this);
  			   Entity_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Entity(addr, Entity_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Entity.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.uima.entitydetection.type.Entity");
 
  /** @generated */
  final Feature casFeat_Identifier;
  /** @generated */
  final int     casFeatCode_Identifier;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getIdentifier(int addr) {
        if (featOkTst && casFeat_Identifier == null)
      jcas.throwFeatMissing("Identifier", "de.nilsreiter.pipeline.uima.entitydetection.type.Entity");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Identifier);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIdentifier(int addr, String v) {
        if (featOkTst && casFeat_Identifier == null)
      jcas.throwFeatMissing("Identifier", "de.nilsreiter.pipeline.uima.entitydetection.type.Entity");
    ll_cas.ll_setStringValue(addr, casFeatCode_Identifier, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Source;
  /** @generated */
  final int     casFeatCode_Source;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getSource(int addr) {
        if (featOkTst && casFeat_Source == null)
      jcas.throwFeatMissing("Source", "de.nilsreiter.pipeline.uima.entitydetection.type.Entity");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Source);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSource(int addr, int v) {
        if (featOkTst && casFeat_Source == null)
      jcas.throwFeatMissing("Source", "de.nilsreiter.pipeline.uima.entitydetection.type.Entity");
    ll_cas.ll_setRefValue(addr, casFeatCode_Source, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Entity_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Identifier = jcas.getRequiredFeatureDE(casType, "Identifier", "uima.cas.String", featOkTst);
    casFeatCode_Identifier  = (null == casFeat_Identifier) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Identifier).getCode();

 
    casFeat_Source = jcas.getRequiredFeatureDE(casType, "Source", "uima.cas.AnnotationBase", featOkTst);
    casFeatCode_Source  = (null == casFeat_Source) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Source).getCode();

  }
}



    