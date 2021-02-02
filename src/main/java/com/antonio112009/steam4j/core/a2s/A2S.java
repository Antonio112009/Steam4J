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

package com.antonio112009.steam4j.core.a2s;

import com.antonio112009.steam4j.core.a2s.model.info.ServerInfo;
import com.antonio112009.steam4j.core.a2s.model.player.Player;
import com.antonio112009.steam4j.core.a2s.model.player.Players;
import com.antonio112009.steam4j.core.a2s.model.rule.Rule;
import com.antonio112009.steam4j.core.a2s.model.rule.Rules;
import com.antonio112009.steam4j.core.a2s.type.A2SType;
import com.antonio112009.steam4j.core.a2s.util.Converter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class A2S {

    private String serverIp;
    private int port;
    private DatagramSocket datagramSocket;
    private int index;

    public A2S(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }


    private byte[] sendData(byte[] data) throws IOException {
        datagramSocket = new DatagramSocket();

        InetAddress address = InetAddress.getByName(serverIp);
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, port);
        datagramSocket.setSoTimeout(500);

        datagramSocket.send(datagramPacket);

        // Steam uses a packet size of 1400 bytes + IP/UDP headers.
        // If a request or response needs more packets for the data it starts the packets with an additional header.
        byte[] receiveBuffer = new byte[1480];

        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        datagramSocket.receive(receivePacket);
        return receivePacket.getData();
    }


    /**
     * GET SERVER RULES
     */
    public Rules getRules() {
        try {
            byte[] data = sendData(Converter.hexStrToBinaryStr(A2SType.A2S_RULES));

            // probably, store challenge in local variable
            if(data[4] == (byte) 0x41) {
                data = sendData(Converter.hexStrToBinaryStr(A2SType.A2S_RULES_REPLY + " " + Converter.binaryToString(Arrays.copyOfRange(data, 5,9))));
            }

            return convertResponseToRules(data);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // First 4 elements from the array - are header. It shows if packet is split or not.
    private Rules convertResponseToRules(byte[] data) {
        index = 4;

        Rules rules = new Rules();
        rules.setHeader(Converter.hexToAscii(Converter.byteToHex(data[index++])));
        rules.setTotalRules(data[index++]);
        index++;

        for (int i = 0; i < rules.getTotalRules(); i++) {
            try {
                Rule rule = new Rule();
                rule.setName(byteArrayToString(data));
                rule.setValue(byteArrayToString(data));
                rules.getRuleList().add(rule);
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
        }

        return rules;
    }













    /**
     * GET PLAYERS
     */
    public Players getPlayers() {
        try {
            byte[] data = sendData(Converter.hexStrToBinaryStr(A2SType.A2S_PLAYER));

            // probably, store challenge in local variable
            if(data[4] == (byte) 0x41) {
                data = sendData(Converter.hexStrToBinaryStr(A2SType.A2S_PLAYER_REPLY + " " + Converter.binaryToString(Arrays.copyOfRange(data, 5,9))));
            }

            return convertResponseToPlayers(data);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    // First 4 elements from the array - are header. It shows if packet is split or not.
    private Players convertResponseToPlayers(byte[] data) {
        index = 4;

        Players players = new Players();
        players.setHeader(Converter.hexToAscii(Converter.byteToHex(data[index++])));
        players.setTotalPlayers(data[index++]);

        for (int i = 0; i < players.getTotalPlayers(); i++) {
            try {
                Player player = new Player();

                player.setIndex(data[index++]);
                player.setName(byteArrayToString(data));
                player.setScore(calculateLong(data));
                player.setDuration(calculateFloat(data));

                players.getPlayerList().add(player);
            } catch (ArrayIndexOutOfBoundsException ex) {
                //TODO: add error handler
            }
        }

        return players;
    }








    /**
     *  GET SERVER INFO
     * @return
     */
    public ServerInfo getServerInfo() {
        try {
            byte[] data = sendData(Converter.hexStrToBinaryStr(A2SType.A2S_INFO));

            if (data[4] == (byte) 0x6d) {
//                oldSourceServer(resBytes, jsonObject);
            } else {
                 return convertResponseToServerInfo(data, false);
//                sourceServer(resBytes, jsonObject);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // First 4 elements from the array - are header. It shows if packet is split or not.
    private ServerInfo convertResponseToServerInfo(byte[] data, boolean addEDF) {
        index = 4;

        ServerInfo serverInfo = new ServerInfo();

        serverInfo.setHeader(Converter.hexToAscii(Converter.byteToHex(data[index++])));

        // TODO: possibly getting wrong data
        serverInfo.setProtocol(data[index++]);
        serverInfo.setName(byteArrayToString(data));
        serverInfo.setMap(byteArrayToString(data));
        serverInfo.setFolder(byteArrayToString(data));
        serverInfo.setGame(byteArrayToString(data));

        // space with 2 zero elements
        index += 2;
        serverInfo.setPlayers(data[index++]);
        serverInfo.setMaxPlayers(data[index++]);
        index++;
        serverInfo.setServerType(Converter.hexToAscii(Converter.byteToHex(data[index++])));
        serverInfo.setEnvironment(Converter.hexToAscii(Converter.byteToHex(data[index++])));
        serverInfo.setVisibility(data[index++]);
        serverInfo.setVac(data[index++]);
        serverInfo.setVersion(byteArrayToString(data));

        // TODO: modify later
        // Work with EDF
        while (data[index] != 0 && addEDF) {
            switch (Converter.byteToHex(data[index++])) {
                case "80": {
                    System.out.println("80");
                    index++;
                }
                case "10": {
                    System.out.println("10");
                    index++;
                }
                case "40": {
                    System.out.println("40");
                    index++;
                }
                case "20": {
                    System.out.println("20");
                    serverInfo.setKeywords(byteArrayToString(data));
                }
                case "01": {
                    System.out.println("1");
                    StringBuilder gameId = new StringBuilder();
                    for (int i = 0; i < 4; i++) {
                        gameId.append(Converter.byteToHex(data[index + i]));
                    }
                    serverInfo.setGameId(Long.parseLong(gameId.toString(), 16));
                    index += 5;
                }
                default: {
                    index++;
                }
            }
        }


        // adding data left in the message in case of broken query from developers of the game)
        serverInfo.setRestDataLeft(Arrays.copyOfRange(data, index, data.length));
        return serverInfo;
    }







    private String byteArrayToString (byte[] data) {
        byte[] tmp = new byte[1000];
        int indexTmp  = 0;
        while (data[index] != 0) {
            tmp[indexTmp] = data[index];
            indexTmp++;
            index++;
        }
        index++;
        return Converter.byteToString(tmp);
    }

    private long calculateLong(byte[] data) {
        long longi = 0;
        for (int j = 0; j < 4; j++, index++) {
            longi = longi | ((long) data[index] << 8 * j);
        }
        return longi;
    }

    private float calculateFloat(byte[] data) {
        byte[] tmp = new byte[4];
        for (int j = 0; j < 4; j++, index++) {
            tmp[j] = data[index];
        }

        int accum = 0;
        accum = accum | (tmp[0] & 0xff);
        accum = accum | (tmp[1] & 0xff) << 8;
        accum = accum | (tmp[2] & 0xff) << 16;
        accum = accum | (tmp[3] & 0xff) << 24;

        return Float.intBitsToFloat(accum) * 1;
    }
}
