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

package com.example.bot.spring.Impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.bot.spring.RateService.GetTime;
import com.example.bot.spring.RateService.Rate;
import com.example.bot.spring.RateService.RateRequest;
import com.google.gson.JsonObject;

@Component
public class RateRequestImpl implements RateRequest {

    public RateRequestImpl() {
        lastAccessTime = 0;
    }

    @Autowired
    private GetTime getTime;

    @Autowired
    private RetrieveRemoteRateJsonImpl retrieveRemoteRateJson;

    private JsonObject jsonDefaultObj;

    private void setJsonDefaultObj(JsonObject jsonObject) {
        this.jsonDefaultObj = jsonObject;
    }

    private long lastAccessTime;

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    private void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Rate rateRequest() throws Exception {
        JsonObject jsonObj;
        Rate myRate = new Rate();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));

        long currentAccessTime = getTime.getTime() / 1000;
        String currentTime = sdf.format(new Date(currentAccessTime * 1000));

        if (currentAccessTime - getLastAccessTime() > 3600) {
            setLastAccessTime(currentAccessTime);

            jsonObj = retrieveRemoteRateJson.retrieveRemoteRateJson();
            String reqResult = jsonObj.get("success").getAsString();
            if ("true".equals(reqResult)) {
                //Update rate from remote server
                setJsonDefaultObj(jsonObj);
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
        myRate.jsonObject = jsonObj;
        return myRate;
    }

}
