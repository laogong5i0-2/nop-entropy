/**
 * Copyright (c) 2017-2023 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-chaos
 * Github: https://github.com/entropy-cloud/nop-chaos
 */
package io.nop.excel.model;

import io.nop.api.core.util.INeedInit;
import io.nop.excel.format.ExcelFormatHelper;
import io.nop.excel.model._gen._ExcelWorkbook;

import java.text.Format;

public class ExcelWorkbook extends _ExcelWorkbook implements INeedInit {
    public ExcelWorkbook() {

    }

    @Override
    public void init() {
        for (ExcelSheet sheet : getSheets()) {
            sheet.init();
        }
    }

    public Object getExcelFormatValue(ExcelCell cell) {
        if (cell.getStyleId() == null)
            return cell.getValue();

        ExcelStyle style = getStyle(cell.getStyleId());
        if (style != null && style.getNumberFormat() != null) {
            Format format = ExcelFormatHelper.getFormat(style.getNumberFormat());
            if (format != null) {
                return format.format(cell.getValue());
            }
        }

        return cell.getValue();
    }

    public ExcelSheet removeSheet(String sheetName) {
        ExcelSheet sheet = getSheet(sheetName);
        if (sheet != null) {
            getSheets().remove(sheet);
        }
        return sheet;
    }
}
