//Vihaan pardeshi and Neil Deshpande

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class realone {

    public static void main(String[] args) {
        // Input / Output file paths
        String inputFile1 = "BIBLE.txt";
        String inputFile2 = "LESMISERABLES.txt";
        String outputFile1 = "newbible.txt";
        String outputFile2 = "newmiserable.txt";

        try {
            System.out.println("Starting single-threaded processing...\n");

            // Process File 1
            long start1 = System.nanoTime();
            String data1 = safeRead(inputFile1);
            String modified1 = modifyText(data1);
            safeWrite(outputFile1, modified1);
            long end1 = System.nanoTime();
            System.out.println("Finished processing " + inputFile1 +
                    " in " + ((end1 - start1) / 1_000_000) + " ms");

            // Process File 2
            long start2 = System.nanoTime();
            String data2 = safeRead(inputFile2);
            String modified2 = modifyText(data2);
            safeWrite(outputFile2, modified2);
            long end2 = System.nanoTime();
            System.out.println("Finished processing " + inputFile2 +
                    " in " + ((end2 - start2) / 1_000_000) + " ms");

            System.out.println("\nAll processing completed (single-threaded).");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Read bytes raw and decode with ISO_8859_1 to avoid UTF-8 decode errors
    private static String safeRead(String filename) throws IOException {
        Path path = Paths.get(filename);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    // Modify text (uppercase transformation)
    private static String modifyText(String input) {
	    char[] chars = input.toCharArray();
	    long sum = 0;
	    for (char c : chars) {
	        for (int i = 0; i < 1000; i++) {   // simulate heavy CPU work
	            sum += Math.pow(c, 2) % 123;
	        }
	    }
	    return "Sum: " + sum;
	}


    // Write string using ISO-8859-1 encoding
    private static void safeWrite(String filename, String content) throws IOException {
        Path path = Paths.get(filename);
        Files.write(path, content.getBytes(StandardCharsets.ISO_8859_1));
    }
}

