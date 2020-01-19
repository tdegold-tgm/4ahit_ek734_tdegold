import com.sun.jndi.toolkit.dir.SearchFilter;

import javax.naming.*;
import javax.naming.directory.*;
import java.util.Hashtable;

/**
 * Connect and bind to LDAP-Server and perform queries
 * @author Tim Degold, 4AHIT
 * @version 19.1.2020
 * Übung: MICT | EK734
 */
public class LDAP_Test {

    // needed to connect to ldap
    static final String FACOTY = "com.sun.jndi.ldap.LdapCtxFactory";
    static final String LDAP_SERVER ="ldap://192.168.242.159";                  // ldap://<ip of server>
    static final String AUTHENTIFICATION_METHOD ="simple";                      // simple, ssl or sasl
    static final String LDAP_ROOT_USER = "cn=admin,dc=syt,dc=tgm,dc=ac,dc=at";  // for simple: dn of ldap root user
    static final String LDAP_ROOT_USER_PASSWORD = "root";                       // for simple: credentials of root user

    public static void main(String[] args) {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, FACOTY);
        env.put(Context.PROVIDER_URL, LDAP_SERVER);
        env.put(Context.SECURITY_AUTHENTICATION, AUTHENTIFICATION_METHOD);
        env.put(Context.SECURITY_PRINCIPAL, LDAP_ROOT_USER);
        env.put(Context.SECURITY_CREDENTIALS, LDAP_ROOT_USER_PASSWORD);

        // try to connect to server
        DirContext ctx;
        try {
            ctx = new InitialDirContext(env);
            System.out.println("Connected successfully to "+LDAP_SERVER);

            // do something with ldap
            String search_base = "ou=People,dc=syt,dc=tgm,dc=ac,dc=at";
            String filter = "(&(uidNumber=10003))";
            SearchControls sc = new SearchControls();
	    // get search result
            NamingEnumeration results = ctx.search(search_base, filter, sc);
	    // process result
            while (results.hasMore()) {
                SearchResult sr = (SearchResult) results.next();
                Attributes attrs = sr.getAttributes();
                Attribute attr = attrs.get("givenName");
                System.out.println(attr.get());
                attr = attrs.get("sn");
                System.out.println(attr.get());
            }

            ctx.close();

        } catch (NamingException e) { e.printStackTrace(); }

    }
}
