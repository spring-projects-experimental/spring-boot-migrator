package org.springframework.sbm.boot.cleanup;

import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.marker.Markers;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.xml.ChangeTagValueVisitor;
import org.openrewrite.xml.tree.Content;
import org.openrewrite.xml.tree.Xml;

import static java.util.Collections.singletonList;
import static org.openrewrite.Tree.randomId;

@Data
@EqualsAndHashCode(callSuper = true)
public class RemovePropertyAndAllOccurrence extends Recipe {

	private final String propertyName;

	private final String newPropertyName;

	@Override
	public String getDisplayName() {
		return "Remove Property and all occurrence of it";
	}

	@Override
	public TreeVisitor<?, ExecutionContext> getVisitor() {
		return new RemovePropertyAndAllOccurrence.RemovePropertyAndAllOccurrenceVisitor();
	}

	private class RemovePropertyAndAllOccurrenceVisitor extends MavenVisitor<ExecutionContext> {

		@Override
		public Xml visitTag(Xml.Tag tag, ExecutionContext ctx) {
			Xml.Tag t = (Xml.Tag)super.visitTag(tag, ctx);
			if (isPropertyTag() && propertyName.equals(tag.getName())) {
				return null;
			}
			Optional<String> value = tag.getValue();
			if (t.getContent() != null && value.isPresent() && value.get().contains("${"))  {
				final String tagValue = value.get().replace("${", "").replace("}", "");
				if (propertyName.equals(tagValue)) {
					doAfterVisit(new ChangeTagValueVisitor<>(t, newPropertyName));

				}
			}
			return t;
		}

	}

}
