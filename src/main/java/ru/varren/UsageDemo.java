package ru.varren;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UsageDemo {

    public static ObjectMapper defaultObjectMapper() {
        return new ObjectMapper();
    }

    // this mapper ignores @JsonIdentityInfo during serialization
    public static ObjectMapper ignoreJsonIdentityInfoMapper() {
        ObjectMapper m = new ObjectMapper();

        SerializationConfig config = m.getSerializationConfig()
                .with(new JacksonAnnotationIntrospector() {
                    @Override
                    public ObjectIdInfo findObjectIdInfo(final Annotated ann) {
                        return null;
                    }
                });
        m.setConfig(config);

        return m;
    }

    // this mapper ignores @JsonIdentityReference(alwaysAsId = true) during serialization
    public static ObjectMapper ignoreJsonIdentityReferenceMapper() {
        ObjectMapper m = new ObjectMapper();
        SerializationConfig config2 = m.getSerializationConfig()
                .with(new JacksonAnnotationIntrospector() {
                    @Override
                    public ObjectIdInfo findObjectReferenceInfo(Annotated ann, ObjectIdInfo objectIdInfo) {
                        return null;
                    }
                });
        m.setConfig(config2);
        return m;
    }


    public static void main(String[] args) throws Exception {
        // init data
        Phone p1 = new Phone();
        p1.setId(3);
        p1.setN("b");
        Phone p2 = new Phone();
        p2.setId(2);
        p2.setN("a");

        List<Phone> phones = new ArrayList<>();
        phones.add(p1);
        phones.add(p2);
        phones.add(p1); // add p1 2 times

        Contact contact = new Contact();
        contact.setPhones(phones);
        contact.setId(1);

        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(contact); // add same contact 2 times
        contacts.add(contact);

        System.out.println("\nObjectMapper1 without customisations");
        fullTest(defaultObjectMapper(), contacts);
        System.out.println("\nObjectMapper2 with ignoreJsonIdentityInfoMapper");
        fullTest(ignoreJsonIdentityInfoMapper(), contacts);
        System.out.println("\nObjectMapper3 with ignoreJsonIdentityReferenceMapper");
        fullTest(ignoreJsonIdentityReferenceMapper(), contacts);

    }

    private static void fullTest(ObjectMapper mapper, ArrayList<Contact> testData) throws IOException {
        System.out.println("Serialization tests:");
        System.out.println("Contact      : " + mapper.writeValueAsString(testData.get(0)));
        System.out.println("List<Contact>: " + mapper.writeValueAsString(testData));
        System.out.println("Deserialization tests:");
        deserializationTest(mapper, "{\"id\":1,\"phones\":[{\"id\":3,\"n\":\"a\"},{\"id\":2,\"n\":null},3]}");
        deserializationTest(mapper, "{\"id\":1,\"phones\":[{\"id\":3,\"n\":\"b\"},{\"id\":2,\"n\":null},{\"id\":3,\"n\":\"b\"}]}");
        deserializationTest(mapper, "{\"id\":1,\"phones\":[1,2,1]}");
    }

    private static void deserializationTest(ObjectMapper mapper, String json) throws IOException {
        System.out.println("From: " + json);
        Contact result = mapper.readValue(json, Contact.class);
        System.out.println("To  : " + mapper.writeValueAsString(result));
    }

}
