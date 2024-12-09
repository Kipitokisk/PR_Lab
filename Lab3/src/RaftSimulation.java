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
        private volatile boolean active = true; // Used to simulate failure
        private DatagramSocket socket;

        public Node(int id, int port) {
            this.id = id;
            this.port = port;
            this.electionTimeout = random.nextInt(3000) + 3000; // 3-6 seconds
        }

        @Override
        public void run() {
            System.out.printf("Node %d started on port %d.\n", id, port);
            scheduler.schedule(this::startElection, electionTimeout, TimeUnit.MILLISECONDS);
            try {
                socket = new DatagramSocket(port);
                byte[] buffer = new byte[1024];

                while (active) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());

                    if (message.startsWith("HEARTBEAT")) {
                        handleHeartbeat();
                    }
                }
            } catch (IOException e) {
                if (active) {
                    e.printStackTrace();
                }
            }
        }

        private void startElection() {
            if (!active) return; // If the node is inactive, skip election
            if (receivedHeartbeat) {
                receivedHeartbeat = false; // Reset for the next timeout
                scheduler.schedule(this::startElection, electionTimeout, TimeUnit.MILLISECONDS);
                return;
            }

            System.out.printf("\nNode %d: Starting election.\n", id);
            isLeader = true; // Simulate that this node becomes the leader
            System.out.println("\nNode " + id + " broadcasted heartbeat.");
            broadcast("HEARTBEAT " + id);
            scheduler.scheduleAtFixedRate(() -> {
                if (active) {
                    System.out.println("Node " + id + " broadcasted heartbeat.");
                    broadcast("HEARTBEAT " + id);
                }
            }, 0, 1000, TimeUnit.MILLISECONDS);
        }

        private void handleHeartbeat() {
            if (isLeader || !active) return;
            System.out.printf("Node %d: Received heartbeat.\n", id);
            receivedHeartbeat = true;
        }

        private void broadcast(String message) {
            try {
                byte[] data = message.getBytes();
                for (int i = 0; i < 5; i++) {
                    if (i == id) continue;
                    DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 5000 + i);
                    socket.send(packet);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void deactivate() {
            active = false;
            System.out.printf("Node %d: Simulated failure.\n", id);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Node[] nodes = new Node[5];

        // Start all nodes
        for (int i = 0; i < 5; i++) {
            nodes[i] = new Node(i, 5000 + i);
            executorService.execute(nodes[i]);
        }

        // Let the simulation run for a few seconds
        Thread.sleep(10000);

        // Simulate the leader dying
        System.out.println("\nSimulating leader failure...\n");
        for (Node node : nodes) {
            if (node.isLeader) {
                node.deactivate();
                break;
            }
        }

        // Let the simulation run for additional time to observe a new election
        Thread.sleep(10000);

        executorService.shutdown();
    }
}
