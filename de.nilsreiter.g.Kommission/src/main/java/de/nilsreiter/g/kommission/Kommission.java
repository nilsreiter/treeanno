package de.nilsreiter.g.kommission;

import de.nilsreiter.g.kommission.data.Community;
import de.nilsreiter.g.kommission.data.Person;

public class Kommission {
	public static void main(String[] args) {

		Community comm = CommunityFactory.getCommunity(0);

		for (Person p : comm.getPersons()) {
			System.out.println(p.toString());

		}
	}
}
