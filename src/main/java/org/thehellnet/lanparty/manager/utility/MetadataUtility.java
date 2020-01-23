package org.thehellnet.lanparty.manager.utility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.thehellnet.utility.StringUtility;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

public class MetadataUtility {

    private static final Class<?>[] FIELD_TYPES = new Class[]{
            Id.class,
            ManyToOne.class,
            OneToMany.class,
            ManyToMany.class,
            Basic.class
    };

    private static final Class<?>[] GENERICS_TYPES = new Class[]{
            List.class,
            Set.class
    };

    private MetadataUtility() {
    }

    public static MetadataUtility getInstance() {
        return new MetadataUtility();
    }

    public JSONArray compute() {
        Reflections reflections = new Reflections("org.thehellnet.lanparty.manager.model.persistence");
        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);

        JSONArray metadata = new JSONArray();

        for (Class<?> entityClass : entityClasses) {
            JSONArray fields = new JSONArray();

            for (Field field : entityClass.getDeclaredFields()) {
                String type = null;

                Annotation[] annotations = field.getAnnotations();

                for (Class<?> clazz : FIELD_TYPES) {
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType().getSimpleName().equals(clazz.getSimpleName())) {
                            type = clazz.getSimpleName();
                            break;
                        }

                        if (type != null) {
                            break;
                        }
                    }
                }

                String className = field.getType().getSimpleName();

                for (Class<?> clazz : GENERICS_TYPES) {
                    if (className.equals(clazz.getSimpleName())) {
                        String[] items = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName().split("\\.");
                        className = items[items.length - 1];
                        break;
                    }
                }

                JSONObject fieldObj = new JSONObject();
                fieldObj.put("name", field.getName());
                fieldObj.put("type", type);
                fieldObj.put("class", StringUtility.firstLetterLowercase(className));
                fields.put(fieldObj);
            }

            JSONObject entityObj = new JSONObject();
            entityObj.put("name", StringUtility.firstLetterLowercase(entityClass.getSimpleName()));
            entityObj.put("fields", fields);
            metadata.put(entityObj);
        }

        return metadata;
    }
}
