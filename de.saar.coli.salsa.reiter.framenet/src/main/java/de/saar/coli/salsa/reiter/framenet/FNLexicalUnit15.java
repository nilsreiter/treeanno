package de.saar.coli.salsa.reiter.framenet;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

import de.uniheidelberg.cl.reiter.pos.FN;

/**
 * This class is for reading FrameNet 1.5 data.
 * 
 * @author reiter
 * 
 */
public class FNLexicalUnit15 extends FNLexicalUnit {
    /**
	 * 
	 */
    private static final long serialVersionUID = -9072569527923105655L;

    /**
     * Constructs a new lexical unit from the corresponding XML node. The node
     * should provide the following attributes: ID, name, status, cDate,
     * lemmaId. A definition node should be a direct sub node of the node. <br/>
     * <code>
     * &lt;lexunit ID="3" name="cause.n" pos="N" status="Finished_Initial" cDate="Thu Feb 08 13:27:25 PST 2001" lemmaId="5802"&gt;
     * 
     * &lt;definition&gt;COD: a person or thing that gives rise to an action, phenomenon, or condition.&lt;/definition&gt;
     * 
     * &lt;/lexunit&gt;</code><br/>
     * The above, for instance, is a complete node that can be processed by the
     * constructor.
     * 
     * @param node
     *            The node for the lexical unit.
     * @param frame
     *            The frame in which this lexical unit appears
     * @param reader
     *            The database reader
     */
    protected FNLexicalUnit15(final FNDatabaseReader15 reader,
	    final Frame frame, final Element node) {
	this.frame = frame;
	id = Integer.valueOf(node.attributeValue("ID"));
	name = node.attributeValue("name");
	status = Strings.getString(node.attributeValue("status"));
	try {
	    if (!node.attributeValue("cDate").equals("")) {
		creationDate =
			reader.getDateFormat().parse(
				node.attributeValue("cDate"));
	    }
	} catch (NumberFormatException e) {
	    System.err.println(node.asXML());
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	lemmaID = node.attributeValue("lemmaId");
	lemma = name.substring(0, name.lastIndexOf('.'));
	definition =
		node.element("definition").getText().replace('\n', ' ')
			.getBytes();

	partOfSpeech = FN.fromShortString(node.attributeValue("POS"));

	lexemes = new LinkedList<Lexeme>();

	List<?> lexemeNodelist = node.elements("lexeme"); // xpath.selectNodes(node);
	for (int i = 0; i < lexemeNodelist.size(); i++) {
	    Element lexemeElement = (Element) lexemeNodelist.get(i);
	    Lexeme lexeme = new FNLexeme15(lexemeElement);
	    lexemes.add(lexeme);
	}

	this.frame.getFramenet().addLexicalUnit(this);
    }
}
