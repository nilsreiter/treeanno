package de.ustu.ims.reiter.treeanno;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.naming.NamingException;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.io.DatabaseIO;

public class TestDataCache {

	BasicDataSource dataSource;

	DataLayer dataLayer;
	Project project;
	User user;

	@Before
	public void setUp() throws ClassNotFoundException, NamingException {
		dataSource = new BasicDataSource();
		dataSource
				.setUrl("jdbc:mysql://localhost/de.ustu.ims.reiter.treeanno.test");
		dataSource.setUsername("reiterns");
		dataSource.setPassword("bybNoaKni");

		dataLayer = new DataCache(new DatabaseIO(dataSource));

		project = mock(Project.class);
		when(project.getDatabaseId()).thenReturn(1);

		user = mock(User.class);
		when(user.getDatabaseId()).thenReturn(1);
	}

	@Test
	public void testEmptyDatabase() {
		assertEquals(0, dataLayer.getProjects().size());
		assertNull(dataLayer.getUser(1));
		assertNull(dataLayer.getDocument(1));
		assertNull(dataLayer.getProject(1));
		assertEquals(0, dataLayer.getAccessLevel(project, user));
		assertEquals(0, dataLayer.getDocuments(project).size());
	}
}
