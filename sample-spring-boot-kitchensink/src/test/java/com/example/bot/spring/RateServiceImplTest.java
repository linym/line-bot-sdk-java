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

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.bot.spring.Impl.RateServiceImpl;
import com.example.bot.spring.RateService.Rate;
import com.example.bot.spring.RateService.RateRequest;
import com.example.bot.spring.RateService.RateService;
import com.google.gson.JsonObject;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RateServiceImplTest {

    private Rate rate;

    @InjectMocks
    private RateService rateService = new RateServiceImpl();

    @Mock
    private RateRequest rateRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        JsonObject innerJsonObject = new JsonObject();
        innerJsonObject.addProperty("USD", 1.141383);
        innerJsonObject.addProperty("TWD", 35.215659);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", true);
        jsonObject.addProperty("timestamp", 1547000000);
        jsonObject.add("rates", innerJsonObject);

        rate = new Rate();
        rate.jsonObject = jsonObject;
    }

    @Test
    public void test01Rate() throws Exception {
        Mockito.when(rateRequest.rateRequest()).thenReturn(rate);
        Rate myRate = rateService.rateCalculator("USD");
        Assert.assertEquals(myRate.rate, String.valueOf(35.215659 / 1.141383));
    }

}
