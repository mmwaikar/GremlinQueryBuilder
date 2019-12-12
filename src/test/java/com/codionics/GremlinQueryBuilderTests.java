package com.codionics;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class GremlinQueryBuilderTests {

  private final String VERSION = "V-1";
  private final String ID = "ID-1";
  private final String KEY = "KEY-1";
  private final String CONT_KEY = "CKEY-1";
  private final String TYPE_KEY = "TKEY-1";
  private final String EDGE_LABEL = "Relation";
  private final String VERTEX_LABEL = "Artifact";

  private final String ALL_EDGES_QUERY = "g.E()";
  private final String ALL_VERTICES_QUERY = "g.V()";

  private final String EDGE_WITH_LABEL_QUERY = String.format("g.E().hasLabel('%s')", EDGE_LABEL);
  private final String VERTEX_WITH_LABEL_QUERY = String.format("g.V().hasLabel('%s')", VERTEX_LABEL);

  private final String EDGE_KEY_CONT_LATEST_V_QUERY = String
      .format("g.E().has('sKey', '%s').has('container', 'CKEY-1').has('_isLatest', 'TRUE')", KEY);

  private final String EDGE_KEY_V_CONT_LATEST_V_QUERY = String.format(
      "g.E().has('sKey', '%s').has('version', '%s').has('container', 'CKEY-1').has('_isLatest', 'TRUE')", KEY, VERSION);

  private final String EDGE_TYPE_CONT_LATEST_V_QUERY = String
      .format("g.E().has('type', '%s').has('container', 'CKEY-1').has('_isLatest', 'TRUE')", TYPE_KEY);

  private final String EDGE_SPEC_CONT_LATEST_V_QUERY = "g.E().hasLabel('Relation').has('container', 'CKEY-1').has('_isLatest', 'TRUE')";

  @Test
  public void should_build_query_for_vertices() {
    GremlinQueryBuilder gqb = new GremlinQueryBuilder().vertices();
    String query = gqb.getQuery();
    System.out.println("all vertices query: " + query);

    assertThat("query is null", query, is(notNullValue()));
    assertThat("query is empty", query.isEmpty(), is(false));
    assertThat("query is empty", query, is(ALL_VERTICES_QUERY));

    // import static org.junit.Assert.fail;
    // fail("Not yet implemented");
  }

  @Test
  public void should_build_query_for_edges() {
    GremlinQueryBuilder gqb = new GremlinQueryBuilder().edges();
    String query = gqb.getQuery();
    System.out.println("all edges query: " + query);

    assertThat("query is null", query, is(notNullValue()));
    assertThat("query is empty", query.isEmpty(), is(false));
    assertThat("query is empty", query, is(ALL_EDGES_QUERY));
  }

  @Test
  public void should_build_query_for_vertex_with_label() {
    VertexSpec vs = new VertexSpec().hasLabel(VERTEX_LABEL);
    GremlinQueryBuilder gqb = new GremlinQueryBuilder().withVertex(vs);
    String query = gqb.getQuery();
    System.out.println("vertex with label query: " + query);

    assertThat("query is null", query, is(notNullValue()));
    assertThat("query is empty", query.isEmpty(), is(false));
    assertThat("query is empty", query, is(VERTEX_WITH_LABEL_QUERY));
  }

  @Test
  public void should_build_query_for_edge_with_label() {
    EdgeSpec es = new EdgeSpec().hasLabel(EDGE_LABEL);
    GremlinQueryBuilder gqb = new GremlinQueryBuilder().withEdge(es);
    String query = gqb.getQuery();
    System.out.println("edge with label query: " + query);

    assertThat("query is null", query, is(notNullValue()));
    assertThat("query is empty", query.isEmpty(), is(false));
    assertThat("query is empty", query, is(EDGE_WITH_LABEL_QUERY));
  }

  @Test
  public void should_build_query_for_source_vertex_with_prop() {
    VertexSpec vs = new VertexSpec().hasLabel(VERTEX_LABEL).hasId(ID);
    EdgeSpec es = new EdgeSpec().hasLabel(EDGE_LABEL).outV(vs);
    GremlinQueryBuilder gqb = new GremlinQueryBuilder().withEdge(es);
    String query = gqb.getQuery();
    System.out.println("edge with label & source vertex with id query: " + query);

    assertThat("query is null", query, is(notNullValue()));
    assertThat("query is empty", query.isEmpty(), is(false));
    assertThat("query is empty", query, is(EDGE_SPEC_CONT_LATEST_V_QUERY));
  }

  @Test
  public void should_build_query_for_edge_with_label_specific_container_latest_version() {
    EdgeSpec es = new EdgeSpec().forSpecificContainer(CONT_KEY).hasLabel(EDGE_LABEL).lastestVersionOfRelations(true);
    GremlinQueryBuilder gqb = new GremlinQueryBuilder().withEdge(es);
    String query = gqb.getQuery();
    System.out.println("edge with label query for specific container key, only latest version: " + query);

    assertThat("query is null", query, is(notNullValue()));
    assertThat("query is empty", query.isEmpty(), is(false));
    assertThat("query is empty", query, is(EDGE_SPEC_CONT_LATEST_V_QUERY));
  }

  @Test
  public void should_build_query_for_edge_with_type_specific_container_latest_version() {
    EdgeSpec es = new EdgeSpec().hasType(TYPE_KEY).forSpecificContainer(CONT_KEY).lastestVersionOfRelations(true);
    GremlinQueryBuilder gqb = new GremlinQueryBuilder().withEdge(es);
    String query = gqb.getQuery();
    System.out.println("edge with type query for specific container key, only latest version: " + query);

    assertThat("query is null", query, is(notNullValue()));
    assertThat("query is empty", query.isEmpty(), is(false));
    assertThat("query is empty", query, is(EDGE_TYPE_CONT_LATEST_V_QUERY));
  }

  @Test
  public void should_build_query_for_edge_with_key_specific_container_latest_version() {
    EdgeSpec es = new EdgeSpec().hasKey(KEY).forSpecificContainer(CONT_KEY).lastestVersionOfRelations(true);
    GremlinQueryBuilder gqb = new GremlinQueryBuilder().withEdge(es);
    String query = gqb.getQuery();
    System.out.println("edge with key query for specific container key, only latest version: " + query);

    assertThat("query is null", query, is(notNullValue()));
    assertThat("query is empty", query.isEmpty(), is(false));
    assertThat("query is empty", query, is(EDGE_KEY_CONT_LATEST_V_QUERY));
  }

  @Test
  public void should_build_query_for_edge_with_key_version_specific_container_latest_version() {
    EdgeSpec es = new EdgeSpec().hasKey(KEY).hasVersion(VERSION).forSpecificContainer(CONT_KEY)
        .lastestVersionOfRelations(true);
    GremlinQueryBuilder gqb = new GremlinQueryBuilder().withEdge(es);
    String query = gqb.getQuery();
    System.out.println("edge with key, version query for specific container key, only latest version: " + query);

    assertThat("query is null", query, is(notNullValue()));
    assertThat("query is empty", query.isEmpty(), is(false));
    assertThat("query is empty", query, is(EDGE_KEY_V_CONT_LATEST_V_QUERY));
  }
}
