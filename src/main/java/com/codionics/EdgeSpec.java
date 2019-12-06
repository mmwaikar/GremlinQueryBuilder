package com.codionics;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.base.Objects;
import com.codionics.utils.StringUtils;

public class EdgeSpec implements VertexOrEdgeSpec, StringUtils {
  private final Logger logger = Logger.getLogger(EdgeSpec.class);

  private final String latestRelationProperty = "_isLatest";

  private String label;
  private boolean isInE;
  private boolean isOutE;
  private List<VertexSpec> inVerticesSpecs = new LinkedList<>();
  private List<VertexSpec> outVerticesSpecs = new LinkedList<>();
  private List<VertexSpec> whereInVerticesSpecs = new LinkedList<>();
  private List<VertexSpec> whereOutVerticesSpecs = new LinkedList<>();
  private Map<String, String> propertyValues = new LinkedHashMap<>();

  public EdgeSpec hasLabel(String label) {
    this.label = label;
    return this;
  }

  public EdgeSpec hasKey(String key) {
    this.propertyValues.put("sKey", key);
    return this;
  }

  public EdgeSpec hasId(String id) {
    this.propertyValues.put("sid", id);
    return this;
  }

  public EdgeSpec hasType(String type) {
    this.propertyValues.put("type", type);
    return this;
  }

  public EdgeSpec hasVersion(String version) {
    this.propertyValues.put("version", version);
    return this;
  }

  public EdgeSpec has(String property, String value) {
    this.propertyValues.put(property, value);
    return this;
  }

  public EdgeSpec forSpecificContainer(String containerKey) {
    this.propertyValues.put("container", containerKey);
    return this;
  }

  public EdgeSpec lastestVersionOfRelations(boolean latestOnly) {
    String latest = latestOnly ? "TRUE" : "FALSE";
    this.propertyValues.put(this.latestRelationProperty, latest);
    return this;
  }

  public EdgeSpec isInE() {
    this.isInE = true;
    return this;
  }

  public EdgeSpec isOutE() {
    this.isOutE = true;
    return this;
  }

  public EdgeSpec inV(VertexSpec vertexSpec) {
    VertexSpec withInV = vertexSpec.isInV();
    this.inVerticesSpecs.add(withInV);
    return this;
  }

  public EdgeSpec outV(VertexSpec vertexSpec) {
    VertexSpec withOutV = vertexSpec.isOutV();
    this.outVerticesSpecs.add(withOutV);
    return this;
  }

  public EdgeSpec whereInV(VertexSpec vertexSpec) {
    VertexSpec withInV = vertexSpec.isInV();
    this.whereInVerticesSpecs.add(withInV);
    return this;
  }

  public EdgeSpec whereOutV(VertexSpec vertexSpec) {
    VertexSpec withOutV = vertexSpec.isOutV();
    this.whereOutVerticesSpecs.add(withOutV);
    return this;
  }

  public boolean isEmpty() {
    boolean hasNoProperties = propertyValues == null || propertyValues.isEmpty();
    logger.debug("hasNoProperties: " + hasNoProperties);

    boolean hasNoInVertices = inVerticesSpecs == null || inVerticesSpecs.isEmpty();
    logger.debug("hasNoInVertices: " + hasNoInVertices);

    boolean hasNoOutVertices = outVerticesSpecs == null || outVerticesSpecs.isEmpty();
    logger.debug("hasNoOutVertices: " + hasNoOutVertices);

    boolean hasNoWhereInVertices = whereInVerticesSpecs == null || whereInVerticesSpecs.isEmpty();
    logger.debug("hasNoInVertices: " + hasNoInVertices);

    boolean hasNoWhereOutVertices = whereOutVerticesSpecs == null || whereOutVerticesSpecs.isEmpty();
    logger.debug("hasNoOutVertices: " + hasNoOutVertices);

    return isNullOrEmpty(label) && hasNoProperties && hasNoInVertices && hasNoOutVertices && hasNoWhereInVertices
        && hasNoWhereOutVertices;
  }

  @Override
  public String getQuery() {
    String query = getEdgeString();
    StringBuilder builder = new StringBuilder(query);

    String labelString = isNullOrEmpty(this.label) ? "" : getLabelString();
    String propertiesString = (propertyValues == null || propertyValues.isEmpty()) ? "" : getPropertiesString();
    String inV = (inVerticesSpecs == null || inVerticesSpecs.isEmpty()) ? "" : getVertexQuery(this.inVerticesSpecs);
    String outV = (outVerticesSpecs == null || outVerticesSpecs.isEmpty()) ? "" : getVertexQuery(this.outVerticesSpecs);
    String whereInV = (whereInVerticesSpecs == null || whereInVerticesSpecs.isEmpty()) ? ""
        : getWhereVertexQuery(this.whereInVerticesSpecs);
    String whereOutV = (whereOutVerticesSpecs == null || whereOutVerticesSpecs.isEmpty()) ? ""
        : getWhereVertexQuery(this.whereOutVerticesSpecs);

    if (isNotNullOrEmpty(labelString)) {
      builder.append(labelString);
    }

    if (isNotNullOrEmpty(propertiesString)) {
      builder.append(propertiesString);
    }

    if (isNotNullOrEmpty(inV)) {
      builder.append(inV);
    }

    if (isNotNullOrEmpty(outV)) {
      builder.append(outV);
    }

    if (isNotNullOrEmpty(whereInV)) {
      builder.append(whereInV);
    }

    if (isNotNullOrEmpty(whereOutV)) {
      builder.append(whereOutV);
    }

    return builder.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EdgeSpec) {
      EdgeSpec that = (EdgeSpec) obj;

      return Objects.equal(this.label, that.label) && Objects.equal(this.inVerticesSpecs, that.inVerticesSpecs)
          && Objects.equal(this.outVerticesSpecs, that.outVerticesSpecs)
          && Objects.equal(this.whereInVerticesSpecs, that.whereInVerticesSpecs)
          && Objects.equal(this.whereOutVerticesSpecs, that.whereOutVerticesSpecs)
          && Objects.equal(this.propertyValues, that.propertyValues);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.label, this.inVerticesSpecs, this.outVerticesSpecs, this.whereInVerticesSpecs,
        this.whereOutVerticesSpecs, this.propertyValues);
  }

  private String getEdgeString() {
    Preconditions.checkArgument(!isInE || !isOutE, "An edge cannot be in and out edge at the same time.");

    if (isInE) {
      return ".inE()";
    } else if (isOutE) {
      return ".outE()";
    } else {
      return "g.E()";
    }
  }

  private String getLabelString() {
    return String.format(getLabelFormat(), this.label);
  }

  private String getPropertiesString() {
    StringBuilder builder = new StringBuilder("");
    this.propertyValues.forEach((k, v) -> builder.append(String.format(getPropertyFormat(), k, v)));
    String edgePropStr = builder.toString();

    logger.info(String.format("Edge properties string: %s", edgePropStr));
    return edgePropStr;
  }

  private String getVertexQuery(List<VertexSpec> verticesSpecs) {
    StringBuilder builder = new StringBuilder("");
    verticesSpecs.forEach(s -> builder.append(s.getQuery()));
    String vertexQuery = builder.toString();

    logger.info(String.format("Vertex query string: %s", vertexQuery));
    return vertexQuery;
  }

  private String getWhereVertexQuery(List<VertexSpec> verticesSpecs) {
    String whereVertexQuery;
    String vertexQuery = getVertexQuery(verticesSpecs);

    if (isNotNullOrEmpty(vertexQuery) && vertexQuery.startsWith(".")) {
      String withoutDot = vertexQuery.substring(1);
      whereVertexQuery = String.format(getWhereFormat(), withoutDot);
    } else {
      whereVertexQuery = String.format(getWhereFormat(), vertexQuery);
    }

    logger.info(String.format("Where vertex query string: %s", whereVertexQuery));
    return whereVertexQuery;
  }
}
