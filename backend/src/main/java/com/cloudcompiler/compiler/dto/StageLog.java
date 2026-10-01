package com.cloudcompiler.compiler.dto;

public class StageLog {
    private String stageName;
    private String description;
    private String status; // SUCCESS, WARNING, FAILED, SKIPPED
    private long durationMs;
    private String message;

    public StageLog() {}

    public StageLog(String stageName, String description, String status, long durationMs, String message) {
        this.stageName = stageName;
        this.description = description;
        this.status = status;
        this.durationMs = durationMs;
        this.message = message;
    }

    public static StageLogBuilder builder() {
        return new StageLogBuilder();
    }

    public static class StageLogBuilder {
        private String stageName;
        private String description;
        private String status;
        private long durationMs;
        private String message;

        public StageLogBuilder stageName(String stageName) { this.stageName = stageName; return this; }
        public StageLogBuilder description(String description) { this.description = description; return this; }
        public StageLogBuilder status(String status) { this.status = status; return this; }
        public StageLogBuilder durationMs(long durationMs) { this.durationMs = durationMs; return this; }
        public StageLogBuilder message(String message) { this.message = message; return this; }

        public StageLog build() {
            return new StageLog(stageName, description, status, durationMs, message);
        }
    }

    public String getStageName() { return stageName; }
    public void setStageName(String stageName) { this.stageName = stageName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
