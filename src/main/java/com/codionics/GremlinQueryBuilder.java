package com.codionics;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.codionics.utils.StringUtils;

public class GremlinQueryBuilder implements StringUtils {
  private final Logger logger = Logger.getLogger(GremlinQueryBuilder.class);

  private final EdgeSpec emptyEdgeSpec = new EdgeSpec();
  private final VertexSpec emptyVertexSpec = new VertexSpec();

  private List<VertexOrEdgeSpec> verticesEdgesSpecs = new LinkedList<>();
  private List<Object> query = new LinkedList<>();

  public GremlinQueryBuilder vertices() {
    this.verticesEdgesSpecs.add(emptyVertexSpec);
    return this;
  }

  public GremlinQueryBuilder edges() {
    this.verticesEdgesSpecs.add(emptyEdgeSpec);
    return this;
  }

  public GremlinQueryBuilder withVertex(VertexSpec vertexSpec) {
    // before adding this vertexSpec, remove any empty vertexSpec(s)
    if (this.verticesEdgesSpecs.contains(emptyVertexSpec)) {
      this.verticesEdgesSpecs.remove(emptyVertexSpec);
    }

    this.verticesEdgesSpecs.add(vertexSpec);
    return this;
  }

  public GremlinQueryBuilder withEdge(EdgeSpec edgeSpec) {
    // before adding this edgeSpec, remove any empty edgeSpec(s)
    if (this.verticesEdgesSpecs.contains(emptyEdgeSpec)) {
      this.verticesEdgesSpecs.remove(emptyEdgeSpec);
    }

    this.verticesEdgesSpecs.add(edgeSpec);
    return this;
  }

  public String getQuery() {
    StringBuilder builder = new StringBuilder("");
    this.verticesEdgesSpecs.forEach(s -> builder.append(s.getQuery()));
    String verticesEdgesStr = builder.toString();

    logger.info(String.format("Vertices/ Edges query string: %s", verticesEdgesStr));
    return verticesEdgesStr;
  }
}
