package uoc.ds.pr.pr2;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

    @DisplayName("NavigationHistory Unit Tests")
    public class NavigationHistoryTest {

        private NavigationHistory navigationHistory;

        @BeforeEach
        void setUp() {
            navigationHistory = new NavigationHistory();
        }

        @Test
        @DisplayName("Should return null and false status when initialized without any visits")
        void testInitialState() {
            assertAll("Initial state properties",
                    () -> assertNull(navigationHistory.getCurrentPage(), "Current page should be null initially"),
                    () -> assertNull(navigationHistory.back(), "back() should return null when back stack is empty"),
                    () -> assertNull(navigationHistory.forward(), "forward() should return null when forward stack is empty")
            );
        }

        @Test
        @DisplayName("Should correctly update current page on first visit without enabling back navigation")
        void testFirstVisit() {
            final String pageA = "https://www.uoc.edu";

            navigationHistory.visit(pageA);

            assertAll("State after single visit",
                    () -> assertEquals(pageA, navigationHistory.getCurrentPage().getUrl(), "Current page should match visited URL")
            );
        }

        @Test
        @DisplayName("Should correctly navigate back and forward through multiple visited pages")
        void testBackAndForwardNavigation() {
            final String pageA = "https://www.uoc.edu";
            final String pageB = "https://www.github.com";
            final String pageC = "https://www.stackoverflow.com";

            // Visit A -> B -> C
            navigationHistory.visit(pageA);
            navigationHistory.visit(pageB);
            navigationHistory.visit(pageC);

            assertEquals(pageC, navigationHistory.getCurrentPage().getUrl(), "Current page after visits should be C");

            // Backward navigation (C -> B -> A)
            final String backToB = navigationHistory.back().getUrl();
            assertAll("State after first back step",
                    () -> assertEquals(pageB, backToB, "Navigating back should return page B"),
                    () -> assertEquals(pageB, navigationHistory.getCurrentPage().getUrl(), "Current page should now be B")
            );

            final String backToA = navigationHistory.back().getUrl();
            assertAll("State after second back step",
                    () -> assertEquals(pageA, backToA, "Navigating back should return page A"),
                    () -> assertEquals(pageA, navigationHistory.getCurrentPage().getUrl(), "Current page should now be A")
            );

            // Attempt to go back past the first page
            assertNull(navigationHistory.back(), "Going back past first page should return null");

            // Forward navigation (A -> B -> C)
            final String forwardToB = navigationHistory.forward().getUrl();
            assertEquals(pageB, forwardToB, "Navigating forward should return page B");
            assertEquals(pageB, navigationHistory.getCurrentPage().getUrl(), "Current page should now be B");

            final String forwardToC = navigationHistory.forward().getUrl();
            assertEquals(pageC, forwardToC, "Navigating forward should return page C");
            assertEquals(pageC, navigationHistory.getCurrentPage().getUrl(), "Current page should now be C");

            // Attempt to go forward past the latest page
            assertAll("State at end of forward stack",
                    () -> assertNull(navigationHistory.forward(), "Going forward past latest page should return null")
            );
        }

        @Test
        @DisplayName("Should clear forward stack when visiting a new page from middle of history")
        void testNewVisitClearsForwardStack() {
            final String pageA = "https://www.uoc.edu";
            final String pageB = "https://www.github.com";
            final String pageC = "https://www.stackoverflow.com";
            final String pageD = "https://www.nytimes.com";

            navigationHistory.visit(pageA);
            navigationHistory.visit(pageB);
            navigationHistory.visit(pageC);

            // Go back (C -> B)
            navigationHistory.back();
            assertEquals(pageB, navigationHistory.getCurrentPage().getUrl(), "Current page should be B");

            // Visiting a new webpage clears the forward stack
            navigationHistory.visit(pageD);

            assertAll("State after visiting D from B",
                    () -> assertEquals(pageD, navigationHistory.getCurrentPage().getUrl(), "Current page should now be D"),
                    () -> assertNull(navigationHistory.forward(), "forward() should return null after forward stack is cleared")
            );

            // Verify back stack sequence (D -> B -> A)
            assertEquals(pageB, navigationHistory.back().getUrl(), "Going back from D should land on B");
            assertEquals(pageA, navigationHistory.back().getUrl(), "Going back from B should land on A");
        }
}
