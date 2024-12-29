package org.poo.utils;

import org.poo.entities.ValutarCourse;

import java.util.*;

/**
 * A generic utility class for storing a pair of values and providing currency conversion functionality.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class Pair<K, V> {

    /**
     * The key of the pair.
     */
    private final K key;

    /**
     * The value of the pair.
     */
    private final V value;

    /**
     * Constructor for initializing the pair with a key and a value.
     *
     * @param key   the key of the pair
     * @param value the value of the pair
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key of the pair.
     *
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * Returns the value of the pair.
     *
     * @return the value
     */
    public V getValue() {
        return value;
    }

    /**
     * Converts an amount from one currency to another using provided exchange rates.
     *
     * @param amount        the amount to be converted
     * @param fromCurrency  the source currency
     * @param toCurrency    the target currency
     * @param exchangeRates the list of exchange rates
     * @return the converted amount, or -1 if conversion is not possible
     */
    public static double convertCurrency(double amount, String fromCurrency, String toCurrency, List<ValutarCourse> exchangeRates) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        // Build a graph representation of currency exchange rates
        Map<String, Map<String, Double>> graph = new HashMap<>();
        for (ValutarCourse rate : exchangeRates) {
            graph.computeIfAbsent(rate.getFrom(), k -> new HashMap<>()).put(rate.getTo(),
                    rate.getRate());
            graph.computeIfAbsent(rate.getTo(), k -> new HashMap<>()).put(rate.getFrom(), 1.0 /
                    rate.getRate());
        }

        // Initialize Dijkstra's algorithm variables
        PriorityQueue<Pair<String, Double>> pq =
                new PriorityQueue<>(Comparator.comparingDouble(Pair::getValue));
        Map<String, Double> minConversion = new HashMap<>();
        Set<String> visited = new HashSet<>();

        pq.add(new Pair<>(fromCurrency, 1.0));
        minConversion.put(fromCurrency, 1.0);

        // Process the priority queue
        while (!pq.isEmpty()) {
            Pair<String, Double> current = pq.poll();
            String currentCurrency = current.getKey();
            double currentRate = current.getValue();

            if (visited.contains(currentCurrency)) {
                continue;
            }
            visited.add(currentCurrency);

            if (currentCurrency.equals(toCurrency)) {
                return amount * currentRate;
            }

            Map<String, Double> neighbors = graph.getOrDefault(currentCurrency, new HashMap<>());
            for (Map.Entry<String, Double> neighbor : neighbors.entrySet()) {
                String neighborCurrency = neighbor.getKey();
                double neighborRate = neighbor.getValue();

                if (!visited.contains(neighborCurrency)) {
                    double newRate = currentRate * neighborRate;
                    if (newRate < minConversion.getOrDefault(neighborCurrency, Double.MAX_VALUE)) {
                        minConversion.put(neighborCurrency, newRate);
                        pq.add(new Pair<>(neighborCurrency, newRate));
                    }
                }
            }
        }

        // Return -1 if conversion is not possible
        return -1;
    }
}
