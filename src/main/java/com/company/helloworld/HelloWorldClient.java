package com.company.helloworld;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple client that requests calculations {@link HelloWorldServer}.
 */
public class HelloWorldClient {
  private static final Logger logger = Logger.getLogger(HelloWorldClient.class.getName());

  private final CalculatorGrpc.CalculatorBlockingStub blockingStub;

  /**
   * Construct client for accessing HelloWorld server using the existing channel.
   */
  public HelloWorldClient(Channel channel) {
    // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
    // shut it down.

    // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
    blockingStub = CalculatorGrpc.newBlockingStub(channel);
  }

  public void add(Double arg1, Double arg2) {
    CalcRequest request1 = CalcRequest.newBuilder().setArg1(arg1).setArg2(arg2).build();
    CalcReply response;
    try {
      response = blockingStub.add(request1);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
    logger.info("add: " + response.getResult());
  }

  public void subtract(Double arg1, Double arg2) {
    CalcRequest request1 = CalcRequest.newBuilder().setArg1(arg1).setArg2(arg2).build();
    CalcReply response;
    try {
      response = blockingStub.subtract(request1);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
    logger.info("subtract: " + response.getResult());
  }

  /**
   * Greet server. If provided, the first element of {@code args} is the name to use in the
   * greeting. The second argument is the target server.
   */
  public static void main(String[] args) throws Exception {
    Double arg1 = 7.0;
    Double arg2 = 5.1;

    // Access a service running on the local machine on port 50051
    String target = "localhost:50051";

    // Create a communication channel to the server, known as a Channel. Channels are thread-safe
    // and reusable. It is common to create channels at the beginning of your application and reuse
    // them until the application shuts down.
    ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
      // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
      // needing certificates.
      .usePlaintext()
      .build();

    try {
      HelloWorldClient client = new HelloWorldClient(channel);
      client.add(arg1, arg2);
      client.subtract(arg1, arg2);
    } finally {
      // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
      // resources the channel should be shut down when it will no longer be used. If it may be used
      // again leave it running.
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
