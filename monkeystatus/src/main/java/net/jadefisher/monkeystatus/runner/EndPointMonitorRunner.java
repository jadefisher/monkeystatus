package net.jadefisher.monkeystatus.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.jadefisher.monkeystatus.event.EventManager;
import net.jadefisher.monkeystatus.exception.AssertionFailedException;
import net.jadefisher.monkeystatus.model.monitor.EndPointMonitor;
import net.jadefisher.monkeystatus.model.monitor.HttpRequestDefinition;
import net.jadefisher.monkeystatus.model.monitor.LogType;
import net.jadefisher.monkeystatus.respository.ServiceRepository;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class EndPointMonitorRunner extends MonitorRunner<EndPointMonitor> {
	private static final Log log = LogFactory
			.getLog(EndPointMonitorRunner.class);

	private ScheduledFuture<?> future;
	private EventManager eventManager;
	private PoolingHttpClientConnectionManager cmgr;
	private ScheduledExecutorService executorService;
	private CloseableHttpClient httpclient;
	private BasicCookieStore cookieStore;

	public EndPointMonitorRunner(ServiceRepository serviceReop,
			PoolingHttpClientConnectionManager cmgr,
			ScheduledExecutorService executorService, EndPointMonitor monitor) {
		super(serviceReop);
		this.cmgr = cmgr;
		this.executorService = executorService;
		this.monitor = monitor;
	}

	@Override
	public void startMonitoring(EventManager eventManager) {
		this.eventManager = eventManager;
		cookieStore = new BasicCookieStore();
		httpclient = HttpClients.custom().setConnectionManager(cmgr)
				.setDefaultCookieStore(cookieStore).build();

		this.future = executorService.scheduleAtFixedRate(this::runMonitor, 5,
				20, TimeUnit.SECONDS);
	}

	private void runMonitor() {

		if (!monitorServiceNow(monitor.getServiceId())) {
			log.info("Skipping monitoring " + monitor.getId()
					+ " as now is a maintenance window");
			return;
		}

		// log.info("Checking monitor: " + this.monitor.getName()
		// + " ----------------------------------------");

		cookieStore.clear();
		Map<String, String> headers = new HashMap<String, String>();
		List<HttpRequestResult> results = new ArrayList<HttpRequestResult>();

		if (this.monitor.getFormAuthentication() != null) {
			results.add(executeAndMonitorRequest(httpclient, this.monitor
					.getFormAuthentication().getLogonRequest(), headers));
			// log.debug("authenticated with form authentication");
		} else if (this.monitor.getBasicAuthentication() != null) {
			String authString = this.monitor.getBasicAuthentication()
					.getUsername()
					+ ":"
					+ this.monitor.getBasicAuthentication().getPassword();
			String encoding = Base64.encodeBase64String(authString.getBytes());
			headers.put("Authorization", "Basic " + encoding);
		}

		for (HttpRequestDefinition req : this.monitor.getRequests()) {
			results.add(executeAndMonitorRequest(httpclient, req, headers));
		}

		if (this.monitor.getFormAuthentication() != null
				&& this.monitor.getFormAuthentication().getLogoffRequest() != null) {
			results.add(executeAndMonitorRequest(httpclient, this.monitor
					.getFormAuthentication().getLogoffRequest(), headers));
			// log.debug("logged off form authentication");
		}

		LogType overallMonitorLogType = LogType.PASSED;
		String overallMonitorMessage = null;

		for (HttpRequestResult result : results) {
			if (result.getType() == LogType.ERROR) {
				overallMonitorLogType = LogType.ERROR;
			} else if (result.getType() == LogType.FAILED
					&& overallMonitorLogType != LogType.ERROR) {
				overallMonitorLogType = LogType.FAILED;
			}

			if (result.getType() != LogType.PASSED) {
				overallMonitorMessage = overallMonitorMessage == null ? result
						.getMessage() : overallMonitorMessage + ", "
						+ result.getMessage();
			}
		}

		// Send through one logMonitor result
		try {
			this.eventManager.logMonitorResult(monitor, overallMonitorLogType,
					overallMonitorMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// log.info("finished checking " + this.monitor.getName()
		// + " ----------------------------------------");
	}

	@Override
	public void stopMonitoring() {
		log.info("stopping monitoring " + this.monitor.getName());
		future.cancel(true);
		try {
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private HttpRequestResult executeAndMonitorRequest(
			CloseableHttpClient client, HttpRequestDefinition def,
			Map<String, String> headers) {
		try {
			log.debug("running request: " + def.getUrl());
			executeRequest(client, def, headers);

			return new HttpRequestResult(LogType.PASSED, null);
		} catch (AssertionFailedException e) {
			return new HttpRequestResult(LogType.FAILED, e.getMessage()
					+ " for " + def.getUrl());
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			return new HttpRequestResult(LogType.ERROR, e.getMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
			return new HttpRequestResult(LogType.ERROR, e.getMessage());
		}
	}

	private static void executeRequest(CloseableHttpClient client,
			HttpRequestDefinition def, Map<String, String> headers)
			throws URISyntaxException, IOException, AssertionFailedException {

		HttpUriRequest request = createRequest(def);
		log.debug("request: " + request);
		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				request.addHeader(header.getKey(), header.getValue());
			}
		}
		if (def.getHeaders() != null) {
			for (Map.Entry<String, String> header : def.getHeaders().entrySet()) {
				request.addHeader(header.getKey(), header.getValue());
			}
		}

		CloseableHttpResponse response = client.execute(request);

		try {
			HttpEntity entity = response.getEntity();
			int responseCode = response.getStatusLine().getStatusCode();
			if (def.getExpectedResponseCodes() != null
					&& !def.getExpectedResponseCodes().contains(responseCode)) {
				throw new AssertionFailedException(
						"Response code check failed. Received " + responseCode
								+ " but expected one of "
								+ def.getExpectedResponseCodes());
			}

			log.debug("request get: " + response.getStatusLine()
					+ " with response: " + entity);

			if (def.getExpectedResponseContentType() != null
					&& !entity.getContentType().getValue()
							.contains(def.getExpectedResponseContentType())) {
				throw new AssertionFailedException(
						"Response content type check failed. Received "
								+ entity.getContentType().getValue()
								+ " but expected "
								+ def.getExpectedResponseContentType());
			}

			if (def.getExpectedResponsePatterns() != null) {
				InputStreamReader serverInput = new InputStreamReader(
						entity.getContent());
				BufferedReader reader = new BufferedReader(serverInput);
				StringBuilder responseBuffer = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					responseBuffer.append(line);
				}
				String responseText = responseBuffer.toString();

				for (String patternStr : def.getExpectedResponsePatterns()) {
					if (!responseText.matches(patternStr)) {
						throw new AssertionFailedException(
								"Response content check failed. Received "
										+ responseText
										+ " but which didn't match "
										+ patternStr);
					}
				}
			}
			EntityUtils.consume(entity);
		} finally {
			response.close();
		}
	}

	private static HttpUriRequest createRequest(HttpRequestDefinition def)
			throws URISyntaxException {
		RequestBuilder builder = null;
		switch (def.getMethod()) {
		case POST:
			builder = RequestBuilder.post();
			break;
		case GET:
			builder = RequestBuilder.get();
			break;
		case PUT:
			builder = RequestBuilder.put();
			break;
		case DELETE:
			builder = RequestBuilder.delete();
			break;
		case HEAD:
			builder = RequestBuilder.head();
			break;
		default:
			log.error("Don't know method: " + def.getMethod());
		}

		builder.setUri(new URI(def.getUrl()));
		if (def.getParameters() != null) {
			for (Map.Entry<String, String> param : def.getParameters()
					.entrySet()) {
				builder.addParameter(param.getKey(), param.getValue());
			}
		}
		return builder.build();
	}

	class HttpRequestResult {
		private LogType type;

		private String message;

		public HttpRequestResult(LogType type, String message) {
			this.type = type;
			this.message = message;
		}

		public LogType getType() {
			return type;
		}

		public String getMessage() {
			return message;
		}
	}
}
