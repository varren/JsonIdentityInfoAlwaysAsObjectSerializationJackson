package ru.varren;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Need this for deserialization
 */
public class MyObjectIdResolver implements ObjectIdResolver {
    private Map<ObjectIdGenerator.IdKey, Object> _items = new HashMap<>();
    private String idFieldName;

    @Override
    public void bindItem(ObjectIdGenerator.IdKey id, Object pojo) {
        if (!_items.containsKey(id)) _items.put(id, pojo);
    }

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey id) {
        Object object = _items.get(id);
        if (object != null) return object;

        try {
            object = id.scope.getConstructor().newInstance();

            Field idField = findIdField(id.scope);
            idField.setAccessible(true);
            idField.set(object, id.key);

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

    protected Field findIdField(Class<?> classs) {
        if (idFieldName != null) {
            try {
                return classs.getDeclaredField(String.valueOf(idFieldName));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }


        Field idField = findIdFromJsonIdentityInfo(classs);
        if (idField == null) idField = findIdFromJsonTypeId(classs);

        if (idField == null)
            throw new RuntimeException("No @JsonIdentityInfo(property=\"<id name>\") or @JsonTypeId above field annotation in class " + classs);


        idFieldName = idField.getName();

        return idField;
    }

    protected Field findIdFromJsonIdentityInfo(Class<?> classs) {
        Class<? extends Annotation> ann = JsonIdentityInfo.class;

        Class<?> c = classs;
        while (c != null) {

            if (c.isAnnotationPresent(ann)) {
                JsonIdentityInfo annotation = c.getAnnotation(JsonIdentityInfo.class);
                for (Field field : c.getDeclaredFields()) {
                    if (Objects.equals(field.getName(), annotation.property())) {
                        return field;
                    }
                }
            }

            c = c.getSuperclass();
        }
        return null;
    }

    protected Field findIdFromJsonTypeId(Class<?> classs) {
        Class<? extends Annotation> ann = JsonTypeId.class;
        Class<?> c = classs;
        while (c != null) {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(ann)) {
                    return field;
                }
            }
            c = c.getSuperclass();
        }
        return null;
    }

}

