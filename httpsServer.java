import javax.net.ssl.*;
import java.io.*;
import java.security.*;

class httpsServer {
     static void main(String[] args) {
        //load keystore : to load stored keys securely
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream("keystore.jks");
            ks.load(fis, "password".toCharArray());


            //Key Manager : Responsible for showing ssl certificates to browser
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, "password".toCharArray());

            //SSL Context : use TLS engine and init it ( creates a empty tls engine using java sslcontext object)
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);  //TLS engine receives certificate , crypto configs , private keys

            // Create TLS server   socket factory for secure socket tunnel
            SSLServerSocketFactory factory = sslContext.getServerSocketFactory(); //pass tls engine into socket : to use it

            //Create HTTPS server using its port number
            SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(8443);
            System.out.println("HTTPS server running on port 8443....");
            while(true) {
            System.out.println("Waiting for Browser connection");

            //Handshake
            SSLSocket socket = (SSLSocket) server.accept(); //wait and join client through TCP and handshake
                try {
                    socket.startHandshake(); //manual handshake intervention
                    System.out.println("Handshake successfull , Client connected successfully through secure tls line");

                    //To get TLS session details
                    SSLSession session = socket.getSession();
                    System.out.println(session.getCipherSuite());

                }catch (SSLHandshakeException e){
                    System.out.println("Browser rejected self signed certificate");
                    e.printStackTrace();
                    socket.close();
                    continue;
                }

            //Read the request
            InputStream input = socket.getInputStream(); //get raw input frm the SOCKET STREAM ENCRYPTED BY TLS
            BufferedReader in = new BufferedReader(new InputStreamReader(input)); //read by converting bytes to char line by line
            String line;
            while ((line = in.readLine()) != null && !(line.isEmpty()) ) {
                System.out.println(line);
            }

            //Send Response
            OutputStream out = socket.getOutputStream();
            String response = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n\r\n" + "<h1>Secure HTTPS Server Line</h1>";
            byte[] byteResponse = response.getBytes(); //conversion of string response to bytes
            out.write(byteResponse); //sending through socket
            out.flush();

            //Closing connections
            socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
