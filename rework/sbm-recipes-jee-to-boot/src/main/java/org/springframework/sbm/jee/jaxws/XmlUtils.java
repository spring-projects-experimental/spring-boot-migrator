/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.jee.jaxws;

import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.XmlVisitor;
import org.openrewrite.xml.tree.Xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Alex Boyko
 */
public class XmlUtils {

    public static final String XMLNS_ATTR_PREFIX = "xmlns:";

    public static List<Xml.Tag> findTags(Xml.Document doc, XPathMatcher matcher) {
        final List<Xml.Tag> context = new ArrayList<>();
        new XmlVisitor<List<Xml.Tag>>() {
            @Override
            public Xml visitTag(Xml.Tag tag, List<Xml.Tag> found) {
                if (matcher.matches(getCursor())) {
                    context.add(tag);
                }
                return super.visitTag(tag, found);
            }
        }.visit(doc, context);
        return context;
    }

    public static Optional<String> getFirstTagAttribute(Xml.Document doc, XPathMatcher matcher, String attr) {
        List<Xml.Tag> tags = findTags(doc, matcher);
        return tags.isEmpty() ? Optional.empty() : tags.get(0).getAttributes().stream().filter(a -> attr.equals(a.getKeyAsString())).map(a -> a.getValueAsString()).findFirst();
    }

    public static String findNsPrefix(Xml.Tag tag, String nsUri) {
        return tag.getAttributes().stream().map(a -> {
            if (a.getKeyAsString().startsWith(XMLNS_ATTR_PREFIX) && nsUri.equals(a.getValueAsString())) {
                return a.getKeyAsString().substring(XMLNS_ATTR_PREFIX.length());
            }
            return "";
        }).filter(s -> !s.isEmpty()).findFirst().orElse(null);
    }

}
