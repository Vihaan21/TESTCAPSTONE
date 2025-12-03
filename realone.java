// Vihaan Pardeshi and Neil Deshpande

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class realone {

    public static void main(String[] args) {

        // FIX: In Java, you must use double backslashes "\\" for file paths on Windows.
        String inputFile1 = "C:\\Users\\10020075\\Desktop\\Capstone\\threadstest\\TESTCAPSTONEFORK\\BIBLE.txt";
        String inputFile2 = "C:\\Users\\10020075\\Desktop\\Capstone\\threadstest\\TESTCAPSTONEFORK\\LESMISERABLES.txt";
        String outputFile1 = "C:\\Users\\10020075\\Desktop\\Capstone\\threadstest\\TESTCAPSTONEFORK\\newbible.txt";
        String outputFile2 = "C:\\Users\\10020075\\Desktop\\Capstone\\threadstest\\TESTCAPSTONEFORK\\newmiserable.txt";

        System.out.println("Starting MULTITHREADED processing...");

        // Thread pool with 2 threads (one for each file)
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Create tasks
        Callable<Long> task1 = () -> processFile(inputFile1, outputFile1);
        Callable<Long> task2 = () -> processFile(inputFile2, outputFile2);

        // --- START TOTAL TIMER ---
        long startTotal = System.nanoTime();

        try {
            // Start both tasks in parallel
            Future<Long> result1 = executor.submit(task1);
            Future<Long> result2 = executor.submit(task2);

            // Wait for results (This blocks until both are done)
            result1.get();
            result2.get();

            // --- END TOTAL TIMER ---
            long endTotal = System.nanoTime();
            long totalDuration = (endTotal - startTotal) / 1_000_000; // Convert to ms

            System.out.println("\nAll processing completed.");
            System.out.println("Total execution time for the whole program: " + totalDuration + " ms");

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    // --- Helper method to process a file in one thread ---
    private static long processFile(String input, String output) throws IOException {
        long start = System.nanoTime();

        String data = safeRead(input);
        String modified = modifyText(data);
        safeWrite(output, modified);

        long end = System.nanoTime();
        return (end - start) / 1_000_000;
    }

    // --- Read bytes raw & decode safely ---
    private static String safeRead(String filename) throws IOException {
        Path path = Paths.get(filename);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    // --- Modify (Heavy CPU Work Simulation) ---
    private static String modifyText(String input) {
        char[] chars = input.toCharArray();
        long sum = 0;
        // This loop makes the CPU work hard so multithreading is actually faster
        for (char c : chars) {
            for (int i = 0; i < 1000; i++) {
                sum += Math.pow(c, 2) % 123;
            }
        }
        return "Sum: " + sum;
    }

    // --- Write file safely ---
    private static void safeWrite(String filename, String content) throws IOException {
        Path path = Paths.get(filename);
        Files.write(path, content.getBytes(StandardCharsets.ISO_8859_1));
    }
}