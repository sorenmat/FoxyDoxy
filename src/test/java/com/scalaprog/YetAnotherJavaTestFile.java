package com.scalaprog;
/**
 * User: soren
 */
/**
 * @Documentation
 *
 * # This is yet another simple piece of documentation
 *
 * Still cooler than java doc...
 *
 * @section Startup
 * @tags test, demo, simple
 */
public class YetAnotherJavaTestFile {


    /*
     * This is a simple test of java doc...
     * @documentation
     */
    /**
     * this is not
     * @param name
     */
    @Documentation(text="This is a test\n" +
            "This tooo")
    public void test(String name) {

    }
}
