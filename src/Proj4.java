/******************
 * @file: Proj4.java
 * @description: This is the main project driver class that performs performance analysis on the SeparateChainingHashTable. It reads a dataset from a specified input file, performs hash table operations under different list configurations (already sorted, shuffled, and reverse sorted). For each configuration, it measures and logs the performance of insertion, search, and deletion operations. The results are printed to the console and written to an analysis.txt file, providing insights into the hash table's performance characteristics under various input distributions.
 * @author: Aashi Patel
 * @date: December 5, 2024
 **********************/
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Proj4 {
    public static void main(String[] args) throws IOException {
        // Use command line arguments to specify the input file
        if (args.length != 2) {
            System.err.println("Usage: java Proj4 <input file> <number of lines>");
            System.exit(1);
        }

        String inputFileName = args[0];
        int numLines = Integer.parseInt(args[1]);

        // For file input.
        FileInputStream inputFileNameStream = null;
        Scanner inputFileNameScanner = null;

        // For performance logging
        FileWriter analysisFileWriter = new FileWriter("analysis.txt", true);
        PrintWriter analysisWriter = new PrintWriter(analysisFileWriter);

        try {
            // Open the input file
            inputFileNameStream = new FileInputStream(inputFileName);
            inputFileNameScanner = new Scanner(inputFileNameStream);

            // Ignore first line
            inputFileNameScanner.nextLine();

            // Read dataset into ArrayList
            ArrayList<String> dataset = new ArrayList<>();
            for (int i = 0; i < numLines && inputFileNameScanner.hasNextLine(); i++) {
                dataset.add(inputFileNameScanner.nextLine());
            }

            // Performance testing for different list configurations
            testHashTablePerformance(dataset, "Already Sorted", analysisWriter);

            Collections.shuffle(dataset);
            testHashTablePerformance(dataset, "Shuffled", analysisWriter);

            Collections.sort(dataset, Collections.reverseOrder());
            testHashTablePerformance(dataset, "Reversed", analysisWriter);

            analysisWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputFileNameScanner != null) inputFileNameScanner.close();
            if (inputFileNameStream != null) inputFileNameStream.close();
        }
    }

    private static void testHashTablePerformance(ArrayList<String> dataset, String listType, PrintWriter writer) {
        SeparateChainingHashTable<String> hashTable = new SeparateChainingHashTable<>();
        int datasetSize = dataset.size();

        // Insertion Performance
        long insertStart = System.nanoTime();
        for (String item : dataset) {
            hashTable.insert(item);
        }
        long insertEnd = System.nanoTime();
        double insertTime = (insertEnd - insertStart) / 1_000_000_000.0;

        // Search Performance
        long searchStart = System.nanoTime();
        for (String item : dataset) {
            hashTable.contains(item);
        }
        long searchEnd = System.nanoTime();
        double searchTime = (searchEnd - searchStart) / 1_000_000_000.0;

        // Deletion Performance
        long deleteStart = System.nanoTime();
        for (String item : dataset) {
            hashTable.remove(item);
        }
        long deleteEnd = System.nanoTime();
        double deleteTime = (deleteEnd - deleteStart) / 1_000_000_000.0;

        // Print results to console
        System.out.println(listType + " List Performance:");
        System.out.println("Dataset Size: " + datasetSize);
        System.out.println("Insertion Time: " + insertTime + " seconds");
        System.out.println("Search Time: " + searchTime + " seconds");
        System.out.println("Deletion Time: " + deleteTime + " seconds");
        System.out.println();

        // Write to CSV file
        writer.printf("%s,%d,%.6f,%.6f,%.6f\n", listType, datasetSize, insertTime, searchTime, deleteTime);
    }
}