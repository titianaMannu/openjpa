/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.openjpa.lib.util.Localizer;

/**
 * Exception indicating that a query timeout occurred.
 *
 * @since 2.0.0
 */
public class QueryException
    extends StoreException {

    private static final long serialVersionUID = 7375049808087780437L;

    private static final transient Localizer _loc = Localizer.forPackage(QueryException.class);

    private int timeout = -1;

    public QueryException(Object failed) {
        super(_loc.get("query-failed"));
        setFailedObject(failed);
    }

    public QueryException(Object failed, int timeout) {
        super(_loc.get("query-timeout", String.valueOf(timeout)));
        setFailedObject(failed);
        setTimeout(timeout);
    }

    public int getSubtype() {
        return QUERY;
    }
    
    /**
     * The number of milliseconds to wait for a query to complete.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * The number of milliseconds to wait for a query to complete.
     */
    public QueryException setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public String toString() {
        String str = super.toString();
        if (timeout < 0)
            return str;
        return str + Exceptions.SEP + "Query Timeout: " + timeout;
    }

    private void writeObject(ObjectOutputStream out)
        throws IOException {
        out.writeInt(timeout);
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        timeout = in.readInt();
    }
}
