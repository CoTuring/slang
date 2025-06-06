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
package org.sonarsource.slang.impl;

import org.sonarsource.slang.api.Token;
import org.sonarsource.slang.api.Tree;
import org.sonarsource.slang.api.TreeMetaData;
import org.junit.jupiter.api.Test;

import static org.sonarsource.slang.api.LoopTree.LoopKind.DOWHILE;
import static org.sonarsource.slang.api.LoopTree.LoopKind.FOR;
import static org.sonarsource.slang.api.LoopTree.LoopKind.WHILE;
import static org.sonarsource.slang.utils.SyntacticEquivalence.areEquivalent;
import static org.assertj.core.api.Assertions.assertThat;

class LoopTreeImplTest {

  @Test
  void test() {
    TreeMetaData meta = null;
    Tree condition = new LiteralTreeImpl(meta, "1");
    Tree body = new IdentifierTreeImpl(meta, "x");

    TokenImpl forToken = new TokenImpl(new TextRangeImpl(1, 0, 1, 3), "for", Token.Type.KEYWORD);
    TokenImpl whileToken = new TokenImpl(new TextRangeImpl(1, 0, 1, 5), "while", Token.Type.KEYWORD);
    TokenImpl doToken = new TokenImpl(new TextRangeImpl(1, 0, 1, 2), "do", Token.Type.KEYWORD);

    LoopTreeImpl forTree = new LoopTreeImpl(meta, condition, body, FOR, forToken);
    assertThat(forTree.children()).containsExactly(condition, body);
    assertThat(forTree.condition()).isEqualTo(condition);
    assertThat(forTree.body()).isEqualTo(body);
    assertThat(forTree.kind()).isEqualTo(FOR);

    LoopTreeImpl whileTree = new LoopTreeImpl(meta, condition, body, WHILE, whileToken);
    LoopTreeImpl doTree = new LoopTreeImpl(meta, condition, body, DOWHILE, doToken);

    assertThat(areEquivalent(forTree, new LoopTreeImpl(meta, condition, body, FOR, forToken))).isTrue();
    assertThat(areEquivalent(forTree, whileTree)).isFalse();
    assertThat(areEquivalent(forTree, doTree)).isFalse();
    assertThat(areEquivalent(whileTree, doTree)).isFalse();

    LoopTreeImpl forTreeWithoutCondition = new LoopTreeImpl(meta, null, body, FOR, forToken);
    assertThat(forTreeWithoutCondition.condition()).isNull();
    assertThat(forTree.body()).isEqualTo(body);
    assertThat(forTree.kind()).isEqualTo(FOR);
  }

}
