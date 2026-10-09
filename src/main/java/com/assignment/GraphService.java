package com.assignment;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import static com.assignment.Models.*;

public class GraphService {
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    private final Map<Integer, Person> people = new ConcurrentHashMap<>();
    private final Map<Integer, Set<Integer>> adjList = new ConcurrentHashMap<>();

    public Person createPerson(String name) {
        int id = idGenerator.getAndIncrement();
        Person person = new Person(id, name);
        people.put(id, person);
        adjList.put(id, ConcurrentHashMap.newKeySet());
        return person;
    }

    public void addConnection(int a, int b) {
        adjList.get(a).add(b);
        adjList.get(b).add(a);
    }

    public boolean exists(int id) {
        return people.containsKey(id);
    }

    public List<Person> getContacts(int id) {
        return adjList.get(id).stream()
                .map(people::get)
                .toList();
    }

    public PathResponse findShortestPath(int from, int to) {
        if (from == to) {
            return new PathResponse(true, 0, List.of(people.get(from)));
        }

        Queue<List<Integer>> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(List.of(from));
        visited.add(from);

        while (!queue.isEmpty()) {
            List<Integer> currentPath = queue.poll();
            int lastNode = currentPath.get(currentPath.size() - 1);
            int currentHops = currentPath.size() - 1;

            if (currentHops == 3) {
                continue;
            }

            for (int neighbor : adjList.get(lastNode)) {
                if (neighbor == to) {
                    List<Integer> finalPath = new ArrayList<>(currentPath);
                    finalPath.add(neighbor);
                    List<Person> personPath = finalPath.stream().map(people::get).toList();
                    return new PathResponse(true, finalPath.size() - 1, personPath);
                }

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    List<Integer> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }
        return new PathResponse(false, null, null);
    }
}
