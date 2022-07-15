package ru.practicum.shareit.common.memory_storage;

import java.util.HashMap;
import java.util.Map;

public class MemoryBaseStorage {

    private int counter = 0;
    private final Map<Integer, Object> objects = new HashMap<>();

    protected int add(Object o) {
        counter++;
        objects.put(counter, o);
        return counter;
    }

    protected Object get(int id) {
        return objects.get(id);
    }

    protected Map<Integer, Object> getObjects() {
        return objects;
    }

    public void delete(int id) {
        objects.remove(id);
    }


}
