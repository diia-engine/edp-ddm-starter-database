/*
 * Copyright 2023 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.digital.data.platform.starter.database.deserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateTimeDeserializerTest {

  private final DateTimeDeserializer deserializer = new DateTimeDeserializer();
  private DeserializationContext deserializationContext;
  private JsonParser jsonParser;

  @BeforeEach
  void setUp() {
    jsonParser = mock(JsonParser.class);
    deserializationContext = mock(DeserializationContext.class);
  }

  @Test
  void deserializeWithTimeZone() throws IOException {
    String dateString = "2022-03-15T10:30:00+03:00";

    when(jsonParser.getText()).thenReturn(dateString);

    LocalDateTime result = deserializer.deserialize(jsonParser, deserializationContext);

    assertEquals(LocalDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME), result);
  }

  @Test
  void deserializeWithoutTimeZone() throws IOException {
    String dateString = "2022-03-15T10:30:00";

    when(jsonParser.getText()).thenReturn(dateString);

    LocalDateTime result = deserializer.deserialize(jsonParser, deserializationContext);

    assertEquals(LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME), result);
  }

  @Test
  void deserializeInvalidDate() throws IOException {
    String invalidDateString = "invalid-date";

    when(jsonParser.getText()).thenReturn(invalidDateString);

    assertThrows(IOException.class, () -> deserializer.deserialize(jsonParser, deserializationContext));
  }
}
