package it.unipd.dei.webapp.utils;

/**
 * Class with some static useful methods.
 *
 * @author BookStudy Group
 * @version 1.0
 * @since 1.0
 */
public class UtilMethods {

    /**
     * Compute an alphanumeric code given the length.
     *
     * @param length the length of the string to generate.
     * @return the random string generated.
     */
    public static String computeAlphanumericCode(int length){
        String ret = "";
        char[] lists = new char[26+26+10];
        for(int i=0; i < 26;i++){
            lists[i] = (char) (65+i);
            lists[i+26] = (char) (97+i);
        }
        for(int i=0; i < 10;i++){
            lists[52+i] = (char) (48+i);
        }
        for(int i=0; i < length;i++){
            ret+=lists[(int)(Math.random()*62)];
        }
        return ret;
    }
}
