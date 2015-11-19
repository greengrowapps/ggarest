package com.greengrowapps.ggarest.streams;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StreamConverterImpl implements StreamConverter{

    private final String encoding;

    public StreamConverterImpl(String encoding){
        this.encoding = encoding;
    }

    @Override
    public void writeToOutputStream(String content, OutputStream outputStream) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter( outputStream );
        out.write(content);
        out.close();
    }

    @Override
    public String readFromInputStream(InputStream inputStream) throws IOException {

        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        byte[] buffer= new byte[1024];
        int nread=0;
        while ( (nread = inputStream.read(buffer)) >0 )  {
            outBytes.write(buffer, 0, nread);
        }
        return new String(outBytes.toByteArray() , encoding);

    }
}
