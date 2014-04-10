package de.uniheidelberg.cl.a10.data2.alignment;

import de.uniheidelberg.cl.a10.api.IdProvider;

public interface AlignmentIdProvider extends IdProvider {

	String getNextAlignmentId();
}