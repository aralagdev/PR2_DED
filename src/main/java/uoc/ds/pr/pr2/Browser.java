package uoc.ds.pr.pr2;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Bookmark;
import uoc.ds.pr.model.HistoryWebPage;
import uoc.ds.pr.model.WebPage;

import java.time.LocalDateTime;

/**
 * Defines the operations supported by the browser simulator.
 *
 * <p>The browser manages user profiles, bookmarks, configuration options,
 * extensions, browsing history and per-user navigation. Visiting a page
 * requires its host to be present in the browser's DNS table.</p>
 */
public interface Browser {

    /** Maximum number of user profiles supported by the implementation. */
    public static final int MAX_USERS_ACCOUNTS = 90;

    /** Maximum number of configuration options supported per user profile. */
    public static final int MAX_CONFIGURATIONS = 200;


    /**
     * Adds a user profile, or updates the details of an existing profile.
     *
     * @param userId unique identifier of the profile
     * @param name user's display name
     * @param user username or account login name
     * @param pass password or password representation stored for the account
     */
    void addUserProfile(String userId, String name, String user, String pass);

    /**
     * Adds a bookmark to a user's profile, or updates the title and folder of
     * the bookmark if that URL is already saved.
     *
     * @param userId unique identifier of the profile
     * @param title bookmark title
     * @param url URL to bookmark; also identifies an existing bookmark
     * @param path folder path where the bookmark is saved
     * @throws UserProfileNotFoundException if the profile does not exist
     */
    void addBookmark(String userId, String title, String url, String path) throws UserProfileNotFoundException;

    /**
     * Adds or updates a configuration option for a user's profile.
     *
     * @param coId unique identifier of the configuration option
     * @param userId unique identifier of the profile
     * @param property name of the configuration property
     * @param value value to assign to the property
     * @throws UserProfileNotFoundException if the profile does not exist
     */
    void editConfigurationOption(String coId, String userId, String property, String value) throws UserProfileNotFoundException;

    /**
     * Adds or updates an extension and associates it with a user's profile.
     *
     * @param extensionId unique identifier of the extension
     * @param userId unique identifier of the profile
     * @param name extension name
     * @param packagePath file system or package path of the extension
     * @param version extension version
     * @throws UserProfileNotFoundException if the profile does not exist
     * @throws ExtensionNotFoundException if the extension cannot be found
     */
    void addExtension(String extensionId, String userId, String name, String packagePath, String version) throws UserProfileNotFoundException, ExtensionNotFoundException;

    /**
     * Adds a comment to an existing extension on behalf of a user.
     *
     * @param extensionId unique identifier of the extension
     * @param userId unique identifier of the profile authoring the comment
     * @param comment comment text
     * @throws ExtensionNotFoundException if the extension does not exist
     * @throws UserProfileNotFoundException if the profile does not exist
     */
    void addCommentToExtension(String extensionId, String userId, String comment) throws ExtensionNotFoundException, UserProfileNotFoundException;

    /**
     * Visits a web page and records the visit in the user's history and
     * navigation history. The page's host must have a DNS entry.
     *
     * @param userId unique identifier of the profile
     * @param url URL of the page to visit
     * @param dateTime date and time of the visit
     * @throws UserProfileNotFoundException if the profile does not exist
     * @throws DNSResolverException if no DNS entry is registered for the URL's host
     */
    void visitWeb(String userId, String url, LocalDateTime dateTime) throws UserProfileNotFoundException, DNSResolverException;

    /**
     * Moves the user's navigation history back by one page.
     *
     * @param userId unique identifier of the profile
     * @return the previous page, or {@code null} if there is no page to go back to
     * @throws UserProfileNotFoundException if the profile does not exist
     */
    WebPage backWeb(String userId) throws UserProfileNotFoundException;

    /**
     * Moves the user's navigation history forward by one page.
     *
     * @param userId unique identifier of the profile
     * @return the next page, or {@code null} if there is no page to go forward to
     * @throws UserProfileNotFoundException if the profile does not exist
     */
    WebPage forwardWeb(String userId) throws UserProfileNotFoundException;

    /**
     * Returns the page with the greatest visit count across all profiles.
     *
     * @return the most visited page
     * @throws NoWebPagesFoundException if no page has been visited
     */
    WebPage getMostVisitedWeb() throws NoWebPagesFoundException;

    /**
     * Returns the page with the greatest visit count for a user's profile.
     *
     * @param userId unique identifier of the profile
     * @return the user's most visited page
     * @throws UserProfileNotFoundException if the profile does not exist
     * @throws NoWebPagesFoundException if the user has not visited any page
     */
    WebPage getMostVisitedWebByUser(String userId) throws UserProfileNotFoundException, NoWebPagesFoundException;

    /**
     * Returns an iterator over the user's browsing history, in visit order.
     *
     * @param userId unique identifier of the profile
     * @return an iterator over the user's {@link HistoryWebPage} records
     * @throws UserProfileNotFoundException if the profile does not exist
     * @throws NoHistoryFoundException if the user has no recorded visits
     */
    Iterator<HistoryWebPage> getHistoryWebsByUser(String userId) throws UserProfileNotFoundException, NoHistoryFoundException;

    /**
     * Returns an iterator over all bookmarks saved by a user.
     *
     * @param userId unique identifier of the profile
     * @return an iterator over the user's bookmarks
     * @throws UserProfileNotFoundException if the profile does not exist
     * @throws NoBookmarksFoundException if the user has no saved bookmarks
     */
    Iterator<Bookmark> getAllBookmarksByUser(String userId) throws UserProfileNotFoundException, NoBookmarksFoundException;

    /**
     * Returns the number of registered user profiles.
     *
     * @return total number of user profiles
     */
    int getTotalUsersAccounts();

    /**
     * Returns a helper for read-only inspection of browser data and for
     * registering DNS entries.
     *
     * @return a helper associated with this browser
     */
    BrowserHelper getHelper();
}