# CORS Headers for Java JAX-RS.

- CORS information:
<a target="_blank" rel="noopener noreferrer" href="https://www.w3.org/TR/cors/">W3C</a>, 
<a target="_blank" rel="noopener noreferrer" href="https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS">MDN</a>, 
<a target="_blank" rel="noopener noreferrer" href="https://en.wikipedia.org/wiki/Cross-origin_resource_sharing">Wikipedia</a>

- Fully customizable using a builder.
  - setXXX methods to set headers and appendXXX to append to the existing ones.
  - Helper enums <a target="_blank" rel="noopener noreferrer" href="https://areklopus.github.io/CorsJaxrs/javadocs/cors/enums/HttpHeaders.html">HttpHeaders</a>
  and <a target="_blank" rel="noopener noreferrer" href="https://areklopus.github.io/CorsJaxrs/javadocs/cors/enums/HttpMethods.html">HttpMethods</a>.
  - allows to set an additional informational header.
- [Default CORS](#default-cors) headers after installation.
- <a target="_blank" rel="noopener noreferrer" href="https://areklopus.github.io/CorsJaxrs/javadocs/index.html">javadocs</a>.

This small library is based on JAX-RS's ContainerResponseFilter so it requires at least **JAX-RS v2.0 or Java EE 7.0** and **Java 8**.

To use, it copy the target/cors-jaxrs.jar file or build it from the source code using `mvn package` command.

Or use `mvn install` command to add it to the local repository and then use as a dependency:
```
  <dependency>
      <groupId>cors</groupId>
      <artifactId>cors-jaxrs</artifactId>
      <version>1.0.0</version>
  </dependency>
```

After adding it to the project the filter is automatically discovered by JAX-RS runtime and the default set of CORS headers is used ([Default CORS](#default-cors)).

### <a name="default-cors"></a>Default CORS Headers
The default headers available after adding this CORS library.
```
Access-Control-Allow-Origin: *
Access-Control-Allow-Headers: Origin,Accept,Content-Type
Access-Control-Allow-Methods: GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH
Access-Control-Allow-Credentials: true
Access-Control-Max-Age: 1440 (24h)
X-CORS-Headers: Powered-by-CorsJaxrs (Info header)
```

### Custimize CORS Headers

The CorsBuilder class is used to get default CORS headers, disable CORS headers, or create custom CORS headers.

To customize CORS headers use CorsBuilder.getBuilder() and then setXXX and/or apppendXXX methods to create custom headers.
- setXXX methods - set headers if they do not exist already or override existing ones.
- appendXXX methods - set headers if they do not exist already or append to the existing ones.

If a header is not declared in the builder, it is not set.

For methods with HTTP headers or HTTP methods arguments there are helper enums 
<a target="_blank" rel="noopener noreferrer" href="https://areklopus.github.io/CorsJaxrs/javadocs/cors/enums/HttpHeaders.html">HttpHeaders</a>
and <a target="_blank" rel="noopener noreferrer" href="https://areklopus.github.io/CorsJaxrs/javadocs/cors/enums/HttpMethods.html">HttpMethods</a>.

Use the `build()` method to finish building.

Example:
```
  CorsBuilder.getBuilder()
      .setAllowOrigin("*")
      .setAllowMethods(HttpMethods.GET, HttpMethods.POST)
      .setAllowHeaders(HttpHeaders.ORIGIN, HttpHeaders.ACCEPT, HttpHeaders.CONTENT_TYPE)
      .setExposeHeaders("X-My-Header1, X-My-Header2")
      .setAllowCredentials(true)
      .setMaxAge(TimeUnit.HOURS, 12)
      .setInfoHeader("X-My-Header", "My_Info")
      .build();
```
From now responses will contain headers:
```
 Access-Control-Allow-Origin: *
 Access-Control-Allow-Methods: GET,POST
 Access-Control-Allow-Headers: Origin,Accept,Content-Type
 Access-Control-Expose-Headers: X-My-Header1, X-My-Header2
 Access-Control-Allow-Credentials: true
 Access-Control-Max-Age: 720
 X-My-Header: My_Info
```
Builder can be built at any place and time, it configures already installed JAX-RS filter.

It just must be run before calls to the resources are made.

You can use a @PostConstruct method, singleton bean or any place that runs at application's startup.

You can use, for example, CDI application scope bean to run it at deployment time:
```
 @ApplicationScoped
 public class CORSHeadersStarter {
     public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
         
         CorsBuilder.getBuilder()
            ...
            .build();
            
     }
 }
```
This bean will be run once when application scope is initialized.


### Disabling CORS Headers
If a header is not declared in the builder, it is not set.

Set null to setXXX or appendXXX method to explicitly disable a header (headers set by any other means, like other filter, are still visible).

If the `CorsBuilder.getDisabledCors()` is used CORS headers are not set.




---






