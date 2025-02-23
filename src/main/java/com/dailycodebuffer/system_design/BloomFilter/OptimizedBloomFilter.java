package com.dailycodebuffer.system_design.BloomFilter;

import java.util.BitSet;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OptimizedBloomFilter {
    private final BitSet bitSet;
    private final int bitArraySize;
    private final int numHashFunctions;

    public OptimizedBloomFilter(int size, int numHashFunctions) {
        this.bitArraySize = size;
        this.numHashFunctions = numHashFunctions;
        this.bitSet = new BitSet(size);
    }

    private int[] getHashValues(String data) {
        int[] hashValues = new int[numHashFunctions];
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        int h1 = murmurHash(bytes, 0x5f3759df);  // Seed 1
        int h2 = murmurHash(bytes, 0xc6a4a793);  // Seed 2

        for (int i = 0; i < numHashFunctions; i++) {
            int combinedHash = Math.abs((h1 + i * h2) % bitArraySize);
            hashValues[i] = combinedHash;
        }
        return hashValues;
    }

    private int murmurHash(byte[] data, int seed) {
        int hash = seed;
        for (byte b : data) {
            hash = (hash * 31) ^ b;
        }
        return hash;
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
        int size = 10_000_000;  // 10 million bits
        int numHashes = 7;  // Optimal for large datasets

        OptimizedBloomFilter bloomFilter = new OptimizedBloomFilter(size, numHashes);

        bloomFilter.add("hello");
        bloomFilter.add("world");

        System.out.println(bloomFilter.mightContain("hello")); // true (most likely)
        System.out.println(bloomFilter.mightContain("world")); // true (most likely)
        System.out.println(bloomFilter.mightContain("java"));  // false (definitely not)
    }
}
