package com.codionics.utils;

public interface StringUtils {

  default boolean isNotNullOrEmpty(String s) {
    return !isNullOrEmpty(s);
  }

  default boolean isNullOrEmpty(String s) {
    return s == null || s.isEmpty();
  }
}
