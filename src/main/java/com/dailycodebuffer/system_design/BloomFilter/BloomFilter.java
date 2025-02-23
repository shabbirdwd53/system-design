package com.dailycodebuffer.system_design.BloomFilter;

import java.util.BitSet;
import java.util.Random;

public class BloomFilter {
    private final BitSet bitSet;
    private final int bitArraySize;
    private final int numHashFunctions;
    private final int seed;

    public BloomFilter(int size, int numHashFunctions) {
        this.bitArraySize = size;
        this.numHashFunctions = numHashFunctions;
        this.bitSet = new BitSet(size);
        this.seed = new Random().nextInt();
    }

    private int[] getHashValues(String data) {
        int[] hashValues = new int[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            int hash = data.hashCode() ^ (seed * (i + 1));
            hashValues[i] = Math.abs(hash % bitArraySize);
        }
        return hashValues;
    }

    public void add(String data) {
        int[] hashValues = getHashValues(data);
        for (int hash : hashValues) {
            bitSet.set(hash);
        }
    }

    public boolean mightContain(String data) {
        int[] hashValues = getHashValues(data);
        for (int hash : hashValues) {
            if (!bitSet.get(hash)) {
                return false; // Definitely not present
            }
        }
        return true; // Might be present
    }

    public static void main(String[] args) {
        BloomFilter bloomFilter = new BloomFilter(1000, 3);

        bloomFilter.add("hello");
        bloomFilter.add("world");

        System.out.println(bloomFilter.mightContain("hello")); // true (most likely)
        System.out.println(bloomFilter.mightContain("world")); // true (most likely)
        System.out.println(bloomFilter.mightContain("java"));  // false (definitely not)
    }
}
