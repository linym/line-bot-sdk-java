/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class RateService {

    public RateService() {
        lastAccessTime = 0;
    }

    private long lastAccessTime;

    private void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    private JsonObject jsonDefaultObj;

    private void setJsonDefaultObj(JsonObject jsonObject) {
        this.jsonDefaultObj = jsonObject;
    }

    public Rate rateCalculator(String text) throws Exception {

        JsonObject jsonObj;
        Rate myRate = new Rate();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));

        long currentAccessTime = System.currentTimeMillis();
        String currentTime = sdf.format(new Date(currentAccessTime));

        if (currentAccessTime / 1000 - lastAccessTime > 3600) {
            //lastAccessTime = currentAccessTime / 1000;
            setLastAccessTime(currentAccessTime / 1000);

            // Making Request
            String urlStr = "http://data.fixer.io/api/latest?access_key=efe087eb1fcfa771cde3bdb8ecc34e38&format=1";
            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to JSON
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent(), "utf-8"));
            jsonObj = root.getAsJsonObject();

            String reqResult = jsonObj.get("success").getAsString();
            if ("true".equals(reqResult)) {
                setJsonDefaultObj(jsonObj);
                //jsonDefaultObj = jsonObj;
                myRate.sourceMessage = "Update successfully by " + currentTime;
            } else {
                //Read from cached rate
                jsonObj = jsonDefaultObj;
                myRate.sourceMessage = "Update failed by " + currentTime;
            }
        } else {
            jsonObj = jsonDefaultObj;
            myRate.sourceMessage = "Use cached data";
        }

        myRate.updateTime = sdf.format(new Date(Long.parseLong(jsonObj.get("timestamp")
                .getAsString()) * 1000));

        Double baseRate = Double.parseDouble(jsonObj.getAsJsonObject().get("rates").getAsJsonObject()
                .get("TWD").getAsString());
        Double currencyRate = Double.parseDouble(jsonObj.getAsJsonObject().get("rates").getAsJsonObject()
                .get(text.toUpperCase()).getAsString());

        myRate.rate = String.valueOf(baseRate / currencyRate);
        return myRate;
    }

}
