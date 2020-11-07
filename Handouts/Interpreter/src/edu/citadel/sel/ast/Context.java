package edu.citadel.sel.ast;


import java.util.Map;
import java.util.HashMap;


/**
 * Context is essentially a map from an identifier
 * (type String) to its value (type double).
 */
public class Context
  {
    private Map<String, Double> idValueMap;
    
    
    public Context()
      {
        idValueMap = new HashMap<>(20);
      }


    /**
     * Add the specified identifier and its associated value to the context. 
     */
    public void put(String identifier, double value)
      {
        idValueMap.put(identifier, value);
      }
    
    
    /**
     * Returns the value associated with the specified identifier.  Returns
     * 0.0 if the identifier has not previously been associated with a value. 
     */
    public double get(String identifier)
      {
        Double value = idValueMap.get(identifier);        
        return value != null ? value.doubleValue() : 0.0;
      }
  }
