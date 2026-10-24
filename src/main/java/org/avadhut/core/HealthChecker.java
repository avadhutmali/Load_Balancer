package org.avadhut.core;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class HealthChecker {
    private final List<Backend> backendList;
    private volatile Set<Backend> healthBackendSet;
    private final ScheduledExecutorService scheduler;
    private final HttpClient client;

    public HealthChecker(List<Backend> backendList){
        this.backendList = backendList;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.healthBackendSet = Set.copyOf(backendList);
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(1))
                .build();
        HealthCheck checker = new HealthCheck();
        this.scheduler.scheduleAtFixedRate(checker,0,5, TimeUnit.SECONDS);
    }

    private class HealthCheck implements Runnable{

        public void helper() {
            Set<Backend> localSet = new HashSet<>();
            for(Backend backend : backendList){
                String uriString = "http://"+backend.host()+":"+backend.port();
                URI uri = URI.create(uriString);
                HttpRequest.BodyPublisher publishers = HttpRequest.BodyPublishers.noBody();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .method("GET",publishers)
                        .timeout(Duration.ofSeconds(1))
                        .build();
                try{
                    client.send(request, HttpResponse.BodyHandlers.ofString());
                    localSet.add(backend);
                } catch (Exception e) {
                    System.out.println("The backend at url : "+uriString+" is Down");
                }
            }
            healthBackendSet = Set.copyOf( localSet);
        }

        @Override
        public void run() {
            try {
                helper();
            }catch (Exception e){

            }
        }
    }
    public void stopScheduler(){
        scheduler.shutdown();
    }
    public Set<Backend> getHealthyBackends(){
        return healthBackendSet;
    }
}
