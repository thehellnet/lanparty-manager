package org.thehellnet.lanparty.manager.utility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.utility.StringUtility;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class MetadataUtility {

    private static final Logger logger = LoggerFactory.getLogger(MetadataUtility.class);

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
        Set<Class<?>> entityClassSet = reflections.getTypesAnnotatedWith(Entity.class);

        List<Class<?>> entityClasses = new ArrayList<>(entityClassSet);
        entityClasses.sort(Comparator.comparing(Class::getSimpleName));

        JSONArray metadata = new JSONArray();

        for (Class<?> entityClass : entityClasses) {
            JSONObject entityObj = computeClass(entityClass);
            metadata.put(entityObj);
        }

        return metadata;
    }

    public JSONObject computeClass(Class<?> entityClass) {
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

            Boolean nullable = null;
            Boolean unique = null;

            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                nullable = columnAnnotation.nullable();
                unique = columnAnnotation.unique();
            }

            JSONObject fieldObj = new JSONObject();
            fieldObj.put("name", field.getName());
            fieldObj.put("title", computeTitle(field.getName()));
            fieldObj.put("type", type);
            fieldObj.put("class", StringUtility.firstLetterLowercase(className));
            fieldObj.put("nullable", nullable != null ? nullable : JSONObject.NULL);
            fieldObj.put("unique", unique != null ? unique : JSONObject.NULL);
            fields.put(fieldObj);
        }

        JSONObject entityObj = new JSONObject();
        entityObj.put("name", StringUtility.firstLetterLowercase(entityClass.getSimpleName()));
        entityObj.put("title", computeTitle(entityClass.getSimpleName()));
        entityObj.put("fields", fields);
        return entityObj;
    }

    static String computeTitle(String simpleName) {
        if (simpleName == null || simpleName.equals("")) {
            return simpleName;
        }

        String[] items = simpleName.split("(?<=.)(?=\\p{Lu})");

        StringJoiner stringJoiner = new StringJoiner(" ");
        for (String item : items) {
            stringJoiner.add(item.substring(0, 1).toUpperCase() + item.substring(1));
        }

        return stringJoiner.toString();
    }
}
