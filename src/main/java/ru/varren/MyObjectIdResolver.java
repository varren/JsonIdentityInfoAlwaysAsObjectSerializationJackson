package ru.varren;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * Need this for deserialization
 */
public class MyObjectIdResolver implements ObjectIdResolver {
    private Map<ObjectIdGenerator.IdKey, Object> _items = new HashMap<>();

    @Override
    public void bindItem(ObjectIdGenerator.IdKey id, Object pojo) {
        if (!_items.containsKey(id)) _items.put(id, pojo);
    }

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey id) {
        Object object = _items.get(id);
        if (object != null) return object;

        try {
            object = id.scope.getConstructor().newInstance(); // create instance
            id.scope.getMethod("setId", int.class).invoke(object, id.key);  // set id
        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return new MyObjectIdResolver();
    }

    @Override
    public boolean canUseFor(ObjectIdResolver resolverType) {
        return resolverType.getClass() == getClass();
    }
}
