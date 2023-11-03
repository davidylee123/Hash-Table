public class HashTable {
    private class Entry {
        private String key;
        private String etymology;
        private boolean removed;

        private Entry(String key, String etymology) {
            this.key = key;
            this.etymology = etymology;
            removed = false;
        }
    }

    private Entry[] table;
    private int tableSize;

    public HashTable(int size) {
        table = new Entry[size];
        tableSize = size;
    }

    public void insert(String key, String etymology) {
        int i = probe(key);
        if (i == -1) {
            throw new RuntimeException("HashTable is full");
        } else {
            table[i] = new Entry(key, etymology); // Create a new Entry object
        }
    }

    private int probe(String key) {
        int i = h1(key);
        int j = h2(key);
        int iterations = 0;

        while (table[i] != null && !table[i].removed) {
            i = (i + j) % tableSize;
            iterations++;
            if (iterations >= tableSize) return -1;
        }
        return i;
    }

    public String search(String key) {
        int i = findKey(key);
        if (i == -1 || table[i] == null) {
            return null;
        } else {
            return table[i].etymology;
        }
    }

    private int findKey(String key) {
        int i = h1(key);
        int j = h2(key);
        int iterations = 0;

        while (table[i] != null && !table[i].removed) {
            i = (i + j) % tableSize;
            iterations++;
            if (iterations >= tableSize) return -1;
        }

        if (table[i] == null) {
            return -1;
        } else {
            return i;
        }
    }

    private int h1(String key) {
        return Math.abs(key.hashCode()) % tableSize;
    }

    private int h2(String key) {
        // The second hash function should be non-zero to ensure that we always probe to a new index
        // Using a prime number less than tableSize to ensure we eventually probe all slots
        int hash = Math.abs(key.hashCode()) % (tableSize - 1) + 1;
        return hash != 0 ? hash : 1;
    }

    public static void main(String[] args) {
        HashTable ht = new HashTable(10); // Create a HashTable with a size of 10

        // Insert some key-value pairs into the hash table
        ht.insert("apple", "A fruit that grows on trees.");
        ht.insert("banana", "A long yellow fruit.");
        ht.insert("cherry", "A small red fruit.");

        // Search for keys and print their etymologies
        System.out.println("Etymology of 'apple': " + ht.search("apple"));
        System.out.println("Etymology of 'banana': " + ht.search("banana"));
        System.out.println("Etymology of 'cherry': " + ht.search("cherry"));
        System.out.println("Etymology of 'date': " + ht.search("date")); // This should return null

        // Try inserting more than the capacity to check the error handling
        for (int i = 0; i < 20; i++) {
            try {
                ht.insert("key" + i, "Etymology of key " + i);
            } catch (RuntimeException e) {
                System.out.println("Insertion failed for key" + i + ": " + e.getMessage());
                break; // Break the loop after the first insertion failure
            }
        }
    }
}

