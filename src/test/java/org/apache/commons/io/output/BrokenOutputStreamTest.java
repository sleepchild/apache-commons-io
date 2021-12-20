/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit Test Case for {@link BrokenOutputStream}.
 */
public class BrokenOutputStreamTest {

    private IOException exception;

    private OutputStream stream;

    @BeforeEach
    public void setUp() {
        exception = new IOException("test exception");
        stream = new BrokenOutputStream(exception);
    }

    @Test
    public void testClose() {
        assertEquals(exception, assertThrows(IOException.class, () -> stream.close()));
    }

    @Test
    public void testFlush() {
        assertEquals(exception, assertThrows(IOException.class, () -> stream.flush()));
    }

    @Test
    public void testWriteByteArray() {
        assertEquals(exception, assertThrows(IOException.class, () -> stream.write(new byte[1])));
    }

    @Test
    public void testWriteByteArrayIndexed() {
        assertEquals(exception, assertThrows(IOException.class, () -> stream.write(new byte[1], 0, 1)));
    }

    @Test
    public void testWriteInt() {
        assertEquals(exception, assertThrows(IOException.class, () -> stream.write(1)));
    }

    @Test
    public void testTryWithResources() {
        final IOException thrown = assertThrows(IOException.class, () -> {
            try (OutputStream newStream = new BrokenOutputStream()) {
                newStream.write(1);
            }
        });
        assertEquals("Broken output stream", thrown.getMessage());

        final Throwable[] suppressed = thrown.getSuppressed();
        assertEquals(1, suppressed.length);
        assertEquals(IOException.class, suppressed[0].getClass());
        assertEquals("Broken output stream", suppressed[0].getMessage());
    }

}
