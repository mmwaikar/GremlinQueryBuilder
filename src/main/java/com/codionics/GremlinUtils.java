package com.codionics;

import org.apache.log4j.Logger;

public interface GremlinUtils {
  final Logger logger = Logger.getLogger(GremlinUtils.class);

  default String getQuery(EdgeSpec es) {
    GremlinQueryBuilder queryBuilder = new GremlinQueryBuilder().withEdge(es);
    String gremlinQuery = queryBuilder.getQuery();
    logger.debug(String.format("Gremlin query using edge specification: %s", gremlinQuery));
    return gremlinQuery;
  }

  default String getQuery(VertexSpec vs) {
    GremlinQueryBuilder queryBuilder = new GremlinQueryBuilder().withVertex(vs);
    String gremlinQuery = queryBuilder.getQuery();
    logger.debug(String.format("Gremlin query using vertex specification: %s", gremlinQuery));
    return gremlinQuery;
  }
}
