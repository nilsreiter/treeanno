package de.uniheidelberg.cl.a10.data2;

import java.util.Set;

import de.uniheidelberg.cl.a10.HasTarget;

public interface Frame extends HasOldId, HasTarget, HasTokens,
		AnnotationObjectInDocument {

	Set<? extends FrameElement> getFrameElms();

	FrameElement getFrameElm(String feName);

	String toExtendedString();

	/**
	 * @return the frameName
	 */
	String getFrameName();

	FrameElement getById(String id);

}