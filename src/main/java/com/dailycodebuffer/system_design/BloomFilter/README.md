A **Bloom Filter** is a space-efficient probabilistic data structure used to test whether an element is present in a set. It can return false positives but never false negatives.

### Implementation Details:

-   We use a **bit array** to store elements.
-   We apply **multiple hash functions** to determine bit positions.
-   When inserting an element, all hash functions set the respective bits.
-   When checking for membership, if all corresponding bits are set, the element *might* be present; otherwise, it is definitely absent.

### **Key Points:**

-   **BitSet** is used to store the bit array.
-   **Multiple hash functions** are simulated by modifying `hashCode()`.
-   `add()` sets the bits at the computed indices.
-   `mightContain()` checks if all corresponding bits are set.

This implementation provides an **efficient** and **compact** way to check for membership with a minimal false positive probability.

To optimize the Bloom Filter for handling **millions of strings**, we need to improve **space efficiency, hashing, and performance**:

### **Optimizations:**

1.  **Efficient Hash Functions**: Use **MurmurHash** (or other non-cryptographic fast hash functions) instead of `hashCode()`, which has weak randomness.
2.  **Use a Larger BitSet**: A larger bit array reduces false positives.
3.  **Parallel Processing**: Use **ConcurrentBitSet** if multiple threads need to access it.
4.  **Optimize Hashing with Double Hashing**: Instead of computing `k` independent hash functions, we derive them from two base hash functions (`h1` and `h2`).


### **Choosing the Right Approach**

| Approach | Use Case | Pros | Cons |
| --- | --- | --- | --- |
| **Partitioned Bloom Filter** | Large-scale, distributed storage | Scalable, low memory per node | Requires querying multiple nodes |
| **Replicated Bloom Filter** | Fast queries, small dataset | Low latency | High memory usage |
| **Counting Bloom Filter** | When deletion is required | Supports element removal | More memory usage |
| **Scalable Bloom Filter** | Large, unbounded datasets | Handles growing data | More false positives |


### **Conclusion**

For **real-world applications**, use:

-   **Redis Bloom Filters** for distributed caching.
-   **Kafka Streams** for **synchronized Bloom Filters**.
-   **Scalable Bloom Filters** for dynamic datasets.