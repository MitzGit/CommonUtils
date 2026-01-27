package com.zmpc.common;

public class NotNull {

    /* Example:

    Exception in thread "main" java.lang.NullPointerException:
     - notNull[2] failed:  <MathService:8> at :  calcSum()

      -> doSomeStuff()   <CheckNotNullApp:16>
         -> calcSum()
           -> notNull([2])
    */
    public static void notNull(Object... objects) {
        int index = 0;
        String nullIndexes = "";
        for (Object obj : objects) {
            index++;
            if (obj == null) {
                nullIndexes += (nullIndexes.isEmpty() ? "" : ", ") + index;

                //errorMsg += "\n - notNull failed:  argument[" + index + "] is null";
            }
        }

        if (nullIndexes.isEmpty()) return;


        StackTraceElement[] traceElements = (new NullPointerException()).getStackTrace();

        nullIndexes = "[" + nullIndexes + "]";

        String errorMsg = "\n - notNull" + nullIndexes + " failed:  "
                //+ "argument" + nullIndexes + " is null"
                + "<" + getShortClassName(traceElements[1].getClassName()) + ":"
                + traceElements[1].getLineNumber() + "> at :  "
                + traceElements[1].getMethodName() + "()";

        String fromMethodToCall = "\n  -> "
                + traceElements[2].getMethodName() + "()   "
                + "<" + getShortClassName(traceElements[2].getClassName())
                + ":" + traceElements[2].getLineNumber()
                + ">";

        String methodToCall = "\n     -> "
                // + getShortClassName(traceElements[1].getClassName()) + "."
                + traceElements[1].getMethodName() + "()";

        String notNullMethod = "\n       -> "
                + traceElements[0].getMethodName() + "(" + nullIndexes + ")";

        errorMsg += "\n" + fromMethodToCall + methodToCall + notNullMethod + "\n";

        throw new NullPointerException(errorMsg);

    }

    private static String getShortClassName(String className) {
        int index = className.lastIndexOf('.');
        return className.substring(index + 1);
    }
}
