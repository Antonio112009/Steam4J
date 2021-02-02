/*
 * MIT License
 *
 * Copyright (c) 2021 Anton Rogalskiy and Steam4J contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.antonio112009.steam4j.core.rcon.packet;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * Basic Packet Structure
 *
 * Size: 32-bit little-endian Signed Integer
 * ID: 32-bit little-endian Signed Integer (4 Bytes)
 * Type: 32-bit little-endian Signed Integer (4 Bytes)
 * Body: Null-terminated ASCII String (at least 1 Byte)
 * Empty String: Null-terminated ASCII String (1 Byte)
 */
//@Slf4j
public class RconPacket {

    private ByteBuffer payload = null;


    /**
     * Setting size of the buffer.
     *
     * From valve's wiki:
     *      Since the only one of these values that can change in length is the body,
     *      an easy way to calculate the size of a packet is to find
     *      the byte-length of the packet body, then add 10 to it.
     *
     */
    public byte[] sendPacket(int id, int type, String body) {
        payload = ByteBuffer.allocate(body.getBytes(StandardCharsets.UTF_8).length + 14);
        payload.order(ByteOrder.LITTLE_ENDIAN);

        // Setting Size
        payload.putInt(body.getBytes(StandardCharsets.UTF_8).length + 10);

        // Setting ID
        payload.putInt(id);

        // Setting Type
        payload.putInt(type);

        // Setting Body
        payload.put(body.getBytes());

        // Adding two null bytes at the end
        payload.put((byte) 0x00);
        payload.put((byte) 0x00);
        return payload.array();
    }

    public byte[] sendEmptyPacket(int id, int type) {
        return sendPacket(id, type, "");
    }


    public ByteBuffer receivePacket(InputStream inputStream) throws IOException {
        payload = ByteBuffer.allocate(4096);
        payload.order(ByteOrder.LITTLE_ENDIAN);

        byte[] length = new byte[4];
        if(inputStream.read(length, 0, 4) == 4) {
            if(length[0] == 0)
                return null; // TODO: модернизировать
            payload.put(length);
            for (int i = 0; i < payload.getInt(0); i++) {
                payload.put((byte) inputStream.read());
            }
            return payload;
        } else {
            return null;
        }
    }




    public static String assemblePackets(ByteBuffer[] packets) {
        // Return the text from all the response packets together
        StringBuilder response = new StringBuilder();

        for (ByteBuffer packet : packets) {
            response.append(getBodySinglePacket(packet));
        }
        return response.toString();
    }

    public static String getBodySinglePacket(ByteBuffer packet) {
        if (packet != null) {
            return new String(packet.array(), 12, packet.position() - 14);
//            log.debug(response.toString());
        } else {
            return "";
        }
    }


}
