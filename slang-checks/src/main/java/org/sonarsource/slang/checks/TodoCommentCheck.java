/*
 * SonarSource SLang
 * Copyright (C) 2018-2025 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonarsource.slang.checks;

import org.sonarsource.slang.api.TextPointer;
import org.sonarsource.slang.api.TextRange;
import org.sonarsource.slang.api.TopLevelTree;
import org.sonarsource.slang.checks.api.InitContext;
import org.sonarsource.slang.checks.api.SlangCheck;
import org.sonarsource.slang.impl.TextPointerImpl;
import org.sonarsource.slang.impl.TextRangeImpl;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.TokenLocation;

@Rule(key = "S1135")
public class TodoCommentCheck implements SlangCheck {

  private final Pattern todoPattern = Pattern.compile("(?i)(^|[[^\\p{L}]&&\\D])(todo)($|[[^\\p{L}]&&\\D])");

  @Override
  public void initialize(InitContext init) {
    init.register(TopLevelTree.class, (ctx, tree) ->
      tree.allComments().forEach(comment -> {
        Matcher matcher = todoPattern.matcher(comment.text());
        if (matcher.find()) {
          TextPointer start = comment.textRange().start();
          TokenLocation location = new TokenLocation(start.line(), start.lineOffset(), comment.text().substring(0, matcher.start(2)));
          TextRange todoRange = new TextRangeImpl(
            new TextPointerImpl(location.endLine(), location.endLineOffset()),
            new TextPointerImpl(location.endLine(), location.endLineOffset() + 4)
          );
          ctx.reportIssue(todoRange, "Complete the task associated to this TODO comment.");
        }
      })
    );
  }

}
