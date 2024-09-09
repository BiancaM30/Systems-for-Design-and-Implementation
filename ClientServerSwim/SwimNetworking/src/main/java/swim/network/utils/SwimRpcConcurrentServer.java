package swim.network.utils;

import swim.network.rpcprotocol.SwimClientRpcReflectionWorker;
import swim.services.ISwimServices;

import java.net.Socket;


public class SwimRpcConcurrentServer extends AbsConcurrentServer {
    private ISwimServices swimServer;

    public SwimRpcConcurrentServer(int port, ISwimServices swimServer) {
        super(port);
        this.swimServer = swimServer;
        System.out.println("Swim- SwimRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        // SwimClientRpcWorker worker=new SwimClientRpcWorker(swimServer, client);
        SwimClientRpcReflectionWorker worker = new SwimClientRpcReflectionWorker(swimServer, client);

        Thread tw = new Thread(worker);
        return tw;
    }

    @Override
    public void stop() {
        System.out.println("Stopping services ...");
    }
}
