package cors.jaxrs;

import cors.enums.CorsType;

/**
 * 
 *    Copyright 2019 Arkadiusz Lopuszynski 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * CORS information: 
 * <a target="_blank" rel="noopener noreferrer" href="https://www.w3.org/TR/cors/">W3C</a>, 
 * <a target="_blank" rel="noopener noreferrer" href="https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS">MDN</a>, 
 * <a target="_blank" rel="noopener noreferrer" href="https://en.wikipedia.org/wiki/Cross-origin_resource_sharing">Wikipedia</a>
 * 
 * <p>
 * The CorsBuilder class is used to get default CORS headers, disable CORS headers, or create custom CORS headers.
 * <p>
 * This small library is based on JAX-RS's ContainerResponseFilter so it requires at least <b>JAX-RS v2.0 or Java EE 7.0</b> and <b>Java 8</b>.
 * <p>
 * After adding it to the project the filter is automatically discovered by JAX-RS runtime and the default set of CORS headers is used ({@link CorsBuilder#getDefaultCors default CORS}).
 * <p>
 * To customize CORS headers use CorsBuilder.getBuilder() and then setXXX and/or apppendXXX methods to create custom headers. See {@link cors.jaxrs.CorsStage}.
 * <p>
 * For methods with HTTP headers or HTTP methods arguments there are helper enums {@link cors.enums.HttpHeaders} and {@link cors.enums.HttpMethods} 
 * <p>
 * Use build() method to finish building.
 * <p>
 * Eexample:
 * <pre>
 *  CorsBuilder.getBuilder()
 *      .setAllowOrigin("*")
 *      .setAllowMethods(HttpMethods.GET, HttpMethods.POST)
 *      .setAllowHeaders(HttpHeaders.ORIGIN, HttpHeaders.ACCEPT, HttpHeaders.CONTENT_TYPE)
 *      .setExposeHeaders("X-My-Header1, X-My-Header2")
 *      .setAllowCredentials(true)
 *      .setMaxAge(TimeUnit.HOURS, 12)
 *      .setInfoHeader("X-My-Header", "My_Info")
 *      .build();
 * </pre>
 * <p>
 * From now JAX-RS calls will produce response with headers:
 * <pre>
 * Access-Control-Allow-Origin: *
 * Access-Control-Allow-Methods: GET,POST
 * Access-Control-Allow-Headers: Origin,Accept,Content-Type
 * Access-Control-Expose-Headers: X-My-Header1, X-My-Header2
 * Access-Control-Allow-Credentials: true
 * Access-Control-Max-Age: 720
 * X-My-Header: My_Info
 * </pre>
 * <p>
 * Builder can be built at any place and time, it configures already installed JAX-RS filter.
 * <p>
 * It just must be run before calls to the resources are made.
 * <p>
 * You can use a {@literal @PostConstruct} method, singleton bean or any place that runs at application's startup. 
 * <p>
 * You can use, for example, CDI application scope bean to run it at deployment time:   
 * 
 * <pre>
 *{@literal @ApplicationScoped}
 * public class CORSHeadersStarter {
 *     public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
 *         
 *         CorsBuilder.getBuilder()
 *            ...
 *            .build();
 *            
 *     }
 * }
 * </pre>
 * This bean will be run once when application scope is initialized.
 * 
 */
public class CorsBuilder {
	
	/**
	 * 
	 * Sets default CORS headers. This is the default option that is available after adding this CORS library. 
	 * <p>
	 * Access-Control-Allow-Origin: *
	 * <p>
	 * Access-Control-Allow-Headers: Origin,Accept,Content-Type
	 * <p>
	 * Access-Control-Allow-Methods: GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH
	 * <p>
	 * Access-Control-Allow-Credentials: true
	 * <p>
	 * Access-Control-Max-Age: 1440 (24h)
	 * <p>
	 * X-CORS-Headers: Powered-by-CorsJaxrs (Info header)
	 * 
	 */
	public static CorsStage getDefaultCors() {
        return new CorsStage(CorsType.DEFAULT);
    }
	
	/**
	 * Starts builder to create custom CORS headers. 
	 */
    public static CorsStage getBuilder() {
        return new CorsStage(CorsType.BUILDER);
    }
	
	/**
	 * Disables CORS headers. 
	 */
	public static CorsStage getDisabledCors() {
        return new CorsStage(CorsType.DISABLED);
    }
	
}