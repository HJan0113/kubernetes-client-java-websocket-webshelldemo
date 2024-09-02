package org.hjan.config;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author HJan
 * @Date 2024/8/25 15:18
 * @Description
 */
@Configuration
public class GsonConfig {
    //序列化
    final static JsonSerializer<LocalDateTime> jsonSerializerDateTime = (localDateTime, type, jsonSerializationContext)
            -> new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    final static JsonSerializer<LocalDate> jsonSerializerDate = (localDate, type, jsonSerializationContext)
            -> new JsonPrimitive(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    //反序列化
    final static JsonDeserializer<LocalDateTime> jsonDeserializerDateTime = (jsonElement, type, jsonDeserializationContext)
            -> LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    final static JsonDeserializer<LocalDate> jsonDeserializerDate = (jsonElement, type, jsonDeserializationContext)
            -> LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    final static JsonSerializer<OffsetDateTime> jsonSerializerOffsetDateTime =
            (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    // 反序列化 OffsetDateTime
    final static JsonDeserializer<OffsetDateTime> jsonDeserializerOffsetDateTime =
            (json, typeOfT, context) -> OffsetDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    @Bean
    public Gson create() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, jsonSerializerDateTime)
                .registerTypeAdapter(LocalDate.class, jsonSerializerDate)
                .registerTypeAdapter(LocalDateTime.class, jsonDeserializerDateTime)
                .registerTypeAdapter(LocalDate.class, jsonDeserializerDate)
                .registerTypeAdapter(OffsetDateTime.class,jsonSerializerOffsetDateTime )
                .registerTypeAdapter(OffsetDateTime.class,jsonDeserializerOffsetDateTime )
                .create();
    }


}
