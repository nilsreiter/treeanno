
/* First created by JCasGen Wed Jun 03 14:38:16 CEST 2015 */
package de.ustu.ims.reiter.treeanno.api.type;

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
 * Updated by JCasGen Mon Jun 08 10:18:42 CEST 2015
 * @generated */
public class TreeSegment_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TreeSegment_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TreeSegment_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TreeSegment(addr, TreeSegment_Type.this);
  			   TreeSegment_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TreeSegment(addr, TreeSegment_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = TreeSegment.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
 
  /** @generated */
  final Feature casFeat_Category;
  /** @generated */
  final int     casFeatCode_Category;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getCategory(int addr) {
        if (featOkTst && casFeat_Category == null)
      jcas.throwFeatMissing("Category", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Category);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setCategory(int addr, String v) {
        if (featOkTst && casFeat_Category == null)
      jcas.throwFeatMissing("Category", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    ll_cas.ll_setStringValue(addr, casFeatCode_Category, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Parent;
  /** @generated */
  final int     casFeatCode_Parent;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getParent(int addr) {
        if (featOkTst && casFeat_Parent == null)
      jcas.throwFeatMissing("Parent", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Parent);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setParent(int addr, int v) {
        if (featOkTst && casFeat_Parent == null)
      jcas.throwFeatMissing("Parent", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    ll_cas.ll_setRefValue(addr, casFeatCode_Parent, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Id;
  /** @generated */
  final int     casFeatCode_Id;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getId(int addr) {
        if (featOkTst && casFeat_Id == null)
      jcas.throwFeatMissing("Id", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    return ll_cas.ll_getIntValue(addr, casFeatCode_Id);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setId(int addr, int v) {
        if (featOkTst && casFeat_Id == null)
      jcas.throwFeatMissing("Id", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment");
    ll_cas.ll_setIntValue(addr, casFeatCode_Id, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public TreeSegment_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Category = jcas.getRequiredFeatureDE(casType, "Category", "uima.cas.String", featOkTst);
    casFeatCode_Category  = (null == casFeat_Category) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Category).getCode();

 
    casFeat_Parent = jcas.getRequiredFeatureDE(casType, "Parent", "de.ustu.ims.reiter.treeanno.api.type.TreeSegment", featOkTst);
    casFeatCode_Parent  = (null == casFeat_Parent) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Parent).getCode();

 
    casFeat_Id = jcas.getRequiredFeatureDE(casType, "Id", "uima.cas.Integer", featOkTst);
    casFeatCode_Id  = (null == casFeat_Id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Id).getCode();

  }
}



    