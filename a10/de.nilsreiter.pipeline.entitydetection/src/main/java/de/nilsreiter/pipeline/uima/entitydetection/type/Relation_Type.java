
/* First created by JCasGen Fri Nov 28 10:57:04 CET 2014 */
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
import org.apache.uima.jcas.cas.AnnotationBase_Type;

/** 
 * Updated by JCasGen Mon Dec 01 13:35:29 CET 2014
 * @generated */
public class Relation_Type extends AnnotationBase_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Relation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Relation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Relation(addr, Relation_Type.this);
  			   Relation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Relation(addr, Relation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Relation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.nilsreiter.pipeline.uima.entitydetection.type.Relation");
 
  /** @generated */
  final Feature casFeat_Arguments;
  /** @generated */
  final int     casFeatCode_Arguments;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getArguments(int addr) {
        if (featOkTst && casFeat_Arguments == null)
      jcas.throwFeatMissing("Arguments", "de.nilsreiter.pipeline.uima.entitydetection.type.Relation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Arguments);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setArguments(int addr, int v) {
        if (featOkTst && casFeat_Arguments == null)
      jcas.throwFeatMissing("Arguments", "de.nilsreiter.pipeline.uima.entitydetection.type.Relation");
    ll_cas.ll_setRefValue(addr, casFeatCode_Arguments, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getArguments(int addr, int i) {
        if (featOkTst && casFeat_Arguments == null)
      jcas.throwFeatMissing("Arguments", "de.nilsreiter.pipeline.uima.entitydetection.type.Relation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Arguments), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Arguments), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Arguments), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setArguments(int addr, int i, int v) {
        if (featOkTst && casFeat_Arguments == null)
      jcas.throwFeatMissing("Arguments", "de.nilsreiter.pipeline.uima.entitydetection.type.Relation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Arguments), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Arguments), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Arguments), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_Name;
  /** @generated */
  final int     casFeatCode_Name;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getName(int addr) {
        if (featOkTst && casFeat_Name == null)
      jcas.throwFeatMissing("Name", "de.nilsreiter.pipeline.uima.entitydetection.type.Relation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Name);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setName(int addr, String v) {
        if (featOkTst && casFeat_Name == null)
      jcas.throwFeatMissing("Name", "de.nilsreiter.pipeline.uima.entitydetection.type.Relation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Name, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Relation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Arguments = jcas.getRequiredFeatureDE(casType, "Arguments", "uima.cas.FSArray", featOkTst);
    casFeatCode_Arguments  = (null == casFeat_Arguments) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Arguments).getCode();

 
    casFeat_Name = jcas.getRequiredFeatureDE(casType, "Name", "uima.cas.String", featOkTst);
    casFeatCode_Name  = (null == casFeat_Name) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Name).getCode();

  }
}



    