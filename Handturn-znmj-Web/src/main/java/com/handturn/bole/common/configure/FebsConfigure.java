package com.handturn.bole.common.configure;

import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.frontfilter.FrontFilter;
import com.handturn.bole.common.properties.FebsProperties;
import com.handturn.bole.common.properties.SwaggerProperties;
import com.handturn.bole.common.xss.XssFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Eric
 */
@Configuration
@EnableSwagger2
public class FebsConfigure {

    @Autowired
    private FebsProperties properties;

    @Bean(FebsConstant.ASYNC_POOL)
    public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("Febs-Async-Thread");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * XssFilter Bean
     */
    @Bean
    @SuppressWarnings("all")
    public FilterRegistrationBean xssFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*,/front/*");
        initParameters.put("isIncludeRichText", "true");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }

    //--------------------------添加邻邻go过滤---------BEGIN--------------------
    /**
     * LinlingoFilter Bean
     */
    @Bean
    @SuppressWarnings("all")
    public FilterRegistrationBean frontFilterRegistrationBean() {
        FilterRegistrationBean frontRegistrationBean = new FilterRegistrationBean();
        frontRegistrationBean.setFilter(new FrontFilter());
        frontRegistrationBean.setOrder(1);
        frontRegistrationBean.setEnabled(true);
        frontRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("includes", "/sitapi/*");
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*,/sitapi/wechat/login");
        frontRegistrationBean.setInitParameters(initParameters);
        return frontRegistrationBean;
    }
    //--------------------------添加邻邻go过滤----------END-------------------

    @Bean
    public Docket swaggerApi() {
        SwaggerProperties swagger = properties.getSwagger();
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo(swagger));
    }

    private ApiInfo apiInfo(SwaggerProperties swagger) {
        return new ApiInfo(
                swagger.getTitle(),
                swagger.getDescription(),
                swagger.getVersion(),
                null,
                new Contact(swagger.getAuthor(), swagger.getUrl(), swagger.getEmail()),
                swagger.getLicense(), swagger.getLicenseUrl(), Collections.emptyList());
    }

}
