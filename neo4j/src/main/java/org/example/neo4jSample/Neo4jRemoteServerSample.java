package org.example.neo4jSample;

import org.neo4j.driver.*;

import static org.neo4j.driver.Values.parameters;

public class Neo4jRemoteServerSample {

    public void boltCreateAndQuery() {
        createAndQuery("bolt://192.168.2.101:7687", "neo4j", "qwer1234");
    }

    public void neo4jCreateAndQuery() {
        createAndQuery("neo4j://192.168.2.101:7474", "neo4j", "qwer1234");
    }


    public void createAndQuery(String uri, String username, String password) {

        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        Session session = driver.session();

//        session.run( "CREATE (a:Person {name: {name}, title: {title}})",
//                parameters( "name", "Arthur001", "title", "King001" ) );

        Result result = session.run("MATCH (a:Person) WHERE a.name = {name} " +
                        "RETURN a.name AS name, a.title AS title",
                parameters("name", "Arthur001"));
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println(record.get("title").asString() + " " + record.get("name").asString());
        }
        session.close();
        driver.close();
    }


}
