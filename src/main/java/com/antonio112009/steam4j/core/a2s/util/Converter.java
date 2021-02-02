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

package com.antonio112009.steam4j.core.a2s.util;

public class Converter {

    public static String byteToString(byte[] src) {
        try {
            int length = 0;
            for (int i = 0; i < src.length; ++i) {
                if (src[i] == 0) {
                    length = i;
                    break;
                }
            }
            return new String(src, 0, length, "UTF-8");
        } catch (Exception e) {
//            log.error(e.toString());
            return "";
        }
    }

    public static byte[] hexStrToBinaryStr(String hexString) {
        if (hexString == null) return null;

        String[] tmp = hexString.split(" ");
        byte[] tmpBytes = new byte[tmp.length];
        int i = 0;
        for (String b : tmp) {
            if (b.equals("FF")) {
                tmpBytes[i++] = -1;
            } else {
                tmpBytes[i++] = Integer.valueOf(b, 16).byteValue();
            }
        }
        return tmpBytes;
    }

    public static String binaryToString(byte[] data) {
        try {
            char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            StringBuilder stringBuffer = new StringBuilder();
            for (byte datum : data) {
                stringBuffer.append(HEX_CHAR[(datum < 0 ? datum + 256 : datum) / 16]);
                stringBuffer.append(HEX_CHAR[(datum < 0 ? datum + 256 : datum) % 16]);
                stringBuffer.append(" ");
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }


    public static String byteToHex(byte input) {
        char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        return String.valueOf(HEX_CHAR[(input < 0 ? input + 256 : input) / 16]) +
               HEX_CHAR[(input < 0 ? input + 256 : input) % 16];
    }


    // POSSIBLY NO NEED!!!
    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }
}
