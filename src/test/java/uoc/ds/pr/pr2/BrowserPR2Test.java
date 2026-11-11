package uoc.ds.pr.pr2;

import edu.uoc.ds.traversal.Iterator;
import org.junit.jupiter.api.*;

import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.CSVDataLoader;
import uoc.ds.pr.util.DateUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BrowserPR2Test {

    protected Browser browser;
    protected BrowserHelper helper;

    @BeforeEach
    public void setUp() throws Exception {
        this.browser = FactoryBrowser.getComputerProjects();
        this.helper = this.browser.getHelper();

        assertEquals(0, helper.numUserProfiles());
        CSVDataLoader.addUsers(browser);
        assertEquals(65, helper.numUserProfiles());

        CSVDataLoader.addBookmarks(browser);
        CSVDataLoader.addDNSEntries(browser);

        assertEquals(13, helper.numDNSEntries());

    }


    @AfterEach
    public void tearDown() {
        this.browser = null;
    }


    @Test
    @DisplayName("addUserProfileTest: Add and update a UserProfile successfully")
    public void addUserProfileTest() throws DSException {

        assertEquals(65, helper.numUserProfiles());
        browser.addUserProfile("USER91", "user 91", "user91", "12345");
        assertEquals(65 + 1, helper.numUserProfiles());

        final UserProfile userProfile91 = helper.getUserProfile("USER91");
        assertNotNull(userProfile91, "The user USER91 should exist");

        // 2. Comprovem totes les seves propietats en un únic bloc assertAll
        assertAll("USER91 account properties",
                () -> assertEquals("user 91", userProfile91.getName(), "Name does not match"),
                () -> assertEquals("user91", userProfile91.getUser(), "Username does not match"),
                () -> assertEquals("12345", userProfile91.getPass(), "Password does not match")
        );

        browser.addUserProfile("USER91", "User 91 Updated", "user91_v2", "01cfcd4f6b8770febfb40cb906715822");

        assertEquals(66, helper.numUserProfiles());

        final UserProfile updateduserProfile91 = helper.getUserProfile("USER91");

        assertAll("USER91 updated properties",
                () -> assertEquals("User 91 Updated", updateduserProfile91.getName(), "Name does not match"),
                () -> assertEquals("user91_v2", updateduserProfile91.getUser(), "Username does not match"),
                () -> assertEquals("01cfcd4f6b8770febfb40cb906715822", updateduserProfile91.getPass(), "Password hash does not match")
        );
    }

    @Test
    @DisplayName("addBookmarkTest: Add and update a bookmark for an existing user account")
    public void addBookmarkTest() throws DSException {

        assertThrows(UserProfileNotFoundException.class, () -> {
            browser.addBookmark("USER_UNKNOWN", "Unknown City", "https://www.unknowncity.com", "/Travel/Unknown");
        });

        int initialUser1Bookmarks = helper.numBookmarksByUser("U1");

        browser.addBookmark("U1", "Granada Tourism", "https://www.turismogranada.org", "/Travel/Europe/Spain");

        assertEquals(initialUser1Bookmarks + 1, helper.numBookmarksByUser("U1"));

        Bookmark bookmarkNew = helper.getBookmark("U1", "https://www.turismogranada.org");
        assertNotNull(bookmarkNew, "The bookmark for Granada Tourism should exist");

        assertAll("New bookmark properties",
                () -> assertEquals("Granada Tourism", bookmarkNew.getTitle(), "Title does not match"),
                () -> assertEquals("/Travel/Europe/Spain", bookmarkNew.getPath(), "Path does not match")
        );

        browser.addBookmark("U1", "Granada Tourism Updated", "https://www.turismogranada.org", "/Travel/Europe/Spain/Andalusia");

        assertEquals(initialUser1Bookmarks + 1, helper.numBookmarksByUser("U1"));

        Bookmark bookmarkUpdated = helper.getBookmark("U1", "https://www.turismogranada.org");
        assertAll("Updated bookmark properties",
                () -> assertEquals("Granada Tourism Updated", bookmarkUpdated.getTitle(), "Title does not match"),
                () -> assertEquals("/Travel/Europe/Spain/Andalusia", bookmarkUpdated.getPath(), "Path does not match")
        );
    }

    @Test
    @DisplayName("editConfigurationOptionTest: Add and update configuration options for an existing user account")
    public void editConfigurationOptionTest() throws DSException {

        assertThrows(UserProfileNotFoundException.class, () -> {
            browser.editConfigurationOption("CO1", "USER_UNKNOWN", "theme", "dark");
        },"Should throw UserProfileNotFoundException when the user does not exist");

        final int initialCount = helper.numConfigurationOptionsByUser("U1");
        assertEquals(0, initialCount);
        browser.editConfigurationOption("CO1", "U1", "theme", "light");
        assertEquals(initialCount+1, helper.numConfigurationOptionsByUser("U1"));
        browser.editConfigurationOption("CO2", "U1", "fontSize", "14px");
        assertEquals(initialCount+2, helper.numConfigurationOptionsByUser("U1"));
        browser.editConfigurationOption("CO3", "U1", "homePage", "https://www.uoc.edu");
        assertEquals(initialCount+3, helper.numConfigurationOptionsByUser("U1"));
        browser.editConfigurationOption("CO4", "U1", "notifications", "enabled");
        assertEquals(initialCount+4, helper.numConfigurationOptionsByUser("U1"));

        final ConfigurationOption optTheme = helper.getConfigurationOption("U1", "CO1");
        assertNotNull(optTheme, "CO1 configuration option should exist");
        assertAll("CO1 initial properties",
                () -> assertEquals("theme", optTheme.getProperty(), "Property does not match"),
                () -> assertEquals("light", optTheme.getValue(), "Value does not match")
        );

        final ConfigurationOption optFontSize = helper.getConfigurationOption("U1", "CO2");
        assertNotNull(optFontSize, "CO2 configuration option should exist");
        assertAll("CO2 initial properties",
                () -> assertEquals("fontSize", optFontSize.getProperty(), "Property does not match"),
                () -> assertEquals("14px", optFontSize.getValue(), "Value does not match")
        );

        final ConfigurationOption optHomePage = helper.getConfigurationOption("U1", "CO3");
        assertNotNull(optHomePage, "CO3 configuration option should exist");
        assertAll("CO3 initial properties",
                () -> assertEquals("homePage", optHomePage.getProperty(), "Property does not match"),
                () -> assertEquals("https://www.uoc.edu", optHomePage.getValue(), "Value does not match")
        );

        final ConfigurationOption optNotif = helper.getConfigurationOption("U1", "CO4");
        assertNotNull(optNotif, "CO4 configuration option should exist");
        assertAll("CO4 initial properties",
                () -> assertEquals("notifications", optNotif.getProperty(), "Property does not match"),
                () -> assertEquals("enabled", optNotif.getValue(), "Value does not match")
        );

        browser.editConfigurationOption("CO1", "U1", "theme", "dark");
        browser.editConfigurationOption("CO2", "U1", "fontSize", "18px");
        browser.editConfigurationOption("CO4", "U1", "notifications", "disabled");

        assertAll("Updated configuration option values",
                () -> assertEquals("dark", helper.getConfigurationOption("U1", "CO1").getValue(), "CO1 value should be updated"),
                () -> assertEquals("18px", helper.getConfigurationOption("U1", "CO2").getValue(), "CO2 value should be updated"),
                () -> assertEquals("https://www.uoc.edu", helper.getConfigurationOption("U1", "CO3").getValue(), "CO3 value should remain unchanged"),
                () -> assertEquals("disabled", helper.getConfigurationOption("U1", "CO4").getValue(), "CO4 value should be updated")
        );
        assertEquals(initialCount+4, helper.numConfigurationOptionsByUser("U1"));
    }

    @Test
    @DisplayName("addExtensionTest: Add and update extension for a valid user account")
    public void addExtensionTest() throws DSException {

        assertThrows(UserProfileNotFoundException.class, () -> {
            browser.addExtension("EXT1", "USER_UNKNOWN", "uBlock Origin", "/extensions/ublock.zip", "1.40.0");
        }, "Should throw UserProfileNotFoundException when the user does not exist");

        final int initialTotalExtensions = helper.numExtensions();
        final int initialUserExtensions = helper.numExtensionsByUser("U1");

        browser.addExtension("EXT1", "U1", "uBlock Origin", "/extensions/ublock.zip", "1.40.0");

        final Extension ext = helper.getExtension("EXT1");

        assertAll("Extension properties after creation and counts",
                () -> assertEquals(initialUserExtensions + 1, helper.numExtensionsByUser("U1"), "User extensions count should increase by 1"),
                () -> assertEquals(initialTotalExtensions + 1, helper.numExtensions(), "Total extensions count should increase by 1"),
                () -> assertNotNull(ext, "Extension EXT1 should exist in helper"),
                () -> assertEquals("EXT1", ext.getExtensionId(), "Extension ID does not match"),
                () -> assertEquals("uBlock Origin", ext.getName(), "Extension name does not match"),
                () -> assertEquals("/extensions/ublock.zip", ext.getPackagePath(), "Package path does not match"),
                () -> assertEquals("1.40.0", ext.getVersion(), "Extension version does not match")
        );

        browser.addExtension("EXT1", "U1", "uBlock Origin Lite", "/extensions/ublock_lite.zip", "1.41.0");

        final Extension extUpdated = helper.getExtension("EXT1");

        assertAll("Extension properties after update and counts",
                () -> assertEquals(initialUserExtensions + 1, helper.numExtensionsByUser("U1"), "User extensions count should remain unchanged after update"),
                () -> assertEquals(initialTotalExtensions + 1, helper.numExtensions(), "Total extensions count should remain unchanged after update"),
                () -> assertNotNull(extUpdated, "Updated extension EXT1 should exist in helper"),
                () -> assertEquals("uBlock Origin Lite", extUpdated.getName(), "Updated extension name does not match"),
                () -> assertEquals("/extensions/ublock_lite.zip", extUpdated.getPackagePath(), "Updated package path does not match"),
                () -> assertEquals("1.41.0", extUpdated.getVersion(), "Updated extension version does not match")
        );

        browser.addExtension("EXT2", "U1", "Stromboli translate", "/extensions/stromboli.zip", "1.41.2");

        final Extension ext2 = helper.getExtension("EXT2");

        assertAll("Second extension properties and counts",
                () -> assertEquals(initialUserExtensions + 2, helper.numExtensionsByUser("U1"), "User extensions count should equal initial + 2"),
                () -> assertEquals(initialTotalExtensions + 2, helper.numExtensions(), "Total extensions count should equal initial + 2"),
                () -> assertNotNull(ext2, "Extension EXT2 should exist in helper"),
                () -> assertEquals("Stromboli translate", ext2.getName(), "Second extension name does not match"),
                () -> assertEquals("/extensions/stromboli.zip", ext2.getPackagePath(), "Second package path does not match"),
                () -> assertEquals("1.41.2", ext2.getVersion(), "Second extension version does not match")
        );
    }

    @Test
    @DisplayName("addCommentToExtensionTest: Add comment to extension and handle exception cases")
    public void addCommentToExtensionTest() throws DSException {
        browser.addExtension("EXT1", "U1", "uBlock Origin Lite", "/extensions/ublock_lite.zip", "1.41.0");

        assertThrows(ExtensionNotFoundException.class, () -> {
            browser.addCommentToExtension("EXTENSION_UNKNOWN", "U1", "Great extension!");
        }, "Should throw ExtensionNotFoundException when the extension does not exist");

        assertThrows(UserProfileNotFoundException.class, () -> {
            browser.addCommentToExtension("EXT1", "USER_UNKNOWN", "Great extension!");
        }, "Should throw UserProfileNotFoundException when the user does not exist");

        final Extension extension = helper.getExtension("EXT1");
        assertNotNull(extension, "Extension EXT1 should exist");

        final int initialCommentsCount = helper.numCommentsByExtension("EXT1");
        browser.addCommentToExtension("EXT1", "U1", "Works really well in this browser");

        assertEquals(initialCommentsCount + 1, helper.numCommentsByExtension("EXT1"),
                "Comments count for EXT1 should increase by 1 after adding a comment");    }

    @Test
    @DisplayName("visitWebTest: Record web visits for multiple users and handle non-existent user exceptions")
    public void visitWebTest() throws DSException {

        final int initialU1History = helper.numHistoryByUser("U1");
        final int initialU2History = helper.numHistoryByUser("U2");
        final int initialU5History = helper.numHistoryByUser("U5");
        final int initialU7History = helper.numHistoryByUser("U7");

        assertAll("Initial user history counts",
                () -> assertEquals(initialU1History, helper.numHistoryByUser("U1"), "U1 initial history count mismatch"),
                () -> assertEquals(initialU2History, helper.numHistoryByUser("U2"), "U2 initial history count mismatch"),
                () -> assertEquals(initialU5History, helper.numHistoryByUser("U5"), "U5 initial history count mismatch"),
                () -> assertEquals(initialU7History, helper.numHistoryByUser("U7"), "U7 initial history count mismatch")
        );

        CSVDataLoader.addVisitsWeb(browser);

        assertAll("User history counts after CSV data load",
                () -> assertEquals(initialU1History + 16, helper.numHistoryByUser("U1"), "U1 history count should increase by 9"),
                () -> assertEquals(initialU2History + 6, helper.numHistoryByUser("U2"), "U2 history count should increase by 6"),
                () -> assertEquals(initialU5History + 5, helper.numHistoryByUser("U5"), "U5 history count should increase by 5"),
                () -> assertEquals(initialU7History + 5, helper.numHistoryByUser("U7"), "U7 history count should increase by 5")
        );


        LocalDateTime testDate = DateUtils.createDateTime("23-10-2026 11:35:00");

        assertThrows(UserProfileNotFoundException.class, () -> {
            browser.visitWeb("USER_UNKNOWN", "https://www.uoc.edu", testDate);
        }, "Should throw UserProfileNotFoundException when the user does not exist");


        browser.visitWeb("U1", "https://www.uoc.edu", testDate);

        assertAll("U1 visits www.uoc.edu",
                () -> assertEquals(initialU1History + 17, helper.numHistoryByUser("U1"), "U1 history count should be initial + 10"),
                () -> assertEquals("https://www.uoc.edu", helper.getLastVisitedWeb("U1"), "Last visited web should be UOC")
        );

        final LocalDateTime date2 = DateUtils.createDateTime("23-10-2026 11:45:00");
        browser.visitWeb("U1", "https://www.wikipedia.org", date2);

        assertAll("U1 visits wikipedia.org",
                () -> assertEquals(initialU1History + 18, helper.numHistoryByUser("U1"), "U1 history count should be initial + 11"),
                () -> assertEquals("https://www.wikipedia.org", helper.getLastVisitedWeb("U1"), "Last visited web should be Wikipedia")
        );


        assertThrows(DNSResolverException.class, () -> {
            browser.visitWeb("U1", "https://UNKNOWN.COM", date2);
        }, "Should throw DNSResolverException when the host does not exist");


    }

    @Test
    @DisplayName("navigationBackAndForwardTest: Should correctly handle back and forward navigation using history stacks")
    public void navigationBackAndForwardTest() throws DSException {

        //  Visit pageA -> pageB -> pageC
        final String pageA = "https://www.uoc.edu";
        final String pageB = "https://www.github.com";
        final String pageC = "https://www.stackoverflow.com";

        CSVDataLoader.addUsers(browser);

        browser.visitWeb("U1", pageA, DateUtils.createDateTime("15-01-2026 10:00:00"));
        browser.visitWeb("U1", pageB, DateUtils.createDateTime("15-01-2026 10:05:00"));
        browser.visitWeb("U1", pageC, DateUtils.createDateTime("15-01-2026 10:10:00"));

        // Current page after visits: pageC (https://www.stackoverflow.com")
        assertEquals(pageC, helper.getCurrentWeb("U1").getUrl(), "Current page should be https://www.stackoverflow.com");

        // Backward navigation (C -> B -> A)
        final WebPage backToB = browser.backWeb("U1");
        assertEquals(pageB, backToB.getUrl(), "Going back from pageC should land on pageB (https://www.github.com)");

        final WebPage backToA = browser.backWeb("U1");
        assertEquals(pageA, backToA.getUrl(), "Going back from B should land on pageA (https://www.uoc.edu)");


        // forward navigation (pageA -> pageB -> pageC)
        final WebPage forwardToB = browser.forwardWeb("U1");
        assertEquals(pageB, forwardToB.getUrl(), "Going forward from A should land on pageB (https://www.github.com)");

        final WebPage forwardToC = browser.forwardWeb("U1");
        assertEquals(pageC, forwardToC.getUrl(), "Going forward from B should land on pageC (https://www.stackoverflow.com)");


        // Visiting a new webpage clears the forward stack
        // Go back (C -> B)
        browser.backWeb("U1"); // pageB (https://www.github.com)
        final String pageD = "https://www.nytimes.com";
        browser.visitWeb("U1", pageD, DateUtils.createDateTime("15-01-2026 10:20:00"));

        assertAll("State after visiting new page D from history point B",
                () -> assertEquals(pageD, helper.getCurrentWeb("U1").getUrl(), "Current page should be D")
        );
    }


    @Test
    @DisplayName("getMostVisitedWebTest: Get most visited web page across all user profiles or throw exception if no visits exist")
    public void getMostVisitedWebTest() throws DSException {

        assertThrows(NoWebPagesFoundException.class, () -> {
            browser.getMostVisitedWeb();
        }, "Should throw NoHistoryFoundException when no web visits exist in any user history");

        CSVDataLoader.addUsers(browser);
        CSVDataLoader.addVisitsWeb(browser);

        final WebPage mostVisitedWeb = browser.getMostVisitedWeb();

        assertNotNull(mostVisitedWeb, "The returned most visited web page should not be null");

        assertAll("Global most visited web page properties",
                () -> assertEquals("https://www.github.com/docs/api/v2", mostVisitedWeb.getUrl(),
                        "URL should match the globally most visited page"),
                () -> assertNotNull(mostVisitedWeb.getUrl(), "Web page title should not be null"),
                () -> assertEquals(10, mostVisitedWeb.getVisits())
        );

        final String newUrl = "https://www.uoc.edu/dataStructuresAreCool";

        browser.visitWeb("U1", newUrl, DateUtils.createDateTime("15-01-2026 09:00:00"));
        browser.visitWeb("U2", newUrl, DateUtils.createDateTime("15-01-2026 10:00:00"));
        browser.visitWeb("U3", newUrl, DateUtils.createDateTime("15-01-2026 11:00:00"));
        browser.visitWeb("U4", newUrl, DateUtils.createDateTime("15-01-2026 12:00:00"));
        browser.visitWeb("U5", newUrl, DateUtils.createDateTime("15-01-2026 13:00:00"));
        browser.visitWeb("U6", newUrl, DateUtils.createDateTime("15-01-2026 14:00:00"));
        browser.visitWeb("U7", newUrl, DateUtils.createDateTime("15-01-2026 15:00:00"));
        browser.visitWeb("U8", newUrl, DateUtils.createDateTime("15-01-2026 16:00:00"));
        browser.visitWeb("U9", newUrl, DateUtils.createDateTime("15-01-2026 17:00:00"));
        browser.visitWeb("U10", newUrl, DateUtils.createDateTime("15-01-2026 18:00:00"));
        browser.visitWeb("U10", newUrl, DateUtils.createDateTime("15-01-2026 19:00:00"));

        final WebPage updatedMostVisitedWeb = browser.getMostVisitedWeb();

        assertNotNull(updatedMostVisitedWeb, "The updated most visited web page should not be null");


        assertAll("Global most visited web page properties after new visits",
                () -> assertEquals(newUrl, updatedMostVisitedWeb.getUrl(),
                        "URL should now match the newly introduced most visited page"),
                () -> assertNotNull(updatedMostVisitedWeb.getUrl(), "Web page URL should not be null"),
                () -> assertEquals(11, updatedMostVisitedWeb.getVisits(), "Updated visit count should equal 9")
        );
    }

    @Test
    @DisplayName("getMostVisitedWebByUserTest: Get most visited web page by user or throw exception if user or history does not exist")
    public void getMostVisitedWebByUserTest() throws DSException {

        assertThrows(UserProfileNotFoundException.class, () -> {
            browser.getMostVisitedWebByUser("USER_UNKNOWN");
        }, "Should throw UserProfileNotFoundException when the user does not exist");

        browser.addUserProfile("USER_NO_HISTORY", "No History User", "nohistory", "12345");

        assertThrows(NoWebPagesFoundException.class, () -> {
            browser.getMostVisitedWebByUser("USER_NO_HISTORY");
        }, "Should throw NoHistoryFoundException when the user has no history or visited web pages");

        CSVDataLoader.addVisitsWeb(browser);

        final WebPage mostVisitedWeb = browser.getMostVisitedWebByUser("U1");

        assertNotNull(mostVisitedWeb, "The returned most visited web page should not be null");

        assertAll("Most visited web page properties for U1",
                () -> assertEquals("https://www.github.com/docs/api/v2", mostVisitedWeb.getUrl(), "URL does not match the most visited page"),
                () -> assertNotNull(mostVisitedWeb.getUrl(), "Web page title should not be null")
        );
    }


    @Test
    @DisplayName("getHistoryWebsByUserTest: Get history webs by user or throw exception if user or history does not exist")
    public void getHistoryWebsByUserTest() throws DSException {

        assertThrows(UserProfileNotFoundException.class, () -> {
            browser.getHistoryWebsByUser("USER_UNKNOWN");
        }, "Should throw UserProfileNotFoundException when the user does not exist");

        browser.addUserProfile("USER_NO_HISTORY", "No History User", "nohistory", "12345");

        assertThrows(NoHistoryFoundException.class, () -> {
            browser.getHistoryWebsByUser("USER_NO_HISTORY");
        }, "Should throw NoHistoryFoundException when the user has no history");

        CSVDataLoader.addVisitsWeb(browser);

        final Iterator<HistoryWebPage> historyIterator = browser.getHistoryWebsByUser("U1");

        assertNotNull(historyIterator, "The returned iterator should not be null");
        assertTrue(historyIterator.hasNext(), "The iterator should contain history items for U1");

        final String[][] expectedU1History = {
                {"https://www.github.com/docs/api/v2", "01-01-2026 10:15:00"},
                {"https://www.github.com/settings/privacy", "01-01-2026 11:27:00"},
                {"https://www.github.com/docs/api/v2", "04-01-2026 11:30:00"},
                {"https://www.github.com/docs/api/v2", "05-01-2026 14:20:00"},
                {"https://www.nytimes.com/explore/popular", "06-01-2026 00:56:00"},
                {"https://www.nytimes.com/docs/api/v2", "06-01-2026 13:53:00"},
                {"https://www.github.com/docs/api/v2", "06-01-2026 18:10:00"},
                {"https://www.github.com/docs/api/v2", "07-01-2026 10:15:00"},
                {"https://www.amazon.com/dashboard", "08-01-2026 00:07:00"},
                {"https://www.wikipedia.org/news/tech", "08-01-2026 20:11:00"},
                {"https://www.github.com/docs/api/v2", "09-01-2026 14:15:00"},
                {"https://www.uoc.edu", "09-01-2026 21:04:00"},
                {"https://www.bbc.com/user/profile", "09-01-2026 23:36:00"},
                {"https://www.linkedin.com/settings/privacy", "12-01-2026 03:12:00"},
                {"https://www.reddit.com/forum/questions/1029", "12-01-2026 22:24:00"},
                {"https://www.github.com/docs/api/v2", "13-01-2026 06:15:00"}
        };
        int visitedWebsCount = 0;

        for (final String[] expected : expectedU1History) {
            assertTrue(historyIterator.hasNext(), "The iterator should contain more history items");

            final HistoryWebPage web = historyIterator.next();
            assertNotNull(web, "The visited web item should not be null");

            final String expectedUrl = expected[0];
            final LocalDateTime expectedDate = DateUtils.createDateTime(expected[1]);

            assertAll("History item properties for " + expectedUrl,
                    () -> assertEquals(expectedUrl, web.getUrl(), "URL does not match"),
                    () -> assertEquals(expectedDate, web.getDateTime(), "Timestamp does not match")
            );

            visitedWebsCount++;
        }
        final int expectedCount = helper.numHistoryByUser("U1");
        assertEquals(expectedCount, visitedWebsCount, "Total iterated history items should match the user history count");
    }



    @Test
    @DisplayName("getAllBookmarksByUserTest: Get all bookmarks by user or throw exception if user or bookmarks do not exist")
    public void getAllBookmarksByUserTest() throws DSException {

        assertThrows(UserProfileNotFoundException.class, () -> {
            browser.getAllBookmarksByUser("USER_UNKNOWN");
        }, "Should throw userProfileNotFoundException when the user does not exist");

        browser.addUserProfile("USER_NO_BOOKMARKS", "No Bookmarks User", "nobookmarks", "12345");

        assertThrows(NoBookmarksFoundException.class, () -> {
            browser.getAllBookmarksByUser("USER_NO_BOOKMARKS");
        }, "Should throw NoBookmarksFoundException when the user has no bookmarks");

        assertEquals(6, helper.numBookmarksByUser("U1"));

        final Iterator<Bookmark> bookmarksIterator = browser.getAllBookmarksByUser("U1");

        final String[][] expectedU1Bookmarks = {
                {"Paris Guide", "https://www.paris.fr", "/Travel/Europe"},
                {"Visit London", "https://www.london.gov.uk", "/Travel/Europe"},
                {"Rome Tourism", "https://www.turismoroma.it", "/Travel/Europe/Italy"},
                {"Berlin Official", "https://www.berlin.de", "/Travel/Europe/Germany"},
                {"Madrid Destino", "https://www.esmadrid.com", "/Travel/Europe/Spain"},
                {"Barcelona Turisme", "https://www.barcelonaturisme.com", "/Travel/Europe/Spain"}
        };

        for (final String[] expected : expectedU1Bookmarks) {
            assertTrue(bookmarksIterator.hasNext(), "The iterator should contain more bookmarks");

            final Bookmark bookmark = bookmarksIterator.next();
            final String title = expected[0];
            final String url = expected[1];
            final String path = expected[2];

            assertAll("Bookmark properties for " + title,
                    () -> assertEquals(title, bookmark.getTitle(), "Title does not match"),
                    () -> assertEquals(url, bookmark.getUrl(), "URL does not match"),
                    () -> assertEquals(path, bookmark.getPath(), "Path does not match")
            );
        }
    }

    @Test
    @DisplayName("getTotalUsersAccountsTest: Get the total count of registered user accounts before and after modifications")
    public void getTotalUsersAccountsTest() throws DSException {

        final int initialUsersCount = browser.getTotalUsersAccounts();

        assertEquals(65, initialUsersCount);

        browser.addUserProfile("USER_TEST_TOTAL", "Test User Total", "usertesttotal", "password123");

        final int countAfterAdd = browser.getTotalUsersAccounts();

        assertAll("Total users count after adding a new user account",
                () -> assertEquals(initialUsersCount + 1, countAfterAdd,
                        "Total users count should increase by 1 after adding an account"),
                () -> assertNotNull(helper.getUserProfile("USER_TEST_TOTAL"),
                        "The newly created user account should exist")
        );
    }
}