import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.*;

public class RaftSimulation {

    static class Node implements Runnable {
        private final int id;
        private final int port;
        private volatile boolean isLeader = false;
        private volatile boolean receivedHeartbeat = false;
        private final int electionTimeout;
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        private final Random random = new Random();

        public Node(int id, int port) {
            this.id = id;
            this.port = port;
            this.electionTimeout = random.nextInt(3000) + 3000; // 3-6 seconds
        }

        @Override
        public void run() {
            System.out.printf("Node %d started on port %d.\n", id, port);
            scheduler.schedule(this::startElection, electionTimeout, TimeUnit.MILLISECONDS);
            try (DatagramSocket socket = new DatagramSocket(port)) {
                byte[] buffer = new byte[1024];

                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());

                    if (message.startsWith("HEARTBEAT")) {
                        handleHeartbeat();
                    } else if (message.startsWith("VOTE_REQUEST")) {
                        handleVoteRequest(packet.getAddress(), packet.getPort(), socket);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void startElection() {
            if (receivedHeartbeat) {
                receivedHeartbeat = false; // Reset for the next timeout
                scheduler.schedule(this::startElection, electionTimeout, TimeUnit.MILLISECONDS);
                return;
            }

            System.out.printf("Node %d: Starting election.\n", id);
            isLeader = true; // Simulate that this node becomes the leader for simplicity
            broadcast("HEARTBEAT " + id);
            scheduler.scheduleAtFixedRate(() -> broadcast("HEARTBEAT " + id), 0, 1, TimeUnit.SECONDS);
        }

        private void handleHeartbeat() {
            if (isLeader) return;
            System.out.printf("Node %d: Received heartbeat.\n", id);
            receivedHeartbeat = true;
        }

        private void handleVoteRequest(InetAddress address, int senderPort, DatagramSocket socket) throws IOException {
            String response = "VOTE_GRANTED";
            System.out.printf("Node %d: Granting vote to %s:%d.\n", id, address.getHostAddress(), senderPort);
            byte[] data = response.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, senderPort);
            socket.send(packet);
        }

        private void broadcast(String message) {
            try (DatagramSocket socket = new DatagramSocket()) {
                byte[] data = message.getBytes();
                for (int i = 0; i < 5; i++) { // Assume we have 5 nodes
                    if (i == id) continue; // Don't send to self
                    DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 5000 + i);
                    socket.send(packet);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int numberOfNodes = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfNodes);

        for (int i = 0; i < numberOfNodes; i++) {
            Node node = new Node(i, 5000 + i);
            executorService.execute(node);
        }

        executorService.shutdown();
    }
}

