package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.Stack;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackLinkedListTest {
    Stack<Integer> stack;

    @BeforeEach
    public void setUp() {
        stack = new StackLinkedList<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
    }

    @Test
    public void addTest() {
        assertEquals(5, stack.size());
        stack.push(6);
        assertEquals(6, stack.size());
    }

    @Test
    public void addPoplTest() {
        assertEquals(5, stack.size());
        assertEquals(5, stack.pop(),0);
        assertEquals(4, stack.size());

        assertEquals(4, stack.pop(),0);
        assertEquals(3, stack.size());

        assertEquals(3, stack.pop(),0);
        assertEquals(2, stack.size());

        assertEquals(2, stack.pop(),0);
        assertEquals(1, stack.size());

        assertEquals(1, stack.pop(),0);
        assertEquals(0, stack.size());

    }

    @Test
    public void addPeekTest() {
        assertEquals(5, stack.size());
        assertEquals(5, stack.peek(),0);
        assertEquals(5, stack.size());
    }


}
