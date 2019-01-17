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

import com.example.bot.spring.RateService.Rate;
import com.example.bot.spring.RateService.RateRequest;
import com.example.bot.spring.RateService.RateService;
import com.google.gson.JsonObject;

@Component
public class RateServiceImpl implements RateService {

    @Autowired
    private RateRequest rateRequest;

    public Rate rateCalculator(String text) throws Exception {

        Rate rate = rateRequest.rateRequest();
        JsonObject jsonObj = rate.jsonObject;

        Double baseRate = Double.parseDouble(jsonObj.getAsJsonObject().get("rates").getAsJsonObject()
                .get("TWD").getAsString());
        Double currencyRate = Double.parseDouble(jsonObj.getAsJsonObject().get("rates").getAsJsonObject()
                .get(text.toUpperCase()).getAsString());

        rate.rate = String.valueOf(baseRate / currencyRate);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        rate.updateTime = sdf.format(new Date(Long.parseLong(jsonObj.get("timestamp")
                .getAsString()) * 1000));
        return rate;
    }

}

