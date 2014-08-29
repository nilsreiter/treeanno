package opennlp.maxent;

/**
 * This interface allows one to implement a prior distribution for use in
 * maximum entropy model training.  
 * @author Tom Morton
 *
 */
public interface Prior {
  /**
   * Populates the specified array with the the log of the distribution for the specified context.  
   * The returned array will be overwritten and needs to be re-initialized with every call to this method.  
   * @param dist An array to be populated with the log of the prior distribution.
   * @param context The indices of the contextual predicates for an event.
   */
  public void logPrior(double[] dist, int[] context);
  
  /**
   * Populates the specified array with the the log of the distribution for the specified context.  
   * The returned array will be overwritten and needs to be re-initialized with every call to this method.  
   * @param dist An array to be populated with the log of the prior distribution.
   * @param context The indices of the contextual predicates for an event.
   * @param values The values associated with the context. 
   */
  public void logPrior(double[] dist, int[] context, float[] values);

  /**
   * Method to specify the label for the outcomes and contexts.  This is used to map 
   * integer outcomes and contexts to their string values.  This method is called prior
   * to any call to #logPrior.
   * @param outcomeLabels An array of each outcome label.
   * @param contextLabels An array of each context label.
   */
  public void setLabels(String[] outcomeLabels, String[] contextLabels);
}
