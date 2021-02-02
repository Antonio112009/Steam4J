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

package com.antonio112009.steam4j.core.a2s.type;

public class A2SType {
    public static final String A2S_INFO = "FF FF FF FF 54 53 6F 75 72 63 65 20 45 6E 67 69 6E 65 20 51 75 65 72 79 00";
    public static final String A2S_PLAYER = "FF FF FF FF 55 FF FF FF FF";
    public static final String A2S_PLAYER_REPLY = "FF FF FF FF 55";
    public static final String A2S_RULES = "FF FF FF FF 56 FF FF FF FF";
    public static final String A2S_RULES_REPLY = "FF FF FF FF 56";
    public static final String A2S_SERVERQUERY_GETCHALLENGE = "FF FF FF FF 57";

}
