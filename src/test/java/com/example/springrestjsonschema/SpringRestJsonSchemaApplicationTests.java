package com.example.springrestjsonschema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.BooleanAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.json.PersistentEntityToJsonSchemaConverter;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.hateoas.mediatype.MessageResolver;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class SpringRestJsonSchemaApplicationTests {

    @Autowired
    MessageResolver resolver;

    @Autowired
    RepositoryRestConfiguration configuration;

    @Autowired
    PersistentEntities entities;

    @Autowired
    Associations associations;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PersistentEntityToJsonSchemaConverter converter;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FltWithJsonCreator {

        @JsonIgnore
        Long id;

        String carrier;

        String fltNum;

        Status status;

        @JsonCreator
        public FltWithJsonCreator(String carrier, String fltNum) {

            this.carrier = carrier;
            this.fltNum = fltNum;
        }

        public static enum Status {

            DRAFT, SUBMITTED;
        }
    }

    @Test
    void springBootObjectMapper_withJsonCreator() {

        JavaType type = objectMapper.constructType(FltWithJsonCreator.class);

        BeanDescription serBeanDesc = objectMapper.getSerializationConfig().introspect(type);
        List<BeanPropertyDefinition> serBeanPropDefinitions = serBeanDesc.findProperties();
        assertCouldSerialize(serBeanPropDefinitions, "carrier").isTrue();
        assertCouldSerialize(serBeanPropDefinitions, "fltNum").isTrue();
        assertCouldSerialize(serBeanPropDefinitions, "status").isTrue();

        BeanDescription deserBeanDesc = objectMapper.getDeserializationConfig().introspect(type);
        List<BeanPropertyDefinition> deserBeanPropDefinitions = deserBeanDesc.findProperties();
        assertCouldDeserialize(deserBeanPropDefinitions, "carrier").isTrue();
        assertCouldDeserialize(deserBeanPropDefinitions, "fltNum").isTrue();
        assertCouldDeserialize(deserBeanPropDefinitions, "status").isFalse();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FltWithoutJsonCreator {

        @JsonIgnore
        Long id;

        String carrier;

        String fltNum;

        Status status;

        public FltWithoutJsonCreator(String carrier, String fltNum) {

            this.carrier = carrier;
            this.fltNum = fltNum;
        }

        public static enum Status {

            DRAFT, SUBMITTED;
        }
    }

    @Test
    void springBootObjectMapper_withoutJsonCreator() {

        JavaType type = objectMapper.constructType(FltWithoutJsonCreator.class);

        BeanDescription serBeanDesc = objectMapper.getSerializationConfig().introspect(type);
        List<BeanPropertyDefinition> serBeanPropDefinitions = serBeanDesc.findProperties();
        assertCouldSerialize(serBeanPropDefinitions, "carrier").isTrue();
        assertCouldSerialize(serBeanPropDefinitions, "fltNum").isTrue();
        assertCouldSerialize(serBeanPropDefinitions, "status").isTrue();

        BeanDescription deserBeanDesc = objectMapper.getDeserializationConfig().introspect(type);
        List<BeanPropertyDefinition> deserBeanPropDefinitions = deserBeanDesc.findProperties();
        assertCouldDeserialize(deserBeanPropDefinitions, "carrier").isFalse();
        assertCouldDeserialize(deserBeanPropDefinitions, "fltNum").isFalse();
        assertCouldDeserialize(deserBeanPropDefinitions, "status").isFalse();
    }

    @Test
    void withJsonCreator() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.INFER_PROPERTY_MUTATORS);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

        JavaType type = objectMapper.constructType(FltWithJsonCreator.class);

        BeanDescription serBeanDesc = objectMapper.getSerializationConfig().introspect(type);
        List<BeanPropertyDefinition> serBeanPropDefinitions = serBeanDesc.findProperties();
        assertCouldSerialize(serBeanPropDefinitions, "carrier").isTrue();
        assertCouldSerialize(serBeanPropDefinitions, "fltNum").isTrue();
        assertCouldSerialize(serBeanPropDefinitions, "status").isTrue();

        BeanDescription deserBeanDesc = objectMapper.getDeserializationConfig().introspect(type);
        List<BeanPropertyDefinition> deserBeanPropDefinitions = deserBeanDesc.findProperties();
        assertCouldDeserialize(deserBeanPropDefinitions, "carrier").isFalse();
        assertCouldDeserialize(deserBeanPropDefinitions, "fltNum").isFalse();
        assertCouldDeserialize(deserBeanPropDefinitions, "status").isFalse();
    }

    @Test
    void withOutJsonCreator() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.INFER_PROPERTY_MUTATORS);


        JavaType type = objectMapper.constructType(Flt.class);

        BeanDescription serBeanDesc = objectMapper.getSerializationConfig().introspect(type);
        List<BeanPropertyDefinition> serBeanPropDefinitions = serBeanDesc.findProperties();
        assertCouldSerialize(serBeanPropDefinitions, "carrier").isTrue();
        assertCouldSerialize(serBeanPropDefinitions, "fltNum").isTrue();
        assertCouldSerialize(serBeanPropDefinitions, "status").isTrue();

        BeanDescription deserBeanDesc = objectMapper.getDeserializationConfig().introspect(type);
        List<BeanPropertyDefinition> deserBeanPropDefinitions = deserBeanDesc.findProperties();
        assertCouldDeserialize(deserBeanPropDefinitions, "carrier").isFalse();
        assertCouldDeserialize(deserBeanPropDefinitions, "fltNum").isFalse();
        assertCouldDeserialize(deserBeanPropDefinitions, "status").isFalse();
    }

    @Test
    void compareObjectMapper() {

        ObjectMapper newObjectMapper = new ObjectMapper();
        newObjectMapper.disable(MapperFeature.INFER_PROPERTY_MUTATORS);
        newObjectMapper.disable(MapperFeature.INFER_PROPERTY_MUTATORS);
        newObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        newObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        newObjectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        log.info("objectMapper.serConfig1 = {}", Integer.toBinaryString(newObjectMapper.getSerializationConfig().getSerializationFeatures()));
        log.info("objectMapper.serConfig2 = {}", Integer.toBinaryString(this.objectMapper.getSerializationConfig().getSerializationFeatures()));

        for (SerializationFeature f : SerializationFeature.values()) {
            if (newObjectMapper.isEnabled(f) != objectMapper.isEnabled(f)) {
                log.info("ser feature {}: new {} vs spring {}", f,
                        newObjectMapper.isEnabled(f),
                        this.objectMapper.isEnabled(f));
            }
        }


        log.info("objectMapper.deserConfig1 = {}", Integer.toBinaryString(newObjectMapper.getDeserializationConfig().getDeserializationFeatures()));
        log.info("objectMapper.deserConfig2 = {}", Integer.toBinaryString(this.objectMapper.getDeserializationConfig().getDeserializationFeatures()));

        for (DeserializationFeature f : DeserializationFeature.values()) {
            if (newObjectMapper.isEnabled(f) != objectMapper.isEnabled(f)) {
                log.info("deser feature {}: new {} vs spring {}", f,
                        newObjectMapper.isEnabled(f),
                        this.objectMapper.isEnabled(f));
            }
        }


        for (MapperFeature f : MapperFeature.values()) {
            if (newObjectMapper.isEnabled(f) != objectMapper.isEnabled(f)) {
                log.info("mapper feature {}: new {} vs spring {}", f,
                        newObjectMapper.isEnabled(f),
                        this.objectMapper.isEnabled(f));
            }
        }

        for (JsonParser.Feature f : JsonParser.Feature.values()) {
            if (newObjectMapper.isEnabled(f) != objectMapper.isEnabled(f)) {
                log.info("mapper feature {}: new {} vs spring {}", f,
                        newObjectMapper.isEnabled(f),
                        this.objectMapper.isEnabled(f));
            }
        }

        for (JsonGenerator.Feature f : JsonGenerator.Feature.values()) {
            if (newObjectMapper.isEnabled(f) != objectMapper.isEnabled(f)) {
                log.info("mapper feature {}: new {} vs spring {}", f,
                        newObjectMapper.isEnabled(f),
                        this.objectMapper.isEnabled(f));
            }
        }

        for (JsonFactory.Feature f : JsonFactory.Feature.values()) {
            if (newObjectMapper.isEnabled(f) != objectMapper.isEnabled(f)) {
                log.info("mapper feature {}: new {} vs spring {}", f,
                        newObjectMapper.isEnabled(f),
                        this.objectMapper.isEnabled(f));
            }
        }
    }

    /**
     * Assert the given field can be serialized
     *
     * @param beanPropertyDefinitions  can be fetched by
     * <pre>
     * {@code
     * objectMapper.getSerializationConfig()
     *             .introspect(objectMapper.constructType(YourClass.class))
     *             .findProperties();}
     * </pre>
     * @param propertyName  the JSON property name, in general the class field name
     */
    public static AbstractBooleanAssert<?> assertCouldSerialize(
            Collection<BeanPropertyDefinition> beanPropertyDefinitions,
            String propertyName) {

        Optional<BeanPropertyDefinition> propertyDefinition = beanPropertyDefinitions.stream()
                .filter(serDefinition -> serDefinition.getName().equals(propertyName))
                .findFirst();
        if (propertyDefinition.isPresent()) {
            return assertThat(propertyDefinition.get().couldSerialize());
        } else {
            log.debug("Property '{}' not found in the provided BeanPropertyDefinition collection", propertyName);
            return new BooleanAssert(false);
        }
    }

    /**
     * Assert the given field can be serialized
     *
     * @param objectMapper  the Jackson {@link ObjectMapper}
     * @param field
     */
    public static AbstractBooleanAssert<?> assertCouldSerialize(ObjectMapper objectMapper, Field field) {

        List<BeanPropertyDefinition> beanPropertyDefinitions = objectMapper.getSerializationConfig()
                .introspect(objectMapper.constructType(field.getDeclaringClass()))
                .findProperties();
        Optional<BeanPropertyDefinition> propertyDefinition = beanPropertyDefinitions.stream()
                .filter(serDefinition -> serDefinition.getName().equals(field.getName()))
                .findFirst();
        if (propertyDefinition.isPresent()) {
            return assertThat(propertyDefinition.get().couldSerialize());
        } else {
            log.debug("Can not get bean property definition from the provided ObjectMapper for field '{}' of class '{}'",
                    field.getName(), field.getDeclaringClass().getCanonicalName());
            return new BooleanAssert(false);
        }
    }

    /**
     * Assert the given field can be deserialized
     *
     * @param beanPropertyDefinitions  can be fetched by
     * <pre>
     * {@code
     * objectMapper.getDeserializationConfig()
     *             .introspect(objectMapper.constructType(YourClass.class))
     *             .findProperties();}
     * </pre>
     * @param propertyName  the JSON property name, in general the class field name
     */
    public static AbstractBooleanAssert<?> assertCouldDeserialize(
            Collection<BeanPropertyDefinition> beanPropertyDefinitions,
            String propertyName) {

        Optional<BeanPropertyDefinition> propertyDefinition = beanPropertyDefinitions.stream()
                .filter(definition -> definition.getName().equals(propertyName))
                .findFirst();

        if (propertyDefinition.isPresent()) {
            return assertThat(propertyDefinition.get().couldDeserialize());
        } else {
            log.debug("Property '{}' not found in the provided BeanPropertyDefinition collection", propertyName);
            return new BooleanAssert(false);
        }
    }

    /**
     * Assert the given field can be deserialized
     *
     * @param objectMapper  the Jackson {@link ObjectMapper}
     * @param field
     */
    public static AbstractBooleanAssert<?> assertCouldDeserialize(ObjectMapper objectMapper, Field field) {

        List<BeanPropertyDefinition> beanPropertyDefinitions = objectMapper.getDeserializationConfig()
                .introspect(objectMapper.constructType(field.getDeclaringClass()))
                .findProperties();
        Optional<BeanPropertyDefinition> propertyDefinition = beanPropertyDefinitions.stream()
                .filter(definition -> definition.getName().equals(field.getName()))
                .findFirst();
        if (propertyDefinition.isPresent()) {
            return assertThat(propertyDefinition.get().couldDeserialize());
        } else {
            log.debug("Can not get bean property definition from the provided ObjectMapper for field '{}' of class '{}'",
                    field.getName(), field.getDeclaringClass().getCanonicalName());
            return new BooleanAssert(false);
        }
    }

    @Test
    void canDeserialize() {


    }
}
