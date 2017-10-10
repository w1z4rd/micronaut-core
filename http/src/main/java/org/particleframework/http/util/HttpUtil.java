/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.http.util;

import org.particleframework.http.HttpHeaders;
import org.particleframework.http.HttpRequest;
import org.particleframework.http.MediaType;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Optional;

/**
 * Utility methods for HTTP handling
 *
 * @author Graeme Rocher
 * @since 1.0
 */
public class HttpUtil {

    /**
     * Return whether the given request features {@link MediaType#APPLICATION_FORM_URLENCODED} or {@link MediaType#MULTIPART_FORM_DATA}
     *
     * @param request The request
     * @return True if it is form data
     */
    public static boolean isFormData(HttpRequest request) {
        MediaType contentType = request.getContentType();
        return contentType != null && (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED_TYPE) || contentType.equals(MediaType.MULTIPART_FORM_DATA_TYPE));
    }

    /**
     * Resolve the {@link Charset} to use for the request
     *
     * @param request The request
     * @return An {@link Optional} of {@link Charset}
     */
    public static Optional<Charset> resolveCharset(HttpRequest request) {
        try {
            MediaType contentType = request.getContentType();
            Optional<Charset> optional = Optional.empty();
            if(contentType != null) {
                optional = contentType
                        .getParameters()
                        .get(MediaType.CHARSET_PARAMETER)
                        .map(Charset::forName);
            }
            if(optional.isPresent()) {
                return optional;
            }
            else {
                return request
                            .getHeaders()
                            .findFirst(HttpHeaders.ACCEPT_CHARSET)
                            .map(Charset::forName);
            }
        } catch (UnsupportedCharsetException e) {
            return Optional.empty();
        }
    }
}