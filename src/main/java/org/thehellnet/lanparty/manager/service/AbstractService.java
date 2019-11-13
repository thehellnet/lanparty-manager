package org.thehellnet.lanparty.manager.service;

import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;

import java.util.List;

public abstract class AbstractService {

    protected static String[] parseStringList(Object object) {
        return parseStringList(object, true);
    }

    protected static String[] parseStringList(Object object, boolean required) {
        if (object != null) {
            if (object instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> stringList = (List<String>) object;
                return stringList.toArray(new String[0]);
            }
        }

        if (required) {
            throw new InvalidDataException("Invalid string list");
        }

        return null;
    }
}
