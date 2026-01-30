package com.wip;

public class MainClass {

    public int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {

        MainClass mc = new MainClass();

        Runnable task = () -> {
            while (true) {
                try {
                    System.out.println("Addition Result: " + mc.add(10, 20));
                    System.out.println("Java for Docker & Kubernetes 🚀");
                    System.out.println("App running... waiting for pipeline / pod lifecycle");

                    // Sleep to avoid CPU overuse
                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted. Shutting down gracefully.");
                    break;
                }
            }
        };

        Thread workerThread = new Thread(task);
        workerThread.start();
    }
}

