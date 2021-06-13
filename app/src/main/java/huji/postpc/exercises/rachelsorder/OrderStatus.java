package huji.postpc.exercises.rachelsorder;

enum OrderStatus {
    WAITING,
    IN_PROGRESS,
    READY,
    DONE;

    @Override
    public String toString() {
        switch (this) {
            case DONE:
                return "done";
            case READY:
                return "ready";
            case WAITING:
                return "waiting";
            case IN_PROGRESS:
                return "in-progress";
            default:
                return "Invalid Order status";
        }
    }
}
