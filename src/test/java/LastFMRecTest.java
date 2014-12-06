import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.mycompany.lastfmgetrecs.engine.LastFMRec;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

public class LastFMRecTest {

    final static Logger logger = LoggerFactory.getLogger(LastFMRecTest.class);

    static LastFMRec instance = null;
    static Date startDate = null;

    public LastFMRecTest() {
        logger.debug("LastFMRecTest()");
    }

    @BeforeClass
    public static void setUpClass() {
        System.out.println("setUpClass");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("tearDownClass");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }

    @Before
    public void setUp() {
        System.out.println("setUp");
        instance = new LastFMRec().initLimits(3, 3, 5);
        startDate = new Date();
    }

    @After
    public void tearDown() {
        System.out.println("tearDown\n"
                + "Done.\n"
                + "start:" + new SimpleDateFormat("hh:mm:ss").format(startDate) + "\n"
                + "stop:" + new SimpleDateFormat("hh:mm:ss").format(new Date()));
    }

    @Test
    public void doTest1() throws Exception {
        System.out.println("doTest1");
        instance.test("vnv192");
    }

    @Test
    public void doTest2() throws Exception {
        System.out.println("doTest2");
        try {
            instance.test((String) null);
            fail("Exception excepted");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void doTest3() throws Exception {
        System.out.println("doTest3");
        try {
            instance.test("sfdsdaaaaafsdfde");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
