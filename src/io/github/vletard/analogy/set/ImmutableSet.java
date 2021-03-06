package io.github.vletard.analogy.set;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ImmutableSet<T> implements Iterable<T>, Serializable {
  private static final long serialVersionUID = -8404210739266224803L;
  private final Set<T> s;
  
  public ImmutableSet(Set<T> s) {
    this.s = Collections.unmodifiableSet(new HashSet<T>(s));
  }

  @Override
  public Iterator<T> iterator() {
    return this.s.iterator();
  }

  public Set<T> asSet() {
    return this.s;
  }

  public Boolean contains(Object o) {
    return this.s.contains(o);
  }
  
  public int size() {
    return this.s.size();
  }
  
  @Override
  public String toString() {
    return this.s.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((s == null) ? 0 : s.hashCode());
    return result;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ImmutableSet other = (ImmutableSet) obj;
    if (s == null) {
      if (other.s != null)
        return false;
    } else if (!s.equals(other.s))
      return false;
    return true;
  }
}
