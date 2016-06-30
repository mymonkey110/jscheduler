/*
 * Copyright 2016  Michael Jiang
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
 */

package com.haoocai.jscheduler.core.shared;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author Michael Jiang on 16/3/16.
 */
@Component
public class JschedulerConfig implements ApplicationContextAware {
    private String connectStr;
    private List<String> namespaces;
    private ApplicationContext applicationContext;

    @Value("classpath:jscheduler.properties")
    private String configPath;

    private static Logger LOG = LoggerFactory.getLogger(JschedulerConfig.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        Resource configFile = applicationContext.getResource(configPath);
        Properties properties = new Properties();
        try {
            properties.load(configFile.getInputStream());
        } catch (IOException e) {
            LOG.info("load properties error:{}.", e.getMessage(), e);
        }

        connectStr = properties.getProperty("connectStr");
        namespaces = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(properties.getProperty("namespace"));
    }

    public String getConnectStr() {
        return connectStr;
    }

    public List<String> getNamespaces() {
        return namespaces;
    }
}
