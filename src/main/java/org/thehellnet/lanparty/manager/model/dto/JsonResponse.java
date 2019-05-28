package org.thehellnet.lanparty.manager.model.dto;

import java.util.HashMap;
import java.util.Map;

public class JsonResponse {

    private boolean success = true;
    private Object data = null;
    private Map<String, Object> errors = new HashMap<>();

    private JsonResponse() {
    }

    public static JsonResponse getInstance() {
        return new JsonResponse();
    }

    public static JsonResponse getInstance(Object data) {
        JsonResponse jsonResponse = JsonResponse.getInstance();
        jsonResponse.data = data;
        return jsonResponse;
    }

    public static JsonResponse getInstance(String key, Object value) {
        Map<String, Object> data = new HashMap<>();
        data.put(key, value);

        return JsonResponse.getInstance(data);
    }

    public static JsonResponse getErrorInstance() {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.success = false;
        return jsonResponse;
    }

    public static JsonResponse getErrorInstance(String message) {
        JsonResponse jsonResponse = JsonResponse.getErrorInstance();
        jsonResponse.errors.put("message", message);
        return jsonResponse;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }
}
