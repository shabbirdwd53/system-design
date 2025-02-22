### **Implementation: Consistent Hashing in Java**

Let's create:

1.  **`ConsistentHashing`** class to manage nodes.
2.  **`Node`** class representing servers.
3.  A **test simulation** distributing 1 million requests.


### **How It Works**

1.  **Virtual Nodes**
    -   Each real node has multiple virtual nodes to ensure uniform distribution.
2.  **MD5 Hashing**
    -   Ensures even spreading of keys across the ring.
3.  **Consistent Mapping**
    -   New node addition/removal affects only nearby points in the ring.


### **Output Example**

`Current Nodes: [Server-1, Server-2, Server-3, Server-4, Server-5]
Request Distribution:

Server-1 -> 201324
Server-2 -> 198765
Server-3 -> 200234
Server-4 -> 199887
Server-5 -> 199790`

Each server gets roughly equal requests. This ensures **load balancing** in distributed systems.


### **Understanding the `hash` Method**

The `hash` method in your code is responsible for generating a hash value from a given key (node or request key). This hash is used to determine the position of a node or request on the **consistent hashing ring**.

#### **Step-by-Step Breakdown of `hash(String key)`**


```
private long hash(String key) {
    md5.update(key.getBytes(StandardCharsets.UTF_8));
    byte[] digest = md5.digest();
    return ((long) (digest[3] & 0xFF) << 24) | ((long) (digest[2] & 0xFF) << 16) |
           ((long) (digest[1] & 0xFF) << 8) | (digest[0] & 0xFF);
}
```

#### **Operations Performed**

1.  **Convert the Key into Bytes**


```md5.update(key.getBytes(StandardCharsets.UTF_8));```

-   The key (node name or request key) is converted into a sequence of bytes using UTF-8 encoding.
-   This ensures consistency across different platforms.

2.  **Generate MD5 Hash Digest**

```
byte[] digest = md5.digest();
```

-   The `MessageDigest` class computes an **MD5 hash** (16-byte array).
-   This is a **one-way** hash function, meaning the same key will always produce the same hash.
3.  **Extracting an Integer (Long) from the Hash**

    ```
    return ((long) (digest[3] & 0xFF) << 24) | ((long) (digest[2] & 0xFF) << 16) |
           ((long) (digest[1] & 0xFF) << 8) | (digest[0] & 0xFF);
    ```

-   The first 4 bytes (`digest[0]`, `digest[1]`, `digest[2]`, `digest[3]`) are extracted.
-   These bytes are converted into a **32-bit integer** (a `long` value).
-   **Bitwise Operations:**
    -   `& 0xFF`: Ensures the byte is treated as an unsigned value.
    -   `<<`: Left shift operator to combine the bytes into a single number.
    -   The resulting **long value** is used as the hash.