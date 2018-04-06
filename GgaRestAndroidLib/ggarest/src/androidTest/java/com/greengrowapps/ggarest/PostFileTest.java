package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class PostFileTest extends GgaRestTest {
    private static final String url="http://httpbin.org/post";

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EchoPostResponse{
        public String data;
        public String json;
        public Headers headers;
        public Files files;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Files{
        public String file;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Headers{

        @JsonProperty("Content-Length")
        public String contentLenth;
        public String Enctype;
        public String File;


    }

    public void testPostSmallFile() throws IOException {
        File f=prepareFile(5,10);
        uploadFile(f,"smallFile.txt");
    }

    public void testBigFile() throws IOException {
        File f=prepareFile(1024,1024);
        uploadFile(f,"bigFile.txt");
    }

    private void uploadFile(final File f, final String filename) throws IOException {
        final String fileContent=getFileContent(f);

        getWebserviceInstance()
                .post(url)
                .withFileBody(f,filename)
                .onSuccess(EchoPostResponse.class, new OnObjResponseListener<EchoPostResponse>() {
                    @Override
                    public void onResponse(int code, final EchoPostResponse message, final Response fullResponse) {
                        assertNotNull(message);
                        assertNotNull(message.headers.contentLenth);
                        int len= 0;
                        try {
                            len = Integer.parseInt(message.headers.contentLenth);
                        } catch (NumberFormatException e) {
                            fail("Content length not set correctly");
                        }
                        assertTrue(len > 0);
                        assertEquals(message.headers.Enctype,"multipart/form-data");
                        assertEquals(message.headers.File,filename);
                        assertEquals(message.files.file,fileContent);
                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        fail();
                    }
                })
                .withTimeout(60*1000*60)
                .executeAndWait();
    }

    private String getFileContent(File f) throws IOException {
        FileInputStream inputStream=new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\r\n");
        }
        reader.close();
        return sb.toString();    }

    private File prepareFile(int lines,int lineLen) throws IOException {
        File f = new File(getContext().getCacheDir() + "/sampleFile_"+lines+"_"+lineLen+".txt");
        if (f.exists()) {
            return f;
        }

        FileOutputStream fos = new FileOutputStream(f);
        for(int i=0; i<lines;i++){
            fos.write(random(lineLen).getBytes());
            fos.write("\r\n".getBytes());
        }
        fos.close();

        return f;
    }

}
