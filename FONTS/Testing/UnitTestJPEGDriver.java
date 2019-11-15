import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import Compressor.UnitTest;

public class UnitTestJPEGDriver {
        public static void main(String args[]) {
            JUnitCore core = new JUnitCore();
            CustomTestListener cl = new CustomTestListener();
            core.addListener(cl);
            core.run(UnitTest.class);
            System.out.printf("\n\nTests Run: %d   Tests Failed: %d   Tests Ignored: %d\n", cl.Started, cl.Failed, cl.Ignored);
        }
}