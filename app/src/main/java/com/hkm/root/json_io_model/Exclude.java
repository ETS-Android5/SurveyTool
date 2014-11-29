package com.hkm.root.json_io_model;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hesk on 6/20/2014.
 * according to http://stackoverflow.com/questions/19315431/gson-tostring-gives-error-illegalargumentexception-multiple-json-fields-name
 * According to my observation if you find multiple JSON fields for ANY_VARIABLE_NAME, then it is likely that it is because GSON is not able to convert object to jsonString and jsonString to object. And you can try below code to solve it.
 *
 * Add below class to to tell GSON to save and/or retrieve only those variables who have Serialized name declared.
 */
public class Exclude implements ExclusionStrategy {
    @Override
    public boolean shouldSkipClass(Class<?> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        SerializedName ns = field.getAnnotation(SerializedName.class);
        if (ns != null)
            return false;
        return true;
    }
}