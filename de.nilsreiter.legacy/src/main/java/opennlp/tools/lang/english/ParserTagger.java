package opennlp.tools.lang.english;

import java.io.File;
import java.io.IOException;
import java.util.List;

import opennlp.maxent.MaxentModel;
import opennlp.maxent.io.SuffixSensitiveGISModelReader;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.postag.DefaultPOSContextGenerator;
import opennlp.tools.postag.POSDictionary;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Sequence;

/**
 * Part-of-speech tagger used by the parser. 
 */
public class ParserTagger extends POSTaggerME implements opennlp.tools.parser.ParserTagger {

  private static final int K = 10;
  int beamSize;

  public ParserTagger(MaxentModel model, Dictionary dict) {
    super(model, dict);
    beamSize = K;
  }
  
  public ParserTagger(String modelFile,Dictionary dict) throws IOException {
    this(modelFile,K,K,dict);
  }
  
  public ParserTagger(MaxentModel model, String tagDictionary, boolean useCase) throws IOException {
    this(model,K,null,tagDictionary,useCase,K);
  }
  
  public ParserTagger(MaxentModel model, int beamSize, Dictionary dict, String tagDictionary, boolean useCase, int cacheSize) throws IOException {
    super(beamSize, model, new DefaultPOSContextGenerator(cacheSize,dict), new POSDictionary(tagDictionary, useCase));
    this.beamSize = beamSize;
  }

  public ParserTagger(String modelFile,int beamSize, int cacheSize,Dictionary dict) throws IOException {
    super(beamSize, new SuffixSensitiveGISModelReader(new File(modelFile)).getModel(), new DefaultPOSContextGenerator(cacheSize,dict), null);
    this.beamSize = beamSize;
  }
  
  public ParserTagger(String modelFile, String tagDictionary, boolean useCase) throws IOException {
    this(modelFile,K,null,tagDictionary,useCase,K);
  }
  
  public ParserTagger(String modelFile, String tagDictionary, boolean useCase, Dictionary dict) throws IOException {
    this(modelFile,K,dict,tagDictionary,useCase,K);
  }
  
  public ParserTagger(String modelFile, int beamSize, Dictionary dict, String tagDictionary, boolean useCase, int cacheSize) throws IOException {
    super(beamSize, new SuffixSensitiveGISModelReader(new File(modelFile)).getModel(), new DefaultPOSContextGenerator(cacheSize,dict), new POSDictionary(tagDictionary, useCase));
    this.beamSize = beamSize;
  }

  public Sequence[] topKSequences(List sentence) {
    return beam.bestSequences(beamSize, sentence.toArray(), null);
  }

  public Sequence[] topKSequences(String[] sentence) {
    return beam.bestSequences(beamSize, sentence, null);
  }
}
