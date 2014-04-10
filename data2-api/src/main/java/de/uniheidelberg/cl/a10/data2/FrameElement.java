package de.uniheidelberg.cl.a10.data2;

import java.util.Collection;
import java.util.List;

public interface FrameElement extends AnnotationObjectInDocument, HasTokensAndHead {

	Collection<? extends Mention> getMentions();

	Collection<? extends Mention> getCoreferringMentions();

	Frame getFrame();

	boolean hasOneToOneMapping();

	/**
	 * @return the name
	 */
	String getName();

	List<? extends Entity> getInLinkingEntities();

	@Deprecated
	String toTargetString();
}