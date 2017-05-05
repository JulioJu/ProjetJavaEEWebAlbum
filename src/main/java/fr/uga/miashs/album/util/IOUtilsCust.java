package fr.uga.miashs.album.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// From
// http://grepcode.com/file/repo1.maven.org/maven2/commons-io/commons-io/2.4/org/apache/commons/io/IOUtils.java#IOUtils.copyLarge(java.io.InputStream,java.io.OutputStream,byte[])
public class IOUtilsCust {
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    public static long  copyLarge(InputStream input, OutputStream output, byte[] buffer)
        throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    public static long  copyLarge(InputStream input, OutputStream output)
        throws IOException {
        return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }
    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }
}
