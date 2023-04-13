package com.evampsaanga.FilterService.filterClasses;

import com.evampsaanga.FilterService.dto.ResponseDto;
import com.evampsaanga.FilterService.utils.Constants;
import com.evampsaanga.FilterService.utils.SecureUtils;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

@Component
public class GenericFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(GenericFilter.class);

	String sha256hex = "";
	String timeStamp = "";
	String base = "";
	String reqConfig = "";

	/* @Autowired */
	ResponseDto responseDto = new ResponseDto();

	@SneakyThrows
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long startTime = System.currentTimeMillis();
		MyCustomHttpRequestWrapper requestWrapper = new MyCustomHttpRequestWrapper((HttpServletRequest) request);

		// Read Request Body
		JSONObject requestBody = requestWrapper.preBody;

		// Read Request String
		String reqString = requestWrapper.body;

		logger.info("Inside My Filter Pre Handle URI: {}, Method: {}, Body: {}", requestWrapper.getRequestURI(),
				requestWrapper.getMethod(), requestBody);

		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setContentType(Constants.CONTENT_TYPE_STRING);
		// SHA-256 Req Hashing starts here

		/**
		 * Validating the SHA256 with Request
		 * 
		 * in IF Block checking the SHA received in request is valid or not
		 * 
		 * if the SHA is valid then Response will build with success scenario
		 */

		timeStamp = requestBody.getString(Constants.TIMESTAMP_STRING);
		base = reqString + Constants.SALT_STRING + timeStamp;
		sha256hex = SecureUtils.sha256(base);
		logger.info("sha256hex is: {}", sha256hex);

		// Setting timestamp in response body
		Calendar calendar = Calendar.getInstance();
		String timestamp = String.valueOf(calendar.getTimeInMillis());
		responseDto.setTimeStamp(timestamp);

		// Check if request config matches sha256hex
		reqConfig = requestBody.getString("requestConfig");
		if (reqConfig.equals(sha256hex)) {
			chain.doFilter(requestWrapper, httpServletResponse);

			// SHA-256 Response Hashing starts here
			String baseRes = responseDto.getResponseString() + Constants.SALT_STRING + timestamp;
			String sha256hexRes = SecureUtils.sha256(baseRes);
			responseDto.setResponseConfig(sha256hexRes);

		} else {
			logger.info("Invalid Request!");
			responseDto.setResponseCode(Constants.ERROR_RESPONSE_CODE);
			responseDto.setResponseDesc(Constants.ERROR_RESPONSE_DESC);
			responseDto.setResponseConfig(null);
			responseDto.setResponseString(null);
		}
		// setting execution time
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		responseDto.setExecTime(String.valueOf(duration));

		httpServletResponse.getOutputStream().write(new Gson().toJson(responseDto).getBytes(StandardCharsets.UTF_8));
		String responseForLogging = new Gson().toJson(responseDto);
		logger.info("Inside My Filter Post Handle Response: {}", responseForLogging);
	}

}
