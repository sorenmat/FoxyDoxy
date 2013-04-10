package com.scalaprog;
/**
 * User: soren
 */
/**
 * @Documentation
 *
 * # This is simple piece of documentation
 *
 * This is so more cool than normal java doc.
 *
 * @section Configuration
 * @tags test, demo, simple
 */
public class JavaTestFile {


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
