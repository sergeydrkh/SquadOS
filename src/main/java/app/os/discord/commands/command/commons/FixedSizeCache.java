package app.os.discord.commands.command.commons;

import java.util.HashMap;
import java.util.Map;

public class FixedSizeCache<K, V> {
    private final Map<K, V> map = new HashMap<>();
    private final K[] keys;
    private int currIndex = 0;

    public FixedSizeCache(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Cache size must be at least 1!");
        } else {
            this.keys = (K[]) new Object[size];
        }
    }

    public void add(K key, V value) {
        if (this.keys[this.currIndex] != null) {
            this.map.remove(this.keys[this.currIndex]);
        }

        this.map.put(key, value);
        this.keys[this.currIndex] = key;
        this.currIndex = (this.currIndex + 1) % this.keys.length;
    }

    public boolean contains(K key) {
        return this.map.containsKey(key);
    }

    public V get(K key) {
        return this.map.get(key);
    }
}
