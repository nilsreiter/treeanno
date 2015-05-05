

/* First created by JCasGen Mon May 04 21:11:55 CEST 2015 */
package webanno.custom;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon May 04 21:11:55 CEST 2015
 * XML source: /Users/reiterns/Documents/SegNarr/Annotationspaket_2/DW/xmi-original/typesystem.xml
 * @generated */
public class Zusammenfassung extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Zusammenfassung.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Zusammenfassung() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Zusammenfassung(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Zusammenfassung(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Zusammenfassung(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: Zusammenfassung

  /** getter for Zusammenfassung - gets 
   * @generated
   * @return value of the feature 
   */
  public String getZusammenfassung() {
    if (Zusammenfassung_Type.featOkTst && ((Zusammenfassung_Type)jcasType).casFeat_Zusammenfassung == null)
      jcasType.jcas.throwFeatMissing("Zusammenfassung", "webanno.custom.Zusammenfassung");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Zusammenfassung_Type)jcasType).casFeatCode_Zusammenfassung);}
    
  /** setter for Zusammenfassung - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setZusammenfassung(String v) {
    if (Zusammenfassung_Type.featOkTst && ((Zusammenfassung_Type)jcasType).casFeat_Zusammenfassung == null)
      jcasType.jcas.throwFeatMissing("Zusammenfassung", "webanno.custom.Zusammenfassung");
    jcasType.ll_cas.ll_setStringValue(addr, ((Zusammenfassung_Type)jcasType).casFeatCode_Zusammenfassung, v);}    
  }

    