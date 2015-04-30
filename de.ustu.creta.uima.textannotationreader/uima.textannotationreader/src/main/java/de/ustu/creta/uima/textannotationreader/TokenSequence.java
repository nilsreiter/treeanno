package de.ustu.creta.uima.textannotationreader;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class TokenSequence {
	List<Pair<Integer, Integer>> characterPositions;
	List<String> surfaces;

	public List<Pair<Integer, Integer>> getCharacterPositions() {
		return characterPositions;
	}

	public void setCharacterPositions(
			List<Pair<Integer, Integer>> characterPositions) {
		this.characterPositions = characterPositions;
	}

	public List<String> getSurfaces() {
		return surfaces;
	}

	public void setSurfaces(List<String> surfaces) {
		this.surfaces = surfaces;
	}
}
