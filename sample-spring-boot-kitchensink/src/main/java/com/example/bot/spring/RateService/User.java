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

package com.example.bot.spring.RateService;

import org.springframework.data.annotation.Id;

public class User {

    @Id
    public String id;

    public String userId;
    public String baseCurrency;
    public String nowCurrency;

    public User() {}

    public User(String userId, String baseCurrency, String nowCurrency) {
        this.userId = userId;
        this.baseCurrency = baseCurrency;
        this.nowCurrency = nowCurrency;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, userId='%s', baseCurrency='%s', nowCurrency='%s']",
                id, userId, baseCurrency, nowCurrency);
    }
}
