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

package com.antonio112009.steam4j.core.rcon;

import com.antonio112009.steam4j.core.rcon.packet.RconPacket;
import com.antonio112009.steam4j.core.rcon.type.RconPacketType;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

//@Slf4j
public class Rcon {

    private String serverIp;
    private int serverPort;

    private Socket socket;
    private static final int RESPONSE_TIMEOUT = 1000;
    private boolean authenticated = false;

    public Rcon(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }


    public boolean openStream() {
        try {
            socket = new Socket(serverIp, serverPort);
            socket.setSoTimeout(RESPONSE_TIMEOUT);
            return socket.isConnected();

        } catch (IOException e) {
            e.printStackTrace();
//            log.error("Error with server connection: " + e.getMessage());
            return false;
        }
    }



    // DO NOT MODIFY!!!!
    public boolean authenticate(String password) {
        RconPacket rconPacket = new RconPacket();
        int id = (int) (Math.random() * 9999999) + 1;
        byte[] authRequest = rconPacket.sendPacket(id, RconPacketType.SERVERDATA_AUTH, password);

        ByteBuffer response;
        try {
            socket.getOutputStream().write(authRequest);

            rconPacket.receivePacket(socket.getInputStream()); //junk response packet
            response = rconPacket.receivePacket(socket.getInputStream());

            if ((response.getInt(4) == id) && (response.getInt(8) == RconPacketType.SERVERDATA_AUTH_RESPONSE))
                authenticated = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authenticated;
    }




    public String sendCommand(String command) {
        try {
            ByteBuffer[] response = sendToServer(command);
            if (response != null) {
                return RconPacket.assemblePackets(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private ByteBuffer[] sendToServer(String command) {
        RconPacket rconPacket = new RconPacket();
        int id = (int) (Math.random() * (9999999) + 1);

        byte[] request = rconPacket.sendPacket(id, RconPacketType.SERVERDATA_EXECCOMMAND, command);
        byte[] emptyRequest = rconPacket.sendEmptyPacket(id, RconPacketType.SERVERDATA_RESPONSE_VALUE);

        ByteBuffer[] resp = new ByteBuffer[4096];
        int i = 0;

        try {
            socket.getOutputStream().write(request);
            socket.getOutputStream().write(emptyRequest);

            while(true) {
                ByteBuffer response = rconPacket.receivePacket(socket.getInputStream());
                if(response != null) {
                    if (response.getInt(4) == id) {
                        if (RconPacket.getBodySinglePacket(response).equals("")) {
                            rconPacket.receivePacket(socket.getInputStream());
                            break;
                        } else {
                            resp[i] = response;
                        }
                        i++;
                    }
                }
            }

            return resp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String receivingServerMessages() throws IOException{
        RconPacket rconPacket = new RconPacket();

        try {
            ByteBuffer response = rconPacket.receivePacket(socket.getInputStream());
            if(response != null) {
                return RconPacket.getBodySinglePacket(response);
            }

        } catch (SocketTimeoutException ignores) {
        }
        return null;
    }

    public void closeStream() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
