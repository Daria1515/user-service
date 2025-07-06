package org.example.kafka;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserOperationTest {

    @Test
    void testUserOperationValues() {
        UserOperation[] operations = UserOperation.values();
        
        assertEquals(3, operations.length);
        assertTrue(contains(operations, UserOperation.CREATE));
        assertTrue(contains(operations, UserOperation.DELETE));
        assertTrue(contains(operations, UserOperation.UPDATE));
    }

    @Test
    void testUserOperationValueOf() {
        assertEquals(UserOperation.CREATE, UserOperation.valueOf("CREATE"));
        assertEquals(UserOperation.DELETE, UserOperation.valueOf("DELETE"));
        assertEquals(UserOperation.UPDATE, UserOperation.valueOf("UPDATE"));
    }

    @Test
    void testUserOperationOrdinal() {
        assertEquals(0, UserOperation.CREATE.ordinal());
        assertEquals(1, UserOperation.DELETE.ordinal());
        assertEquals(2, UserOperation.UPDATE.ordinal());
    }

    private boolean contains(UserOperation[] operations, UserOperation operation) {
        for (UserOperation op : operations) {
            if (op == operation) {
                return true;
            }
        }
        return false;
    }
} 