**1\. Introduction to Geospatial Indexing**
-------------------------------------------

Geospatial indexing is a technique used to efficiently store and query location-based data. When working with maps, GPS coordinates, or location-based searches, databases must quickly find nearby locations.

**Example Use Cases:**

-   Finding nearby restaurants in Google Maps
-   Showing nearby riders in Uber
-   Locating friends in social media apps like Snapchat

Traditional databases struggle with latitude/longitude searches because they require complex mathematical calculations. Instead, **geospatial indexes** (like GeoHash) convert locations into compact, searchable formats.

**What is GeoHash?**
--------------------

GeoHash is a **geocoding system** that encodes latitude and longitude into a short string of letters and digits. Instead of storing floating-point numbers, GeoHash stores locations in a **fixed-length string**, which makes indexing and searching efficient.

-   **Example:**
    -   **Latitude, Longitude** → (37.7749, -122.4194) (San Francisco)
    -   **GeoHash** → `9q8yyh6z`

Each character in the GeoHash represents a more precise area. The more characters, the smaller the area.

**How GeoHash Works?**
----------------------

GeoHashing works by dividing the world into a grid and encoding each section. The process follows these steps:

1.  **Divide the world** into a grid (binary partitioning).
2.  **Assign a unique code** to each grid cell using a combination of `latitude` and `longitude`.
3.  **Refine the grid** further with each additional character in the hash.

Here's how GeoHash refines location:

| GeoHash | Region Size (approx.) |
| --- | --- |
| `9` | 5,000 km × 5,000 km |
| `9q` | 1,250 km × 625 km |
| `9q8` | 156 km × 156 km |
| `9q8y` | 39 km × 19.5 km |
| `9q8yy` | 4.9 km × 4.9 km |
| `9q8yyh` | 0.61 km × 0.61 km |
| `9q8yyh6z` | 4.8m × 4.8m |

The longer the hash, the more precise the location.

**Benefits of GeoHashing in Databases**
---------------------------------------

GeoHashing is widely used in databases for geospatial searches. Instead of performing slow latitude-longitude comparisons, databases can:

✅ **Index locations efficiently** using GeoHash strings.\
✅ **Perform fast range queries** (like "Find places near me").\
✅ **Reduce database storage** because strings are more compact than float values.\
✅ **Support hierarchical searching** (shorter hashes cover larger areas).

### **Databases Supporting GeoHash**

-   **Elasticsearch** (supports GeoHash-based searches)
-   **MongoDB** (`2dsphere` and `2d` indexes)
-   **PostgreSQL** (with PostGIS extension)
-   **Redis** (with geospatial indexing commands)