package com.codionics;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.base.Objects;
import com.codionics.utils.StringUtils;

public class VertexSpec implements VertexOrEdgeSpec, StringUtils {
  private final Logger logger = Logger.getLogger(VertexSpec.class);

  private String label;
  private boolean isInV;
  private boolean isOutV;
  private List<EdgeSpec> inEdgesSpecs = new LinkedList<>();
  private List<EdgeSpec> outEdgesSpecs = new LinkedList<>();
  private Map<String, String> propertyValues = new LinkedHashMap<>();

  public VertexSpec hasLabel(String label) {
    this.label = label;
    return this;
  }

  public VertexSpec hasKey(String key) {
    this.propertyValues.put("sKey", key);
    return this;
  }

  public VertexSpec hasId(String id) {
    this.propertyValues.put("sid", id);
    return this;
  }

  public VertexSpec hasExternalId(String externalId) {
    this.propertyValues.put("externalId", externalId);
    return this;
  }

  public VertexSpec hasExternalKey(String externalKey) {
    this.propertyValues.put("externalKey", externalKey);
    return this;
  }

  public VertexSpec hasType(String type) {
    this.propertyValues.put("type", type);
    return this;
  }

  public VertexSpec hasVersion(String version) {
    this.propertyValues.put("version", version);
    return this;
  }

  public VertexSpec has(String property, String value) {
    this.propertyValues.put(property, value);
    return this;
  }

  public VertexSpec isInV() {
    this.isInV = true;
    return this;
  }

  public VertexSpec isOutV() {
    this.isOutV = true;
    return this;
  }

  public VertexSpec inE(EdgeSpec edgeSpec) {
    EdgeSpec withInE = edgeSpec.isInE();
    this.inEdgesSpecs.add(withInE);
    return this;
  }

  public VertexSpec outE(EdgeSpec edgeSpec) {
    EdgeSpec withOutE = edgeSpec.isOutE();
    this.outEdgesSpecs.add(withOutE);
    return this;
  }

  public boolean isEmpty() {
    boolean hasNoProperties = propertyValues == null || propertyValues.isEmpty();
    logger.debug("hasNoProperties: " + hasNoProperties);
    return isNullOrEmpty(label) && hasNoProperties;
  }

  @Override
  public String getQuery() {
    String query = getVertexString();
    StringBuilder builder = new StringBuilder(query);

    String labelString = isNullOrEmpty(this.label) ? "" : getLabelString();
    String propertiesString = (propertyValues == null || propertyValues.isEmpty()) ? "" : getPropertiesString();
    String inE = (inEdgesSpecs == null || inEdgesSpecs.isEmpty()) ? "" : getEdgeQuery(this.inEdgesSpecs);
    String outE = (outEdgesSpecs == null || outEdgesSpecs.isEmpty()) ? "" : getEdgeQuery(this.outEdgesSpecs);

    if (isNotNullOrEmpty(labelString)) {
      builder.append(labelString);
    }

    if (isNotNullOrEmpty(propertiesString)) {
      builder.append(propertiesString);
    }

    if (isNotNullOrEmpty(inE)) {
      builder.append(inE);
    }

    if (isNotNullOrEmpty(outE)) {
      builder.append(outE);
    }

    return builder.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof VertexSpec) {
      VertexSpec that = (VertexSpec) obj;

      return Objects.equal(this.label, that.label) && Objects.equal(this.isInV, that.isInV)
          && Objects.equal(this.isOutV, that.isOutV) && Objects.equal(this.propertyValues, that.propertyValues);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.label, this.isInV, this.isOutV, this.propertyValues);
  }

  private String getVertexString() {
    Preconditions.checkArgument(!isInV || !isOutV, "A vertex cannot be in and out vertex at the same time.");

    if (isInV) {
      return ".inV()";
    } else if (isOutV) {
      return ".outV()";
    } else {
      return "g.V()";
    }
  }

  private String getLabelString() {
    return String.format(getLabelFormat(), this.label);
  }

  private String getPropertiesString() {
    StringBuilder builder = new StringBuilder("");
    this.propertyValues.forEach((k, v) -> builder.append(String.format(getPropertyFormat(), k, v)));
    String vertexPropStr = builder.toString();

    logger.info(String.format("Vertex properties string: %s", vertexPropStr));
    return vertexPropStr;
  }

  private String getEdgeQuery(List<EdgeSpec> edgesSpecs) {
    StringBuilder builder = new StringBuilder("");
    edgesSpecs.forEach(s -> builder.append(s.getQuery()));
    String edgeQuery = builder.toString();

    logger.info(String.format("Edge query string: %s", edgeQuery));
    return edgeQuery;
  }
}
