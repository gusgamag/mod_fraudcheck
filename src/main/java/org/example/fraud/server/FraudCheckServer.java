package org.example.fraud.server;

import com.sun.net.httpserver.HttpServer;
import org.example.fraud.controller.FraudCheckHandler;
import org.example.fraud.rule.EmailFraudRule;
import org.example.fraud.rule.FraudRule;
import org.example.fraud.rule.PhoneFormatRule;
import org.example.fraud.service.FraudCheckService;

import java.net.InetSocketAddress;
import java.util.List;

//Main class that contains the elements to build the server at 8080
public class FraudCheckServer {

    public static void main(String[] args) throws Exception {

        // Build fraud rules
        List<FraudRule> rules = List.of(
                new EmailFraudRule(),
                new PhoneFormatRule()
        );

        FraudCheckService fraudCheckService = new FraudCheckService(rules);

        // Create HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Register endpoint
        server.createContext("/fraud-check",
                new FraudCheckHandler(fraudCheckService));

        server.setExecutor(null); // default executor
        server.start();

        System.out.println("Fraud Check Service started on port 8080");
    }
}
