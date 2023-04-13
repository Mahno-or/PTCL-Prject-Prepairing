package com.evampsaanga.FilterService.filterClasses;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Muhammad Fazeel
 *
 *         we are using MyCustomHttpRequestWrapper class to Manipulate the
 *         Request and Set the request as required
 */

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class MyCustomHttpRequestWrapper extends HttpServletRequestWrapper {
	
    private static final Logger logger = LoggerFactory.getLogger(MyCustomHttpRequestWrapper.class);

    public final String body;
    public final JSONObject preBody;

    public MyCustomHttpRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        
        /**
		 * Reading HttpServletRequest and Mapping this Request into Readable form
		 */

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try(InputStream inputStream = request.getInputStream()){
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}

			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			logger.info("Exception Caught !" + ex.getMessage());
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					logger.info("Exception Caught !" + ex.getMessage());
				}
			}
		}
        /**
		 * End of Reading Servlet Request
		 * 
		 * Now Parsing Request Setting Actual Request is PreBody param and parsed
		 * request in body param
		 */
        preBody = new JSONObject(stringBuilder.toString());
        body = preBody.getString("requestString");

    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

				/**
				 * this Block is EMpty will work in this later
				 */
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

}