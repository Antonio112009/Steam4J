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

package com.antonio112009.steam4j.core.a2s.model.info;

import lombok.Data;

@Data
public class ServerInfo {

    private String header;
    private byte protocol;
    private String name;
    private String map;
    private String folder;
    private String game;
    private short id;
    private int players;
    private int maxPlayers;
    private int bots;
    private String serverType; // https://developer.valvesoftware.com/wiki/Server_queries#A2S_INFO
    private String environment; // https://developer.valvesoftware.com/wiki/Server_queries#A2S_INFO
    private byte visibility;
    private byte vac;

    // If server running "The Ship"
    //TODO: add Ship support

    private String version;

    //Extra Data Flag (EDF)
    private int serverGamePort; // 0x80
    private long steamId; // 0x10
    private long spectatorPort; // 0x40
    private long spectatorName; // 0x40
    private String keywords; // 0x20
    private long gameId; // 0x01

    private byte[] restDataLeft;
}
