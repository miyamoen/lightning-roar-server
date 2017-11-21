package net.unit8.lightningroar;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import enkan.exception.UnreachableException;
import kotowari.data.Validatable;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

public class MalformedRequestException extends RuntimeException {
    private Validatable validatableObject;

    public MalformedRequestException(Validatable validatableObject) {
        this.validatableObject = validatableObject;
    }

    public String toJson() {
        StringWriter sw = new StringWriter();
        try {
            JsonGenerator generator = new JsonFactory().createGenerator(sw);
            generator.writeStartObject();
            generator.writeObjectFieldStart("errors");
            for(String key : validatableObject.getErrors().keySet()) {
                generator.writeArrayFieldStart(key);
                for (Object value : validatableObject.getErrors(key)) {
                    generator.writeString(Objects.toString(value));
                }
                generator.writeEndArray();
            }
            generator.writeEndObject();
            generator.writeEndObject();
            generator.flush();

            return sw.toString();
        } catch (IOException e) {
            throw new UnreachableException(e);
        }
    }
}
