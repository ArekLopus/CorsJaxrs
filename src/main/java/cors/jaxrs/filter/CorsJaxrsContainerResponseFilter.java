package cors.jaxrs.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import cors.jaxrs.Cors;
import cors.jaxrs.CorsBuilder;
import cors.jaxrs.CorsStage;
import cors.jaxrs.CorsUtils;


@Provider
public class CorsJaxrsContainerResponseFilter implements ContainerResponseFilter {
	
	private static CorsStage JAXRS_CORS_HEADERS;
	private Cors cors;
	
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		
		if(JAXRS_CORS_HEADERS == null) {
			CorsBuilder.getDefaultCors();
		}
		
		if(JAXRS_CORS_HEADERS == null) {					// CorsJaxrs object is null, return.
			return;
		} else {
			cors = JAXRS_CORS_HEADERS.getCors();
			
			if(cors == null || cors.isNoCors()) {			// if Cors object is null or we skip cors, return.
				return;
			}
		}
		
		MultivaluedMap<String, Object> headers = responseContext.getHeaders();
		
		
		setOrAppendCorsHeader(CorsUtils.ALLOW_ORIGIN, cors.isAppendAllowOrigin(), cors.getAllowOrigin(), headers);

		setOrAppendCorsHeader(CorsUtils.ALLOW_METHODS, cors.isAppendAllowMethods(), cors.getAllowMethods(), headers);
		setOrAppendCorsHeader(CorsUtils.ALLOW_HEDAERS, cors.isAppendAllowHeaders(), cors.getAllowHeaders(), headers);
		setOrAppendCorsHeader(CorsUtils.EXPOSE_HEADERS, cors.isAppendExposeHeaders(), cors.getExposeHeaders(), headers);
		
		setCorsHeader(CorsUtils.ALLOW_CREDENTIALS, cors.isAllowCredentials(), headers);
		setCorsHeader(CorsUtils.MAX_AGE, cors.getMaxAge(), headers);
		
		setOrAppendCorsHeader(cors.getInfoHeaderName(), cors.isAppendInfoHeader(), cors.getInfoHeaderInfo(), headers);
		
	}
	
	
	
	private <T> void setCorsHeader(String headerName, T newHeader, MultivaluedMap<String, Object> headers) {
		
		if(newHeader == null) {											// Header is not set.
			return;
		}
		if(newHeader instanceof String && newHeader.equals("")) {		// Also return when empty string 
			return;
		}
		
		if(!headers.containsKey(headerName)) {							// If no header, add it.
			headers.add(headerName, newHeader);
		} else {														// If header exists, override to a new value.
			List<Object> newHeaderAsList = Stream.of(newHeader).collect(Collectors.toList());
			headers.replace(headerName, newHeaderAsList);
		}
		
	}
	
	
	
	private void setOrAppendCorsHeader(String headerName, boolean appendFlag, String newHeaders, MultivaluedMap<String, Object> headers) {
		
		if(newHeaders == null || newHeaders.equals("")) {				// Header is not set.
			return;
		}
		
		if(!headers.containsKey(headerName)) {							// If no header, add it (for add or append).
			
			headers.add(headerName, newHeaders);
		
		} else {
			
			if(appendFlag == false) {									// If header exists but no append flag set, override to a new value.
				
				List<Object> newHeadersList = Stream.of(newHeaders).collect(Collectors.toList());
				headers.replace(headerName, newHeadersList);
			
			} else {													// If header exists and append flag is set.
				
				List<Object> oldHeaders = headers.get(headerName);
				
				String appendedHeader = this.createAppendedHeader(oldHeaders, newHeaders);
				
				headers.remove(headerName);
				headers.add(headerName, appendedHeader);
			}
		}
	}
	
	// Parses new and old headers, eliminates duplicates, and creates a String of all headers 
	private String createAppendedHeader(List<Object> oldHeaders, String newHeaders) {
        
		HashSet<String> headers = new HashSet<>();						// To remove duplicates.
		
		String[] splittedNewHeaders = newHeaders.split("\\s*,\\s*");
		headers.addAll(Arrays.asList(splittedNewHeaders));
		
		oldHeaders.forEach(e -> {
			String str = (String) e;
			String[] splittedOldHeaders = str.split("\\s*,\\s*");
			headers.addAll(Arrays.asList(splittedOldHeaders));
		});
		
		StringBuilder sb = new StringBuilder();
		
		headers.forEach(e -> {
			sb.append(e);
			sb.append(",");
		});
		
		sb.deleteCharAt(sb.length() - 1);		// Remove last comma.
		
		return sb.toString();
	}
	
	
	
	public static void setJaxrsCorsHeaders(CorsStage corsJaxrs) {
		JAXRS_CORS_HEADERS = corsJaxrs;
	}
	
}
