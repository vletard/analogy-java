package util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class Tuple<V> implements RecursivePrintable {
  private final HashMap<?, V> items;
  
  public Tuple(Map<?, V> m) {
    this.items = new HashMap<Object, V>(m);
  };

  public Tuple(Collection<V> c) {
    HashMap<Integer, V> items = new HashMap<Integer, V>();
    int i = 0;
    for (V item: c) {
      items.put(i, item);
      i++;
    }
    this.items = items;
  }

  public Collection<? extends Object> keySet() {
    return this.items.keySet();
  }

  public V get(Object key) {
    return this.items.get(key);
  };
  
  @Override
  public String toString() {
    return this.items.toString();
  }
  
  private static String writeOffset(int offset) {
    String s = "";
    for (int i = 0; i < offset; i++)
      s += "  ";
    return s;
  }
  
  public String prettyPrint(int offset) {
    HashSet<Object> simpleKeys = new HashSet<Object>();
    HashSet<Object> recursiveValues = new HashSet<Object>();
    HashSet<Object> complexKeys = new HashSet<Object>();
    
    for (Object k: this.items.keySet()) {
      V v = this.items.get(k);
      if (v instanceof RecursivePrintable)
        recursiveValues.add(k);
      else if (!(k instanceof String || k instanceof Integer || k instanceof Boolean
          || k instanceof Double || k instanceof Float || k instanceof Character))
        complexKeys.add(k);
      else
        simpleKeys.add(k);
    }
    
    String output = "";
    
    output += "{ \n";
    for (Object k: simpleKeys)
      output += writeOffset(offset+1) + k + "=" + this.items.get(k) + ",\n";

    for (Object k: recursiveValues)
      output += writeOffset(offset+1) + k + "=" + ((RecursivePrintable)this.items.get(k)).prettyPrint(offset+1) + ",\n";
    
    for (Object k: complexKeys)
      output += writeOffset(offset+1) + k + "=" + this.items.get(k) + ",\n";
    
    output = output.substring(0, output.length()-2) + "\n" + writeOffset(offset) + "}";
    
    return output;
  }
}