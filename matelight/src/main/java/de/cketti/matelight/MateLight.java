package de.cketti.matelight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MateLight {
    private static final String HOST = "api.matelight.rocks";
    private static final int PORT = 1337;
    private static final String SUCCESS = "KTHXBYE!";
    private static final int TIMEOUT = 60 * 1000;


    private Socket mSocket;


    public void sendMessage(String message) throws IOException {
        mSocket = new Socket();
        mSocket.connect(new InetSocketAddress(HOST, PORT));
        try {
            OutputStream out = mSocket.getOutputStream();
            try {
                mSocket.setSoTimeout(TIMEOUT);

                out.write(message.getBytes());
                out.write('\n');
                out.flush();

                InputStream in = mSocket.getInputStream();
                try {
                    BufferedReader buf = new BufferedReader(new InputStreamReader(in));
                    String response = buf.readLine();

                    if (!SUCCESS.equals(response)) {
                        throw new RuntimeException("No success message from server");
                    }
                } finally {
                    in.close();
                }
            } finally {
                out.close();
            }
        } finally {
            mSocket.close();
        }
    }

    public void cancel() {
        try {
            mSocket.close();
        } catch (Exception e) {
            // ignore
        }
    }
}
