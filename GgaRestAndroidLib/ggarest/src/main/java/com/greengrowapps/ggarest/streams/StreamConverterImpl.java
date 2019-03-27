package com.greengrowapps.ggarest.streams;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;

public class StreamConverterImpl implements StreamConverter{

    private final String encoding;

    public StreamConverterImpl(String encoding){
        this.encoding = encoding;
    }

    @Override
    public void writeToOutputStream(String content, OutputStream outputStream, boolean useGzip) throws IOException {

        if (useGzip) {

            // byte[] toSend = gzip(content.getBytes());
           // outputStream.write(toSend);
            OutputStreamWriter out = new OutputStreamWriter(new GZIPOutputStream(outputStream));
            out.write(content);
            out.close();
        } else {
            OutputStreamWriter out = new OutputStreamWriter(outputStream);
            out.write(content);
            out.close();
        }
    }

    @Override
    public String readFromInputStream(InputStream inputStream) throws IOException {

        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        byte[] buffer= new byte[1024];
        int nread;
        while ( (nread = inputStream.read(buffer)) >0 )  {
            outBytes.write(buffer, 0, nread);
        }
        return new String(outBytes.toByteArray() , encoding);

    }

    @Override
    public int getContentLength(String stringBody, boolean useGzip) {

        if(useGzip) {
            byte[] toSend = gzip(stringBody.getBytes());
            return toSend.length;
        }else{
            return stringBody.getBytes().length;
        }
    }


    static byte[] gzip(byte[] input) {
        GZIPOutputStream gzipOS = null;
        try {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            gzipOS = new GZIPOutputStream(byteArrayOS);
            gzipOS.write(input);
            gzipOS.flush();
            gzipOS.close();
            gzipOS = null;
            return byteArrayOS.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e); // <-- just a RuntimeException
        } finally {
            if (gzipOS != null) {
                try { gzipOS.close(); } catch (Exception ignored) {}
            }
        }
    }
}
