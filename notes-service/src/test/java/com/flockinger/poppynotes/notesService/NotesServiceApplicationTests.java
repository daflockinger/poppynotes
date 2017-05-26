package com.flockinger.poppynotes.notesService;

import java.io.IOException;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CassandraConfig.class)
public class NotesServiceApplicationTests {

	@BeforeClass
	public static void startCassandraEmbedded()
			throws ConfigurationException, TTransportException, IOException, InterruptedException {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra();
		Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").withPort(9142).build();
		Session session = cluster.connect();

		String query = "CREATE KEYSPACE poppynotes WITH replication "
				+ "= {'class':'SimpleStrategy', 'replication_factor':1}; ";
		session.execute(query);
	}

	@Test
	public void contextLoads() {
	}

	@AfterClass
	public static void stopCassandraEmbedded() {
		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}
}
