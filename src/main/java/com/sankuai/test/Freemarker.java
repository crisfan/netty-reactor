/**
 * meituan.com Inc.
 * Copyright (c) 2010-2021 All Rights Reserved.
 */
package com.sankuai.test;

import com.google.gson.JsonElement;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author fanyuhao
 * @version :Freemarker.java v1.0 2021/4/29 8:14 下午 fanyuhao Exp $
 */
public class Freemarker {

    public static final Freemarker INSTANCE = new Freemarker();
    private final StringTemplateLoader stringTemplateLoader;

    private final Configuration cfg;

    public Freemarker() {
        cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setNumberFormat("#.######################");
        stringTemplateLoader = new StringTemplateLoader();
        cfg.setTemplateLoader(stringTemplateLoader);
    }

    public String process(String express, Object valueSource) {
        if (stringTemplateLoader.findTemplateSource(express) == null) {
            synchronized (this) {
                if (stringTemplateLoader.findTemplateSource(express) == null) {
                    stringTemplateLoader.putTemplate(express, express);
                }
            }
        }

        try {
            Template template = cfg.getTemplate(express, "utf-8");
            StringWriter writer = new StringWriter();
            template.process(valueSource, writer);
            return writer.toString();
        } catch (Throwable t) {
            System.out.println(t);
            return "";
        }
    }

    public String processValue(String express, Object valueSource) {
        if (stringTemplateLoader.findTemplateSource(express) == null) {
            synchronized (this) {
                if (stringTemplateLoader.findTemplateSource(express) == null) {
                    stringTemplateLoader.putTemplate(express, express);
                }
            }
        }

        try {
            Template template = cfg.getTemplate(express, "utf-8");
            StringWriter writer = new StringWriter();
            template.process(valueSource, writer);


            return writer.toString();
        } catch (Throwable t) {
            System.out.println(t);
            return "";
        }
    }

    public static void main(String[] args) throws Exception{
        Map<String, String> map = new HashMap<>();
        map.put("ss", "");

        String json = "{\"name\": \"data\"}";
        Object biz = GsonUtils.deserialize(json, Object.class);

        String json2 = "{\"hth\": 7105042, \"sktno\": 201938,\"appid\": \"hypos60461ebf\", \"secert\": \"sss\"}";
        Object exConfig = GsonUtils.deserialize(json2, Object.class);

        Map<String, Object> originData = new HashMap<>();
        originData.put("biz", biz);
        originData.put("exConfig", exConfig);


        String express = "${.now?string('yyyy-MM-dd HH:mm:ss:SSS')}";
        String value = Freemarker.INSTANCE.processValue(express, originData);
        System.out.println(value);
    }
}

