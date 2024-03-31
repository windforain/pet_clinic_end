package com.pet_clinic_end.config;

import com.pet_clinic_end.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("开始扩展消息转换器...");

        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        // 设置对象映射器，底层使用Jackson将Java对象转换为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        // 将上面的消息转换器对象添加到mvc框架的转换器容器中（注意要放在首位）
        converters.add(0, messageConverter);
    }
}

