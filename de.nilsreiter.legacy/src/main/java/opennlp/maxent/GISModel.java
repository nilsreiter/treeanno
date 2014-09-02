///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2004 Jason Baldridge, Gann Bierner, and Tom Morton
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////   
package opennlp.maxent;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * A maximum entropy model which has been trained using the Generalized
 * Iterative Scaling procedure (implemented in GIS.java).
 *
 * @author      Tom Morton and Jason Baldridge
 * @version     $Revision: 1.22 $, $Date: 2007/04/12 17:22:31 $
 */
public final class GISModel implements MaxentModel {
    /** Maping between predicates/contexts and an integer representing them. */
    private final TObjectIndexHashMap pmap;
    /** The names of the outcomes. */
    private final String[] ocNames;
    private DecimalFormat df;
    private EvalParameters evalParams;
    private Prior prior;
    

    /**
     * Creates a new model with the specified parameters, outcome names, and predicate/feature labels. 
     * @param params The parameters of the model.
     * @param predLabels The names of the predicates used in this model.
     * @param outcomeNames The names of the outcomes this model predicts.
     * @param correctionConstant The maximum number of active features which occur in an event.
     * @param correctionParam The parameter associated with the correction feature.
     */
    public GISModel (Context[] params, String[] predLabels, String[] outcomeNames, int correctionConstant, double correctionParam) {
      this(params,predLabels,outcomeNames,correctionConstant,correctionParam, new UniformPrior());
    }

    /**
      * Creates a new model with the specified parameters, outcome names, and predicate/feature labels. 
      * @param params The parameters of the model.
      * @param predLabels The names of the predicates used in this model.
      * @param outcomeNames The names of the outcomes this model predicts.
      * @param correctionConstant The maximum number of active features which occur in an event.
      * @param correctionParam The parameter associated with the correction feature.
      * @param prior The prior to be used with this model.
      */
    public GISModel (Context[] params, String[] predLabels, String[] outcomeNames, int correctionConstant,double correctionParam, Prior prior) {
      this.pmap = new TObjectIndexHashMap(predLabels.length);
      for (int i=0; i<predLabels.length; i++) {
        pmap.put(predLabels[i], i);
      }
      this.ocNames =  outcomeNames;
      this.evalParams = new EvalParameters(params,correctionParam,correctionConstant,ocNames.length);
      this.prior = prior;
      prior.setLabels(ocNames, predLabels);
    }

    /**
     * Use this model to evaluate a context and return an array of the
     * likelihood of each outcome given that context.
     *
     * @param context The names of the predicates which have been observed at
     *                the present decision point.
     * @return        The normalized probabilities for the outcomes given the
     *                context. The indexes of the double[] are the outcome
     *                ids, and the actual string representation of the
     *                outcomes can be obtained from the method
     *  	      getOutcome(int i).
     */
    public final double[] eval(String[] context) {
      return(eval(context,new double[evalParams.numOutcomes]));
    }
    
    public final double[] eval(String[] context, float[] values) {
      return(eval(context,values,new double[evalParams.numOutcomes]));
    }
    
    /**
     * Use this model to evaluate a context and return an array of the
     * likelihood of each outcome given the specified context and the specified parameters.
     * @param context The integer values of the predicates which have been observed at
     *                the present decision point.
     * @param prior The prior distribution for the specified context.
     * @param model The set of parametes used in this computation.
     * @return The normalized probabilities for the outcomes given the
     *                context. The indexes of the double[] are the outcome
     *                ids, and the actual string representation of the
     *                outcomes can be obtained from the method
     *                getOutcome(int i).
     */
    public static double[] eval(int[] context, double[] prior, EvalParameters model) {
      return eval(context,null,prior,model);
    }
    
    /**
     * Use this model to evaluate a context and return an array of the
     * likelihood of each outcome given the specified context and the specified parameters.
     * @param context The integer values of the predicates which have been observed at
     *                the present decision point.
     *                @param values The values for each of the parameters.
     * @param prior The prior distribution for the specified context.
     * @param model The set of parametes used in this computation.
     * @return The normalized probabilities for the outcomes given the
     *                context. The indexes of the double[] are the outcome
     *                ids, and the actual string representation of the
     *                outcomes can be obtained from the method
     *                getOutcome(int i).
     */
    public static double[] eval(int[] context, float[] values, double[] prior, EvalParameters model) {
      Context[] params = model.params;
      int numfeats[] = new int[model.numOutcomes];
      int[] activeOutcomes;
      double[] activeParameters;
      double value = 1;
      for (int ci = 0; ci < context.length; ci++) {
        if (context[ci] >= 0) {
          Context predParams = params[context[ci]];
          activeOutcomes = predParams.getOutcomes();
          activeParameters = predParams.getParameters();
          if (values != null) {
            value = values[ci];
          }
          for (int ai = 0; ai < activeOutcomes.length; ai++) {
            int oid = activeOutcomes[ai];
            numfeats[oid]++;
            prior[oid] += activeParameters[ai] * value;
          }
        }
      }

      double normal = 0.0;
      for (int oid = 0; oid < model.numOutcomes; oid++) {
        if (model.correctionParam != 0) {
          prior[oid] = Math.exp(prior[oid]*model.constantInverse+((1.0 - ((double) numfeats[oid] / model.correctionConstant)) * model.correctionParam));
        }
        else {
          prior[oid] = Math.exp(prior[oid]*model.constantInverse);
        }
        normal += prior[oid];
      }

      for (int oid = 0; oid < model.numOutcomes; oid++) {
        prior[oid] /= normal;
      }
      return prior;
    }
    
    public final double[] eval(String[] context, double[] outsums) {
      return eval(context,null,outsums);
    }
    
    /**
     * Use this model to evaluate a context and return an array of the
     * likelihood of each outcome given that context.
     *
     * @param context The names of the predicates which have been observed at
     *                the present decision point.
     * @param outsums This is where the distribution is stored.
     * @return        The normalized probabilities for the outcomes given the
     *                context. The indexes of the double[] are the outcome
     *                ids, and the actual string representation of the
     *                outcomes can be obtained from the method
     *                getOutcome(int i).
     */
    public final double[] eval(String[] context, float[] values, double[] outsums) {
      int[] scontexts = new int[context.length];
      for (int i=0; i<context.length; i++) {
        scontexts[i] = pmap.get(context[i]);
      }
      prior.logPrior(outsums, scontexts,values);
      return GISModel.eval(scontexts,values,outsums,evalParams);
    }

    
    /**
     * Return the name of the outcome corresponding to the highest likelihood
     * in the parameter ocs.
     *
     * @param ocs A double[] as returned by the eval(String[] context)
     *            method.
     * @return    The name of the most likely outcome.
     */    
    public final String getBestOutcome(double[] ocs) {
        int best = 0;
        for (int i = 1; i<ocs.length; i++)
            if (ocs[i] > ocs[best]) best = i;
        return ocNames[best];
    }

    
    /**
     * Return a string matching all the outcome names with all the
     * probabilities produced by the <code>eval(String[] context)</code>
     * method.
     *
     * @param ocs A <code>double[]</code> as returned by the
     *            <code>eval(String[] context)</code>
     *            method.
     * @return    String containing outcome names paired with the normalized
     *            probability (contained in the <code>double[] ocs</code>)
     *            for each one.
     */    
    public final String getAllOutcomes (double[] ocs) {
        if (ocs.length != ocNames.length) {
            return "The double array sent as a parameter to GISModel.getAllOutcomes() must not have been produced by this model.";
        }
        else {
            if (df == null) { //lazy initilazation
              df = new DecimalFormat("0.0000");
            }
            StringBuffer sb = new StringBuffer(ocs.length*2);
            sb.append(ocNames[0]).append("[").append(df.format(ocs[0])).append("]");
            for (int i = 1; i<ocs.length; i++) {
                sb.append("  ").append(ocNames[i]).append("[").append(df.format(ocs[i])).append("]");
            }
            return sb.toString();
        }
    }

    
    /**
     * Return the name of an outcome corresponding to an int id.
     *
     * @param i An outcome id.
     * @return  The name of the outcome associated with that id.
     */
    public final String getOutcome(int i) {
        return ocNames[i];
    }

    /**
     * Gets the index associated with the String name of the given outcome.
     *
     * @param outcome the String name of the outcome for which the
     *          index is desired
     * @return the index if the given outcome label exists for this
     * model, -1 if it does not.
     **/
    public int getIndex (String outcome) {
        for (int i=0; i<ocNames.length; i++) {
            if (ocNames[i].equals(outcome))
                return i;
        }
        return -1;
    } 

    public int getNumOutcomes() {
      return(evalParams.numOutcomes);
    }

    
    /**
     * Provides the fundamental data structures which encode the maxent model
     * information.  This method will usually only be needed by
     * GISModelWriters.  The following values are held in the Object array
     * which is returned by this method:
     *
     * <li>index 0: opennlp.maxent.Context[] containing the model
     *            parameters  
     * <li>index 1: java.util.Map containing the mapping of model predicates
     *            to unique integers
     * <li>index 2: java.lang.String[] containing the names of the outcomes,
     *            stored in the index of the array which represents their
     * 	          unique ids in the model.
     * <li>index 3: java.lang.Integer containing the value of the models
     *            correction constant
     * <li>index 4: java.lang.Double containing the value of the models
     *            correction parameter
     *
     * @return An Object[] with the values as described above.
     */
    public final Object[] getDataStructures () {
        Object[] data = new Object[5];
        data[0] = evalParams.params;
        data[1] = pmap;
        data[2] = ocNames;
        data[3] = new Integer((int)evalParams.correctionConstant);
        data[4] = new Double(evalParams.correctionParam);
        return data;
    }
    
    public static void main(String[] args) throws java.io.IOException {
      if (args.length == 0) {
        System.err.println("Usage: GISModel modelname < contexts");
        System.exit(1);
      }
      GISModel m = new opennlp.maxent.io.SuffixSensitiveGISModelReader(new File(args[0])).getModel();
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      DecimalFormat df = new java.text.DecimalFormat(".###");
      for (String line = in.readLine(); line != null; line = in.readLine()) {
        String[] context = line.split(" ");
        double[] dist = m.eval(context);
        for (int oi=0;oi<dist.length;oi++) {
          System.out.print("["+m.getOutcome(oi)+" "+df.format(dist[oi])+"] ");
        }
        System.out.println();
      }
    }
}