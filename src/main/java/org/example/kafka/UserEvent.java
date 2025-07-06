package org.example.kafka;

public class UserEvent {
    public static final String OPERATION_CREATE = "CREATE";
    public static final String OPERATION_DELETE = "DELETE";
    public static final String OPERATION_UPDATE = "UPDATE";
    
    private String email;
    private String operation;

    public UserEvent() {}
    
    public UserEvent(String email, String operation) {
        this.email = email;
        this.operation = operation;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
}