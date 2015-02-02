package de.nilsreiter.g.kommission;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.Well19937c;

import de.nilsreiter.g.kommission.data.Community;
import de.nilsreiter.g.kommission.data.impl.Community_impl;
import de.nilsreiter.g.kommission.data.impl.Person_impl;
import de.nilsreiter.g.kommission.data.impl.Property_impl;

public class CommunityFactory {
	public static Community getCommunity(int seed) {

		NormalDistribution propertyDistribution =
				new NormalDistribution(new Well19937c(seed),
						Settings.number_of_properties * 0.5,
						Settings.number_of_properties * 0.1,
						NormalDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);

		NormalDistribution propertyValueDistribution =
				new NormalDistribution(new Well19937c(seed),
						Settings.best_property_value * 0.5,
						Settings.best_property_value * 0.1,
						NormalDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);

		Community_impl community = new Community_impl();

		for (int i = 0; i < Settings.number_of_properties; i++) {
			community.getProperties().add(
					new Property_impl("Property" + String.valueOf(i)));
		}

		for (int i = 0; i < Settings.size_of_community; i++) {
			Person_impl person = new Person_impl();
			person.setId(i);
			Map<Property_impl, Double> props =
					new HashMap<Property_impl, Double>();
			for (int j = 0; j < Settings.number_of_properties_per_player; j++) {
				Property_impl p =
						community.getProperties()
						.get((int) Math.round(propertyDistribution
								.sample()));
				props.put(p, propertyValueDistribution.sample());
			}
			person.setProperties(props);
			community.getPersons().add(person);
		}
		return community;
	}
}
