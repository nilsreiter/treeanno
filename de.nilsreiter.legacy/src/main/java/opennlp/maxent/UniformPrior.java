package opennlp.maxent;

/**
 * Provide a maximum entropy model with a uniform prior.
 * @author Tom Morton
 *
 */
public class UniformPrior implements Prior {

  private int numOutcomes;
  private double r;
    
  public void logPrior(double[] dist, int[] context, float[] values) {
    for (int oi=0;oi<numOutcomes;oi++) {
      dist[oi] = r;
    }
  }
  
  public void logPrior(double[] dist, int[] context) {
    logPrior(dist,context,null);
  }

  public void setLabels(String[] outcomeLabels, String[] contextLabels) {
    this.numOutcomes = outcomeLabels.length;
    r = Math.log(1.0/numOutcomes);
  }
}
