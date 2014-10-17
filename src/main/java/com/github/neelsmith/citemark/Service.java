/*
 * Copyright (C) 2011 René Jeschke <rene_jeschke@yahoo.de>
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
package com.github.neelsmith.citemark;

/**
 * A CITE service
 * 
 */
public class Service {


    public final String type;

    public String citeRequest;

    public String quoteRequest;

    /**
     * Constructor.
     * 
     * @param type The type.
     * @param cite The request to use for citation.
     * @param quote The request to use for quotation.
     *
     */
    public Service(final String type, final String cite, final String quote) {
        this.type = type;
        this.citeRequest = cite;
        this.quoteRequest = quote;
    }

    /** @see java.lang.Object#toString() */
    @Override
    public String toString()   {
        return this.type + ", " + this.citeRequest + ", " + this.quoteRequest;
    }
}
