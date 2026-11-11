package uoc.ds.pr.util;

import uoc.ds.pr.exceptions.DSException;
import uoc.ds.pr.pr2.Browser;

public class CSVDataLoader {

    public static void addUsers(Browser browser) throws DSException {
        CSVUtil.addDataSource("/user-accounts.csv",
                new String[]{"userId", "name", "user", "pass"},
                browser,
                (b, v) -> b.addUserProfile(v[0], v[1], v[2], v[3])
        );
    }

    public static void addBookmarks(Browser browser) throws DSException {
        CSVUtil.addDataSource("/bookmarks.csv",
                new String[]{"userId", "title", "url", "path"},
                browser,
                (b, v) -> b.addBookmark(v[0], v[1], v[2], v[3])
        );
    }

    public static void addVisitsWeb(Browser browser) throws DSException {
        CSVUtil.addDataSource("/web_visits.csv",
                new String[]{"userId", "url", "dateTime"},
                browser,
                (b, v) -> {
                    try {
                        b.visitWeb(v[0], v[1], DateUtils.createDateTime(v[2]));
                    } catch (DSException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    public static void addDNSEntries(Browser browser) throws DSException  {
        CSVUtil.addDataSource("/dns_entries.csv",
                new String[]{"host", "ip"},
                browser,
                (b, v) -> {
                        b.getHelper().addDNSEntry(v[0], v[1]);
                }
        );
    }
}