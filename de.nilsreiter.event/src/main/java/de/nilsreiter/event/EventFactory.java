package de.nilsreiter.event;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.HasTokens;

public interface EventFactory {
	Event makeEvent(HasTokens source);
}