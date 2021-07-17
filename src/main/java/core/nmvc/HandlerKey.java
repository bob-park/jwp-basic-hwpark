package core.nmvc;

import core.annotation.RequestMethod;

public class HandlerKey {
  private final String url;
  private final RequestMethod requestMethod;

  public HandlerKey(String url, RequestMethod requestMethod) {
    this.url = url;
    this.requestMethod = requestMethod;
  }

  public String getUrl() {
    return url;
  }

  public RequestMethod getRequestMethod() {
    return requestMethod;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((requestMethod == null) ? 0 : requestMethod.hashCode());
    result = prime * result + ((url == null) ? 0 : url.hashCode());

    return result;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }

    if (o == null) {
      return false;
    }

    if (getClass() != o.getClass()) {
      return false;
    }

    HandlerKey other = (HandlerKey) o;

    if (requestMethod != other.requestMethod) {
      return false;
    }
    if (url == null) {
      return other.url == null;
    } else {
      return url.equals(other.url);
    }
  }

  @Override
  public String toString() {
    return "HandlerKey{" + "url='" + url + ", requestMethod=" + requestMethod + '}';
  }
}
