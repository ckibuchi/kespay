package kespay.enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public enum PaymentStatus {
    STARTED("STARTED"), COMPLETED("COMPLETED"),FIRST_FAILED("FIRST_FAILED"),FAILED("FAILED"),INIT("INIT"),RECEIVED("RECEIVED");

    private String name;

    private static final Map<String, PaymentStatus> ENUM_MAP;

    PaymentStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

// Build an immutable map of String name to enum pairs.
// Any Map impl can be used.

    static {
        Map<String, PaymentStatus> map = new ConcurrentHashMap<String, PaymentStatus>();
        for (PaymentStatus instance : PaymentStatus.values()) {
            map.put(instance.getName(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static PaymentStatus get(String name) {
        return ENUM_MAP.get(name);
    }
}