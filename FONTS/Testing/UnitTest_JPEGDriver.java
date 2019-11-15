import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import UnitTest_JPEG.UnitTest;

public class UnitTest_JPEGDriver {
        public static void main(String args[]) {
            Result result = JUnitCore.runClasses(UnitTest.class);
            
            for (Failure failure : result.getFailures()) {
                System.out.printf("\n\n\n%s\n", failure.toString());
                System.out.printf("\n%s\n", failure.getTrace());
            }
            System.out.printf("\n\nTests Run: %d   Tests Failed: %d   Tests Ignored: %d\n", result.getRunCount(), result.getFailureCount(), result.getIgnoreCount());
            if (result.wasSuccessful()) {
                System.out.println("ALL TESTS PASSED!");
            }
            else {
                System.out.println("FAIL!");
            }
        }
}