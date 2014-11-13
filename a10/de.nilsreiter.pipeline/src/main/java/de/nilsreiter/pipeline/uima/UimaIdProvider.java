package de.nilsreiter.pipeline.uima;

public class UimaIdProvider {
	int sentence = 0;
	int token = 0;
	int entity = 0;
	int mention = 0;
	int frame = 0;
	int frameelement = 0;
	int sense = 0;
	int event = 0;

	String sentence_prefix = "s";
	String token_prefix = "t";
	String entity_prefix = "e";
	String mention_prefix = "m";
	String frame_prefix = "f";
	String frameelement_prefix = "fe";
	String sense_prefix = "sense";
	String event_prefix = "event";

	public String getNextSentenceId() {
		return sentence_prefix + (sentence++);
	}

	public String getNextTokenId() {
		return token_prefix + (token++);
	}

	public String getNextEntityId() {
		return entity_prefix + (entity++);
	}

	public String getNextMentionId() {
		return mention_prefix + (mention++);
	}

	public String getNextFrameId() {
		return frame_prefix + (frame++);
	}

	public String getNextFrameElementId() {
		return frameelement_prefix + (frameelement++);
	}

	public String getNextSenseId() {
		return sense_prefix + (sense++);
	}

	public String getNextEventId() {
		return event_prefix + (event++);
	}
}
