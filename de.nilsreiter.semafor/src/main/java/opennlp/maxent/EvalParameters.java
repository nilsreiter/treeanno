package opennlp.maxent;

 /**
 * This class encapsulates the varibales used in producing probabilities from a model 
 * and facilitaes passing these variables to the eval method.  Variables are declared
 * non-private so that they may be accessed and updated without a method call for efficiency
 * reasons.
 * @author Tom Morton
 *
 */
public class EvalParameters {
  
 /** Mapping between outcomes and paramater values for each context. 
   * The integer representation of the context can be found using <code>pmap</code>.*/
  Context[] params;
  /** The number of outcomes being predicted. */
  final int numOutcomes;
  /** The maximum number of feattures fired in an event. Usually refered to a C.
   * This is used to normalize the number of features which occur in an event. */
  double correctionConstant;
  
  /**  Stores inverse of the correction constant, 1/C. */
  final double constantInverse;
  /** The correction parameter of the model. */
  double correctionParam;
  /** Log of 1/C; initial value of probabilities. */
  final double iprob;
    
  /**
   * Creates a set of paramters which can be evaulated with the eval method.
   * @param params The parameters of the model.
   * @param correctionParam The correction paramter.
   * @param correctionConstant The correction constant.
   * @param numOutcomes The number of outcomes.
   */
  public EvalParameters(Context[] params, double correctionParam, double correctionConstant, int numOutcomes) {
    this.params = params;
    this.correctionParam = correctionParam;
    this.numOutcomes = numOutcomes;
    this.correctionConstant = correctionConstant;
    this.constantInverse = 1.0 / correctionConstant;
    this.iprob = Math.log(1.0/numOutcomes);
  }  
}