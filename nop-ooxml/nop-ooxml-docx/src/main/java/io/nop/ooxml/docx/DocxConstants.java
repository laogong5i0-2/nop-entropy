/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.ooxml.docx;

import io.nop.ooxml.common.OfficeConstants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface DocxConstants extends OfficeConstants {

    String LIB_DOCX_GEN = "/nop/ooxml/xlib/docx-gen.xlib";

    String TAG_LINK_EXPR = "wml:LinkExpr";
    String TAG_PARAGRAPH_EXPR = "wml:ParagraphExpr";

    String TABLE_WML_CONFIG = "WmlConfig";

    String PATH_WORD_RELS = "word/_rels/document.xml.rels";
    String PATH_WORD_DOCUMENT = "word/document.xml";

    String SRC_NAME = "src";

    String EXPR_IMG_ID = "${_imgId}";
    String EXPR_IMG_NAME = "${_imgName}";

    String TAG_WML_RENDER = "wml:Render";
    String TAG_DRAWING = "docx-gen:Drawing";

    String VAR_WORD_MODEL = "wordModel";
    String VAR_WORD_TEMPLATE = "wordTemplate";

    String VAR_RELS_FILE = "relsFile";

    String VAR_ZIP_OUT = "zipOut";

    Set<String> WML_NS_LIST = new HashSet<>(Arrays.asList("w", "mc"));

    String HEADER_XPL_GEN_CONFIG = "XplGenConfig";
}
