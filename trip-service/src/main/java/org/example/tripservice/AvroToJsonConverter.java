package org.example.tripservice;

import org.apache.avro.Schema;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class AvroToJsonConverter {

    public <T extends org.apache.avro.specific.SpecificRecord> String deserialize(T avroObejct) throws IOException {
        if (avroObejct == null) {
            throw new IllegalArgumentException("Avro Object Null");
        }

        Schema schema = avroObejct.getSchema();
        SpecificDatumWriter<T> writer = new SpecificDatumWriter<>(schema);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final Encoder jsonEncoder = EncoderFactory.get().jsonEncoder(schema, baos);
        writer.write(avroObejct, jsonEncoder);
        jsonEncoder.flush();
        return baos.toString(StandardCharsets.UTF_8);

    }

}
