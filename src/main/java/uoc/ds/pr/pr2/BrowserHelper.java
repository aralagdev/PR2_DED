package uoc.ds.pr.pr2;

import uoc.ds.pr.model.*;

/**
 * Provides inspection and DNS configuration operations for a {@link Browser}.
 *
 * <p>Lookup methods that return model objects return {@code null} when the
 * requested object is unavailable. The per-user count methods expect an
 * existing user profile.</p>
 */
public interface BrowserHelper {

    /**
     * Finds a user profile by its identifier.
     *
     * @param id unique profile identifier
     * @return the profile, or {@code null} if it does not exist
     */
    UserProfile getUserProfile(String id);

    /**
     * Returns the number of registered user profiles.
     *
     * @return number of profiles
     */
    int numUserProfiles();

    /**
     * Returns the number of bookmarks saved by a user.
     *
     * @param id unique identifier of an existing profile
     * @return number of bookmarks
     */
    int numBookmarksByUser(String id);

    /**
     * Finds a bookmark by its URL in the specified user's bookmarks.
     *
     * @param id unique identifier of an existing profile
     * @param url URL identifying the bookmark
     * @return the bookmark, or {@code null} if it is not saved
     */
    Bookmark getBookmark(String id, String url);

    /**
     * Finds a configuration option belonging to a user.
     *
     * @param userId unique identifier of an existing profile
     * @param co configuration option identifier
     * @return the option, or {@code null} if it does not exist
     */
    ConfigurationOption getConfigurationOption(String userId, String co);

    /**
     * Returns the number of configuration options belonging to a user.
     *
     * @param userId unique identifier of an existing profile
     * @return number of configuration options
     */
    int numConfigurationOptionsByUser(String userId);

    /**
     * Returns the number of extensions registered by the browser.
     *
     * @return total number of extensions
     */
    int numExtensions();

    /**
     * Returns the number of extensions associated with a user.
     *
     * @param userId unique identifier of an existing profile
     * @return number of extensions associated with the profile
     */
    int numExtensionsByUser(String userId);

    /**
     * Finds an extension by its identifier.
     *
     * @param extensionId unique extension identifier
     * @return the extension, or {@code null} if it does not exist
     */
    Extension getExtension(String extensionId);

    /**
     * Returns the number of comments associated with an extension.
     *
     * @param extensionId extension identifier
     * @return number of comments, or {@code 0} if the extension does not exist
     */
    int numCommentsByExtension(String extensionId);

    /**
     * Returns the number of visits recorded in a user's history.
     *
     * @param userId unique profile identifier
     * @return number of history records, or {@code 0} if the profile does not exist
     */
    int numHistoryByUser(String userId);

    /**
     * Returns the URL of the last page recorded in a user's visit history.
     *
     * @param userId unique profile identifier
     * @return last visited URL, or {@code null} if the profile has no visits
     */
    String getLastVisitedWeb(String userId);

    /**
     * Returns the page currently selected in a user's back/forward navigation.
     *
     * @param userId unique profile identifier
     * @return current page, or {@code null} if the profile does not exist or
     *         has no current page
     */
    WebPage getCurrentWeb(String userId);

    /**
     * Registers a DNS entry used to resolve page hosts during visits.
     *
     * @param host host name to register
     * @param ip IP address associated with the host
     */
    void addDNSEntry(String host, String ip);

    /**
     * Returns the number of DNS entries registered with the browser.
     *
     * @return number of DNS entries
     */
    int numDNSEntries();
}
