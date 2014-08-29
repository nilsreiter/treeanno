package de.uniheidelberg.cl.a10.semafortraining.uima;

public class SemaforFrameElementAnnotation extends SemaforAnnotation {
	String frameElementName;

	public SemaforFrameElementAnnotation(final String frameElementName) {
		this.frameElementName = frameElementName;
	}

	public String getFrameElementName() {
		return frameElementName;
	}

	@Override
	public String getTargetIdString() throws Exception {
		if (targetTokens.size() == 0) {
			throw new Exception("Sentence has no target tokens");
		} else if (targetTokens.size() == 1) {
			return String.valueOf((targetTokens.first()));
		} else {
			StringBuffer buf = new StringBuffer();
			buf.append(String.valueOf(targetTokens.first())).append(':');
			buf.append(String.valueOf(targetTokens.last()));
			return buf.toString();
		}
	}

}
