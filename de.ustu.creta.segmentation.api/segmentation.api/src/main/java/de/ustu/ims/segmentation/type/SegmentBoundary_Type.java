
/* First created by JCasGen Wed May 20 09:02:43 CEST 2015 */
package de.ustu.ims.segmentation.type;

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
 * Updated by JCasGen Wed May 20 09:02:43 CEST 2015
 * @generated */
public class SegmentBoundary_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (SegmentBoundary_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = SegmentBoundary_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new SegmentBoundary(addr, SegmentBoundary_Type.this);
  			   SegmentBoundary_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new SegmentBoundary(addr, SegmentBoundary_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = SegmentBoundary.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.ustu.ims.segmentation.type.SegmentBoundary");
 
  /** @generated */
  final Feature casFeat_Level;
  /** @generated */
  final int     casFeatCode_Level;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getLevel(int addr) {
        if (featOkTst && casFeat_Level == null)
      jcas.throwFeatMissing("Level", "de.ustu.ims.segmentation.type.SegmentBoundary");
    return ll_cas.ll_getIntValue(addr, casFeatCode_Level);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLevel(int addr, int v) {
        if (featOkTst && casFeat_Level == null)
      jcas.throwFeatMissing("Level", "de.ustu.ims.segmentation.type.SegmentBoundary");
    ll_cas.ll_setIntValue(addr, casFeatCode_Level, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public SegmentBoundary_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Level = jcas.getRequiredFeatureDE(casType, "Level", "uima.cas.Integer", featOkTst);
    casFeatCode_Level  = (null == casFeat_Level) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Level).getCode();

  }
}



    