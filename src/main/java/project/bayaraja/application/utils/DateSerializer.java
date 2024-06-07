package project.bayaraja.application.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");

        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));

        String formattedDate = dateFormat.format(date);

        jsonGenerator.writeString(formattedDate);
    }
}
