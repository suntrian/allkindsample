package org.example.neo4jSample;

import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Neo4jEmbededSample {

    public static void main(String[] args) {
        String relativeDir = Neo4jEmbededSample.class.getClassLoader().getResource("./..").getPath();

        DatabaseManagementServiceBuilder builder = new DatabaseManagementServiceBuilder(new File(relativeDir));
        DatabaseManagementService managementService = builder.build();
        managementService.createDatabase("abc");
        GraphDatabaseService graphDatabaseService = managementService.database("abc");

        graphDatabaseService.executeTransactionally("CREATE (a:Person {name: {name}, title: {title}})",
                new HashMap<String, Object>() {{
                    put("name", "张三");
                    put("title", "家的瓜");
                }});

        Map<String, Object> r = graphDatabaseService.executeTransactionally("MATCH (a:Person) WHERE a.name = {name} " +
                        "RETURN a.name AS name, a.title AS title",
                new HashMap<String, Object>() {{
                    put("name", "张三");
                    put("title", "家的瓜");
                }}, x -> {
                    Map<String, Object> y = new HashMap<>();
                    y.put("x", x.resultAsString());
                    return y;
                }
        );
    }
}
