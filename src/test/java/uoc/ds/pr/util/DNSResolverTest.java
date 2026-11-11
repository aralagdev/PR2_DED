package uoc.ds.pr.util;
import edu.uoc.ds.adt.sequential.Set;
import edu.uoc.ds.adt.sequential.SetLinkedListImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uoc.ds.pr.model.DNSEntry;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DNSResolver Unit Tests")
class DNSResolverTest {

    private DNSResolver dnsResolver;

    @BeforeEach
    void setUp() {
        dnsResolver = new DNSResolver();
    }

    @Test
    @DisplayName("Should return null when resolving host in empty DNSResolver")
    void testGetDNSEntryEmpty() {
        assertNull(dnsResolver.getDNSEntry("www.uoc.edu"));
    }

    @Test
    @DisplayName("Should add a single DNSEntry and resolve it correctly")
    void testAddAndGetDNSEntry() {
        DNSEntry entry = new DNSEntry("www.uoc.edu", "212.170.241.130");
        dnsResolver.add(entry);

        DNSEntry result = dnsResolver.getDNSEntry("www.uoc.edu");

        assertNotNull(result);
        assertEquals("www.uoc.edu", result.getHost());
        assertEquals("212.170.241.130", result.getIp());
    }

    @Test
    @DisplayName("Should return null when host is not present in DNSResolver")
    void testGetDNSEntryNotFound() {
        dnsResolver.add(new DNSEntry("www.uoc.edu", "212.170.241.130"));
        dnsResolver.add(new DNSEntry("www.github.com", "140.82.121.4"));

        assertNull(dnsResolver.getDNSEntry("www.google.com"));
    }

    @Test
    @DisplayName("Should add multiple DNSEntries using addAllDNSEntry and resolve them all")
    void testAddAllDNSEntry() {
        Set<DNSEntry> newEntries = new SetLinkedListImpl<>();
        newEntries.add(new DNSEntry("www.bbc.com", "151.101.0.81"));
        newEntries.add(new DNSEntry("www.reddit.com", "151.101.1.140"));

        dnsResolver.addAllDNSEntry(newEntries);

        assertAll("Verify entries added via union",
                () -> assertNotNull(dnsResolver.getDNSEntry("www.bbc.com")),
                () -> assertEquals("151.101.0.81", dnsResolver.getDNSEntry("www.bbc.com").getIp()),
                () -> assertNotNull(dnsResolver.getDNSEntry("www.reddit.com")),
                () -> assertEquals("151.101.1.140", dnsResolver.getDNSEntry("www.reddit.com").getIp())
        );
    }

    @Test
    @DisplayName("Should correctly extract host from various URL formats including http and https")
    void testExtractHost() {
        assertAll("Host extraction scenarios",
                // HTTP i HTTPS
                () -> assertEquals("www.bbc.com", DNSResolver.extractHost("http://www.bbc.com/news/tech")),
                () -> assertEquals("www.bbc.com", DNSResolver.extractHost("https://www.bbc.com/news/tech")),
                () -> assertEquals("www.github.com", DNSResolver.extractHost("www.github.com/docs/api/v2")),
                () -> assertEquals("www.nytimes.com", DNSResolver.extractHost("https://www.nytimes.com/search?q=ds_exception")),
                () -> assertEquals("www.uoc.edu", DNSResolver.extractHost("https://www.uoc.edu")),
                () -> assertEquals("www.reddit.com", DNSResolver.extractHost("http://www.reddit.com/")),
                () -> assertNull(DNSResolver.extractHost(null)),
                () -> assertNull(DNSResolver.extractHost(""))
        );
    }
}