package com.dsa.menu;

import com.dsa.model.ApplicationState;
import com.dsa.queue.CustomQueue;
import com.dsa.util.ConsoleFormatter;
import com.dsa.util.InputValidator;

/**
 * "Registration Requests": student registration requests handled first come, first served.
 * Technically this is the circular-array queue (CustomQueue, FIFO); a slot is an array index.
 */
public class QueueMenu extends BaseMenu {

    private final CustomQueue<Integer> queue;

    public QueueMenu(InputValidator input, ApplicationState state) {
        super(input, state);
        this.queue = state.getQueue();
    }

    @Override
    protected String getTitle() {
        return "REGISTRATION REQUESTS";
    }

    @Override
    protected String getTechnicalNote() {
        return "circular-array queue (FIFO - first come, first served)";
    }

    @Override
    protected String[] getOptions() {
        return new String[]{
                "Submit Registration Request",
                "Process Next Request",
                "View Next Request",
                "View Waiting Requests",
                "Check if Any Requests Are Waiting",
                "Check if Request Queue is Full"
        };
    }

    @Override
    protected void handleChoice(int choice) {
        switch (choice) {
            case 1 -> submitRequest();
            case 2 -> processNextRequest();
            case 3 -> viewNextRequest();
            case 4 -> viewWaitingRequests();
            case 5 -> ConsoleFormatter.info(queue.isEmpty()
                    ? "No registration requests are waiting."
                    : queue.size() + " registration request(s) waiting.");
            case 6 -> ConsoleFormatter.info(queue.isFull()
                    ? "The request queue is full (" + queue.capacity() + " of " + queue.capacity() + ")."
                    : "The request queue has room for " + (queue.capacity() - queue.size()) + " more request(s).");
            default -> ConsoleFormatter.error("Invalid choice.");
        }
    }

    private void submitRequest() {
        if (queue.isFull()) {
            ConsoleFormatter.error("The request queue is full (" + queue.capacity()
                    + " requests). Process a request first. (queue overflow)");
            return;
        }
        int studentId = input.readInt("Enter the student ID submitting the request: ");
        queue.enqueue(studentId);
        ConsoleFormatter.success("Registration request from student ID " + studentId
                + " added to the queue (stored in slot " + queue.getRearIndex() + "). [O(1)]");
    }

    private void processNextRequest() {
        if (queue.isEmpty()) {
            ConsoleFormatter.error("There are no registration requests to process. (queue underflow)");
            return;
        }
        int slot = queue.getFrontIndex();
        int studentId = queue.dequeue();
        ConsoleFormatter.success("Processed the registration request from student ID " + studentId
                + " (slot " + slot + "); the next request is in slot " + queue.getFrontIndex() + ". [O(1)]");
    }

    private void viewNextRequest() {
        if (queue.isEmpty()) {
            ConsoleFormatter.error("There are no registration requests waiting. (empty queue)");
            return;
        }
        ConsoleFormatter.success("Next request to process: student ID " + queue.peek() + ". [O(1)]");
    }

    private void viewWaitingRequests() {
        if (queue.isEmpty()) {
            ConsoleFormatter.info("No registration requests are waiting (0 of " + queue.capacity() + ").");
            return;
        }
        System.out.println("Waiting registration requests (student IDs, next to be processed on the left):");
        queue.display();
    }
}
