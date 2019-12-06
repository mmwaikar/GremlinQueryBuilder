package com.codionics;

public interface VertexOrEdgeSpec {

  public String getQuery();

  default String getLabelFormat() {
    return ".hasLabel('%s')";
  }

  default String getPropertyFormat() {
    return ".has('%s', '%s')";
  }

  default String getWhereFormat() {
    return ".where(%s)";
  }
}
