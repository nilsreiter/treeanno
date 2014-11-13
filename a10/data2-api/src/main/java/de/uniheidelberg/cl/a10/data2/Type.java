package de.uniheidelberg.cl.a10.data2;

@Deprecated
public enum Type {
	Token, Entity, Frame, FrameElm, Mention, Mantra, Sentence, Section, Sense, Chunk, Document, Event;

	public static Type getType(final Class<? extends AnnotationObject> cl) {
		if (cl.isAssignableFrom(Token.class))
			return Token;
		if (cl.isAssignableFrom(Entity.class))
			return Entity;
		if (cl.isAssignableFrom(Frame.class))
			return Frame;
		if (cl.isAssignableFrom(FrameElement.class))
			return FrameElm;
		if (cl.isAssignableFrom(Mention.class))
			return Mention;
		if (cl.isAssignableFrom(Mantra.class))
			return Mantra;
		if (cl.isAssignableFrom(Sentence.class))
			return Sentence;
		if (cl.isAssignableFrom(Section.class))
			return Section;
		if (cl.isAssignableFrom(Sense.class))
			return Sense;
		if (cl.isAssignableFrom(Chunk.class))
			return Chunk;
		if (cl.isAssignableFrom(Document.class))
			return Document;
		if (FrameTokenEvent.class.isAssignableFrom(cl))
			return Event;
		return null;
	}
}
